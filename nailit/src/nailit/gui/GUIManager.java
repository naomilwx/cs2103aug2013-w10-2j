//@author A0091372H
package nailit.gui;

import nailit.AppLauncher;
import nailit.NailItGlobalKeyListener;
import nailit.logic.CommandType;

import javax.swing.ImageIcon;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.plaf.FontUIResource;

import nailit.common.NIConstants;
import nailit.common.Result;
import nailit.common.Task;
import nailit.logic.LogicManager;
import nailit.logic.exception.InvalidCommandFormatException;
import nailit.logic.exception.InvalidCommandTypeException;
import nailit.storage.FileCorruptionException;

public class GUIManager {	
	public static final String APPLICATION_NAME = "NailIt!";
	protected static final Color BORDER_COLOR = Color.gray;
	protected static final Color FOCUSED_BORDER_COLOR = Color.orange;
	protected static final int Y_BUFFER_HEIGHT = 7;
	protected static final int X_BUFFER_WIDTH = 5;
	protected static final int WINDOW_RIGHT_BUFFER = 12;
	protected static final int WINDOW_BOTTOM_BUFFER = 32;
	
	protected static final String DEFAULT_FONT = "HelveticaNeue Light";
	protected static final String DEFAULT_WINDOW_LOOKANDFEEL = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
	protected static final String DEFAULT_WINDOW_LOOKANDFEEL_FALLBACK = "javax.swing.plaf.metal.MetalLookAndFeel";
	
	protected static final Point DEFAULT_COMPONENT_LOCATION = new Point(0, 0);
	protected static final int HOME_WINDOW_WIDTH = 400;
	protected static final int HISTORY_WINDOW_WIDTH = 400;
	
	protected static final int SCREEN_BOTTOM_BUFFER = 50;
	protected static final Dimension SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize(); 
	protected static final int MAIN_WINDOW_X_POS = (SCREEN_DIMENSION.width - MainWindow.WINDOW_WIDTH - HOME_WINDOW_WIDTH)/2;
	protected static final int MAIN_WINDOW_Y_POS = Math.min(SCREEN_DIMENSION.height/2, 
			SCREEN_DIMENSION.height - SCREEN_BOTTOM_BUFFER - MainWindow.WINDOW_HEIGHT);
	
	private static final String WELCOME_MESSAGE = "Welcome to NailIt!";
	private static final String INVALID_COMMAND_ERROR_MESSAGE = "An error occured while executing your command. Check your command format";
	private static final String FAIL_TO_LOAD_ICON_IN_SYSTEM_TRAY_ERROR = "Failed to load system tray icon";
	private static final String FAIL_TO_OPEN_FONT_RESOURCE_ERROR = "Failed to open font file data";
	private static final String FONT_FORMAT_ERROR = "Problem encountered when reading font format file";
	
	private static final URL TRAY_ICON_IMG_PATH = GUIManager.class.getResource("/resources/todo.png");
//	private static final URL TRAY_ICON_IMG_PATH = GUIManager.class.getResource("/todo.png"); //TODO: change when compiling
	private static final ImageIcon TRAY_ICON_IMG = new ImageIcon(TRAY_ICON_IMG_PATH);
	private static final String NAILIT_TRAY_TOOLTIP_TEXT = "NailIt!";
	
	protected static final int ID_COLUMN_WIDTH = 45;
	protected static final int NAME_COLUMN_WIDTH = 400;
	protected static final int START_TIME_COLUMN_WIDTH = 130;
	protected static final int END_TIME_COLUMN_WIDTH = 130;
	protected static final int TOTAL_TABLE_WIDTH
	= ID_COLUMN_WIDTH + NAME_COLUMN_WIDTH + START_TIME_COLUMN_WIDTH + END_TIME_COLUMN_WIDTH;
	protected static final int[] TASKS_TABLE_COLUMN_WIDTH = 
		{ID_COLUMN_WIDTH, START_TIME_COLUMN_WIDTH, END_TIME_COLUMN_WIDTH, NAME_COLUMN_WIDTH};
	protected static final int TASK_NAME_COLUMN_NUMBER = 3;
	
	protected static final String ID_COL_NAME = "ID";
	protected static final String TASK_NAME_COL_NAME = "Task Name";
	protected static final String TASK_START_TIME_COL_NAME = "Start";
	protected static final String TASK_END_TIME_COL_NAME = "End";
	protected static final String[] ALL_TASKS_TABLE_HEADER = 
		{ID_COL_NAME, TASK_START_TIME_COL_NAME, TASK_END_TIME_COL_NAME, TASK_NAME_COL_NAME};
	public static final String DELETED_TASK_DISPLAY_ID = "DEL";
	
	protected static Font DEFAULT_FONT_OBJECT;
	
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
	
	public GUIManager(final AppLauncher launcher){
		try{
			this.launcher = launcher;
			logger = AppLauncher.getLogger();
			loadRequiredFontsInGraphicsEnvironmentAndInitialiseDefaultFont();
			setWindowLookAndFeel();
			initialiseAndSetDefaultFont();
			createComponentsAndAddToMainFrame();
			initialiseExtendedWindows();
			showInSystemTray(this);
//			globalKeyListener = new NailItGlobalKeyListener(this);
			logicExecutor = new LogicManager();
			showDefaultDisplayAndReminders();
		}catch(FileCorruptionException e){
			logger.info("Storage file corrupted.");
			displayNotificationAndForceExit("File corrupted. Delete NailIt's storage file and restart NailIt");
		}catch(Exception e){
			//TODO:
			e.printStackTrace();
		}
	}

	private void showDefaultDisplayAndReminders(){
		if(!getAndDisplayReminders()){
			homeWindow.setVisible(false);
		}
		processAndDisplayExecutionResult(logicExecutor.getListOfTasksForTheDay());
	}
	private boolean getAndDisplayReminders(){
		Vector<Vector <Task>> reminderList = logicExecutor.getReminderList();
		return homeWindow.displayReminders(reminderList);
	}	
	private void getAndRefreshHistoryDisplay(){
		Vector<Vector <String>> commandHistory = logicExecutor.getCommandList();
		historyWindow.displayHistoryList(commandHistory, false);
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
		mainWindow.setIconImage(TRAY_ICON_IMG.getImage());
		commandBar = new CommandBar(this, mainWindow.getWidth(), mainWindow.getHeight());
		initialiseAndConfigureDisplayArea();
		loadComponentsUntoMainFrame();
	}
	private void initialiseAndConfigureDisplayArea(){
		displayArea = new DisplayArea(this, mainWindow.getWidth(), mainWindow.getHeight(), commandBar.getHeight());
		notificationArea = new NotificationArea(displayArea.getWidth());
		displayArea.addPopup(notificationArea);
		displayArea.hideNotificationsPane();
	}
	private void loadComponentsUntoMainFrame(){
		mainWindow.addItem(commandBar);
		mainWindow.addItem(displayArea);
	}
	protected void resizeMainDisplayArea(){
		displayArea.dynamicallyResizeAndRepositionDisplayArea(commandBar.getHeight());
	}
	protected void reduceMainWindowSize(){
		mainWindow.transformIntoReducedWindow();
		commandBar.resizeToFitMainContainer(mainWindow.getWidth(), mainWindow.getHeight());
		displayArea.removeTaskDisplay();
		displayArea.resizeDisplayToFitMainContainer(mainWindow.getWidth(), mainWindow.getHeight());
	}
	protected void restoreMainWindowSize(){
		mainWindow.restoreDefaultWindow();
		commandBar.resizeToFitMainContainer(mainWindow.getWidth(), mainWindow.getHeight());
		displayArea.resizeDisplayToFitMainContainer(mainWindow.getWidth(), mainWindow.getHeight());
	}
	protected int getCommandBarHeight(){
		return commandBar.getFrameHeight();
	}
	public void enableGlobalKeyListener(){
//		if(!globalKeyListener.isEnabled()){
//			globalKeyListener.registerGlobalKeyHook();
//		}
	}
	
	//functions to manipulate visiblitiy of GUI Components
	public void setVisible(boolean isVisible){
		if(!isVisible){
			enableGlobalKeyListener();
		}else{
			setFocusOnCommandBar();
		}
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
			if(historyWindow.isVisible()){
				getAndRefreshHistoryDisplay();
			}
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
	protected int getDisplayAreaHeight(){
		return displayArea.getHeight();
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
	protected void taskTableScrollDown(){
		displayArea.taskTableScroll(false);
	}
	protected void taskTableScollUp(){
		displayArea.taskTableScroll(true);
	}
	//functions to handle user commands from command bar or keyboard shortcut
	/**
	 * Executes command entered by user
	 * @param input
	 */
	protected Result executeUserInputCommand(String input){
		Result executionResult = null;
		try{
			executionResult = logicExecutor.executeCommand(input);
			assert executionResult != null;
			displayCommandFeedback(executionResult);
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
		}catch(InvalidCommandFormatException e){
			helpWindow.displaySyntaxForCommandType(e.getCommandType());
		}catch(InvalidCommandTypeException e){
			helpWindow.displayListOfAvailableCommands();
		}catch(Exception e){
			displayNotification(INVALID_COMMAND_ERROR_MESSAGE, false);
			e.printStackTrace(); //TODO:
		}
		return executionResult;
	}
	
	private void displayCommandFeedback(Result executionResult){
		clearUserInputAndCleanUpDisplay();
		processAndDisplayExecutionResult(executionResult);
		resizeMainDisplayArea();
	}
	private void clearUserInputAndCleanUpDisplay(){
		commandBar.clearUserInput();
		displayArea.hideNotificationsPane();
		displayArea.removeDeletedTasksFromTaskListTable();
		displayArea.removeTaskDisplay(); //TODO:
	}
	
	//functions to execute commands via keyboard shortcuts. may be refactored as a separate unit later
	protected void executeTriggeredTaskDelete(int taskDisplayID) {
		try {
			Result delCommandResult = logicExecutor.executeDirectIDCommand(CommandType.DELETE, taskDisplayID);
			displayCommandFeedback(delCommandResult);
		} catch (Exception e) {
			e.printStackTrace(); //TODO:
		}
	}
	protected Result executeTriggeredTaskDisplay(int taskDisplayID){
		try {
			Result displayCommandResult = logicExecutor.executeDirectIDCommand(CommandType.DISPLAY, taskDisplayID);
			displayCommandFeedback(displayCommandResult);
			return displayCommandResult;
		} catch (Exception e) {
			e.printStackTrace(); //TODO:
			return null;
		}
	}
	protected void loadExistingTaskDescriptionInCommandBar(){
		int displayID = displayArea.getTaskTableSelectedRowID();
		if(displayID > 0){
			loadExistingTaskDescriptionInCommandBar(displayID);
		}
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
	protected void loadExistingTaskNameInCommandBar(){
		int displayID = displayArea.getTaskTableSelectedRowID();
		if(displayID > 0){
			loadExistingTaskNameInCommandBar(displayID);
		}
	}
	protected void loadExistingTaskNameInCommandBar(int taskDisplayID){
		Result result = executeTriggeredTaskDisplay(taskDisplayID);
		if(result !=  null){
			Task task = result.getTaskToDisplay();
			if(task != null){
				commandBar.setUserInput(CommandType.UPDATE.toString() +" "+ taskDisplayID
						+" " + "name " + task.getName());
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
			if(historyWindow.isVisible()){
				getAndRefreshHistoryDisplay();
			}
			displayExecutionResult(result);
		}
	}
	private void handleTaskDisplay(Result result){
		restoreMainWindowSize();
		displayArea.displayTaskDetails(result.getTaskToDisplay());
	}
	private void handleTaskListDisplay(Result result){
		displayArea.displayTaskList(result);
	}
	private void handleHistoryDisplay(Result result){
		historyWindow.displayHistoryList(result.getHistoryList(), true);
	}
	private void handleExecutionResultDisplay(Result result){
		displayArea.displayExecutionResultDisplay(result);
	}
	protected void displayExecutionResult(Result result){
		displayExecutionNotification(result);
		int displayType = result.getDisplayType();
		switch (displayType){
			case Result.TASK_DISPLAY:
				handleTaskDisplay(result);
				break;
			case Result.LIST_DISPLAY:
				handleTaskListDisplay(result);
				break;
			case Result.HISTORY_DISPLAY:
				handleHistoryDisplay(result);
				break;
			case Result.EXECUTION_RESULT_DISPLAY:
				handleExecutionResultDisplay(result);
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
			displayArea.hideNotificationsPane();
		}
	}
	private void displayNotification(String notificationStr, boolean isSuccess){
		notificationArea.displayNotification(notificationStr, isSuccess);
		displayArea.showNotificationsPane();
	}
	
	private void displayNotificationAndForceExit(String notificationStr){
		notificationArea.displayNotification(notificationStr, false);
		displayArea.showNotificationsPaneAndForceExit();
	}
	
	private void exit(){
		launcher.exit();
	}
	
	protected void forceExit(){
		launcher.forceExit();
	}
	
	private void setWindowLookAndFeel() throws Exception{
		try {
			for(LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
		        if (info.getClassName().equals(DEFAULT_WINDOW_LOOKANDFEEL)){
		            UIManager.setLookAndFeel(DEFAULT_WINDOW_LOOKANDFEEL);
		            break;
		        }
		    }
		} catch (InstantiationException e) {
			if(e.getMessage() != null){
				logger.info(e.getMessage());
			}
		} catch (IllegalAccessException e) {
			if(e.getMessage() != null){
				logger.info(e.getMessage());
			}
		} catch (UnsupportedLookAndFeelException e) {
			try{
				UIManager.setLookAndFeel(DEFAULT_WINDOW_LOOKANDFEEL_FALLBACK);
			}catch(Exception e1){
				throw new Exception("Unable to load window look and feel");
			}
		}
	}
	private void initialiseAndSetDefaultFont(){
		DEFAULT_FONT_OBJECT = new Font(DEFAULT_FONT, Font.PLAIN, 14);
		UIDefaults defaultUI = UIManager.getLookAndFeelDefaults();
		defaultUI.put("defaultFont", new FontUIResource(DEFAULT_FONT_OBJECT));
	}
	private void loadRequiredFontsInGraphicsEnvironmentAndInitialiseDefaultFont(){
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			Font helveticaN = Font.createFont(Font.TRUETYPE_FONT, GUIManager.class.getResourceAsStream("/fonts/HelveticaNeue_Lt.ttf"));
			Font helveticaR = Font.createFont(Font.TRUETYPE_FONT, GUIManager.class.getResourceAsStream("/fonts/Helvetica_Reg.ttf"));
			Font Lucida = Font.createFont(Font.TRUETYPE_FONT, GUIManager.class.getResourceAsStream("/fonts/Lucida_Grande.ttf"));
			env.registerFont(helveticaN);
			env.registerFont(helveticaR);
			env.registerFont(Lucida);
		} catch (FontFormatException e) {
			logger.info(FONT_FORMAT_ERROR);
			logger.info(e.getMessage());
		} catch (IOException e) {
			logger.info(FAIL_TO_OPEN_FONT_RESOURCE_ERROR);
		}
	}
	private void showInSystemTray(final GUIManager GUIBoss){
		if(SystemTray.isSupported()){
			final SystemTray systemTray = SystemTray.getSystemTray();
			Image trayImage = TRAY_ICON_IMG.getImage();
			final TrayIcon trayIcon = new TrayIcon(trayImage, NAILIT_TRAY_TOOLTIP_TEXT);

			MenuItem showMain = new MenuItem("Main");
			MenuItem showHome = new MenuItem("Reminders");
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
				logger.info(FAIL_TO_LOAD_ICON_IN_SYSTEM_TRAY_ERROR);
			}
		}
	}

}
