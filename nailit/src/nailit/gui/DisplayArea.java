//@author A0091372H
package nailit.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;
import java.util.concurrent.Callable;

import javax.swing.BoxLayout;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import nailit.common.Result;
import nailit.common.Task;
import nailit.gui.renderer.TaskDetailsFormatter;

@SuppressWarnings("serial")
public class DisplayArea extends JLayeredPane {
	private static final Color DISPLAYAREA_DEFAULT_BACKGROUND_COLOR = Color.white;
	private static final int Y_BUFFER_HEIGHT = GUIManager.Y_BUFFER_HEIGHT;
	private static final int X_BUFFER_WIDTH = GUIManager.X_BUFFER_WIDTH;
	private static final int DISPLAY_AREA_TOP_BUFFER = 2;
	private static final int WINDOW_RIGHT_BUFFER = GUIManager.WINDOW_RIGHT_BUFFER;
	private static final int WINDOW_BOTTOM_BUFFER = GUIManager.WINDOW_BOTTOM_BUFFER;
	private static final int NULL_FOCUS = -1;
	private static final int NOTIFICATION_OFFSET = NotificationArea.NOTIFICATION_HEIGHT;
	
	private static final int TIMER_INTERVAL = 100;
	private static final int TIMER_DELAY = 5000; //amount of time before item starts fading out
	private static final float OPACITY_INTERVAL_STEP = 0.1f;
	protected static final float FULL_OPACITY = 1.0f;
	protected static final float NO_OPACITY = 0.5f;
	public static final float MAX_OPACITY_VALUE = 255;
	
	private final Timer fadeOutTimer = new Timer(0, null);
	
	private GUIManager GUIBoss;
	private JPanel defaultPane;
	private JPanel popupPane;
	private TaskTable taskTable;
	private TextDisplay textDisplay;
	private int lastDisplayedTaskID;
	
	private int displayWidth;
	private int displayHeight;
	private int defaultPaneWidth;
	private int defaultPaneHeight;
	private int containerHeight;
	@SuppressWarnings("unused")
	private int containerWidth;
	private int xPos;
	private int yPos;
	
	private int currentFocusElement = NULL_FOCUS;
	private final FocusListener defaultPaneFocusListener = new FocusListener(){
		public void focusGained(FocusEvent event) {
			defaultPaneSetFocusHandler();
		 }
		public void focusLost(FocusEvent event){
		}
	};
	
	private KeyAdapter keyEventListener = new KeyAdapter(){
		boolean ctrlPressed = false;
		@Override
		public void keyPressed(KeyEvent keyStroke){
			int keyCode = keyStroke.getKeyCode();
			if(keyCode == KeyEvent.VK_CONTROL){
				ctrlPressed = true;
			}else if(ctrlPressed && keyCode == KeyEvent.VK_TAB){
				defaultPaneSetFocusHandler();
			}else if(keyCode == KeyEvent.VK_ESCAPE){
				GUIBoss.setFocusOnCommandBar();
			}else if(keyCode == KeyEvent.VK_COMMA){
				GUIBoss.setVisible(false);
			}else if(keyCode == KeyEvent.VK_H){
				GUIBoss.toggleHomeWindow();
			}
		}
		@Override
		public void keyReleased(KeyEvent keyStroke){
			int keyCode = keyStroke.getKeyCode();
			if(keyCode == KeyEvent.VK_CONTROL){
				ctrlPressed = false;
			}
		}
	};
	
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
		addDisplayAreaListeners();
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
		defaultPane = new JPanel();
		defaultPane.setLayout(new BoxLayout(defaultPane,BoxLayout.Y_AXIS));
		setLayerToDefaultSettings(defaultPane);
		defaultPane.setFocusable(true);
		add(defaultPane,JLayeredPane.DEFAULT_LAYER);
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
				if(defaultPane.getY() == GUIManager.DEFAULT_COMPONENT_LOCATION.y){
					shiftDefaultLayer(defaultPane.getX(), NotificationArea.NOTIFICATION_HEIGHT);
				}
			}
			
		});
	}
	/**
	 * Sets given JPanel to span the whole container (DisplayArea)
	 * @param JPanel layer
	 */
	private void setLayerToDefaultSettings(JPanel layer){
		layer.setSize(displayWidth, displayHeight);
		layer.setLocation(GUIManager.DEFAULT_COMPONENT_LOCATION);
		layer.setOpaque(false);
	}
	
	protected void resizeDisplayToFitMainContainer(int containerWidth, int containerHeight){
		this.containerHeight = containerHeight;
		displayWidth = containerWidth - X_BUFFER_WIDTH - WINDOW_RIGHT_BUFFER;
		dynamicallyResizeAndRepositionDisplayArea(GUIBoss.getCommandBarHeight());
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
	private void shiftDefaultLayer(int xpos, int ypos){
		defaultPane.setLocation(xpos, ypos);
		dynamicallyResizeDefaultPaneHeight();
		revalidate();
	}
	private void adjustDefaultPaneHeight(){
		if(popupPane.isVisible()){
			defaultPaneHeight = displayHeight - NOTIFICATION_OFFSET;
		}else{
			defaultPaneHeight = displayHeight;
		}
	}
	private void dynamicallyResizeDefaultPaneHeight(){
		adjustDefaultPaneHeight();
		defaultPane.setSize(defaultPaneWidth, defaultPaneHeight);
	}
	private void configureDisplayArea(){
		this.setBorder(null);
		this.setBackground(DISPLAYAREA_DEFAULT_BACKGROUND_COLOR);
		setDisplayAreaSizeAndPos();
		this.setOpaque(true);
	}
	private void adjustDisplayAreaPos(){
		xPos = X_BUFFER_WIDTH;
		yPos = containerHeight - displayHeight - WINDOW_BOTTOM_BUFFER;
	}
	private void addDisplayAreaListeners(){
		defaultPane.addFocusListener(defaultPaneFocusListener);
	}
	private void defaultPaneSetFocusHandler(){
		int nextFocusElement = NULL_FOCUS;
		if(currentFocusElement == NULL_FOCUS){
			nextFocusElement = 0;
		}else{
			nextFocusElement = currentFocusElement + 1;
		}
		//toggle between table and task display
		Component[] components = defaultPane.getComponents();
		if(components.length == 0){
			currentFocusElement = NULL_FOCUS;
			GUIBoss.setFocusOnCommandBar();
		}else{
			if(components.length <= nextFocusElement){
				nextFocusElement = 0;
			}
			components[nextFocusElement].requestFocus();
			currentFocusElement = nextFocusElement;
		}
	}
	
	protected void hideNotificationsPane(){
		popupPane.setVisible(false);
	}
	protected void showNotificationsPane(){
		popupPane.setVisible(true);
		fadeOutComponentAndPerformActionOnComplete(popupPane.getComponent(0), fadeOutTimer, TIMER_DELAY, 
				TIMER_INTERVAL, OPACITY_INTERVAL_STEP,
				new Callable<Boolean>(){
					public Boolean call() throws Exception {
						removeDeletedTasksFromTaskListTable();
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
	
	private void addContent(Component component){
		component.addKeyListener(keyEventListener);
		component.setFocusTraversalKeysEnabled(false);
		defaultPane.add(component);
	}
	private void addContent(Component component, int pos){
		component.addKeyListener(keyEventListener);
		component.setFocusTraversalKeysEnabled(false);
		defaultPane.add(component, pos);
	}
	
	private void clearContents(){
		defaultPane.removeAll();
		textDisplay = null;
		taskTable = null;
		lastDisplayedTaskID = Task.TASKID_NULL;
	}
	protected void displayTaskList(Result result){
		Vector<Task>  tasks = result.getTaskList();
		Task task = result.getTaskToDisplay();
		if(tasks == null){
			return;
		}
		addNewTaskTable();
		displayTaskListInTaskTable(tasks, task);
		revalidate();
	}
	private void addNewTaskTable(){
		clearContents();
		createAndConfigureTaskTable();
	}
	private void displayTaskListInTaskTable(Vector<Task>  tasks, Task task){
		taskTable.displayTaskList(tasks, task);
	}
	private void createAndConfigureTaskTable(){
		taskTable = new TaskTable(displayWidth, displayHeight);
		addAdditionalKeyListenerToTaskTable();
		addContent(taskTable);
	}
	protected void displayExecutionResultDisplay(Result result){
		int currDisplayTask = lastDisplayedTaskID;
		displayTaskList(result);
		Task taskDisp = result.getTaskToDisplay();
		if(result.getDeleteStatus() == true){
			showDeletedTaskInTaskListTable(taskDisp);
		}else{
			if(taskDisp != null && taskDisp.getID() ==  currDisplayTask){
				displayTaskDetails(taskDisp);
				lastDisplayedTaskID =  currDisplayTask;
			}
		}
	}
	protected void displayTaskDetails(Task task){
		if(task!=null){
			addNewTextDisplay();
			displayTaskInTextDisplay(task);
			revalidate();
			lastDisplayedTaskID = task.getID();
		}else{
			lastDisplayedTaskID = Task.TASKID_NULL;
		}
	}
	private void addNewTextDisplay(){
		removeExistingTextDisplay();
		createAndConfigureNewTextDisplay();
	}
	private void displayTaskInTextDisplay(Task task){
		String details = TaskDetailsFormatter.formatTaskForDisplay(task);
		textDisplay.displayHTMLFormattedText(details);
		GUIUtilities.scrollDisplayToTop(textDisplay);
	}
	private void createAndConfigureNewTextDisplay(){
		int textDisplayWidth = defaultPane.getWidth();
		int textDisplayHeight = defaultPane.getHeight();
		textDisplay = new TextDisplay(textDisplayWidth, textDisplayHeight);
		addContent(textDisplay, 0);
	}
	private void removeExistingTextDisplay(){
		if(textDisplay != null){
			defaultPane.remove(textDisplay);
			textDisplay = null;
		}
	}
	protected void removeTaskDisplay(){
		removeExistingTextDisplay();
		revalidate();
	}
	protected void showDeletedTaskInTaskListTable(Task task){
		if(task != null){
			taskTable.addDeletedTaskToTable(task);
		}
	}
	protected void removeDeletedTasksFromTaskListTable(){
		if(taskTable != null){
			taskTable.clearDeletedTaskRowsFromTable();
		}
	}
	
	protected void quickTaskTableScroll(boolean up){
		if(taskTable != null){
			if(up){
				taskTable.quickScrollUp();
			}else{
				taskTable.quickScrollDown();
			}
		}
	}
	protected void taskTableScroll(boolean up){
		if(taskTable != null){
			if(up){
				taskTable.scrollUp();
			}else{
				taskTable.scrollDown();
			}
		}
	}
	private void addAdditionalKeyListenerToTaskTable(){
		KeyAdapter taskTableKeyEventListener = new KeyAdapter(){
			private boolean ctrlPressed = false;
			@Override
			public void keyPressed(KeyEvent keyStroke){
				int keyCode = keyStroke.getKeyCode();
				if(keyCode == KeyEvent.VK_CONTROL){
					ctrlPressed = true;
				}else if(ctrlPressed && keyCode == KeyEvent.VK_D){
					ctrlPressed = false;
					taskTableOnCtrlDEvent();
				}else if(ctrlPressed && keyCode == KeyEvent.VK_N){
					taskTableOnCtrlNEvent();
				}else if(keyCode == KeyEvent.VK_ENTER){
					taskTableOnEnterEvent();
				}else if(keyCode == KeyEvent.VK_DELETE){
					taskTableOnDeleteEvent();
				}else if(keyCode == KeyEvent.VK_COMMA){
					GUIBoss.setVisible(false);
				}else if(keyCode == KeyEvent.VK_H){
					GUIBoss.toggleHomeWindow();
				}
			}
			@Override
			public void keyReleased(KeyEvent keyStroke){
				int keyCode = keyStroke.getKeyCode();
				if(keyCode == KeyEvent.VK_CONTROL){
					ctrlPressed = false;
				}
			}
		};
		taskTable.addKeyListenerToTable(taskTableKeyEventListener);
	}
	protected int getTaskTableSelectedRowID(){
		if(taskTable != null){
			return taskTable.getSelectedRowDisplayID();
		}else{
			return -1;
		}
	}
	private void taskTableOnDeleteEvent() {
		GUIBoss.setFocusOnCommandBar();
		int displayID = taskTable.getSelectedRowDisplayID();
		if(displayID >= 1){
			GUIBoss.executeTriggeredTaskDelete(displayID);
		}
	}
	protected void taskTableOnEnterEvent(){
		GUIBoss.setFocusOnCommandBar();
		int displayID = taskTable.getSelectedRowDisplayID();
		if(displayID >= 1){
			GUIBoss.executeTriggeredTaskDisplay(displayID);
		}
	}
	protected void taskTableOnCtrlDEvent(){
		GUIBoss.setFocusOnCommandBar();
		int displayID = taskTable.getSelectedRowDisplayID();
		if(displayID >= 1){
			GUIBoss.loadExistingTaskDescriptionInCommandBar(displayID);
		}
	}
	protected void taskTableOnCtrlNEvent(){
		GUIBoss.setFocusOnCommandBar();
		int displayID = taskTable.getSelectedRowDisplayID();
		if(displayID >= 1){
			GUIBoss.loadExistingTaskNameInCommandBar(displayID);
		}
	}
	
	protected void addPopup(Component component){
		popupPane.add(component);
	}
	protected void setFocus(){
		defaultPane.requestFocus();
	}
	protected void stopAllTimers(){
		fadeOutTimer.stop();
	}
	private void fadeOutComponentAndPerformActionOnComplete(final Component component, final Timer timer,
			int displayTime, int timeInterval, final float opacityStep, final Callable<?> func){
		timer.setInitialDelay(displayTime);
		timer.setDelay(timeInterval);
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
}
