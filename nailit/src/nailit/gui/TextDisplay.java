package nailit.gui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class TextDisplay extends JScrollPane{
	private final LineBorder FOCUS_LINE_BORDER = new LineBorder(GUIManager.FOCUSED_BORDER_COLOR);
	private final LineBorder UNFOCUS_LINE_BORDER = new LineBorder(GUIManager.BORDER_COLOR);
	private final FocusListener textDisplayFocusListener = new FocusListener(){
		public void focusGained(FocusEvent event) {
			setBorder(FOCUS_LINE_BORDER);
		 }
		public void focusLost(FocusEvent event){
			setBorder(UNFOCUS_LINE_BORDER);
		}
	};
	
	protected JTextPane textPane;
	
	public TextDisplay(int width, int height){
		setSize(width, height);
		setBorder(UNFOCUS_LINE_BORDER);
		addFocusListener(textDisplayFocusListener);
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
