//@author A0091372H
package nailit.gui.renderer;

import javax.swing.table.DefaultTableCellRenderer;

import nailit.common.Task;

public class TaskStatusDisplayRenderer extends DefaultTableCellRenderer{
	private static final String NULL_STATUS_DISPLAY = "<p>" + "-" + "</p>";
	
	@Override
	protected void setValue(Object value){
		String output = "<html>" + (String) value + "<html>";
		setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		setText(output);
	}
	public static String formatStatusCellDisplay(Task task){
		String formattedStr = "";
		if(!task.isEvent()){
			if(task.checkCompleted()){
				formattedStr = "<p class = \"status\">" + "Done" + "</p>";
			}else{
				formattedStr = "<p class = \" status\">" + "Undone" + "</p>";
			}
		}else{
			formattedStr = NULL_STATUS_DISPLAY;
		}
		return formattedStr;
	}
}
