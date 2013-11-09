//@author A0091372H
package nailit.gui;

import java.awt.Dimension;

import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTMLEditorKit;

@SuppressWarnings("serial")
public class TextDisplay extends ScrollableFocusableDisplay{
	protected JTextPane textPane;
	private int preferredHeight;
	
	public TextDisplay(int width, int height){
		configureMainFrame(width, height);
		createAndConfigureTextPane();
	}
	
	private void createAndConfigureTextPane(){
		preferredHeight = containerHeight;
		textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setPreferredSize(new Dimension(containerWidth, preferredHeight));
		textPane.setSize(containerWidth, preferredHeight);
		textPane.addFocusListener(super.displayFocusListener);
		setViewportView(textPane);
	}
	
	protected void displayText(String text){
		centeriseTextDisplay();
		textPane.setText(text);
	}
	protected void displayHTMLFormattedText(String text){
		textPane.setContentType("text/html");
		HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
		textPane.setEditorKit(htmlEditorKit);
		displayText(text);
	}
	protected void centeriseTextDisplay(){
		SimpleAttributeSet textAttribute = new SimpleAttributeSet();
		StyleConstants.setAlignment(textAttribute, StyleConstants.ALIGN_CENTER);
		textPane.setParagraphAttributes(textAttribute, true);
	}
}
