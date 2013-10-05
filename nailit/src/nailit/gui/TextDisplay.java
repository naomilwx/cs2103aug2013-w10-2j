package nailit.gui;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class TextDisplay extends JScrollPane{
	protected JTextPane textPane;
	public TextDisplay(int width, int height){
		this.setSize(width, height);
		createAndConfigureTextPane();
	}
	private void createAndConfigureTextPane(){
		textPane = new JTextPane();
		textPane.setEditable(false);
		setViewportView(textPane);
	}
	protected void basicDisplay(String text){
		centeriseTextDisplay();
		textPane.setText(text);
	}
	protected void centeriseTextDisplay(){
		SimpleAttributeSet textAttribute = new SimpleAttributeSet();
		StyleConstants.setAlignment(textAttribute, StyleConstants.ALIGN_CENTER);
		textPane.setParagraphAttributes(textAttribute, true);
	}
}
