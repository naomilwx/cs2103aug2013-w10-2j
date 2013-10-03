package nailit.gui;

import nailit.AppLauncher;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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
	protected static final int WINDOW_RIGHT_BUFFER = 12;
	protected static final int WINDOW_BOTTOM_BUFFER = 35;
	protected static final int MAIN_WINDOW_X_POS = 100;
	protected static final int MAIN_WINDOW_Y_POS = 100;
	protected static final String DEFAULT_WINDOW_LOOKANDFEEL = "javax.swing.plaf.metal.MetalLookAndFeel";
	
	private MainWindow mainWindow;
	private CommandBar commandBar;
	private DisplayArea displayArea;
	private AppLauncher launcher;
	private LogicManager logicExecutor;
	
	/**
	 * Create the application.
	 */
	public GUIManager(final AppLauncher launcher) {
		setWindowLookAndFeel();
		this.launcher = launcher;
		logicExecutor = new LogicManager();
		createComponentsAndAddToMainFrame();
		//stub to be modified later
	}
	
	private void createComponentsAndAddToMainFrame() {
		mainWindow = new MainWindow(this);
		commandBar = new CommandBar(this, mainWindow.getWidth(), mainWindow.getHeight());
		displayArea = new DisplayArea(this, mainWindow.getWidth(), mainWindow.getHeight());
		loadComponentsUntoMainFrame();
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
		System.out.println(input);
		commandBar.clearUserInput();
//		logicExecutor.executeCommand(input);
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
