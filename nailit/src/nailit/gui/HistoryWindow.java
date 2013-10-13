package nailit.gui;

import java.util.Vector;

import nailit.common.Result;

public class HistoryWindow extends ExtendedWindow{
	public HistoryWindow(GUIManager GUIMain, int width) {
		super(GUIMain, width);
		initialiseAndConfigureWindowContent();
	}
	private void initialiseAndConfigureWindowContent(){
		contentHeight = EXTENDED_WINDOW_HEIGHT - 2 * EXTENDED_WINDOW_Y_BUFFER;
		contentWidth = windowWidth - 2 * EXTENDED_WINDOW_X_BUFFER;
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
