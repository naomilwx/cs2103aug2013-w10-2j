package nailit.gui;

import javax.swing.JPanel;

public class HomeWindow extends ExtendedWindow{
	public HomeWindow(GUIManager GUIMain, int width) {
		super(GUIMain, width);
		initialiseAndConfigureWindowContent();
	}
	private void initialiseAndConfigureWindowContent(){
		contentHeight = EXTENDED_WINDOW_HEIGHT - 2 * EXTENDED_WINDOW_Y_BUFFER;
		contentWidth = windowWidth - 2 * EXTENDED_WINDOW_X_BUFFER;
		displayPane = new TextDisplay(contentWidth, contentHeight);
		displayPane.setLocation(EXTENDED_WINDOW_X_BUFFER, EXTENDED_WINDOW_Y_BUFFER);
		addItem(displayPane);
	}
	protected void displayReminders(){
		//TODO:
	}
}
