//@author A0091372H
package nailit.gui;

import java.util.Vector;

import nailit.common.Result;

public class HistoryWindow extends ExtendedWindow{
	public HistoryWindow(GUIManager GUIMain, int width) {
		super(GUIMain, width);
	}
	@Override
	protected void initialiseAndConfigureWindowContent(){
		setWindowContentSize();
		displayPane = new TableDisplay(contentWidth, contentHeight, Result.HISTORY_DISPLAY);
		displayPane.setLocation(EXTENDED_WINDOW_X_BUFFER, EXTENDED_WINDOW_Y_BUFFER);
		addItem(displayPane);
	}
	protected void displayHistoryList(Vector<String> list){
		//TODO
		Vector<String> row;
		int rowNum;
		for(int i = 0; i < list.size(); i++){
			row = new Vector<String>();
			rowNum = i+1;
			row.add(""+rowNum);
			row.add(list.get(i));
			((TableDisplay) displayPane).addContentToTable(row);
		}
	}
}
