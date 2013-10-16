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
	private static final String TASK_DISPLAY_STYLE = 
			"<head><style type = \"text/css\">" 
			+ "p.name {font-size: 14px;}"
			+ "p.tag {font-size: 9px; color: gray;}"
			+ "p.date {font-size: 12px;}"
			+ "p.time {font-size: 10px;}"
			+ "p.title {font-size: 12px;}"
			+ "</style></head>";
	
	private GUIManager GUIBoss;
	private JPanel defaultPane;
	private JPanel popupPane;
	private LinkedList<Component> items;
	
	private int displayWidth;
	private int displayHeight;
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
			}else if(ctrlPressed && (keyCode == KeyEvent.VK_TAB)){
				GUIBoss.setFocusOnCommandBar();
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
	
	//DisplayArea constructor
	public DisplayArea(final GUIManager GUIMain, int containerWidth, int containerHeight) {
		GUIBoss = GUIMain;
		items = new LinkedList<Component>();
		displayWidth = containerWidth - X_BUFFER_WIDTH - WINDOW_RIGHT_BUFFER;
		displayHeight = (int) Math.ceil(containerHeight*DISPLAY_AREA_SCALE);
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
			String details = formatTaskForDisplay(task);
			TextDisplay textDisplay = new TextDisplay(displayWidth, displayHeight);
			textDisplay.displayHTMLFormattedText(details);
			addContent(textDisplay, false);
		}
	}
	protected String formatTaskForDisplay(Task task){
		int rowCount = 2;
		String details = "<html>" + TASK_DISPLAY_STYLE;
		details += "<tr><td><p class = \"title\">Name: </p></td>";
		details += "<td><p class = \"name\">" + task.getName() + "</p>";
		if(!task.getTag().isEmpty()){
			details += "<p class = \"tag\">" + task.getTag() + "<p></td>";
		}else{
			details += "</td>";
		}
		String otherDetails = formatTaskDateTimeForDisplay(task);
		
		otherDetails += "<tr><td><p class = \"title\">Priority: </p></td><td><p class = \"prority\">" 
				+ task.getPriority() + "</p></td></tr>";
		if(!task.isEvent()){
			if(task.isFloatingTask()){
				rowCount += 1;
			}else{
				rowCount += 2; //including row for due date
			}
			otherDetails += "<tr><td><p class = \"title\">Status: </p></td><td><p class = \"status\">";
			if(task.checkCompleted()){
				otherDetails += "Done</p></td></tr>";
			}else{
				otherDetails += "Not done</p></td></tr>";
			}
		}else{
			rowCount += 2; //for start and end time
		}
		
		String taskDesc = task.getDescription();
		if(taskDesc != null && !taskDesc.isEmpty()){
			details += formatTaskDetailsForDisplay(taskDesc, rowCount);
		}
	
		details += otherDetails + "</html>";
		return details;
	}
	
	private String formatTaskDateTimeForDisplay(Task task){
		String details = "";
		if(!task.isFloatingTask()){
			if(task.isEvent()){
				details += "<tr><td><p class = \"title\">Start: </p></td><td>"
							 + TaskDateTimeDisplayRenderer.formatTaskDateTimeCellDisplay(task.getStartTime()) 
							 +"</td></tr>";
				details += "<tr><td><p class = \"title\">End: </p></td><td>" 
							 + TaskDateTimeDisplayRenderer.formatTaskDateTimeCellDisplay(task.getEndTime())  
							 + "</td></tr>";
			}else{
				if(task.getStartTime() != null){
					details += "<tr><td><p class = \"title\">Due: </p></td><td><p class = \"datetime\">" 
								 + TaskDateTimeDisplayRenderer.formatTaskDateTimeCellDisplay(task.getStartTime())  
								 + "</td></tr>";
				}
			}
		}
		return details;
	}
	private String formatTaskDetailsForDisplay(String desc, int rowCount){
		String details = "<td style = \" width: 20px\" colspan = \"1\" rowspan = \"" 
						+ rowCount + "\"></td>";
		details += "<td colspan = \"1\" rowspan = \"" 
				+ rowCount + "\"><p class = \"title\"> Description: </p></td>";
		details += "<td rowspan = \"" + rowCount + "\">"
				+ "<p class = \"desc\">" + desc + "</p></td>";
		return details;
	}
	
	protected void displayTaskList(Vector<Task> tasks){
		TableDisplay taskTable = 
				new TableDisplay(displayWidth, displayHeight , Result.LIST_DISPLAY);
		Vector<String> row;
		for(int i = 0; i < tasks.size(); i++){
			row = formatTaskForRowDisplay(tasks.get(i), i+1);
			taskTable.addContentToTable(row);	
		}
		addContent(taskTable, false);
	}
	
	protected Vector<String> formatTaskForRowDisplay(Task task, int rowNum){
		Vector<String> row = new Vector<String>();
		String ID = "" + rowNum;
		row.add(ID);
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
