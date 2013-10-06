package nailit.gui;

import nailit.AppLauncher;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.BorderLayout;
import java.awt.Point;

import javax.swing.JPanel;

import java.awt.Color;
import java.util.Vector;

import javax.swing.border.LineBorder;
import javax.swing.text.Utilities;

import nailit.common.Result;
import nailit.common.Task;
import nailit.logic.LogicManager;

public class GUIManager {	
	public static final String APPLICATION_NAME = "NailIt!";
	protected static final Color BORDER_COLOR = Color.black;
	protected static final int Y_BUFFER_HEIGHT = 10;
	protected static final int X_BUFFER_WIDTH = 5;
	protected static final int WINDOW_RIGHT_BUFFER = 12;
	protected static final int WINDOW_BOTTOM_BUFFER = 35;
	protected static final int MAIN_WINDOW_X_POS = 100;
	protected static final int MAIN_WINDOW_Y_POS = 100;
	protected static final String DEFAULT_WINDOW_LOOKANDFEEL = "javax.swing.plaf.metal.MetalLookAndFeel";
	protected static final Point DEFAULT_COMPONENT_LOCATION = new Point(0, 0);
	private static final String WELCOME_MESSAGE = "Welcome to NailIt!";
	
	protected static final int ID_COLUMN_WIDTH = 50;
	protected static final int NAME_COLUMN_WIDTH = 300;
	protected static final int TIME_COLUMN_WIDTH = 180;
	protected static final int TAG_COLUMN_WIDTH = 80;
	protected static final int STATUS_COLUMN_WIDTH = 50;
//	protected static final int TOTAL_TABLE_WIDTH
//		= ID_COLUMN_WIDTH + NAME_COLUMN_WIDTH + TIME_COLUMN_WIDTH 
//		+ TAG_COLUMN_WIDTH + STATUS_COLUMN_WIDTH;
	protected static final int TOTAL_TABLE_WIDTH
	= ID_COLUMN_WIDTH + NAME_COLUMN_WIDTH + TIME_COLUMN_WIDTH + STATUS_COLUMN_WIDTH;
	protected static final int COMMAND_COLUMN_WIDTH = TOTAL_TABLE_WIDTH - ID_COLUMN_WIDTH;
//	protected static final int[] TASKS_TABLE_COLUMN_WIDTH = 
//		{ID_COLUMN_WIDTH, NAME_COLUMN_WIDTH, TIME_COLUMN_WIDTH, TAG_COLUMN_WIDTH, STATUS_COLUMN_WIDTH};
	protected static final int[] TASKS_TABLE_COLUMN_WIDTH = 
		{ID_COLUMN_WIDTH, NAME_COLUMN_WIDTH, TIME_COLUMN_WIDTH, STATUS_COLUMN_WIDTH};
	protected static final int[] COMMAND_HISTORY_COLUMN_WIDTH =
		{ID_COLUMN_WIDTH, COMMAND_COLUMN_WIDTH};
	
//	protected static final String[] ALL_TASKS_TABLE_HEADER = 
//		{"ID", "Task Name" ,"Time" , "Tag", "Status"};
	protected static final String[] ALL_TASKS_TABLE_HEADER = 
		{"ID", "Task Name" ,"Time" , "Status"};
	protected static final String[] COMMAND_HISTORY_HEADER =
		{"ID", "Command"};
	
	private MainWindow mainWindow;
	private CommandBar commandBar;
	private DisplayArea displayArea;
	private NotificationArea notificationArea;
	
	private AppLauncher launcher;
	private LogicManager logicExecutor;
	
	/**
	 * Create the application.
	 */
	public GUIManager(final AppLauncher launcher) {
		setWindowLookAndFeel();
		this.launcher = launcher;
		//TODO:
//		logicExecutor = new LogicManager();
		createComponentsAndAddToMainFrame();
		//stub to be modified later
	}
	
	private void createComponentsAndAddToMainFrame() {
		mainWindow = new MainWindow(this);
		commandBar = new CommandBar(this, mainWindow.getWidth(), mainWindow.getHeight());
		initialiseAndConfigureDisplayArea();
		loadComponentsUntoMainFrame();
	}
	private void initialiseAndConfigureDisplayArea(){
		displayArea = new DisplayArea(this, mainWindow.getWidth(), mainWindow.getHeight());
		notificationArea = new NotificationArea(displayArea.getWidth());
		notificationArea.displayNotification(GUIManager.WELCOME_MESSAGE, true);
		displayArea.addPopup(notificationArea);
	}
	private void loadComponentsUntoMainFrame(){
		mainWindow.addItem(commandBar);
		mainWindow.addItem(displayArea);
	}
	public void setVisible(boolean isVisible){
		mainWindow.setVisible(isVisible);
	}
	
	protected void setFocusOnDisplay(){
		displayArea.setFocus();
	}
	protected void setFocusOnCommandBar(){
		commandBar.setFocus();
	}
	/**
	 * Executes command entered by user
	 * @param input
	 */
	protected void executeUserInputCommand(String input){
		TextDisplay testpane = new TextDisplay(displayArea.getWidth(), displayArea.getHeight());
		testpane.basicDisplay(input);
		displayArea.addContent(testpane, false);
		TableDisplay test = 
				new TableDisplay(displayArea.getWidth(),displayArea.getHeight(), Result.LIST_DISPLAY);
		displayArea.addContent(test, false);
		System.out.println(input);
		commandBar.clearUserInput();
//		logicExecutor.executeCommand(input);
	}
	protected void processAndDisplayExecutionResult(Result result){
		int displayType = result.getDisplayType();
		switch (displayType){
			case Result.LIST_DISPLAY:
				displayTaskDetails(result.getTaskList());
				break;
			case Result.TASK_DISPLAY:
				displayTaskList(result.getTaskList());
				break;
			case Result.HISTORY_DISPLAY:
				displayHistoryList(result.getHistoryList());
				break;
			default:
				displayExecutionNotification(result);
				break;
		}
	}
	private void displayExecutionNotification(Result result){
		String notificationStr = result.getPrintOut();
		boolean isSuccess = result.getExecutionSuccess();
		if(!notificationStr.isEmpty()){
			notificationArea.displayNotification(notificationStr, isSuccess);
		}
	}
	private void displayTaskDetails(Vector<Task> task){
		String taskDisplay = task.toString();
		displayArea.displayTaskDetails(taskDisplay);
	}
	private void displayTaskList(Vector<Task> tasks){
		
	}
	private void displayHistoryList(Vector<String> list){
		
	}
	private void exit(){
		launcher.exit();
	}

	private void setWindowLookAndFeel(){
		try {
			UIManager.setLookAndFeel(DEFAULT_WINDOW_LOOKANDFEEL);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
