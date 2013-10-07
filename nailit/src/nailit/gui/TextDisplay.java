package nailit.gui;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class TextDisplay extends JScrollPane{
	protected JTextPane textPane;
	public TextDisplay(int width, int height){
		setSize(width, height);
		setBorder(new LineBorder(GUIManager.BORDER_COLOR));
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
