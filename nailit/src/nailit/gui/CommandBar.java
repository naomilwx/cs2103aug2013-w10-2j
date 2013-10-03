package nailit.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CommandBar extends JPanel {
	private static final int COMMANDBAR_HEIGHT = 20;
	private static final int Y_BUFFER_HEIGHT = GUIManager.Y_BUFFER_HEIGHT;
	private static final int X_BUFFER_WIDTH = GUIManager.X_BUFFER_WIDTH;
	private static final int WINDOW_RIGHT_BUFFER = GUIManager.WINDOW_RIGHT_BUFFER;
	private static final int WINDOW_BOTTOM_BUFFER = GUIManager.WINDOW_BOTTOM_BUFFER;
	//reference to main GUI container class so CommandBar can have access to the methods there
	private GUIManager GUIBoss;
	private JTextField textBar;
	private int frameHeight;
	private int frameWidth;
	/**
	 * Create the panel.
	 */
	public CommandBar(final GUIManager GUIMain, int containerWidth, int containerHeight){
		GUIBoss = GUIMain;
		
		configureCommandFrame(containerWidth, containerHeight);
		createAndConfigureTextInputField();
		add(textBar);
	}
	
	private void configureCommandFrame(int containerWidth, int containerHeight){
		frameWidth = containerWidth-X_BUFFER_WIDTH-WINDOW_RIGHT_BUFFER;
		frameHeight = COMMANDBAR_HEIGHT+2*Y_BUFFER_HEIGHT;
		this.setBorder(new LineBorder(GUIManager.BORDER_COLOR));
		this.setLocation(X_BUFFER_WIDTH, containerHeight-frameHeight-WINDOW_BOTTOM_BUFFER);
		this.setSize(frameWidth, frameHeight);
		this.setLayout(null);
	}
	
	private void createAndConfigureTextInputField(){
		textBar = new JTextField();
		resizeAndpositionTextInputField();
		textBar.setBorder(new LineBorder(GUIManager.BORDER_COLOR));
		addListenersToTextInputField();
	}
	
	private void resizeAndpositionTextInputField(){
		int width = frameWidth - 2* X_BUFFER_WIDTH;
		textBar.setLocation(X_BUFFER_WIDTH,Y_BUFFER_HEIGHT);
		textBar.setSize(width,COMMANDBAR_HEIGHT);
	}
	private void addListenersToTextInputField(){
		textBar.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent keyStroke){
				if(keyStroke.getKeyCode() == KeyEvent.VK_ENTER){
					GUIBoss.executeUserInputCommand(getUserInput());
				}
			}
		});
	}
	protected String getUserInput(){
		return textBar.getText();
	}
	//Methods to manipulate the text in the user input JTextField
	protected void clearUserInput(){
		textBar.setText("");
		return;
	}
	protected void setUserInput(String text){
		textBar.setText(text);
		return;
	}
	//Methods to manipulate text in user input field.
}
