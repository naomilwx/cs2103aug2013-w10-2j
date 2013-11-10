package nailit.gui;

import java.awt.Color;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Vector;

import nailit.logic.CommandType;

@SuppressWarnings("serial")
public class HelpWindow extends ExtendedFadableWindow{
	protected static final String HTML_FORMATTED_STRING = "<html>" + HelpWindowConstants.TEXT_DISPLAY_STYLE + "%1s</html>";
	protected static final String COMMAND_SYNTAX_HTML_FORMAT = "<tr><td>%1s  </td><td>%2s</td></tr>";
	protected static final String KEYBOARD_SHORTCUT_HTML_FORMAT = "<tr><td>%1s : </td><td>%2s</td></tr>";
	
	private static final String ALL_COMMAND_SYNTAX_TITLE = "Command Syntax";
	private static final String ALL_KEYBOARD_SHORTCUT_TITLE = "Keyboard Shortcuts";
	private static final int DEFAULT_Y_TOP_OFFSET = 30;
	private static final int DEFAULT_WINDOW_HEIGHT = 70;
	private static final int EXTRA_LINE_HEIGHT = 20;
	private static final int MAX_COMMAND_SYNTAX_WINDOW_HEIGHT = 150;
	
	private static final int FULL_WINDOW_HEIGHT = 300;
	
	
	private static final Color HELP_WINDOW_DEFAULT_COLOR = Color.white;
	
	protected static final int TIMER_INTERVAL = 250;
	protected static final int TIMER_DELAY = 10000; //amount of time before item starts fading out
	protected static final float OPACITY_INTERVAL_STEP = 0.05f;
	protected static final float NO_OPACITY = 0.5f;
	protected static final float FULL_OPACITY = 0.9f;

	private int offSet;

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
	
	//Methods to format and display help window contents
	protected String getSyntaxDisplayStringForCommandType(String command){
		String commandToGet = command.toUpperCase().trim();
		Vector<String[]> syntaxString = HelpWindowConstants.COMMAND_SYNTAX_LIST.get(commandToGet);
		String textStr = "";
		offSet = 0;
		if(syntaxString != null){
			for(String[] strArr: syntaxString){
				textStr += formatCommandSyntaxRowText(strArr);
				offSet = offSet + 1;
			}
		}
		return textStr;
	}
	private String formatCommandSyntaxRowText(String[] strArr){
		return String.format(COMMAND_SYNTAX_HTML_FORMAT, strArr[HelpWindowConstants.COMMAND_DESC_POS], 
				strArr[HelpWindowConstants.COMMAND_SYNTAX_POS]);
	}
	private String formatTitleForDisplay(String title){
		return String.format(HelpWindowConstants.TITLE_TEXT_HTML_FORMAT, title);
	}
	protected String formatDisplayForKeyBoardShortcuts(){
		StringBuilder overallStr = new StringBuilder();
		for(Entry<String, HashMap<String, String>> entry: HelpWindowConstants.ALL_KEYBOARD_COMMANDS.entrySet()){
			overallStr.append(String.format(HelpWindowConstants.SUBTITLE_TEXT_HTML_FORMAT, entry.getKey()));
			formatWindowKeyBoardShortcut(overallStr, entry.getValue());
		}
		return overallStr.toString();
	}
	protected void formatWindowKeyBoardShortcut(StringBuilder builder, HashMap<String, String> keyboardShortcutMap){
		for(Entry<String, String> entry: keyboardShortcutMap.entrySet()){
			builder.append(String.format(KEYBOARD_SHORTCUT_HTML_FORMAT, entry.getKey(), entry.getValue()));
		}
	}
	protected String formatDisplayForSupportedCommandsSyntax(){
		StringBuilder overallStr = new StringBuilder();
		for(CommandType commandType: CommandType.values()){
			if(!commandType.equals(CommandType.INVALID)){
				Vector<String[]> syntaxString = HelpWindowConstants.COMMAND_SYNTAX_LIST.get(commandType.toString());
				if(syntaxString != null){
					for(String[] strArr: syntaxString){
						if(strArr.length >= 3 && strArr[2].equals(HelpWindowConstants.DATE_SYNTAX_LABEL)){
							continue;
						}else{
							overallStr.append(formatCommandSyntaxRowText(strArr));
						}
					}
				}
			}
		}
		return overallStr.toString();
	}
	protected void displayFullHelpWindow(){
		String displayText = formatTitleForDisplay(ALL_KEYBOARD_SHORTCUT_TITLE) + formatDisplayForKeyBoardShortcuts() 
				+ formatTitleForDisplay(ALL_COMMAND_SYNTAX_TITLE)
				+ formatDisplayForSupportedCommandsSyntax();
		String display = formatStringforDisplay(displayText);
		displayFormattedText(display);
		int yposOffset = GUIBoss.getHelpWindowOverlayTopOffset();
		adjustAndshowHelpWindow(yposOffset, FULL_WINDOW_HEIGHT);
		GUIUtilities.scrollDisplayToTop((TextDisplay) displayPane);
	}
	protected void displaySyntaxForSupportedCommands(){
		String displayText = formatTitleForDisplay(ALL_COMMAND_SYNTAX_TITLE)
							+ formatDisplayForSupportedCommandsSyntax();
		String display = formatStringforDisplay(displayText);
		displayFormattedText(display);
		int yposOffset = GUIBoss.getHelpWindowOverlayTopOffset();
		adjustAndshowHelpWindow(yposOffset, FULL_WINDOW_HEIGHT);
		GUIUtilities.scrollDisplayToTop((TextDisplay) displayPane);
	}
	protected void displaySyntaxForCommandType(String command){
		String displayText = formatTitleForDisplay("Syntax for "+ command + " command")
				+ getSyntaxDisplayStringForCommandType(command);
		String display = formatStringforDisplay(displayText);
		displayFormattedText(display);
		
		int newWindowHeight = Math.min(DEFAULT_WINDOW_HEIGHT + offSet * EXTRA_LINE_HEIGHT,
				MAX_COMMAND_SYNTAX_WINDOW_HEIGHT);
		int yposOffset = - newWindowHeight;
		adjustAndshowHelpWindow(yposOffset, newWindowHeight);
		fadeOutWindow(TIMER_DELAY, TIMER_INTERVAL, OPACITY_INTERVAL_STEP);
		GUIUtilities.scrollDisplayToTop((TextDisplay) displayPane);
		GUIBoss.setFocusOnCommandBar();
	}
	
	protected void displayListOfAvailableCommands(){
		String display = formatStringforDisplay(HelpWindowConstants.generateListOfSupportedCommands());
		displayFormattedText(display);
		int yposOffset = - DEFAULT_WINDOW_HEIGHT;
		adjustAndshowHelpWindow(yposOffset, DEFAULT_WINDOW_HEIGHT);
		fadeOutWindow(TIMER_DELAY, TIMER_INTERVAL, OPACITY_INTERVAL_STEP);
		GUIBoss.setFocusOnCommandBar();
	}
	private String formatStringforDisplay(String displayText){
		return String.format(HTML_FORMATTED_STRING, displayText);
	}
	private void displayFormattedText(String text){
		((TextDisplay) displayPane).displayHTMLFormattedText(text);
	}
	private void adjustHelpWindowLocation(int offset){
		windowYPos = windowYPos + offset;
		setLocation(windowXPos, windowYPos);
	}
	
	private void adjustHelpWindowHeight(int height){
		windowHeight = height;
		setSize(windowWidth, windowHeight);
		refreshContentSize();
	}
	private void adjustAndshowHelpWindow(int yPosOffset, int height){
		adjustHelpWindowHeight(height);
		setOpacity(FULL_OPACITY); //temporarily commented out. only works on java 7
		setVisible(true);
		adjustHelpWindowLocation(yPosOffset);
	}
	
}
