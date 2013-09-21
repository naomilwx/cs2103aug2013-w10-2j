package nailit.gui;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class CommandBar extends JPanel {
	//reference to main GUI container class so CommandBar can have access to the methods there
	private GUIManager GUIBoss;
	private JTextField textBar;
	/**
	 * Create the panel.
	 */
	public CommandBar(final GUIManager GUIMain) {
		GUIBoss = GUIMain;
	}

}
