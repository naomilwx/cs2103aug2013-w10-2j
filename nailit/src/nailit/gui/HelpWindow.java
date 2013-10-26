package nailit.gui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class HelpWindow extends ExtendedWindow{
	private static final int DEFAULT_Y_TOP_OFFSET = 20;
	private static final int DEFAULT_WINDOW_HEIGHT = 300;
	
	private static final float HELP_WINDOW_OPACITY = 0.7f;
	private static final Color HELP_WINDOW_DEFAULT_COLOR = Color.white;
	
	public HelpWindow(GUIManager GUIMain, int width){
		super(GUIMain, width);
		initialiseAndConfigureWindowContent();
	}
	
	@Override
	protected void positionFrameBasedOnMainWindowPos(){
		windowHeight = DEFAULT_WINDOW_HEIGHT;
		defaultYPos = GUIManager.MAIN_WINDOW_Y_POS + DEFAULT_Y_TOP_OFFSET;
		defaultXPos = GUIManager.MAIN_WINDOW_X_POS;
		super.positionFrameBasedOnMainWindowPos();
	}
	@Override
	protected void recalculateExtendedWindowPosition(){
		windowYPos = GUIBoss.getMainWindowLocationCoordinates().height + DEFAULT_Y_TOP_OFFSET;
		windowXPos = GUIBoss.getMainWindowLocationCoordinates().width;
	}
	@Override
	protected void configureHomeWindowFrame(){
		super.configureHomeWindowFrame();
		setBackground(HELP_WINDOW_DEFAULT_COLOR);
		setOpacity(HELP_WINDOW_OPACITY);
		setAlwaysOnTop(true);
	}
}
