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

	private JFrame frame;
	private static String APPLICATION_NAME = "NailIt!";
	
	private AppLauncher launcher;
	private CommandBar commandBar;
	private LogicManager logicExecutor;
	
	private final WindowListener windowClosePressed =  new WindowAdapter(){
		public void windowClosing(WindowEvent event){
			frame.setVisible(false);
		}
	};

	public void setVisible(boolean isVisible){
		frame.setVisible(isVisible);
	}
	/**
	 * Create the application.
	 */
	public GUIManager(final AppLauncher launcher) {
		initialize();
		//stub to be modified later
		this.launcher = launcher;
		logicExecutor = new LogicManager();
	}
	
	private void addListenersToMainFrame(){
		frame.addWindowListener(windowClosePressed);
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		commandBar = new CommandBar(this);
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300); //temp to be replaced by constants later
		frame.setTitle(APPLICATION_NAME);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		frame.getContentPane().add(commandBar,BorderLayout.SOUTH);
		
		addListenersToMainFrame();
	}
	
	/**
	 * Executes command entered by user
	 * @param input
	 */
	protected void executeUserInputCommand(String input){
		System.out.println(input);
		commandBar.clearUserInput();
	}

}
