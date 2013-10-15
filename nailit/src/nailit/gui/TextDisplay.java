package nailit.gui;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class TextDisplay extends ScrollableFocusableDisplay{
	protected JTextPane textPane;
	private int containerWidth;
	private int containerHeight;
	private int preferredHeight;
	
	public TextDisplay(int width, int height){
		configureMainFrame(width, height);
		createAndConfigureTextPane();
	}
	private void configureMainFrame(int width, int height){
		containerWidth = width;
		containerHeight = height;
		setSize(containerWidth, containerHeight);
		setBorder(UNFOCUS_LINE_BORDER);
		addFocusListener(displayFocusListener);		
	}
	private void createAndConfigureTextPane(){
		preferredHeight = containerWidth/2;
		textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setPreferredSize(new Dimension(containerWidth, preferredHeight));
		textPane.setSize(containerWidth, preferredHeight);
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
