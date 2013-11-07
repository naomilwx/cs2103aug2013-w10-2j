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

public class CommandBar extends JPanel {
	protected static final String COMMANDBAR_EMPTY_DISPLAY = "";
	private static final int COMMANDBAR_TEXT_HEIGHT = 30;
	private static final int COMMANDBAR_TEXT_BUFFER_HEIGHT = 5;
	private static final int Y_BUFFER_HEIGHT = GUIManager.Y_BUFFER_HEIGHT;
	private static final int X_BUFFER_WIDTH = GUIManager.X_BUFFER_WIDTH;
	public static final int TEXTBAR_Y_BUFFER_HEIGHT = 5;
	public static final int TEXTBAR_X_BUFFER_WIDTH = 5;
	protected static final int MAX_HEIGHT_OF_TEXTBAR = 3 * COMMANDBAR_TEXT_HEIGHT + 2 * COMMANDBAR_TEXT_BUFFER_HEIGHT;
	private static final int WINDOW_RIGHT_BUFFER = GUIManager.WINDOW_RIGHT_BUFFER;
	private static final int WINDOW_BOTTOM_BUFFER = GUIManager.WINDOW_BOTTOM_BUFFER;
	private static final Font COMMANDBAR_FONT = new Font("HelveticaNeue_Lt.tff", Font.PLAIN, 18);
	
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
	protected void resizeCommandBarToFitMainContainer(int containerWidth, int containerHeight){
		mainContainerWidth = containerWidth;
		mainContainerHeight = containerHeight;
		positionAndResizeCommandFrame();
	}
	private void positionAndResizeCommandFrame(){
		adjustFrameWidth();
		adjustCommandBarWidth();
		adjustCommandBarAndFrameHeightAndPos();
		this.setBorder(new LineBorder(GUIManager.BORDER_COLOR));
		this.setLocation(frameXPos, frameYPos);
		this.setSize(frameWidth, frameHeight);
		this.setLayout(null);
	}
	private void adjustFrameWidth(){
		frameWidth = mainContainerWidth - TEXTBAR_X_BUFFER_WIDTH - WINDOW_RIGHT_BUFFER;
	}
	private void adjustCommandBarWidth(){
		commandBarWidth = frameWidth - 2* X_BUFFER_WIDTH;
	}
	private void adjustCommandBarAndFrameHeightAndPos(){
		adjustCommandBarHeight();
		adjustFrameHeight();
		adjustFramePos();
	}
	private void adjustFrameHeight(){
		frameHeight = commandBarHeight + 2*TEXTBAR_Y_BUFFER_HEIGHT;
	}
	private void adjustCommandBarHeight(){
		if(textBar == null){
			//initialisation of CommandBar
			commandBarHeight = COMMANDBAR_TEXT_HEIGHT + 2 * COMMANDBAR_TEXT_BUFFER_HEIGHT;
		}else{
			int newHeight = textBar.getHeight();
			if(newHeight < MAX_HEIGHT_OF_TEXTBAR){
				commandBarHeight = newHeight;
			}
		}
	}
	private void adjustFramePos(){
		frameXPos = X_BUFFER_WIDTH;
		frameYPos = mainContainerHeight - frameHeight - WINDOW_BOTTOM_BUFFER;
	}
	
	private void commandFrameAndBarDynamicResize(){
		adjustCommandBarAndFrameHeightAndPos();
		this.setSize(frameWidth, frameHeight);
		this.setLocation(frameXPos, frameYPos);
		textBarWrapper.setSize(commandBarWidth, commandBarHeight);
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
		textBar.setSize(commandBarWidth, commandBarHeight);
		textBar.setFont(COMMANDBAR_FONT);
		textBar.addComponentListener(
				//component listener to detect when JTextArea grows due to change in number of lines
				new ComponentAdapter(){
					@Override
			        public void componentResized(ComponentEvent resize) {
						commandFrameAndBarDynamicResize();
			        }
				});
	}
	
	private void configureTextBarWrapper(){
		textBarWrapper.setLocation(TEXTBAR_X_BUFFER_WIDTH, TEXTBAR_Y_BUFFER_HEIGHT);
		textBarWrapper.setSize(commandBarWidth, commandBarHeight);
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
				}else if(keyCode == KeyEvent.VK_UP){
					GUIBoss.taskTableScollUp();
				}else if(keyCode == KeyEvent.VK_DOWN){
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
