//@author A0091372H
package nailit.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MainWindow extends JFrame {
	protected static final int WINDOW_HEIGHT = 470;
	protected static final int WINDOW_WIDTH = 
			GUIManager.TOTAL_TABLE_WIDTH + GUIManager.X_BUFFER_WIDTH + 
			GUIManager.WINDOW_RIGHT_BUFFER;
	
	private GUIManager GUIBoss;
	private JPanel contentPane;
	private final WindowListener windowClosePressed =  new WindowAdapter(){
		public void windowClosing(WindowEvent event){
			GUIBoss.setVisible(false);
		}
	};
	
	public MainWindow(final GUIManager GUIMain){
		GUIBoss = GUIMain;
		this.initialize();
	}
	
	protected void addListenersToMainFrame(){
		addWindowListener(windowClosePressed);
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		createAndConfigureContentPane();
		configureMainWindow();
		addListenersToMainFrame();
	}
	private void createAndConfigureContentPane(){
		contentPane = new JPanel();
		contentPane.setLayout(null);
		setContentPane(contentPane);
	}
	private void configureMainWindow(){
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setBounds(GUIManager.MAIN_WINDOW_X_POS, GUIManager.MAIN_WINDOW_Y_POS, WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setTitle(GUIManager.APPLICATION_NAME);
		this.setResizable(false);
	}
	protected void addItem(Component component) {
		contentPane.add(component);
	}
	
}
