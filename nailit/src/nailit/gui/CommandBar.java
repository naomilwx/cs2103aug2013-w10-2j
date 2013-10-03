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
	//reference to main GUI container class so CommandBar can have access to the methods there
	private GUIManager GUIBoss;
	private JTextField textBar;
	
	/**
	 * Create the panel.
	 */
	public CommandBar(final GUIManager GUIMain) {
		GUIBoss = GUIMain;
		
		configureCommandFrame();
		createAndConfigureTextInputField();
		add(textBar);
	}
	
	private void configureCommandFrame(){
		this.setLayout(new BoxLayout(this,BoxLayout.LINE_AXIS));
	}
	
	private void createAndConfigureTextInputField(){
		textBar = new JTextField();
		resizeAndpositionTextInputField();
		textBar.setBorder(new LineBorder(GUIManager.BORDER_COLOR));
		addListenersToTextInputField();
	}
	
	private void resizeAndpositionTextInputField(){
		
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
