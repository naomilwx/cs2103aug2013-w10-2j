package nailit.gui;

import nailit.AppLauncher;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextPane;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.border.LineBorder;

import nailit.logic.LogicManager;;

public class GUIManager {	
	public static final String APPLICATION_NAME = "NailIt!";
	
	private MainWindow frame;
	private CommandBar commandBar;
	private AppLauncher launcher;
	private LogicManager logicExecutor;
	
	/**
	 * Create the application.
	 */
	public GUIManager(final AppLauncher launcher) {
		this.launcher = launcher;
		logicExecutor = new LogicManager();
		frame = new MainWindow(this);
		commandBar = new CommandBar(this);
		loadComponentsUntoMainFrame();
		//stub to be modified later
	}
	
	public void setVisible(boolean isVisible){
		frame.setVisible(isVisible);
	}
	/**
	 * Executes command entered by user
	 * @param input
	 */
	protected void executeUserInputCommand(String input){
		System.out.println(input);
		commandBar.clearUserInput();
	}
	
	private void exit(){
		launcher.exit();
	}
	private void loadComponentsUntoMainFrame(){
		frame.getContentPane().add(commandBar,BorderLayout.SOUTH);
	}
}
