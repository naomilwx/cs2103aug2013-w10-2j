package nailit.gui;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.border.LineBorder;

public class NotificationArea extends TextDisplay{
	private static final Color SUCCESS_COLOR = Color.green;
	private static final Color FAILURE_COLOR = Color.red;
	protected static final int NOTIFICATION_HEIGHT = 50;
	
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
		textPane.setBorder(new LineBorder(displayColor));
		textPane.setForeground(displayColor);
		displayText(notification);
	}
}
