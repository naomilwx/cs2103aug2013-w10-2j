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

import nailit.common.Result;
import nailit.common.Task;

public class DisplayArea extends JLayeredPane {
	private static final Color DISPLAYAREA_BACKGROUND_COLOR = Color.white;
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
		@Override
		public void keyPressed(KeyEvent keyStroke){
			int keyCode = keyStroke.getKeyCode();
			if(keyCode == KeyEvent.VK_SHIFT){
				defaultPaneSetFocusHandler();
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
		this.setBackground(DISPLAYAREA_BACKGROUND_COLOR);
		this.setLocation(X_BUFFER_WIDTH, Y_BUFFER_HEIGHT);
		this.setSize(displayWidth, displayHeight);
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
		
		if(items.isEmpty() || items.size() <= nextFocusElement){
			 currentFocusElement = NULL_FOCUS;
			 GUIBoss.setFocusOnCommandBar();
		 }else{
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
		}
		
		if(replace){
			defaultPane.removeAll();
			items.clear();
		}else{
			//always keep number of items displayed to 2.
			items.add(component);
			removeExtraContent();
		}
		component.addKeyListener(keyEventListener);
		defaultPane.add(component);
		revalidate();
	}
	protected void displayTaskDetails(String details){
		TextDisplay textDisplay = new TextDisplay(displayWidth, displayHeight);
		textDisplay.basicDisplay(details);
		addContent(textDisplay, false);
	}
	protected void displayTaskList(Vector<Task> tasks){
		TableDisplay taskTable = 
				new TableDisplay(displayWidth, displayHeight , Result.LIST_DISPLAY);
		for(int i = 0; i < tasks.size(); i++){
			taskTable.addContentToTable(tasks.get(i));	
		}
		addContent(taskTable, false);
	}
	protected void displayHistoryList(Vector<String> list){
		//TODO
	}
	private void removeExtraContent(){
		while(items.size() > MAX_NUM_ITEMS_IN_DEFAULTPANE){
			Component extra = items.removeFirst();
			defaultPane.remove(extra);
		}
	}
	protected void addPopup(Component component){
		popupPane.add(component);
	}
	protected void setFocus(){
		defaultPane.requestFocus();
	}
}
