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
	
	public TextDisplay(int width, int height){
		configureMainFrame(width, height);
		createAndConfigureTextPane();
	}
	
	private void createAndConfigureTextPane(){
		textPane = new JTextPane();
		textPane.setEditable(false);
		setTextPaneDimensions(containerWidth, containerHeight);
		textPane.addFocusListener(super.displayFocusListener);
		setViewportView(textPane);
	}
	private void setTextPaneDimensions(int width, int height){
		if(textPane != null){
			textPane.setPreferredSize(new Dimension(width, height));
			textPane.setSize(width, height);
		}
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
	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		setTextPaneDimensions(width, height);
	}
}
