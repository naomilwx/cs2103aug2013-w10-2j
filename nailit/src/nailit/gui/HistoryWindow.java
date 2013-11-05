//@author A0091372H
package nailit.gui;

import java.util.Vector;

import nailit.common.NIConstants;
import nailit.common.Result;

public class HistoryWindow extends ExtendedWindow{
	private static final String COMMANDS_EXECUTED_HEADER 
	= "<h1 style = \"padding-left: 5px\">Commands executed: </h1>";
	private static final String COMMANDS_UNDID_HEADER 
	= "<h1 style = \"padding-left: 5px\">Commands undone: </h1>";
	public HistoryWindow(GUIManager GUIMain, int width) {
		super(GUIMain, width);
	}
	
	protected void displayHistoryList(Vector<Vector <String>> list){
		if(list == null){
			return;
		}
		StringBuilder str = new StringBuilder();
		str.append("<html>");
		
		Vector<String> undidCommands = list.get(NIConstants.HISTORY_UNDO_INDEX);
		str.append(COMMANDS_EXECUTED_HEADER);
		str.append("<table>");
		for(String command: undidCommands){
			str.append("<tr>");
			str.append(command);
			str.append("</tr>");
		}
		str.append("</table>");
		
		Vector<String> redoableCommands = list.get(NIConstants.HISTORY_REDO_INDEX);
		if(!redoableCommands.isEmpty()){
			str.append(COMMANDS_UNDID_HEADER);
			str.append("<table>");
			for(String command: redoableCommands){
				str.append("<tr>");
				str.append(command);
				str.append("</tr>");
			}
			str.append("</table>");
		}
		
		str.append("</html>");
		((TextDisplay) displayPane).displayHTMLFormattedText(str.toString());
	}
	
}
