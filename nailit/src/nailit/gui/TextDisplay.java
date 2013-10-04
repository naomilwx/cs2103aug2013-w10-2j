package nailit.gui;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class TextDisplay extends JScrollPane{
	private JTextPane textPane;
	public TextDisplay(int width, int height){
		resizeArea(width, height);
		createAndConfigureTextPane();
		setViewportView(textPane);
	}
	protected void resizeArea(int width, int height){
		this.setSize(width, height);
	}
	private void createAndConfigureTextPane(){
		textPane = new JTextPane();
		textPane.setEditable(false);
	}
	protected void basicDisplay(String text){
		textPane.setText(text);
	}
	protected JTextPane getTextPane(){
		return textPane;
	}
}
