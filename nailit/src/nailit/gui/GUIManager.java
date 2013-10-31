//@author A0091372H
package nailit.gui;

import nailit.AppLauncher;
import nailit.NailItGlobalKeyListener;
import nailit.logic.CommandType;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.border.LineBorder;
import javax.swing.text.Utilities;

import test.logic.LogicManagerStub;
import nailit.common.NIConstants;
import nailit.common.Result;
import nailit.common.Task;
import nailit.logic.LogicManager;
import nailit.storage.FileCorruptionException;

public class GUIManager {	
	public static final String APPLICATION_NAME = "NailIt!";
	protected static final Color BORDER_COLOR = Color.black;
	protected static final Color FOCUSED_BORDER_COLOR = Color.orange;
	protected static final int Y_BUFFER_HEIGHT = 7;
	protected static final int X_BUFFER_WIDTH = 5;
	protected static final int WINDOW_RIGHT_BUFFER = 12;
	protected static final int WINDOW_BOTTOM_BUFFER = 32;
	protected static final int MAIN_WINDOW_X_POS = 100;
	protected static final int MAIN_WINDOW_Y_POS = 150;
	
	protected static final String DEFAULT_WINDOW_LOOKANDFEEL = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
	protected static final String DEFAULT_WINDOW_LOOKANDFEEL_FALLBACK = "javax.swing.plaf.metal.MetalLookAndFeel";
	protected static final Point DEFAULT_COMPONENT_LOCATION = new Point(0, 0);
	protected static final int HOME_WINDOW_WIDTH = 400;
	protected static final int HISTORY_WINDOW_WIDTH = 400;
	
	private static final String WELCOME_MESSAGE = "Welcome to NailIt!";
	private static final String INVALID_COMMAND_ERROR_MESSAGE = "An error occured while executing your command. Check your command format";
	private static final URL TRAY_ICON_IMG_PATH = GUIManager.class.getResource("todo.png");
	private static final ImageIcon TRAY_ICON_IMG = new ImageIcon(TRAY_ICON_IMG_PATH);
	private static final String NAILIT_TRAY_TOOLTIP_TEXT = "NailIt!";
	
	protected static final int ID_COLUMN_WIDTH = 45;
	protected static final int NAME_COLUMN_WIDTH = 400;
	protected static final int START_TIME_COLUMN_WIDTH = 130;
	protected static final int END_TIME_COLUMN_WIDTH = 130;
	protected static final int TOTAL_TABLE_WIDTH
	= ID_COLUMN_WIDTH + NAME_COLUMN_WIDTH + START_TIME_COLUMN_WIDTH + END_TIME_COLUMN_WIDTH;
	protected static final int COMMAND_COLUMN_WIDTH = TOTAL_TABLE_WIDTH - ID_COLUMN_WIDTH;
	protected static final int[] TASKS_TABLE_COLUMN_WIDTH = 
		{ID_COLUMN_WIDTH, NAME_COLUMN_WIDTH, START_TIME_COLUMN_WIDTH, END_TIME_COLUMN_WIDTH};
	protected static final int[] COMMAND_HISTORY_COLUMN_WIDTH =
		{ID_COLUMN_WIDTH, COMMAND_COLUMN_WIDTH};
	
	protected static final String ID_COL_NAME = "ID";
	protected static final String TASK_NAME_COL_NAME = "Task Name";
	protected static final String TASK_START_TIME_COL_NAME = "Start";
	protected static final String TASK_END_TIME_COL_NAME = "End";
	protected static final String COMMAND_COL_NAME = "Command";
	protected static final String[] ALL_TASKS_TABLE_HEADER = 
		{ID_COL_NAME, TASK_NAME_COL_NAME, TASK_START_TIME_COL_NAME, TASK_END_TIME_COL_NAME};
	protected static final String[] COMMAND_HISTORY_HEADER =
		{ID_COL_NAME, COMMAND_COL_NAME};
	public static final String DELETED_TASK_DISPLAY_ID = "DEL";
	
	private MainWindow mainWindow;
	private CommandBar commandBar;
	private DisplayArea displayArea;
	private NotificationArea notificationArea;
	private HomeWindow homeWindow;
	private HistoryWindow historyWindow;
	private HelpWindow helpWindow;
	
	private AppLauncher launcher;
	private LogicManager logicExecutor;
	private NailItGlobalKeyListener globalKeyListener;
	private Logger logger;
	
	public GUIManager(final AppLauncher launcher) {
		try{
			this.launcher = launcher;
			logger = AppLauncher.getLogger();
			setWindowLookAndFeel();
			createComponentsAndAddToMainFrame();
			initialiseExtendedWindows();
			showInSystemTray(this);
//			globalKeyListener = new NailItGlobalKeyListener(this);
			logicExecutor = new LogicManager();
			showDefaultDisplayAndReminders();
//			helpWindow.displaySyntaxForCommandType("add"); //testing: to be removed later
		}catch(FileCorruptionException e){
			logger.info("Storage file corrupted.");
			displayNotification("File corrupted. Delete NailIt's storage file and restart NailIt", false);
		}catch(Exception e){
			//TODO:
			e.printStackTrace();
		}
	}
	
	private void showDefaultDisplayAndReminders(){
		getAndDisplayReminders();
		processAndDisplayExecutionResult(logicExecutor.getListOfTasksForTheDay());
	}
	private void getAndDisplayReminders(){
		Vector<Task> reminderList = logicExecutor.getReminderList();
		updateReminderDisplay(reminderList);
	}
	private void updateReminderDisplay(Vector<Task> reminders){
		homeWindow.displayReminders(reminders);
	}
	//functions to initialise and configure GUI components
	private void initialiseExtendedWindows(){
		createAndDisplayHomeWindow();
		historyWindow = new HistoryWindow(this, HISTORY_WINDOW_WIDTH);
		helpWindow = new HelpWindow(this, MainWindow.WINDOW_WIDTH);
	}
	private void createAndDisplayHomeWindow() {
		homeWindow = new HomeWindow(this, HOME_WINDOW_WIDTH);
		homeWindow.setVisible(true);
	}
	private void createComponentsAndAddToMainFrame() {
		mainWindow = new MainWindow(this);
		commandBar = new CommandBar(this, mainWindow.getWidth(), mainWindow.getHeight());
		initialiseAndConfigureDisplayArea();
		loadComponentsUntoMainFrame();
	}
	private void initialiseAndConfigureDisplayArea(){
		displayArea = new DisplayArea(this, mainWindow.getWidth(), mainWindow.getHeight(), commandBar.getHeight());
		notificationArea = new NotificationArea(displayArea.getWidth());
		displayArea.addPopup(notificationArea);
		displayArea.hideNotifications();
	}
	private void loadComponentsUntoMainFrame(){
		mainWindow.addItem(commandBar);
		mainWindow.addItem(displayArea);
	}
	protected void resizeMainDisplayArea(){
		displayArea.dynamicallyResizeDisplayArea(commandBar.getHeight());
	}
	public void enableGlobalKeyListener(){
		if(!globalKeyListener.isEnabled()){
			globalKeyListener.registerGlobalKeyHook();
		}
	}
	
	//functions to manipulate visiblitiy of GUI Components
	public void setVisible(boolean isVisible){
//		if(!isVisible){
//			enableGlobalKeyListener();
//		}
		mainWindow.setVisible(isVisible);
	}
	
	public void toggleHomeWindow(){
		if(homeWindow != null){
			boolean currentVisibility = homeWindow.isVisible();
			homeWindow.setVisible(!currentVisibility);
			if(homeWindow.isVisible()){
				getAndDisplayReminders();
			}
		}
	}
	protected void toggleHistoryWindow(){
		if(historyWindow != null){
			boolean currentVisibility = historyWindow.isVisible();
			historyWindow.setVisible(!currentVisibility);
		}
	}
	protected void hideHistoryWindow(){
		historyWindow.setVisible(false);
	}
	protected void showHelpWindow(){
		helpWindow.setVisible(true);
	}
	protected void hideHelpWindow(){
		helpWindow.setVisible(false);
	}
	//functions to set focus of GUI Components
	public void setFocusOnDisplay(){
		removeDeletedTaskFromTaskListDisplay();
		displayArea.setFocus();
	}
	public void setFocusOnCommandBar(){
		commandBar.setFocus();
	}
	public void setFocusOnHomeWindow(){
		homeWindow.requestFocus();
	}
	public void setFocusOnHelpWindow(){
		helpWindow.requestFocus();
	}
	
	protected Dimension getMainWindowLocationCoordinates(){
		return new Dimension(mainWindow.getX(), mainWindow.getY());
	}
	protected boolean getMainWindowVisibility(){
		return mainWindow.isVisible();
	}
	
	protected void displayCommandSyantaxHelpWindow(){
		helpWindow.displaySyntaxForSupportedCommands();
	}
	
	protected void displayFullHelpWindow(){
		helpWindow.displayFullHelpWindow();
	}
	
	protected void scrollToNextPageInTaskTable(){
		displayArea.quickTaskTableScroll(false);
	}
	protected void scrollToPrevPageInTaskTable(){
		displayArea.quickTaskTableScroll(true);
	}
	//functions to handle user commands from command bar or keyboard shortcut
	/**
	 * Executes command entered by user
	 * @param input
	 */
	protected Result executeUserInputCommand(String input){
		Result executionResult = null;
		try{
			displayArea.hideNotifications();
			executionResult = logicExecutor.executeCommand(input);
			if(executionResult == null){
				System.out.println("die!");
			}
			assert executionResult != null;
			clearUserInputAndCleanUpDisplay();
			processAndDisplayExecutionResult(executionResult);
			resizeMainDisplayArea();
		}catch(Error err){
			String notificationStr = err.getMessage();
			if(notificationStr == null){
				notificationStr = "";
				err.printStackTrace(); //TODO:
			}
			if(!notificationStr.isEmpty()){
				//temp code to change after better exception handling in parser is implemented
				if(notificationStr.equals("Unrecognized command type")){
					helpWindow.displayListOfAvailableCommands();
				}
				//end of temp code
				notificationStr = INVALID_COMMAND_ERROR_MESSAGE + "\n " + notificationStr;
				displayNotification(notificationStr, false);
			}
			err.printStackTrace();
		}catch(Exception e){
			displayNotification(INVALID_COMMAND_ERROR_MESSAGE, false);
			e.printStackTrace(); //TODO:
		}
		return executionResult;
	}
	
	private void clearUserInputAndCleanUpDisplay(){
		commandBar.clearUserInput();
		displayArea.removeDeletedTasksFromTaskListTable();
		displayArea.removeTaskDisplay();
	}
	
	//functions to execute commands via keyboard shortcuts. may be refactored as a separate unit later
	protected void executeTriggeredTaskDelete(int taskDisplayID) {
		String deleteCommand = CommandType.DELETE.toString()+ " " + taskDisplayID;
		executeUserInputCommand(deleteCommand);
		return;
	}
	protected Result executeTriggeredTaskDisplay(int taskDisplayID){
		String displayCommand = CommandType.DISPLAY.toString() + " " + taskDisplayID;
		return executeUserInputCommand(displayCommand);
	}
	protected void loadExistingTaskDescriptionInCommandBar(int taskDisplayID){
		Result result = executeTriggeredTaskDisplay(taskDisplayID);
		if(result !=  null){
			Task task = result.getTaskToDisplay();
			if(task != null){
				commandBar.setUserInput(CommandType.UPDATE.toString() +" "+ taskDisplayID
						+" " + "description " + task.getDescription());
			}
		}
	}
	//
	protected void removeTaskDisplay(){
		displayArea.removeTaskDisplay();
	}
	protected void removeDeletedTaskFromTaskListDisplay(){
		displayArea.removeDeletedTasksFromTaskListTable();
	}
	protected void processAndDisplayExecutionResult(Result result){
		if(result.getExitStatus()){
			exit();
		}else{
			if(homeWindow.isVisible()){
				getAndDisplayReminders();
			}
			displayExecutionResult(result);
		}
	}
	protected void displayExecutionResult(Result result){
		displayExecutionNotification(result);
		int displayType = result.getDisplayType();
		switch (displayType){
			case Result.TASK_DISPLAY:
				displayArea.displayTaskDetails(result.getTaskToDisplay());
				break;
			case Result.LIST_DISPLAY:
				displayArea.displayTaskList(result);
				break;
			case Result.HISTORY_DISPLAY:
				historyWindow.displayHistoryList(result.getHistoryList());
				historyWindow.setVisible(true);
				break;
			case Result.EXECUTION_RESULT_DISPLAY:
				displayArea.displayTaskList(result);
				if(result.getDeleteStatus() == true){
					displayArea.showDeletedTaskInTaskListTable(result.getTaskToDisplay());
				}else{
					displayArea.displayTaskDetails(result.getTaskToDisplay());
				}
				break;
			default:
				break;
		}
	}
	private void displayExecutionNotification(Result result){
		String notificationStr = result.getPrintOut();
		boolean isSuccess = result.getExecutionSuccess();
		if(!notificationStr.isEmpty()){
			displayNotification(notificationStr, isSuccess);
		}else{
			displayArea.hideNotifications();
		}
	}
	private void displayNotification(String notificationStr, boolean isSuccess){
		notificationArea.displayNotification(notificationStr, isSuccess);
		displayArea.showNotifications();
	}
	
	private void exit(){
		launcher.exit();
	}

	private void setWindowLookAndFeel() throws Exception{
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
			try{
				UIManager.setLookAndFeel(DEFAULT_WINDOW_LOOKANDFEEL_FALLBACK);
			}catch(Exception e1){
				throw new Exception("Unable to load window look and feel");
			}
		}
	}
	
	private void showInSystemTray(final GUIManager GUIBoss){
		if(SystemTray.isSupported()){
			final SystemTray systemTray = SystemTray.getSystemTray();
			Image trayImage = TRAY_ICON_IMG.getImage();
			final TrayIcon trayIcon = new TrayIcon(trayImage, NAILIT_TRAY_TOOLTIP_TEXT);

			MenuItem showMain = new MenuItem("Main");
			MenuItem showHome = new MenuItem("Home");
			MenuItem exitApp = new MenuItem("Exit");
			final PopupMenu menu = new PopupMenu();
			menu.add(showMain);
			menu.add(showHome);
			menu.add(exitApp);

			trayIcon.setPopupMenu(menu);
			trayIcon.setImageAutoSize(true);
			
			showMain.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent event) {
					mainWindow.setVisible(true);
				}
			});
			
			showHome.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					toggleHomeWindow();
				}
				
			});
			
			exitApp.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					GUIBoss.exit();
				}
				
			});
			
			try {
				systemTray.add(trayIcon);
			} catch (AWTException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
