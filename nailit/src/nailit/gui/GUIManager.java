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

import test.logic.LogicManagerStub;
import nailit.common.Result;
import nailit.common.Task;
import nailit.logic.LogicManager;
import nailit.storage.FileCorruptionException;

public class GUIManager {	
	public static final String APPLICATION_NAME = "NailIt!";
	protected static final Color BORDER_COLOR = Color.black;
	protected static final Color FOCUSED_BORDER_COLOR = Color.orange;
	protected static final int Y_BUFFER_HEIGHT = 10;
	protected static final int X_BUFFER_WIDTH = 5;
	protected static final int WINDOW_RIGHT_BUFFER = 12;
	protected static final int WINDOW_BOTTOM_BUFFER = 35;
	protected static final int MAIN_WINDOW_X_POS = 100;
	protected static final int MAIN_WINDOW_Y_POS = 100;
	protected static final int HOME_WINDOW_X_POS = MAIN_WINDOW_X_POS + MainWindow.WINDOW_WIDTH + WINDOW_RIGHT_BUFFER;
	protected static final int HOME_WINDOW_Y_POS = MAIN_WINDOW_Y_POS;
	protected static final String DEFAULT_WINDOW_LOOKANDFEEL = "javax.swing.plaf.metal.MetalLookAndFeel";
	protected static final Point DEFAULT_COMPONENT_LOCATION = new Point(0, 0);
	
	private static final String WELCOME_MESSAGE = "Welcome to NailIt!";
	
	protected static final int ID_COLUMN_WIDTH = 50;
	protected static final int NAME_COLUMN_WIDTH = 300;
	protected static final int TIME_COLUMN_WIDTH = 180;
	protected static final int TAG_COLUMN_WIDTH = 80;
	protected static final int STATUS_COLUMN_WIDTH = 50;
	protected static final int TOTAL_TABLE_WIDTH
	= ID_COLUMN_WIDTH + NAME_COLUMN_WIDTH + TIME_COLUMN_WIDTH + STATUS_COLUMN_WIDTH;
	protected static final int COMMAND_COLUMN_WIDTH = TOTAL_TABLE_WIDTH - ID_COLUMN_WIDTH;
	protected static final int[] TASKS_TABLE_COLUMN_WIDTH = 
		{ID_COLUMN_WIDTH, NAME_COLUMN_WIDTH, TIME_COLUMN_WIDTH, STATUS_COLUMN_WIDTH};
	protected static final int[] COMMAND_HISTORY_COLUMN_WIDTH =
		{ID_COLUMN_WIDTH, COMMAND_COLUMN_WIDTH};
	
	protected static final String ID_COL_NAME = "ID";
	protected static final String TASK_NAME_COL_NAME = "Task Name";
	protected static final String TASK_TIME_DET_COL_NAME = "Time";
	protected static final String TASK_STATUS_COL_NAME = "Status";
	protected static final String COMMAND_COL_NAME = "Command";
	protected static final String[] ALL_TASKS_TABLE_HEADER = 
		{ID_COL_NAME, TASK_NAME_COL_NAME, TASK_TIME_DET_COL_NAME, TASK_STATUS_COL_NAME};
	protected static final String[] COMMAND_HISTORY_HEADER =
		{ID_COL_NAME, COMMAND_COL_NAME};
	
	private MainWindow mainWindow;
	private CommandBar commandBar;
	private DisplayArea displayArea;
	private NotificationArea notificationArea;
	private HomeWindow homeWindow;
	
	private AppLauncher launcher;
	private LogicManager logicExecutor;
	
	public GUIManager(final AppLauncher launcher) {
		try{
			setWindowLookAndFeel();
			this.launcher = launcher;
			//TODO:
	//		logicExecutor = new LogicManager();
			logicExecutor = new LogicManagerStub();
			createComponentsAndAddToMainFrame();
//			createAndDisplayHomeWindow();
		}catch(FileCorruptionException e){
			
		}
	}
	
	private void createAndDisplayHomeWindow() {
		homeWindow = new HomeWindow(this);
		homeWindow.setVisible(true);
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
	
	public void setFocusOnDisplay(){
		displayArea.setFocus();
	}
	public void setFocusOnCommandBar(){
		commandBar.setFocus();
	}
	/**
	 * Executes command entered by user
	 * @param input
	 */
	protected void executeUserInputCommand(String input){
		System.out.println(input);
		displayArea.hideNotifications();
		Result executionResult = logicExecutor.executeCommand(input);
		if(executionResult == null){
			System.out.println("die!");
		}
		commandBar.clearUserInput();
		processAndDisplayExecutionResult(executionResult);
	}
	protected void processAndDisplayExecutionResult(Result result){
		if(result.getExitStatus()){
			exit();
		}else{
			displayExecutionResult(result);
		}
	}
	protected void displayExecutionResult(Result result){
		int displayType = result.getDisplayType();
		switch (displayType){
			case Result.TASK_DISPLAY:
				displayTaskDetails(result.getTaskList());
				break;
			case Result.LIST_DISPLAY:
				displayArea.displayTaskList(result.getTaskList());
				break;
			case Result.HISTORY_DISPLAY:
				displayArea.displayHistoryList(result.getHistoryList());
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
		displayArea.showNotifications();
	}
	private void displayTaskDetails(Vector<Task> taskList){
		assert taskList.size() > 0;
		Task task = taskList.firstElement();
		String taskDisplay = task.toString();
		displayArea.displayTaskDetails(taskDisplay);
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
