package nailit.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextPane;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class GUIManager {

	private JFrame frame;
	private static String APPLICATION_NAME = "NailIt!";
	
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
	}
	
	private void addListenersToMainFrame(){
		frame.addWindowListener(windowClosePressed);
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setTitle(APPLICATION_NAME);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addListenersToMainFrame();
	}
	
	/**
	 * Executes command entered by user
	 * @param input
	 */
	protected void executeUserInputCommand(String input){
		
	}

}
