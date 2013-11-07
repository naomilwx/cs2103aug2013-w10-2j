//@author A0091372H
package nailit.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class CommandBar extends JPanel {
	protected static final String COMMANDBAR_EMPTY_DISPLAY = "";
	private static final int COMMANDBAR_TEXT_HEIGHT = 30;
	private static final int COMMANDBAR_TEXT_BUFFER_HEIGHT = 3;
	private static final int Y_BUFFER_HEIGHT = GUIManager.Y_BUFFER_HEIGHT;
	private static final int X_BUFFER_WIDTH = GUIManager.X_BUFFER_WIDTH;
	public static final int TEXTBAR_Y_BUFFER_HEIGHT = 3;
	public static final int TEXTBAR_X_BUFFER_WIDTH = 3;
	private static final int DEFAULT_COMMANDBAR_HEIGHT = COMMANDBAR_TEXT_HEIGHT + 2 * COMMANDBAR_TEXT_BUFFER_HEIGHT;
	protected static final int DEFAULT_FRAME_HEIGHT = DEFAULT_COMMANDBAR_HEIGHT + 2*TEXTBAR_Y_BUFFER_HEIGHT;
	protected static final int MAX_COMMANDBAR_HEIGHT = 3 * COMMANDBAR_TEXT_HEIGHT + 2 * COMMANDBAR_TEXT_BUFFER_HEIGHT;
	private static final int WINDOW_RIGHT_BUFFER = GUIManager.WINDOW_RIGHT_BUFFER;
	private static final int WINDOW_BOTTOM_BUFFER = GUIManager.WINDOW_BOTTOM_BUFFER;
	private static final Font COMMANDBAR_FONT = new Font("HelveticaNeue_Lt.tff", Font.PLAIN, 18);
	private static final LineBorder COMMAND_FRAME_BORDER  = new LineBorder(GUIManager.BORDER_COLOR);
	//reference to main GUI container class so CommandBar can have access to the methods there
	private GUIManager GUIBoss;
	private JScrollPane textBarWrapper;
	private JTextArea textBar;
	private int frameHeight = DEFAULT_FRAME_HEIGHT;
	private int frameWidth;
	private int frameXPos = X_BUFFER_WIDTH;
	private int frameYPos = Y_BUFFER_HEIGHT;
	private int commandBarHeight = DEFAULT_COMMANDBAR_HEIGHT;
	private int commandBarWidth;
	private int mainContainerWidth;
	private int mainContainerHeight;
	
	/**
	 * Create and initialise CommandBar Frame and Contents
	 */
	public CommandBar(final GUIManager GUIMain, int containerWidth, int containerHeight){
		GUIBoss = GUIMain;
		createConfigureAndAddInputField();
		resizeToFitMainContainer(containerWidth, containerHeight);
		this.setBorder(COMMAND_FRAME_BORDER);
		this.setLayout(null);
	}
	
	//set position and size of command frame and bar based on main container. 
	//NOTE: height is independent of main window
	protected void resizeToFitMainContainer(int containerWidth, int containerHeight){
		storeMainContainerDimensions(containerWidth, containerHeight);
		adjustFrameWidth();
		adjustCommandBarWidth();
//		adjustFramePos();
		setCommandFramePosAndSize();
		textBarWrapper.setSize(commandBarWidth, commandBarHeight);
	}
	private void storeMainContainerDimensions(int containerWidth, int containerHeight){
		mainContainerWidth = containerWidth;
		mainContainerHeight = containerHeight;
	}
	
	private void setCommandFramePosAndSize(){
		this.setLocation(frameXPos, frameYPos);
		this.setSize(frameWidth, frameHeight);
	}

	private void adjustFrameWidth(){
		frameWidth = mainContainerWidth - X_BUFFER_WIDTH - WINDOW_RIGHT_BUFFER;
	}
	
	private void adjustCommandBarWidth(){
		commandBarWidth = frameWidth - 2* TEXTBAR_X_BUFFER_WIDTH;
	}
	
	private void adjustFrameHeight(){
		frameHeight = commandBarHeight + 2*TEXTBAR_Y_BUFFER_HEIGHT;
	}
	
	private void adjustCommandBarHeight(){
		int newHeight = textBar.getHeight();
		if(newHeight < MAX_COMMANDBAR_HEIGHT){
			commandBarHeight = newHeight;
		}else{
			commandBarHeight = MAX_COMMANDBAR_HEIGHT;
		}
	}
	
	//this is needed for resizing based on text.  only resizes height.
	//command frame height controlled by command bar height
	private void commandFrameAndBarDynamicResize(){
		adjustCommandBarHeight();
		adjustFrameHeight();
//		adjustFramePos();
		setCommandFramePosAndSize();
		textBarWrapper.setSize(commandBarWidth, commandBarHeight);
	}
	private void textBarResizeAction(){
		commandFrameAndBarDynamicResize();
		GUIBoss.resizeMainDisplayArea();
		revalidate();
	}
	
	private void createConfigureAndAddInputField(){
		textBarWrapper = new JScrollPane();
		textBar = new JTextArea();
		configureTextBarWrapper();
		configureTextInputField();
		addTextInputFieldKeyBindings();
		addListenersToTextInputField();
		
		textBarWrapper.setViewportView(textBar);
		add(textBarWrapper);
	}
	
	
	private void configureTextInputField(){
		textBar.setLineWrap(true);
		textBar.setFocusTraversalKeysEnabled(false); //disable default tab operation
		textBar.setFont(COMMANDBAR_FONT);
		textBar.addComponentListener(
				//component listener to detect when JTextArea grows due to change in number of lines
				new ComponentAdapter(){
					@Override
			        public void componentResized(ComponentEvent resize) {
						textBarResizeAction();
			        }
				});
	}
	
	private void configureTextBarWrapper(){
		textBarWrapper.setLocation(TEXTBAR_X_BUFFER_WIDTH, TEXTBAR_Y_BUFFER_HEIGHT);
		textBarWrapper.setFocusable(false);
		textBarWrapper.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        textBarWrapper.setHorizontalScrollBar(null);
	}
	private void addNewLineOfTextFromPos(){
		int pos = textBar.getCaretPosition();
		textBar.insert("\n", pos);
	}
	
	private void addTextInputFieldKeyBindings(){
		InputMap textFieldInputMap = textBar.getInputMap();
		ActionMap textFieldActionMap = textBar.getActionMap();
		textFieldInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "key enter");
		textFieldActionMap.put("key enter", new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent event){
				GUIBoss.executeUserInputCommand(getUserInput());
			}
		});
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
					addNewLineOfTextFromPos();
					GUIBoss.resizeMainDisplayArea();
				}else if(keyCode == KeyEvent.VK_TAB){
					resetKeys();
					GUIBoss.setFocusOnDisplay();
				}else if(keyCode == KeyEvent.VK_F1){
					GUIBoss.displayFullHelpWindow();
					GUIBoss.setFocusOnHelpWindow();
				}else if(keyCode == KeyEvent.VK_CONTROL){
					ctrlPressed = true;
				}else if(keyCode == KeyEvent.VK_SHIFT){
					shiftDown = true;
				}else if(ctrlPressed && keyCode == KeyEvent.VK_MINUS){
					GUIBoss.reduceMainWindowSize();
				}else if(ctrlPressed && keyCode == KeyEvent.VK_EQUALS){
					GUIBoss.restoreMainWindowSize();
				}else if(ctrlPressed && keyCode == KeyEvent.VK_H){
					GUIBoss.toggleHomeWindow();
					GUIBoss.setFocusOnCommandBar();
				}else if(ctrlPressed && keyCode == KeyEvent.VK_COMMA){
					resetKeys();
					GUIBoss.setVisible(false);
				}else if(ctrlPressed && keyCode == KeyEvent.VK_J){
					GUIBoss.toggleHistoryWindow();
					GUIBoss.setFocusOnCommandBar();
				}else if(ctrlPressed && keyCode == KeyEvent.VK_W){
					GUIBoss.removeTaskDisplay();
				}else if(ctrlPressed && keyCode == KeyEvent.VK_SLASH){
					GUIBoss.displayCommandSyantaxHelpWindow();
					GUIBoss.setFocusOnHelpWindow();
				}else if(ctrlPressed && keyCode == KeyEvent.VK_N){
					GUIBoss.loadExistingTaskNameInCommandBar();
				}else if(ctrlPressed && keyCode == KeyEvent.VK_D){
					GUIBoss.loadExistingTaskDescriptionInCommandBar();
				}else if(keyCode == KeyEvent.VK_PAGE_UP){
					GUIBoss.scrollToPrevPageInTaskTable();
				}else if(keyCode == KeyEvent.VK_PAGE_DOWN){
					GUIBoss.scrollToNextPageInTaskTable();
				}else if(ctrlPressed && keyCode == KeyEvent.VK_UP){
					GUIBoss.taskTableScollUp();
				}else if(ctrlPressed && keyCode == KeyEvent.VK_DOWN){
					GUIBoss.taskTableScrollDown();
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
	protected int getFrameHeight(){
		return frameHeight;
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
}
