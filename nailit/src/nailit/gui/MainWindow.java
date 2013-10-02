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
		this.setBounds(100, 100, 450, 300); //temp to be replaced by constants later
		this.setTitle(GUIManager.APPLICATION_NAME);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addListenersToMainFrame();
	}
}
