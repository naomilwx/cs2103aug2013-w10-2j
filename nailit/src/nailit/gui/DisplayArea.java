package nailit.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class DisplayArea extends JPanel {
	private static final Color DISPLAYAREA_BACKGROUND_COLOR = Color.white;
	private static final int Y_BUFFER_HEIGHT = GUIManager.Y_BUFFER_HEIGHT;
	private static final int X_BUFFER_WIDTH = GUIManager.X_BUFFER_WIDTH;
	private static final int WINDOW_RIGHT_BUFFER = GUIManager.WINDOW_RIGHT_BUFFER;
	private static final int WINDOW_BOTTOM_BUFFER = GUIManager.WINDOW_BOTTOM_BUFFER;
	
	private GUIManager GUIBoss;
	
	private int displayWidth;
	private int displayHeight;
	/**
	 * Create the panel.
	 */
	public DisplayArea(final GUIManager GUIMain, int containerWidth, int containerHeight) {
		GUIBoss = GUIMain;
		configureDisplayArea(containerWidth, containerHeight);
		
	}
	private void configureDisplayArea(int containerWidth, int containerHeight){
		displayWidth = containerWidth - X_BUFFER_WIDTH - WINDOW_RIGHT_BUFFER;
		displayHeight = containerHeight*4/5;
		this.setBorder(new LineBorder(GUIManager.BORDER_COLOR));
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.setBackground(DISPLAYAREA_BACKGROUND_COLOR);
		this.setLocation(X_BUFFER_WIDTH, Y_BUFFER_HEIGHT);
		this.setSize(displayWidth, displayHeight);
	}
	protected void addContent(Component component, boolean replace){
		if(replace){
			removeAll();
		}
		add(component);
		revalidate();
	}
	protected void setFocus(){
		requestFocus();
	}
}
