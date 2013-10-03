package nailit.gui;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class TextDisplay extends JScrollPane{
	private JTextPane textPane;
	public TextDisplay(){
		createAndConfigureTextPane();
		setViewportView(textPane);
	}
	private void createAndConfigureTextPane(){
		textPane = new JTextPane();
		textPane.setEditable(false);
	}
	protected void display(String text){
		textPane.setText(text);
	}
}
