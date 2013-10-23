package nailit.gui;

import java.awt.Color;
import java.awt.Component;
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

import javax.swing.BoxLayout;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import nailit.common.NIConstants;
import nailit.common.Result;
import nailit.common.Task;
import nailit.gui.renderer.TaskDateTimeDisplayRenderer;
import nailit.gui.renderer.TaskDetailsFormatter;
import nailit.gui.renderer.TaskNameDisplayRenderer;

public class DisplayArea extends JLayeredPane {
	private static final Color DISPLAYAREA_DEFAULT_BACKGROUND_COLOR = Color.white;
	private static final int Y_BUFFER_HEIGHT = GUIManager.Y_BUFFER_HEIGHT;
	private static final int X_BUFFER_WIDTH = GUIManager.X_BUFFER_WIDTH;
	private static final int WINDOW_RIGHT_BUFFER = GUIManager.WINDOW_RIGHT_BUFFER;
	private static final int WINDOW_BOTTOM_BUFFER = GUIManager.WINDOW_BOTTOM_BUFFER;
	private static final double DISPLAY_AREA_SCALE = 0.8;
	private static final int MAX_NUM_ITEMS_IN_DEFAULTPANE = 2;
	private static final int NULL_FOCUS = -1;
	
	private GUIManager GUIBoss;
	private JPanel defaultPane;
	private JPanel popupPane;
	private LinkedList<Component> items;
	private TableDisplay taskTable;
	private TextDisplay textDisplay;
	
	private int displayWidth;
	private int displayHeight;
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
		@Override
		public void keyPressed(KeyEvent keyStroke){
			int keyCode = keyStroke.getKeyCode();
			if(keyCode == KeyEvent.VK_SHIFT){
				defaultPaneSetFocusHandler();
			}else if(keyCode == KeyEvent.VK_ESCAPE){
				GUIBoss.setFocusOnCommandBar();
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
		defaultPane = new JPanel();
		defaultPane.setLayout(new BoxLayout(defaultPane,BoxLayout.Y_AXIS));
		setLayerToDefaultSettings(defaultPane);
		defaultPane.setFocusable(true);
		add(defaultPane,JLayeredPane.DEFAULT_LAYER);
		
		popupPane = new JPanel();
		popupPane.setLayout(null);
		setLayerToDefaultSettings(popupPane);
		add(popupPane, JLayeredPane.POPUP_LAYER);
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
		displayHeight = containerHeight - 3 * Y_BUFFER_HEIGHT -  WINDOW_BOTTOM_BUFFER - additionalOffset;
	}
	
	protected void dynamicallyResizeDisplayArea(int additionalOffset){
		adjustDisplayHeight(additionalOffset);
		this.setSize(displayWidth, displayHeight);
		defaultPane.setSize(displayWidth, displayHeight);
		popupPane.setSize(displayWidth, displayHeight);
		revalidate();
	}
	
	private void shiftLayer(JPanel layer, int xpos, int ypos){
		layer.setLocation(xpos, ypos);
	}
	private void configureDisplayArea(){
		this.setBorder(new LineBorder(GUIManager.BORDER_COLOR));
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
	}
	protected void addContent(Component component, boolean replace){
		if(popupPane.isVisible()){
			shiftLayer(defaultPane, defaultPane.getX(), NotificationArea.NOTIFICATION_HEIGHT);
		}else{
			shiftLayer(defaultPane, GUIManager.DEFAULT_COMPONENT_LOCATION.x, GUIManager.DEFAULT_COMPONENT_LOCATION.y);
		}
		
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
			textDisplay = new TextDisplay(displayWidth, displayHeight);
			textDisplay.displayHTMLFormattedText(details);
			addContent(textDisplay, false);
		}
	}
	protected void showDeletedTaskInTaskListTable(Task task){
		Vector<String> row = formatTaskForRowDisplay(task, GUIManager.DELETED_TASK_DISPLAY_ID);
		taskTable.addDeletedTaskToTable(row);
	}
	protected void removeDeletedTasksFromTaskListTable(){
		taskTable.clearDeletedTaskRowsFromTable();
	}
	protected void displayTaskList(Vector<Task> tasks){
		taskTable = new TableDisplay(displayWidth, displayHeight , Result.LIST_DISPLAY);
		addAdditionalKeyListenerToTaskTable();
		Vector<String> row;
		for(int i = 0; i < tasks.size(); i++){
			String IDVal = i+1 + "";
			row = formatTaskForRowDisplay(tasks.get(i), IDVal);
			taskTable.addContentToTable(row);	
		}
		addContent(taskTable, false);
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
				}else if(keyCode == KeyEvent.VK_ENTER){
					taskTableOnEnterEvent();
				}else if(keyCode == KeyEvent.VK_DELETE){
					taskTableOnDeleteEvent();
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
		GUIBoss.executeTriggeredTaskDelete(taskTable.getSelectedRowDisplayID());
	}
	protected void taskTableOnEnterEvent(){
		GUIBoss.setFocusOnCommandBar();
		GUIBoss.executeTriggeredTaskDisplay(taskTable.getSelectedRowDisplayID());
	}
	protected void taskTableOnCtrlEnterEvent(){
		GUIBoss.setFocusOnCommandBar();
		GUIBoss.loadExistingTaskDescriptionInCommandBar(taskTable.getSelectedRowDisplayID());
	}
	protected Vector<String> formatTaskForRowDisplay(Task task, String IDVal){
		Vector<String> row = new Vector<String>();
		row.add(IDVal);
		String nameAndTag = TaskNameDisplayRenderer.formatTaskNameCellDisplay(task);
		row.add(nameAndTag);
		String timeStartDet = TaskDateTimeDisplayRenderer.formatTaskDateTimeCellDisplay(task.getStartTime());
		row.add(timeStartDet);
		String timeEndDet = TaskDateTimeDisplayRenderer.formatTaskDateTimeCellDisplay(task.getEndTime());
		row.add(timeEndDet);
		return row;
	}
	
	//function to keep top displayed component if it is a table and new display is not a table
	private void removeExtraContent(Component newComponent){
		Component first = items.peekFirst();
		items.clear();
		defaultPane.removeAll();
		if((first instanceof TableDisplay) && !(newComponent instanceof TableDisplay)){
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
}
