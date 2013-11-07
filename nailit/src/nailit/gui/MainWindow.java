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
import javax.swing.border.LineBorder;

public class MainWindow extends JFrame {
	protected static final int WINDOW_HEIGHT = 425;
	protected static final int DEFAULT_BORDER_WIDTH = 1;
	protected static final int WINDOW_WIDTH = 
			GUIManager.TOTAL_TABLE_WIDTH + GUIManager.X_BUFFER_WIDTH + 
			GUIManager.WINDOW_RIGHT_BUFFER + 2 * DEFAULT_BORDER_WIDTH;
	protected static final int REDUCED_WINDOW_HEIGHT = CommandBar.MAX_COMMANDBAR_HEIGHT 
			+ TableDisplay.TABLE_HEADER_HEIGHT + TableDisplay.TABLE_ROW_HEIGHT
			+ 4 * GUIManager.Y_BUFFER_HEIGHT + GUIManager.WINDOW_BOTTOM_BUFFER;
	
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
	protected void transformIntoReducedWindow(){
		this.setSize(WINDOW_WIDTH, REDUCED_WINDOW_HEIGHT);
		this.setAlwaysOnTop(true);
	}
	protected void restoreDefaultWindow(){
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setAlwaysOnTop(false);
	}
	protected void addItem(Component component) {
		contentPane.add(component);
	}
	
}
