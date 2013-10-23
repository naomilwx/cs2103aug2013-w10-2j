package nailit.gui;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;

import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CommandBar extends JPanel {
	protected static final String COMMANDBAR_EMPTY_DISPLAY = "";
	private static final int COMMANDBAR_TEXT_HEIGHT = 20;
	private static final int COMMANDBAR_TEXT_BUFFER_HEIGHT = 5;
	private static final int Y_BUFFER_HEIGHT = GUIManager.Y_BUFFER_HEIGHT;
	private static final int X_BUFFER_WIDTH = GUIManager.X_BUFFER_WIDTH;
	public static final int TEXTBAR_Y_BUFFER_HEIGHT = 5;
	public static final int TEXTBAR_X_BUFFER_WIDTH = 5;
	private static final int WINDOW_RIGHT_BUFFER = GUIManager.WINDOW_RIGHT_BUFFER;
	private static final int WINDOW_BOTTOM_BUFFER = GUIManager.WINDOW_BOTTOM_BUFFER;
	private static final int MAX_ROWS_IN_TEXTFIELD = 4;
	
	//reference to main GUI container class so CommandBar can have access to the methods there
	private GUIManager GUIBoss;
	private JScrollPane textBarWrapper;
	private JTextArea textBar;
	private int frameHeight;
	private int frameWidth;
	private int frameXPos;
	private int frameYPos;
	private int commandBarHeight;
	private int commandBarWidth;
	private int rowsInTextField = 1;
	private int numOfTextLines = 1;
	private int mainContainerWidth;
	private int mainContainerHeight;
	/**
	 * Create the panel.
	 */
	public CommandBar(final GUIManager GUIMain, int containerWidth, int containerHeight){
		GUIBoss = GUIMain;
		mainContainerWidth = containerWidth;
		mainContainerHeight = containerHeight;
		positionAndResizeCommandFrame();
		createConfigureAndAddInputField();
	}
	
	private void positionAndResizeCommandFrame(){
		frameWidth = mainContainerWidth - TEXTBAR_X_BUFFER_WIDTH - WINDOW_RIGHT_BUFFER;
		commandBarWidth = frameWidth - 2* X_BUFFER_WIDTH;
		adjustCommandBarAndFrameHeightAndPos(rowsInTextField);
		this.setBorder(new LineBorder(GUIManager.BORDER_COLOR));
		this.setLocation(frameXPos, frameYPos);
		this.setSize(frameWidth, frameHeight);
		this.setLayout(null);
	}
	private void adjustCommandBarAndFrameHeightAndPos(int textRowNum){
		commandBarHeight = textRowNum * COMMANDBAR_TEXT_HEIGHT + 2 * COMMANDBAR_TEXT_BUFFER_HEIGHT;
		frameHeight = commandBarHeight + 2*TEXTBAR_Y_BUFFER_HEIGHT;
		frameXPos = X_BUFFER_WIDTH;
		frameYPos = mainContainerHeight - frameHeight - WINDOW_BOTTOM_BUFFER;
	}
	private void addNewLineToTextField(){
		if(rowsInTextField < MAX_ROWS_IN_TEXTFIELD){
			rowsInTextField += 1;
		}
		commandFrameAndBarDynamicResize();
	}
	private void removeLineFromTextField(){
		if(numOfTextLines > textBar.getLineCount()){
			numOfTextLines -= 1;
		}
		if(numOfTextLines < rowsInTextField){
			rowsInTextField -= 1;
			commandFrameAndBarDynamicResize();
			GUIBoss.resizeMainDisplayArea();
		}
	}
	private void commandFrameAndBarDynamicResize(){
		adjustCommandBarAndFrameHeightAndPos(rowsInTextField);
		this.setSize(frameWidth, frameHeight);
		this.setLocation(frameXPos, frameYPos);
		textBarWrapper.setSize(commandBarWidth, commandBarHeight);
		textBar.setSize(commandBarWidth, commandBarHeight);
		revalidate();
	}
	private void createConfigureAndAddInputField(){
		textBarWrapper = new JScrollPane();
		textBar = new JTextArea();
		configureTextBarWrapper();
		configureTextInputField();
		addListenersToTextInputField();
		
		textBarWrapper.setViewportView(textBar);
		add(textBarWrapper);
	}
	
	private void configureTextInputField(){
		textBar.setLineWrap(true);
		textBar.setFocusTraversalKeysEnabled(false); //disable default tab operation
		textBar.setSize(commandBarWidth, commandBarHeight);
	}
	
	private void configureTextBarWrapper(){
		textBarWrapper.setLocation(TEXTBAR_X_BUFFER_WIDTH, TEXTBAR_Y_BUFFER_HEIGHT);
		textBarWrapper.setSize(commandBarWidth, commandBarHeight);
		textBarWrapper.setFocusable(false);
		textBarWrapper.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        textBarWrapper.setHorizontalScrollBar(null);
	}
	private void addNewLineOfTextFromPos(){
		numOfTextLines += 1;
		int pos = textBar.getCaretPosition();
		textBar.insert("\n", pos);
	}
	private void addListenersToTextInputField(){
		textBar.addKeyListener(new KeyAdapter(){
			private boolean ctrlPressed = false;
			private boolean shiftDown = false;
			private void resetKeys(){
				ctrlPressed = false;
				shiftDown = false;
			}
			@Override
			public void keyPressed(KeyEvent keyStroke){
				int keyCode = keyStroke.getKeyCode();
				if((ctrlPressed || shiftDown) && keyCode == KeyEvent.VK_ENTER){
					addNewLineToTextField();
					addNewLineOfTextFromPos();
					GUIBoss.resizeMainDisplayArea();
				}else if(keyCode == KeyEvent.VK_ENTER){
					GUIBoss.executeUserInputCommand(getUserInput());
				}else if(keyCode == KeyEvent.VK_TAB){
					resetKeys();
					GUIBoss.setFocusOnDisplay();
				}else if(keyCode == KeyEvent.VK_CONTROL){
					ctrlPressed = true;
				}else if(keyCode == KeyEvent.VK_SHIFT){
					shiftDown = true;
				}else if(ctrlPressed && keyCode == KeyEvent.VK_H){
					resetKeys();
					GUIBoss.toggleHomeWindow();
					GUIBoss.setFocusOnCommandBar();
				}else if(ctrlPressed && keyCode == KeyEvent.VK_COMMA){
					resetKeys();
					GUIBoss.setVisible(false);
				}else if(ctrlPressed && keyCode == KeyEvent.VK_J){
					resetKeys();
					GUIBoss.toggleHistoryWindow();
					GUIBoss.setFocusOnCommandBar();
				}else if(keyCode == KeyEvent.VK_BACK_SPACE){
					removeLineFromTextField();
				}
			}
			@Override
			public void keyReleased(KeyEvent keyStroke){
				int keyCode = keyStroke.getKeyCode();
				if(keyCode == KeyEvent.VK_CONTROL){
					ctrlPressed = false;
				}else if(keyCode == KeyEvent.VK_SHIFT){
					shiftDown = false;
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
