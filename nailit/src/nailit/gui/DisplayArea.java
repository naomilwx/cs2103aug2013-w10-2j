//@author A0091372H
package nailit.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.Callable;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import nailit.common.Result;
import nailit.common.Task;

@SuppressWarnings("serial")
public class DisplayArea extends JLayeredPane implements Resizable{
	private static final Color DISPLAYAREA_DEFAULT_BACKGROUND_COLOR = Color.white;
	private static final int Y_BUFFER_HEIGHT = GUIManager.Y_BUFFER_HEIGHT;
	private static final int X_BUFFER_WIDTH = GUIManager.X_BUFFER_WIDTH;
	private static final int DISPLAY_AREA_TOP_BUFFER = 2;
	private static final int WINDOW_RIGHT_BUFFER = GUIManager.WINDOW_RIGHT_BUFFER;
	private static final int WINDOW_BOTTOM_BUFFER = GUIManager.WINDOW_BOTTOM_BUFFER;
	private static final int NOTIFICATION_OFFSET = NotificationArea.NOTIFICATION_HEIGHT;
	
	private static final int TIMER_INTERVAL = 100;
	private static final int TIMER_DELAY = 5000; //amount of time before item starts fading out
	private static final float OPACITY_INTERVAL_STEP = 0.1f;
	protected static final float FULL_OPACITY = 1.0f;
	protected static final float NO_OPACITY = 0.5f;
	public static final float MAX_OPACITY_VALUE = 255;
	
	private final Timer fadeOutTimer = new Timer(0, null);
	
	private GUIManager GUIBoss;
	private DisplayPane defaultDisplayPane;
	private JPanel popupPane;
	private NotificationArea notificationArea;
	
	private int displayWidth;
	private int displayHeight;
	private int defaultPaneWidth;
	private int defaultPaneHeight;
	private int containerHeight;
	@SuppressWarnings("unused")
	private int containerWidth;
	private int xPos;
	private int yPos;
	
	//DisplayArea constructor. offset is the height of the CommandBar below
	public DisplayArea(final GUIManager GUIMain, int containerWidth, int containerHeight, int offset) {
		GUIBoss = GUIMain;
		displayWidth = containerWidth - X_BUFFER_WIDTH - WINDOW_RIGHT_BUFFER;
		this.containerHeight = containerHeight;
		this.containerWidth = containerWidth;
		adjustDisplayHeight(offset);
		adjustDisplayAreaPos();
		configureDisplayArea();
		initialiseLayers();
		initialiseNotificationDisplay();
	}
	
	private void configureDisplayArea(){
		this.setBorder(null);
		this.setBackground(DISPLAYAREA_DEFAULT_BACKGROUND_COLOR);
		setDisplayAreaSizeAndPos();
		this.setOpaque(true);
	}
	private void initialiseNotificationDisplay(){
		notificationArea = new NotificationArea(getWidth());
		addPopup(notificationArea);
		hideNotifications();
	}
	protected void cleanupDisplayArea(){
		hideNotifications();
		removeDeletedTasksFromTaskListTable();
		removeTaskDisplay(); //TODO:
	}
	/**
	 * DisplayArea has 2 layers - the popup layer where the notifications are displayed and the default layer where 
	 * the result of user commands are displayed
	 * This method initialises and adds the 2 layers to the DisplayArea instance
	 */
	private void initialiseLayers() {
		initialiseDefaultPane();
		initialisePopupPane();
	}
	private void initialiseDefaultPane(){
		defaultPaneWidth = displayWidth;
		defaultPaneHeight = displayHeight;
		defaultDisplayPane = new DisplayPane(this);
		setLayerToDefaultSettings(defaultDisplayPane);
		add(defaultDisplayPane,JLayeredPane.DEFAULT_LAYER);
	}
	private void initialisePopupPane(){
		popupPane = new JPanel();
		popupPane.setLayout(null);
		setLayerToDefaultSettings(popupPane);
		add(popupPane, JLayeredPane.POPUP_LAYER);
		popupPane.addComponentListener(new ComponentAdapter(){

			@Override
			public void componentHidden(ComponentEvent event) {
				shiftDefaultLayer(GUIManager.DEFAULT_COMPONENT_LOCATION.x, GUIManager.DEFAULT_COMPONENT_LOCATION.y);
			}
			@Override
			public void componentShown(ComponentEvent event) {
				if(defaultDisplayPane.getY() == GUIManager.DEFAULT_COMPONENT_LOCATION.y){
					shiftDefaultLayer(defaultDisplayPane.getX(), NotificationArea.NOTIFICATION_HEIGHT);
				}
			}
			
		});
	}
	private void setLayerToDefaultSettings(JPanel layer){
		layer.setSize(displayWidth, displayHeight);
		layer.setLocation(GUIManager.DEFAULT_COMPONENT_LOCATION);
		layer.setOpaque(false);
	}
	
	protected void addPopup(Component component){
		popupPane.add(component);
	}
	protected void setFocus(){
		defaultDisplayPane.requestFocus();
	}
	protected void stopAllTimers(){
		fadeOutTimer.stop();
	}
	//Start of functions to resize and reposition DisplayArea and its constituent layers 
	public void resizeToFitContainer(int containerWidth, int containerHeight){
		this.containerHeight = containerHeight;
		displayWidth = containerWidth - X_BUFFER_WIDTH - WINDOW_RIGHT_BUFFER;
		dynamicallyResizeAndRepositionDisplayArea(GUIBoss.getDisplayAreaHeightOffset());
	}
	//adjust display height based on available space
	private void adjustDisplayHeight(int additionalOffset){
		displayHeight = containerHeight - DISPLAY_AREA_TOP_BUFFER - Y_BUFFER_HEIGHT -  WINDOW_BOTTOM_BUFFER - additionalOffset;
	}
	protected void dynamicallyResizeAndRepositionDisplayArea(int additionalOffset){
		adjustDisplayHeight(additionalOffset);
		adjustDisplayAreaPos();
		setDisplayAreaSizeAndPos();
		dynamicallyResizeDefaultPaneHeight();
		popupPane.setSize(displayWidth, displayHeight);
		revalidate();
	}
	private void setDisplayAreaSizeAndPos(){
		this.setSize(displayWidth, displayHeight);
		this.setLocation(xPos, yPos);
	}
	private void adjustDisplayAreaPos(){
		xPos = X_BUFFER_WIDTH;
		yPos = containerHeight - displayHeight - WINDOW_BOTTOM_BUFFER;
	}
	
	//display pane resizing
	private void adjustDefaultPaneHeight(){
		if(popupPane.isVisible()){
			defaultPaneHeight = displayHeight - NOTIFICATION_OFFSET;
		}else{
			defaultPaneHeight = displayHeight;
		}
	}
	private void dynamicallyResizeDefaultPaneHeight(){
		adjustDefaultPaneHeight();
		defaultDisplayPane.setSize(defaultPaneWidth, defaultPaneHeight);
	}
	private void shiftDefaultLayer(int xpos, int ypos){
		defaultDisplayPane.setLocation(xpos, ypos);
		dynamicallyResizeDefaultPaneHeight();
		revalidate();
	}
	//End of functions to resize and reposition DisplayArea and its consitutient layers
	//notification area
	protected void displayNotification(String notificationStr, boolean isSuccess){
		notificationArea.displayNotification(notificationStr, isSuccess);
		showNotificationsPane();
	}
	
	protected void displayNotificationAndForceExit(String notificationStr){
		notificationArea.displayNotification(notificationStr, false);
		showNotificationsPaneAndForceExit();
	}
	//Functions to control visibility of popupPane
	protected void hideNotifications(){
		popupPane.setVisible(false);
	}
	protected void showNotificationsPane(){
		popupPane.setVisible(true);
		fadeOutComponentAndPerformActionOnComplete(popupPane.getComponent(0), fadeOutTimer, TIMER_DELAY, 
				TIMER_INTERVAL, OPACITY_INTERVAL_STEP,
				new Callable<Boolean>(){
					public Boolean call() throws Exception {
						defaultDisplayPane.removeDeletedTasksFromTaskListTable();
						return true;
					}
		});
	}
	protected void showNotificationsPaneAndForceExit(){
		popupPane.setVisible(true);
		fadeOutComponentAndPerformActionOnComplete(popupPane.getComponent(0), fadeOutTimer, TIMER_DELAY, 
				TIMER_INTERVAL, OPACITY_INTERVAL_STEP,
				new Callable<Boolean>(){
					public Boolean call() throws Exception {
						GUIBoss.forceExit();
						return true;
					}
		});
	}
	
	private void fadeOutComponentAndPerformActionOnComplete(final Component component, final Timer timer,
			int displayTime, int timeInterval, final float opacityStep, final Callable<?> func){
		timer.setInitialDelay(displayTime);
		timer.setDelay(timeInterval);
		//stop fadeout if user mouseover component
		component.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent event){
				timer.stop();
			}
			@Override
			public void mouseExited(MouseEvent event){
				timer.restart();
			}
		});
		timer.addActionListener(new ActionListener(){
			int originalOpacity = GUIUtilities.getComponentOpacity(component);
			float nextOpacityRatio = ((float) originalOpacity)/MAX_OPACITY_VALUE;
			@Override
			public void actionPerformed(ActionEvent event) {
				nextOpacityRatio -= opacityStep;
				if(nextOpacityRatio <= NO_OPACITY){
					timer.stop();
					component.getParent().setVisible(false);
					//restore original opacity so component's original background settings is restored when it is made visible again
					GUIUtilities.setComponenetOpacity(component, originalOpacity);
					timer.removeActionListener(this);
					try {
						func.call();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if(component.getParent().isVisible()){
					int nextOpacity = Math.round(nextOpacityRatio*MAX_OPACITY_VALUE);
					GUIUtilities.setComponenetOpacity(component, nextOpacity);
				}else{
					timer.stop();
				}
			}
			
		});
		timer.restart();
	}
	protected KeyAdapter getTriggeredCommandKeyListener(){
		KeyAdapter triggeredCommandKeyEventListener = new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent keyStroke){
				int keyCode = keyStroke.getKeyCode();
				if(keyCode == KeyEvent.VK_ENTER){
					GUIBoss.executeTriggeredTaskDisplay();
				}else if(keyCode == KeyEvent.VK_DELETE){
					GUIBoss.executeTriggeredTaskDelete();
				}
			}
		};
		return triggeredCommandKeyEventListener;
	}
	//acts as facade between GUIManager and components displayed in DisplayArea
	protected void removeTaskDisplay(){
		defaultDisplayPane.removeTaskDisplay();
	}
	protected void quickTaskTableScroll(boolean up){
		defaultDisplayPane.quickTaskTableScroll(up);
	}
	protected void taskTableScroll(boolean up){
		defaultDisplayPane.taskTableScroll(up);
	}
	protected void removeDeletedTasksFromTaskListTable(){
		defaultDisplayPane.removeDeletedTasksFromTaskListTable();
	}
	protected int getTaskTableSelectedRowID(){
		return defaultDisplayPane.getTaskTableSelectedRowID();
	}
	protected void displayTaskDetails(Task task){
		defaultDisplayPane.displayTaskDetails(task);
	}
	protected void scrollToTaskDisplayID(int ID){
		defaultDisplayPane.scrollToTaskDisplayID(ID);
	}
	protected void displayTaskList(Result result){
		defaultDisplayPane.displayTaskList(result);
	}
	protected void displayExecutionResultDisplay(Result result){
		defaultDisplayPane.displayExecutionResultDisplay(result);
	}
	protected void setDefaultFocus(){
		GUIBoss.setFocusOnCommandBar();
	}
	protected KeyAdapter getBasicKeyListener(){
		return GUIBoss.getMainWindowComponentBasicKeyListener();
	}
}
