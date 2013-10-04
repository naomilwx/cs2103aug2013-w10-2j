package nailit.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class DisplayArea extends JLayeredPane {
	private static final Color DISPLAYAREA_BACKGROUND_COLOR = Color.white;
	private static final int Y_BUFFER_HEIGHT = GUIManager.Y_BUFFER_HEIGHT;
	private static final int X_BUFFER_WIDTH = GUIManager.X_BUFFER_WIDTH;
	private static final int WINDOW_RIGHT_BUFFER = GUIManager.WINDOW_RIGHT_BUFFER;
	private static final int WINDOW_BOTTOM_BUFFER = GUIManager.WINDOW_BOTTOM_BUFFER;
	
	private GUIManager GUIBoss;
	private JPanel defaultPane;
	private JPanel popupPane;
	
	private int displayWidth;
	private int displayHeight;
	/**
	 * Create the panel.
	 */
	public DisplayArea(final GUIManager GUIMain, int containerWidth, int containerHeight) {
		GUIBoss = GUIMain;
		configureDisplayArea(containerWidth, containerHeight);
		initialiseLayers();
	}
	private void initialiseLayers() {
		defaultPane = new JPanel();
		defaultPane.setLayout(new BoxLayout(defaultPane,BoxLayout.Y_AXIS));
		setLayerToDefaultSettings(defaultPane);
		this.add(defaultPane,JLayeredPane.DEFAULT_LAYER);
		
		popupPane = new JPanel();
		popupPane.setLayout(null);
		setLayerToDefaultSettings(popupPane);
		add(popupPane, JLayeredPane.POPUP_LAYER);
	}
	private void setLayerToDefaultSettings(JPanel layer){
		layer.setSize(this.getSize());
		layer.setLocation(GUIManager.DEFAULT_COMPONENT_LOCATION);
		layer.setOpaque(false);
	}
	private void configureDisplayArea(int containerWidth, int containerHeight){
		displayWidth = containerWidth - X_BUFFER_WIDTH - WINDOW_RIGHT_BUFFER;
		displayHeight = containerHeight*4/5;
		this.setBorder(new LineBorder(GUIManager.BORDER_COLOR));
		this.setBackground(DISPLAYAREA_BACKGROUND_COLOR);
		this.setLocation(X_BUFFER_WIDTH, Y_BUFFER_HEIGHT);
		this.setSize(displayWidth, displayHeight);
	}
	protected void hideNotifications(){
		popupPane.setVisible(false);
	}
	protected void showNotifications(){
		popupPane.setVisible(true);
	}
	protected void addContent(Component component, boolean replace){
		if(replace){
			defaultPane.removeAll();
		}
		defaultPane.add(component);
		revalidate();
	}
	protected void addPopup(Component component){
		popupPane.add(component);
	}
	protected void setFocus(){
		requestFocus();
	}
}
