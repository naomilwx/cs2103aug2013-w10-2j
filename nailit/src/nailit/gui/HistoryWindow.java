//@author A0091372H
package nailit.gui;

import java.util.Vector;

import nailit.common.NIConstants;

@SuppressWarnings("serial")
public class HistoryWindow extends ExtendedFadableWindow{
	private static final String COMMANDS_EXECUTED_HEADER 
	= "<h1 style = \"padding-left: 5px\">Commands executed: </h1>";
	private static final String COMMANDS_UNDID_HEADER 
	= "<h1 style = \"padding-left: 5px\">Commands undone: </h1>";
	
	protected static final int TIMER_INTERVAL = 250;
	protected static final int TIMER_DELAY = 10000; //amount of time before item starts fading out
	protected static final float OPACITY_INTERVAL_STEP = 0.05f;
	protected static final float NO_OPACITY = 0.5f;
	protected static final float FULL_OPACITY = 1.0f;
	
	public HistoryWindow(GUIManager GUIMain, int width) {
		super(GUIMain, width);
	}
	protected void formatAndAppendCommandsList(StringBuilder str, String header, Vector<String> list){
		str.append(header);
		str.append("<table>");
		for(String command: list){
			str.append("<tr>");
			str.append(command);
			str.append("</tr>");
		}
		str.append("</table>");
	}
	private void displayHistoryList(Vector<Vector <String>> list){
		if(list == null){
			return;
		}
		StringBuilder str = new StringBuilder();
		str.append("<html>");
		
		Vector<String> undidCommands = list.get(NIConstants.HISTORY_UNDO_INDEX);
		formatAndAppendCommandsList(str, COMMANDS_EXECUTED_HEADER, undidCommands);
		
		Vector<String> redoableCommands = list.get(NIConstants.HISTORY_REDO_INDEX);
		if(!redoableCommands.isEmpty()){
			formatAndAppendCommandsList(str, COMMANDS_UNDID_HEADER, redoableCommands);
		}
		
		str.append("</html>");
		((TextDisplay) displayPane).displayHTMLFormattedText(str.toString());
	}
	protected void displayHistoryList(Vector<Vector <String>> list, boolean fadeOut){
		displayHistoryList(list);
		if(!isVisible()){
			setVisible(true);
		}
		if(fadeOut){
			startFadeOutTimer();
		}
	}
	@Override
	public void setVisible(boolean visible){
		super.setVisible(visible);
		GUIBoss.setFocusOnCommandBar();
	}
}
