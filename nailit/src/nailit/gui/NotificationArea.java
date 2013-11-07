//@author A0091372H
package nailit.gui;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

@SuppressWarnings("serial")
public class NotificationArea extends TextDisplay{
	private static final int NOTIFICATION_BORDER_WIDTH = 3;
	private static final Color SUCCESS_COLOR = new Color(204, 240, 153);
	private static final Color FAILURE_COLOR = new Color(250, 88, 88);
	protected static final int NOTIFICATION_HEIGHT = 40;
	
	public NotificationArea(int width){
		super(width, NOTIFICATION_HEIGHT);
	}
	
	protected void displayNotification(String notification, boolean isSuccess){
		Color displayColor;
		if(isSuccess){
			displayColor = SUCCESS_COLOR;
		}else{
			displayColor = FAILURE_COLOR;
		}
		
		setBorder(new LineBorder(displayColor, NOTIFICATION_BORDER_WIDTH));
		displayText(notification);
	}
}
