//@author A0091372H
package nailit.gui;

import nailit.AppLauncher;
import nailit.NailItGlobalKeyListener;

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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.plaf.FontUIResource;

import org.jnativehook.NativeHookException;

import nailit.common.CommandType;
import nailit.common.Result;
import nailit.common.Task;
import nailit.logic.LogicManager;
import nailit.logic.exception.InvalidCommandFormatException;
import nailit.logic.exception.InvalidCommandTypeException;
import nailit.storage.FileCorruptionException;

public class GUIManager {	
	public static final String APPLICATION_NAME = "NailIt!";
	protected static final String DEFAULT_FONT = "HelveticaNeue Light";
	protected static final String DEFAULT_WINDOW_LOOKANDFEEL = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
	protected static final String DEFAULT_WINDOW_LOOKANDFEEL_FALLBACK = "javax.swing.plaf.metal.MetalLookAndFeel";
	protected static final Point DEFAULT_COMPONENT_LOCATION = new Point(0, 0);
	
	protected static final Color BORDER_COLOR = Color.gray;
	protected static final Color FOCUSED_BORDER_COLOR = Color.orange;
	
	protected static final int Y_BUFFER_HEIGHT = 7;
	protected static final int X_BUFFER_WIDTH = 5;
	protected static final int WINDOW_RIGHT_BUFFER = 12;
	protected static final int WINDOW_BOTTOM_BUFFER = 32;
	protected static final int HOME_WINDOW_WIDTH = 400;
	protected static final int HISTORY_WINDOW_WIDTH = 400;
	protected static final int SCREEN_BOTTOM_BUFFER = 50;
	protected static final Dimension SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize(); 
	protected static final int MAIN_WINDOW_X_POS = (SCREEN_DIMENSION.width - MainWindow.WINDOW_WIDTH - HOME_WINDOW_WIDTH)/2;
	protected static final int MAIN_WINDOW_Y_POS = Math.min(SCREEN_DIMENSION.height/2, 
			SCREEN_DIMENSION.height - SCREEN_BOTTOM_BUFFER - MainWindow.WINDOW_HEIGHT);
	
	private static final String INVALID_COMMAND_ERROR_MESSAGE = "An error occured while executing your command. Check your command format";
	private static final String FAIL_TO_LOAD_ICON_IN_SYSTEM_TRAY_ERROR = "Failed to load system tray icon";
	private static final String FAIL_TO_OPEN_FONT_RESOURCE_ERROR = "Failed to open font file data";
	private static final String FONT_FORMAT_ERROR = "Problem encountered when reading font format file";
	
//	private static final URL TRAY_ICON_IMG_PATH = GUIManager.class.getResource("/resources/todo.png");
	private static final URL TRAY_ICON_IMG_PATH = GUIManager.class.getResource("/todo.png"); //TODO: change when compiling
	private static final ImageIcon TRAY_ICON_IMG = new ImageIcon(TRAY_ICON_IMG_PATH);
	private static final String NAILIT_TRAY_TOOLTIP_TEXT = "NailIt!";
	
	//table width constants
	protected static final int ID_COLUMN_WIDTH = 45;
	protected static final int NAME_COLUMN_WIDTH = 400;
	protected static final int START_TIME_COLUMN_WIDTH = 130;
	protected static final int END_TIME_COLUMN_WIDTH = 130;
	protected static final int TOTAL_TABLE_WIDTH
	= ID_COLUMN_WIDTH + NAME_COLUMN_WIDTH + START_TIME_COLUMN_WIDTH + END_TIME_COLUMN_WIDTH;
	protected static final int[] TASKS_TABLE_COLUMN_WIDTH = 
		{ID_COLUMN_WIDTH, START_TIME_COLUMN_WIDTH, END_TIME_COLUMN_WIDTH, NAME_COLUMN_WIDTH};
	protected static final int TASK_NAME_COLUMN_NUMBER = 3;
	
	//table header constants
	protected static final String ID_COL_NAME = "ID";
	protected static final String TASK_NAME_COL_NAME = "Task Name";
	protected static final String TASK_START_TIME_COL_NAME = "Start";
	protected static final String TASK_END_TIME_COL_NAME = "End";
	protected static final String[] ALL_TASKS_TABLE_HEADER = 
		{ID_COL_NAME, TASK_START_TIME_COL_NAME, TASK_END_TIME_COL_NAME, TASK_NAME_COL_NAME};
	public static final String DELETED_TASK_DISPLAY_ID = "DEL";
	
	protected static Font DEFAULT_FONT_OBJECT;
	
	//GUI components
	private MainWindow mainWindow;
	private CommandBar commandBar;
	private DisplayArea displayArea;
	private HomeWindow homeWindow;
	private HistoryWindow historyWindow;
	private HelpWindow helpWindow;
	
	//reference to other components
	private AppLauncher launcher;
	private LogicManager logicExecutor;
	private Logger logger;
	private NailItGlobalKeyListener globalKeyListener;
	
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
			globalKeyListener = new NailItGlobalKeyListener(this);
			logicExecutor = new LogicManager();
			showDefaultDisplayAndReminders();
		}catch(FileCorruptionException e){
			logger.info("Storage file corrupted.");
			displayNotificationAndForceExit("File corrupted. Delete NailIt's storage file and restart NailIt");
		}catch(NativeHookException ex){
			try{
				logicExecutor = new LogicManager();
				showDefaultDisplayAndReminders();
			}catch(FileCorruptionException e){
				logger.info("Storage file corrupted.");
				displayNotificationAndForceExit("File corrupted. Delete NailIt's storage file and restart NailIt");
			}
		}catch(Exception e){
			logger.info(e.getMessage());
			displayNotificationAndForceExit("An unexpected error occured. NailIt will shutdown.");
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
	}
	private void loadComponentsUntoMainFrame(){
		mainWindow.addItem(commandBar);
		mainWindow.addItem(displayArea);
	}
	protected void handleCommandBarResizeEvent(){
		displayArea.dynamicallyResizeAndRepositionDisplayArea(commandBar.getHeight());
	}
	protected void reduceMainWindowSize(){
		mainWindow.transformIntoReducedWindow();
		displayArea.removeTaskDisplay();
	}
	protected void restoreMainWindowSize(){
		mainWindow.restoreDefaultWindow();
	}
	protected int getHelpWindowOverlayTopOffset(){
		return commandBar.getFrameHeight();
	}
	protected int getDisplayAreaHeightOffset(){
		return commandBar.getFrameHeight();
	}
	protected KeyAdapter getMainWindowComponentBasicKeyListener(){
		KeyAdapter listener = new KeyAdapter(){
			private boolean ctrlPressed = false;

			private void reset(){
				ctrlPressed = false;
			}
			@Override
			public void keyPressed(KeyEvent keyStroke){
				int keyCode = keyStroke.getKeyCode();
				if(keyCode == KeyEvent.VK_F1){
					displayFullHelpWindow();
					setFocusOnHelpWindow();
				}else if(keyCode == KeyEvent.VK_CONTROL){
					ctrlPressed = true;
				}else if(ctrlPressed && keyCode == KeyEvent.VK_MINUS){
					reset();
					reduceMainWindowSize();
				}else if(ctrlPressed && keyCode == KeyEvent.VK_EQUALS){
					reset();
					restoreMainWindowSize();
				}else if(ctrlPressed && keyCode == KeyEvent.VK_H){
					reset();
					toggleHomeWindow();
					setFocusOnCommandBar();
				}else if(ctrlPressed && keyCode == KeyEvent.VK_COMMA){
					reset();
					setVisible(false);
				}else if(ctrlPressed && keyCode == KeyEvent.VK_J){
					reset();
					toggleHistoryWindow();
					setFocusOnCommandBar();
				}else if(ctrlPressed && keyCode == KeyEvent.VK_W){
					reset();
					removeTaskDisplay();
				}else if(ctrlPressed && keyCode == KeyEvent.VK_SLASH){
					reset();
					displayCommandSyantaxHelpWindow();
					setFocusOnHelpWindow();
				}else if(ctrlPressed && keyCode == KeyEvent.VK_N){
					reset();
					loadExistingTaskNameInCommandBar();
					setFocusOnCommandBar();
				}else if(ctrlPressed && keyCode == KeyEvent.VK_D){
					reset();
					loadExistingTaskDescriptionInCommandBar();
					setFocusOnCommandBar();
				}else if(keyCode == KeyEvent.VK_PAGE_UP){
					scrollToPrevPageInTaskTable();
				}else if(keyCode == KeyEvent.VK_PAGE_DOWN){
					scrollToNextPageInTaskTable();
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
		return listener;
	}
	public void enableGlobalKeyListener(){
		if(globalKeyListener!=null && !globalKeyListener.isEnabled()){
			globalKeyListener.registerGlobalKeyHook();
		}
	}
	
	//functions to manipulate visiblitiy of GUI Components
	public void setVisible(boolean isVisible){
		if(!isVisible){
			enableGlobalKeyListener();
			historyWindow.setVisible(false);
			helpWindow.setVisible(false);
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
		}catch(InvalidCommandFormatException e){
			handleInvalidCommandFormatExeception(e);
		}catch(InvalidCommandTypeException e){
			handleInvalidCommandTypeException(e);
		}catch(Exception e){
			displayNotification(INVALID_COMMAND_ERROR_MESSAGE, false);
			logger.info(e.getMessage());
		}
		return executionResult;
	}
	private void handleInvalidCommandFormatExeception(InvalidCommandFormatException e){
		if(e.getCommandType() != null){
			helpWindow.displaySyntaxForCommandType(e.getCommandType());
		}
		String notificationStr = INVALID_COMMAND_ERROR_MESSAGE + "\n " + e.getMessage();
		displayNotification(notificationStr, false);
	}
	private void handleInvalidCommandTypeException(InvalidCommandTypeException e){
		helpWindow.displayListOfAvailableCommands();
		displayNotification(e.getMessage(), false);
	}
	
	//functions to execute commands via keyboard shortcuts
	protected void executeTriggeredTaskDelete(){
		setFocusOnCommandBar();
		int displayID = displayArea.getTaskTableSelectedRowID();
		if(displayID >= 1){
			executeTriggeredTaskDelete(displayID);
		}
	}
	protected void executeTriggeredTaskDelete(int taskDisplayID) {
		try {
			Result delCommandResult = logicExecutor.executeDirectIDCommand(CommandType.DELETE, taskDisplayID);
			displayCommandFeedback(delCommandResult);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}
	protected void executeTriggeredTaskDisplay(){
		setFocusOnCommandBar();
		int displayID = displayArea.getTaskTableSelectedRowID();
		if(displayID >= 1){
			executeTriggeredTaskDisplay(displayID);
		}
	}
	protected Result executeTriggeredTaskDisplay(int taskDisplayID){
		try {
			Result displayCommandResult = logicExecutor.executeDirectIDCommand(CommandType.DISPLAY, taskDisplayID);
			displayCommandFeedback(displayCommandResult);
			return displayCommandResult;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
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
						+", " + "description, " + task.getDescription());
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
						+", " + "name, " + task.getName());
			}
		}
	}
	//functions to handle display of feedback and execution result
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
		displayArea.scrollToTaskDisplayID(result.getDisplayedTaskDisplayID());
	}
	private void handleTaskListDisplay(Result result){
		displayArea.displayTaskList(result);
	}
	private void handleHistoryDisplay(Result result){
		historyWindow.displayHistoryList(result.getHistoryList(), true);
	}
	private void handleExecutionResultDisplay(Result result){
		displayArea.displayExecutionResult(result);
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
	private void displayCommandFeedback(Result executionResult){
		clearUserInputAndCleanUpDisplay();
		processAndDisplayExecutionResult(executionResult);
		handleCommandBarResizeEvent();
	}
	private void clearUserInputAndCleanUpDisplay(){
		commandBar.clearUserInput();
		displayArea.cleanupDisplayArea();
	}
	protected void removeTaskDisplay(){
		displayArea.removeTaskDisplay();
	}
	protected void removeDeletedTaskFromTaskListDisplay(){
		displayArea.removeDeletedTasksFromTaskListTable();
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
		displayArea.displayNotification(notificationStr, isSuccess);
	}
	
	private void displayNotificationAndForceExit(String notificationStr){
		displayArea.displayNotificationAndForceExit(notificationStr);
	}
	
	private void exit(){
		launcher.exit();
	}
	
	protected void forceExit(){
		launcher.forceExit();
	}
	//functions to configure look and field of GUI
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
