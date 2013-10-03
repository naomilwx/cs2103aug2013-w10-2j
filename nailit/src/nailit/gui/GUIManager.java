package nailit.gui;

import nailit.AppLauncher;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import java.awt.Color;

import javax.swing.border.LineBorder;

import nailit.logic.LogicManager;

public class GUIManager {	
	public static final String APPLICATION_NAME = "NailIt!";
	protected static final Color BORDER_COLOR = Color.black;
	protected static final int Y_BUFFER_HEIGHT = 10;
	protected static final int X_BUFFER_WIDTH = 5;
	protected static final int WINDOW_RIGHT_BUFFER = 22;
	protected static final int WINDOW_BOTTOM_BUFFER = 40;
	
	private MainWindow mainWindow;
	private CommandBar commandBar;
	private DisplayArea displayArea;
	private AppLauncher launcher;
	private LogicManager logicExecutor;
	
	/**
	 * Create the application.
	 */
	public GUIManager(final AppLauncher launcher) {
		this.launcher = launcher;
		logicExecutor = new LogicManager();
		mainWindow = new MainWindow(this);
		commandBar = new CommandBar(this, mainWindow.getWidth(), mainWindow.getHeight());
		displayArea = new DisplayArea(this, mainWindow.getWidth(), mainWindow.getHeight());
		loadComponentsUntoMainFrame();
		//stub to be modified later
	}
	
	public void setVisible(boolean isVisible){
		mainWindow.setVisible(isVisible);
	}
	/**
	 * Executes command entered by user
	 * @param input
	 */
	protected void executeUserInputCommand(String input){
		System.out.println(input);
		commandBar.clearUserInput();
//		logicExecutor.executeCommand(input);
	}
	
	private void exit(){
		launcher.exit();
	}
	private void loadComponentsUntoMainFrame(){
		mainWindow.addItem(commandBar);
		mainWindow.addItem(displayArea);
	}
}
