package nailit.gui;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class DisplayArea extends JPanel {
	private static final Color DISPLAYAREA_BACKGROUND_COLOR = Color.white;
	
	private GUIManager GUIBoss;
	/**
	 * Create the panel.
	 */
	public DisplayArea(final GUIManager GUIMain) {
		GUIBoss = GUIMain;
		configureDisplayArea();
	}
	private void configureDisplayArea(){
		this.setBorder(new LineBorder(GUIManager.BORDER_COLOR));
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.setBackground(DISPLAYAREA_BACKGROUND_COLOR);
	}
}
