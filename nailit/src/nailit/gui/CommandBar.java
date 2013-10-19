package nailit.gui;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;

import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CommandBar extends JPanel {
	protected static final String COMMANDBAR_EMPTY_DISPLAY = "";
	private static final int DEFAULT_COMMANDBAR_HEIGHT = 25;
	private static final int Y_BUFFER_HEIGHT = GUIManager.Y_BUFFER_HEIGHT;
	private static final int X_BUFFER_WIDTH = GUIManager.X_BUFFER_WIDTH;
	public static final int TEXTBAR_Y_BUFFER_HEIGHT = 5;
	public static final int TEXTBAR_X_BUFFER_WIDTH = 5;
	private static final int WINDOW_RIGHT_BUFFER = GUIManager.WINDOW_RIGHT_BUFFER;
	private static final int WINDOW_BOTTOM_BUFFER = GUIManager.WINDOW_BOTTOM_BUFFER;
	//reference to main GUI container class so CommandBar can have access to the methods there
	private GUIManager GUIBoss;
	private JScrollPane textBarWrapper;
	private JTextArea textBar;
	private int frameHeight;
	private int frameWidth;
	private int frameXPos;
	private int frameYPos;
	private int commandBarHeight;
	/**
	 * Create the panel.
	 */
	public CommandBar(final GUIManager GUIMain, int containerWidth, int containerHeight){
		GUIBoss = GUIMain;
		
		positionAndResizeCommandFrame(containerWidth, containerHeight);
		createConfigureAndAddInputField();
	}
	
	private void positionAndResizeCommandFrame(int containerWidth, int containerHeight){
		frameWidth = containerWidth - TEXTBAR_X_BUFFER_WIDTH - WINDOW_RIGHT_BUFFER;
		adjustCommandBarAndFrameHeight(1);
		frameXPos = X_BUFFER_WIDTH;
		frameYPos = containerHeight - frameHeight - WINDOW_BOTTOM_BUFFER;
		this.setBorder(new LineBorder(GUIManager.BORDER_COLOR));
		this.setLocation(frameXPos, frameYPos);
		this.setSize(frameWidth, frameHeight);
		this.setLayout(null);
	}
	private void adjustCommandBarAndFrameHeight(int textRowNum){
		commandBarHeight = textRowNum * DEFAULT_COMMANDBAR_HEIGHT;
		frameHeight = commandBarHeight + 2*TEXTBAR_Y_BUFFER_HEIGHT;
	}
	private void createConfigureAndAddInputField(){
		textBarWrapper = new JScrollPane();
		textBar = new JTextArea();
		configureTextBarWrapper();
		resizeAndpositionTextInputField();
		addListenersToTextInputField();
		textBar.setLineWrap(true);
		textBar.setFocusTraversalKeysEnabled(false); //disable default tab operation
		textBarWrapper.setViewportView(textBar);
		add(textBarWrapper);
	}
	
	private void resizeAndpositionTextInputField(){
		int width = frameWidth - 2* X_BUFFER_WIDTH;
		textBarWrapper.setLocation(TEXTBAR_X_BUFFER_WIDTH, TEXTBAR_Y_BUFFER_HEIGHT);
		textBarWrapper.setSize(width, commandBarHeight);
		textBar.setSize(width, commandBarHeight);
	}
	private void configureTextBarWrapper(){
		textBarWrapper.setFocusable(false);
		textBarWrapper.setBorder(null);
		textBarWrapper.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        textBarWrapper.setHorizontalScrollBar(null);
	}
	private void addListenersToTextInputField(){
		textBar.addKeyListener(new KeyAdapter(){
			private boolean ctrlPressed = false;
			@Override
			public void keyPressed(KeyEvent keyStroke){
				int keyCode = keyStroke.getKeyCode();
				if(keyCode == KeyEvent.VK_ENTER){
					GUIBoss.executeUserInputCommand(getUserInput());
				}else if(keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_TAB){
					GUIBoss.setFocusOnDisplay();
				}else if(keyCode == KeyEvent.VK_CONTROL){
					ctrlPressed = true;
				}else if(ctrlPressed && keyCode == KeyEvent.VK_H){
					GUIBoss.toggleHomeWindow();
					GUIBoss.setFocusOnCommandBar();
				}else if(ctrlPressed && keyCode == KeyEvent.VK_COMMA){
					GUIBoss.setVisible(false);
				}else if(ctrlPressed && keyCode == KeyEvent.VK_J){
					GUIBoss.toggleHistoryWindow();
					GUIBoss.setFocusOnCommandBar();
				}
			}
			@Override
			public void keyReleased(KeyEvent keyStroke){
				int keyCode = keyStroke.getKeyCode();
				if(keyCode == KeyEvent.VK_CONTROL){
					ctrlPressed = false;
				}
			}
		});
	}
	protected void setFocus(){
		textBar.requestFocus();
	}
	protected String getUserInput(){
		return textBar.getText();
	}
	//Methods to manipulate the text in the user input JTextField
	protected void clearUserInput(){
		textBar.setText(COMMANDBAR_EMPTY_DISPLAY);
		return;
	}
	protected void setUserInput(String text){
		textBar.setText(text);
		return;
	}
	//Methods to manipulate text in user input field.
}
