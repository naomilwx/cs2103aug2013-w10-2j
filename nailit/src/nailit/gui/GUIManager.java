package nailit.gui;

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

public class GUIManager {

	private JFrame frame;
	private static String APPLICATION_NAME = "NailIt!";
	private CommandBar commandBar;
	
	private final WindowListener windowClosePressed =  new WindowAdapter(){
		public void windowClosing(WindowEvent event){
			frame.setVisible(false);
		}
	};
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIManager window = new GUIManager();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUIManager() {
		initialize();
		//stub to be modified later
		
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
