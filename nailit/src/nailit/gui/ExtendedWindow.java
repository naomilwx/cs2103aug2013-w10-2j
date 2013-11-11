//@author A0091372H
package nailit.gui;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;


@SuppressWarnings("serial")
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
	protected int windowHeight = EXTENDED_WINDOW_HEIGHT;
	protected int contentWidth;
	protected int contentHeight;
	
	protected int defaultXPos = GUIManager.MAIN_WINDOW_X_POS + MainWindow.WINDOW_WIDTH + GUIManager.WINDOW_RIGHT_BUFFER;
	protected int defaultYPos = GUIManager.MAIN_WINDOW_Y_POS;
	protected int windowXPos = defaultXPos;
	protected int windowYPos = defaultYPos;
	
	protected final MouseAdapter extendedWindowMouseListener = new MouseAdapter(){
		int initialMouseX;
		int initialMouseY;
		int currX;
		int currY;
		@Override
		public void mousePressed(MouseEvent mouseEvent){
			requestFocus();
			initialMouseX = mouseEvent.getXOnScreen();
			initialMouseY = mouseEvent.getYOnScreen();
			currX = getX();
			currY = getY();
		}
		@Override
		public void mouseDragged(MouseEvent mouseEvent){
			if((mouseEvent.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0){
				int XShift = mouseEvent.getXOnScreen() - initialMouseX;
				int YShift = mouseEvent.getYOnScreen() - initialMouseY;
				((ExtendedWindow) mouseEvent.getComponent()).setLocation(currX + XShift,
																		currY + YShift);
			}
		}
	};
	
	public ExtendedWindow(final GUIManager GUIMain, int width){
		GUIBoss = GUIMain;
		windowWidth = width;
		initialiseHomeWindow();
		initialiseAndConfigureWindowContent();
	}
	protected void initialiseHomeWindow(){
		configureHomeWindowFrame();
		createAndInitialiseContentPane();
		addMouseListener(extendedWindowMouseListener);
		addMouseMotionListener(extendedWindowMouseListener);
	}
	protected void configureHomeWindowFrame(){
		setUndecorated(true);
		positionFrameBasedOnMainWindowPos();
		setSize(windowWidth, windowHeight);
		setResizable(false);
	}
	protected void positionFrameBasedOnMainWindowPos(){
		recalculateExtendedWindowPosition();
		setLocation(windowXPos, windowYPos);
	}
	protected void recalculateExtendedWindowPosition(){
		int mainWindowXPos = GUIBoss.getMainWindowLocationCoordinates().width;
		int mainWindowYPos = GUIBoss.getMainWindowLocationCoordinates().height;
		windowXPos = mainWindowXPos + MainWindow.WINDOW_WIDTH + GUIManager.WINDOW_RIGHT_BUFFER;
		windowYPos = mainWindowYPos;
	}
	protected void createAndInitialiseContentPane(){
		contentPane = new JPanel();
		contentPane.setLayout(null);
		contentPane.setBorder(EXTENDED_WINDOW_BORDER);
		setContentPane(contentPane);
	}
	protected void refreshContentSize(){
		setWindowContentSize();
		displayPane.setSize(contentWidth, contentHeight);
	}
	protected void setWindowContentSize(){
		contentHeight = windowHeight - 2 * EXTENDED_WINDOW_Y_BUFFER;
		contentWidth = windowWidth - 2 * EXTENDED_WINDOW_X_BUFFER;
	}
	protected void initialiseAndConfigureWindowContent(){
		setWindowContentSize();
		displayPane = new TextDisplay(contentWidth, contentHeight);
		displayPane.setLocation(EXTENDED_WINDOW_X_BUFFER, EXTENDED_WINDOW_Y_BUFFER);
		addItem(displayPane);
	}
	protected void addItem(Component component) {
		contentPane.add(component);
	}
	protected void addKeyListenerToDisplayPaneAndChild(KeyAdapter keyListener){
		displayPane.addKeyListener(keyListener);
		Component componentInDisplayPane = displayPane.getViewport().getComponent(0);
		if(componentInDisplayPane != null){
			componentInDisplayPane.addKeyListener(keyListener);
		}
	}
	@Override
	public void requestFocus() {
		displayPane.requestFocus();
	}
	@Override
	public void setVisible(boolean isVisible){
		if(isVisible){
			if(GUIBoss.getMainWindowVisibility()){
				positionFrameBasedOnMainWindowPos();
			}else{
				setLocation(defaultXPos, defaultYPos);
			}
		}
		super.setVisible(isVisible);
	}
	public void showWindowAsItIs(){
		super.setVisible(true);
	}
}
