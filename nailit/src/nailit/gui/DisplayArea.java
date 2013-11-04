//@author A0091372H
package nailit.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;
import java.util.Vector;
import java.util.concurrent.Callable;

import javax.swing.BoxLayout;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.sun.java.swing.SwingUtilities3;

import nailit.common.NIConstants;
import nailit.common.Result;
import nailit.common.Task;
import nailit.gui.renderer.TaskDetailsFormatter;

public class DisplayArea extends JLayeredPane {
	private static final Color DISPLAYAREA_DEFAULT_BACKGROUND_COLOR = Color.white;
	private static final int Y_BUFFER_HEIGHT = GUIManager.Y_BUFFER_HEIGHT;
	private static final int X_BUFFER_WIDTH = GUIManager.X_BUFFER_WIDTH;
	private static final int WINDOW_RIGHT_BUFFER = GUIManager.WINDOW_RIGHT_BUFFER;
	private static final int WINDOW_BOTTOM_BUFFER = GUIManager.WINDOW_BOTTOM_BUFFER;
	private static final int MAX_NUM_ITEMS_IN_DEFAULTPANE = 2;
	private static final int NULL_FOCUS = -1;
	private static final int NOTIFICATION_OFFSET = NotificationArea.NOTIFICATION_HEIGHT;
	
	private static final int TIMER_INTERVAL = 80;
	private static final int TIMER_DELAY = 3000; //amount of time before item starts fading out
	private static final float OPACITY_INTERVAL_STEP = 0.1f;
	protected static final float FULL_OPACITY = 1.0f;
	protected static final float NO_OPACITY = 0.5f;
	public static final float MAX_OPACITY_VALUE = 255;
	
	private final Timer fadeOutTimer = new Timer(0, null);
	
	private GUIManager GUIBoss;
	private JPanel defaultPane;
	private JPanel popupPane;
	private LinkedList<Component> items;
	private TaskTable taskTable;
	private TextDisplay textDisplay;
	
	private int displayWidth;
	private int displayHeight;
	private int defaultPaneWidth;
	private int defaultPaneHeight;
	private int containerHeight;
	
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
			}else if(keyCode == KeyEvent.VK_SHIFT){
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
		items = new LinkedList<Component>();
		displayWidth = containerWidth - X_BUFFER_WIDTH - WINDOW_RIGHT_BUFFER;
		this.containerHeight = containerHeight;
		adjustDisplayHeight(offset);
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
	//adjust display height based on available space
	private void adjustDisplayHeight(int additionalOffset){
		displayHeight = containerHeight - 2 * Y_BUFFER_HEIGHT -  WINDOW_BOTTOM_BUFFER - additionalOffset;
	}
	
	protected void dynamicallyResizeDisplayArea(int additionalOffset){
		adjustDisplayHeight(additionalOffset);
		this.setSize(displayWidth, displayHeight);
		dynamicallyResizeDefaultPaneHeight();
		popupPane.setSize(displayWidth, displayHeight);
		revalidate();
	}
	
	private void shiftDefaultLayer(int xpos, int ypos){
		defaultPane.setLocation(xpos, ypos);
		dynamicallyResizeDefaultPaneHeight();
		revalidate();
	}
	private void dynamicallyResizeDefaultPaneHeight(){
		if(popupPane.isVisible()){
			defaultPaneHeight = displayHeight - NOTIFICATION_OFFSET;
		}else{
			defaultPaneHeight = displayHeight;
		}
		defaultPane.setSize(defaultPaneWidth, defaultPaneHeight);
	}
	private void configureDisplayArea(){
		this.setBorder(null);
		this.setBackground(DISPLAYAREA_DEFAULT_BACKGROUND_COLOR);
		this.setLocation(X_BUFFER_WIDTH, Y_BUFFER_HEIGHT);
		this.setSize(displayWidth, displayHeight);
		this.setOpaque(true);
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
		
		if(items.isEmpty()){
			currentFocusElement = NULL_FOCUS;
			GUIBoss.setFocusOnCommandBar();
		 }else{
			if(items.size() <= nextFocusElement){
				nextFocusElement = 0;
			}
			Component item = items.get(nextFocusElement);
			item.requestFocus();
			currentFocusElement = nextFocusElement;
		 }
	}
	
	protected void hideNotifications(){
		popupPane.setVisible(false);
	}
	protected void showNotifications(){
		popupPane.setVisible(true);
		fadeOutComponentAndPerformActionOnComplete(popupPane.getComponent(0), fadeOutTimer, TIMER_DELAY, 
				TIMER_INTERVAL, OPACITY_INTERVAL_STEP,
				new Callable(){
					public Boolean call() throws Exception {
						removeDeletedTasksFromTaskListTable();
						return true;
					}
		});
	}
	
	protected void addContent(Component component, boolean replace){
		if(replace){
			defaultPane.removeAll();
			items.clear();
		}else{
			//always keep number of items displayed to 2.
			removeExtraContent(component);
			items.add(component);
		}
		component.addKeyListener(keyEventListener);
		component.setFocusTraversalKeysEnabled(false);
		defaultPane.add(component);
		revalidate();
	}
	protected void displayTaskDetails(Task task){
		if(task!=null){
			String details = TaskDetailsFormatter.formatTaskForDisplay(task);
			int textDisplayWidth = defaultPane.getWidth()/2;
			int textDisplayHeight = defaultPane.getHeight()/2;
			textDisplay = new TextDisplay(textDisplayWidth, textDisplayHeight);
			textDisplay.displayHTMLFormattedText(details);
			Utilities.scrollTextDisplayToTop(textDisplay);
			
			addContent(textDisplay, false);
		}
	}
	protected void removeTaskDisplay(){
		if(textDisplay != null){
			defaultPane.remove(textDisplay);
			items.remove(textDisplay);
		}
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
	protected void displayTaskList(Result result){
		Vector<Task>  tasks = result.getTaskList();
		if(tasks == null){
			return;
		}
		Task task = result.getTaskToDisplay();
		taskTable = new TaskTable(displayWidth, displayHeight , Result.LIST_DISPLAY);
		addAdditionalKeyListenerToTaskTable();
		taskTable.displayTaskList(tasks, task);
		addContent(taskTable, false);
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
	private void addAdditionalKeyListenerToTaskTable(){
		KeyAdapter taskTableKeyEventListener = new KeyAdapter(){
			private boolean ctrlPressed = false;
			@Override
			public void keyPressed(KeyEvent keyStroke){
				int keyCode = keyStroke.getKeyCode();
				if(keyCode == KeyEvent.VK_CONTROL){
					ctrlPressed = true;
				}else if(ctrlPressed && keyCode == KeyEvent.VK_ENTER){
					ctrlPressed = false;
					taskTableOnCtrlEnterEvent();
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
	protected void taskTableOnCtrlEnterEvent(){
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
	
	//function to keep top displayed component if it is a table and new display is not a table
	private void removeExtraContent(Component newComponent){
		Component first = items.peekFirst();
		items.clear();
		defaultPane.removeAll();
		if((first instanceof TaskTable) && !(newComponent instanceof TaskTable)){
			items.add(first);
			defaultPane.add(first);
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
			int originalOpacity = Utilities.getComponentOpacity(component);
			float nextOpacityRatio = ((float) originalOpacity)/MAX_OPACITY_VALUE;
			@Override
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				nextOpacityRatio -= opacityStep;
				if(nextOpacityRatio <= NO_OPACITY){
					timer.stop();
					component.getParent().setVisible(false);
					//restore original opacity so component's original background settings is restored when it is made visible again
					Utilities.setComponenetOpacity(component, originalOpacity);
					timer.removeActionListener(this);
					try {
						func.call();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if(component.getParent().isVisible()){
					int nextOpacity = Math.round(nextOpacityRatio*MAX_OPACITY_VALUE);
					Utilities.setComponenetOpacity(component, nextOpacity);
				}else{
					timer.stop();
				}
			}
			
		});
		timer.restart();
	}
}
