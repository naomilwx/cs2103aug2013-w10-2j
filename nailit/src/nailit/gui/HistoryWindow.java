//@author A0091372H
package nailit.gui;

import java.util.Vector;

import nailit.common.Result;

public class HistoryWindow extends ExtendedWindow{
	private static final String COMMANDS_EXECUTED__HEADER 
	= "<h1 style = \"padding-left: 5px\">Commands executed: </h1>";
	private static final String COMMANDS_UNDID__HEADER 
	= "<h1 style = \"padding-left: 5px\">Commands undone: </h1>";
	public HistoryWindow(GUIManager GUIMain, int width) {
		super(GUIMain, width);
	}
	
	protected void displayHistoryList(Vector<Vector <String>> list){
		//TODO
		
	}
	
}
