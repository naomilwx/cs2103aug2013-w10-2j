package nailit.gui;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

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
	
	protected int windowXPos = GUIManager.MAIN_WINDOW_X_POS + MainWindow.WINDOW_WIDTH + GUIManager.WINDOW_RIGHT_BUFFER;
	protected int windowYPos = GUIManager.MAIN_WINDOW_Y_POS;
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
		positionFrameBasedOnMainWindowPos();
		setResizable(false);
	}
	private void positionFrameBasedOnMainWindowPos(){
		recalculateExtendedWindowPosition();
		setLocation(windowXPos, windowYPos);
		setSize(windowWidth, EXTENDED_WINDOW_HEIGHT);
	}
	private void recalculateExtendedWindowPosition(){
		int mainWindowXPos = GUIBoss.getMainWindowLocationCoordinates().width;
		int mainWindowYPos = GUIBoss.getMainWindowLocationCoordinates().height;
		windowXPos = mainWindowXPos + MainWindow.WINDOW_WIDTH + GUIManager.WINDOW_RIGHT_BUFFER;
		windowYPos = mainWindowYPos;
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
	protected void setFocus() {
		displayPane.requestFocus();
	}
	@Override
	public void setVisible(boolean isVisible){
		positionFrameBasedOnMainWindowPos();
		super.setVisible(isVisible);
	}
}
