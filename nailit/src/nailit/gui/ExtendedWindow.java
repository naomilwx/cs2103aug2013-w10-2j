package nailit.gui;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class ExtendedWindow extends JFrame{
	protected static final int EXTENDED_WINDOW_HEIGHT = MainWindow.WINDOW_HEIGHT;
	protected static final int EXTENDED_WINDOW_BORDER_WIDTH = 3;
	protected static final LineBorder EXTENDED_WINDOW_BORDER = new LineBorder(GUIManager.BORDER_COLOR, EXTENDED_WINDOW_BORDER_WIDTH);
	protected static final int EXTENDED_WINDOW_X_BUFFER = 10;
	protected static final int EXTENDED_WINDOW_Y_BUFFER = 10;
	
	protected GUIManager GUIBoss;
	protected JPanel contentPane;
	protected ScrollableFocusableDisplay displayPane;
	protected int displayPaneWidth;
	protected int displayPaneHeight;
	protected int windowWidth;
	protected int contentWidth;
	protected int contentHeight;
	
	public ExtendedWindow(final GUIManager GUIMain, int width){
		GUIBoss = GUIMain;
		windowWidth = width;
		initialiseHomeWindow();
	}
	
	private void initialiseHomeWindow(){
		configureHomeWindowFrame();
		createAndInitialiseContentPane();
	}
	private void configureHomeWindowFrame(){
		setUndecorated(true);
		setLocation(GUIManager.EXTENDED_WINDOW_X_POS, GUIManager.EXTENDED_WINDOW_Y_POS);
		setSize(windowWidth, EXTENDED_WINDOW_HEIGHT);
		setResizable(false);
	}
	private void createAndInitialiseContentPane(){
		contentPane = new JPanel();
		contentPane.setLayout(null);
		contentPane.setBorder(EXTENDED_WINDOW_BORDER);
		setContentPane(contentPane);
	}
	protected void addItem(Component component) {
		contentPane.add(component);
	}
}
