package nailit.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import nailit.logic.CommandType;

public class HelpWindow extends ExtendedWindow{
	private static final int DEFAULT_Y_TOP_OFFSET = 22;
	private static final int DEFAULT_WINDOW_HEIGHT = 70;
	
	private static final float HELP_WINDOW_OPACITY = 0.9f;
	private static final Color HELP_WINDOW_DEFAULT_COLOR = Color.white;
	
	private static final int TIMER_INTERVAL = 100;
	private static final int TIMER_DELAY = 10000; //amount of time before item starts fading out
	private static final float OPACITY_INTERVAL_STEP = 0.1f;
	protected static final float NO_OPACITY = 0.5f;
	
	private final Timer fadeOutTimer = new Timer(0, null);
	
	public HelpWindow(GUIManager GUIMain, int width){
		super(GUIMain, width);
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
		setAlwaysOnTop(true);
	}
	
	protected String getSyntaxDisplayStringForCommandType(String command){
		String commandToGet = command.toUpperCase().trim();
		Vector<String[]> syntaxString = HelpWindowConstants.COMMAND_SYNTAX_LIST.get(commandToGet);
		String textStr = "";
		if(syntaxString != null){
			for(String[] strArr: syntaxString){
				if(!textStr.isEmpty()){
					textStr += "\n";
				}
				textStr += strArr[HelpWindowConstants.COMMAND_DESC_POS] + " : " + strArr[HelpWindowConstants.COMMAND_SYNTAX_POS];
			}
		}
		return textStr;
	}
	protected void displaySyntaxForCommandType(String command){
		String display = getSyntaxDisplayStringForCommandType(command);
		((TextDisplay) displayPane).displayText(display);
		adjustHelpWindowLocation(defaultYPos - DEFAULT_WINDOW_HEIGHT);
		showHelpWindow(DEFAULT_WINDOW_HEIGHT);
	}
	
	protected void displayListOfAvailableCommands(){
		((TextDisplay) displayPane).displayText(HelpWindowConstants.generateListOfSupportedCommands());
		adjustHelpWindowLocation(defaultYPos - DEFAULT_WINDOW_HEIGHT);
		showHelpWindow(DEFAULT_WINDOW_HEIGHT);
		fadeOutWindow(TIMER_DELAY, TIMER_INTERVAL, OPACITY_INTERVAL_STEP);
	}
	
	private void adjustHelpWindowLocation(int newYPos){
		windowYPos = newYPos;
		setLocation(windowXPos, windowYPos);
	}
	
	private void adjustHelpWindowHeight(int height){
		windowHeight = height;
		setSize(windowWidth, windowHeight);
		refreshContentSize();
	}
	private void showHelpWindow(int height){
		adjustHelpWindowHeight(height);
		setOpacity(HELP_WINDOW_OPACITY); //temporarily commented out. only works on java 7
		showWindowAsItIs();
	}
	private void fadeOutWindow(int displayTime, int timeInterval, final float opacityStep){
		fadeOutTimer.setInitialDelay(displayTime);
		fadeOutTimer.setDelay(timeInterval);
		fadeOutTimer.addActionListener(new ActionListener(){
			float nextOpacity = HELP_WINDOW_OPACITY;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				nextOpacity -= opacityStep;
				if(nextOpacity <= NO_OPACITY){
					fadeOutTimer.stop();
					setVisible(false);
				}else{
					setOpacity(nextOpacity);
				}
			}
		});
		fadeOutTimer.restart();
	}
}
