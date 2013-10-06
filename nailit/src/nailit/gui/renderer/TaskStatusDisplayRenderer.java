package nailit.gui.renderer;

import javax.swing.table.DefaultTableCellRenderer;

import nailit.common.Task;

public class TaskStatusDisplayRenderer extends DefaultTableCellRenderer{
	private static final String NULL_STATUS_DISPLAY = "<p>" + "-" + "</p>";
	
	@Override
	protected void setValue(Object value){
		setText((String) value);
	}
	public static String formatStatusCellDisplay(Task task){
		String formattedStr = "";
		if(task.isEvent()){
			if(task.checkCompleted()){
				formattedStr = "<p>" + "Done" + "</p>";
			}else{
				formattedStr = "<p>" + "Not done" + "</p>";
			}
		}else{
			formattedStr = NULL_STATUS_DISPLAY;
		}
		return formattedStr;
	}
}
