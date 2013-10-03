package nailit.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MainWindow extends JFrame {
	protected static final int WINDOW_HEIGHT = 500;
	protected static final int WINDOW_WIDTH = 450;
	
	private GUIManager GUIBoss;
	
	
	public MainWindow(final GUIManager GUIMain){
		GUIBoss = GUIMain;
		this.initialize();
	}
	private final WindowListener windowClosePressed =  new WindowAdapter(){
		public void windowClosing(WindowEvent event){
			setVisible(false);
		}
	};
	protected void addListenersToMainFrame(){
		addWindowListener(windowClosePressed);
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		configureMainWindowDisplay();
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addListenersToMainFrame();
	}
	
	private void configureMainWindowDisplay(){
		this.setBounds(100, 100, WINDOW_WIDTH, WINDOW_HEIGHT); //temp to be replaced by constants later
		this.setTitle(GUIManager.APPLICATION_NAME);
	}
}
