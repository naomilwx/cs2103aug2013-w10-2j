//@author A0091372H
package nailit.gui;

import java.util.Vector;

import javax.swing.JPanel;

import nailit.common.Task;
import nailit.gui.renderer.TaskDateTimeDisplayRenderer;
import nailit.gui.renderer.TaskDetailsFormatter;

public class HomeWindow extends ExtendedWindow{
	public HomeWindow(GUIManager GUIMain, int width) {
		super(GUIMain, width);
	}
	
	protected void displayReminders(Vector<Task> tasks){
		//TODO:
		StringBuilder str = new StringBuilder();
		str.append("<html>");
		String dateDetails;
		int count = 1;
		for(Task task: tasks){
			if(task.isEvent()){
				dateDetails = String.format(TaskDetailsFormatter.TASK_DOUBLE_CONCISE_DATE, 
						TaskDateTimeDisplayRenderer.formatTaskDateTimeCellDisplay(task.getStartTime()),
						TaskDateTimeDisplayRenderer.formatTaskDateTimeCellDisplay(task.getEndTime()));
			}else if(task.getStartTime() != null){
				dateDetails = String.format(TaskDetailsFormatter.TASK_SINGLE_CONCISE_DATE, 
						TaskDateTimeDisplayRenderer.formatTaskDateTimeCellDisplay(task.getStartTime()));
			}else if(task.getEndTime() != null){
				dateDetails = String.format(TaskDetailsFormatter.TASK_SINGLE_CONCISE_DATE, 
						TaskDateTimeDisplayRenderer.formatTaskDateTimeCellDisplay(task.getEndTime()));
			}else{
				dateDetails = "";
			}
			str.append("<tr>");
			str.append(String.format(TaskDetailsFormatter.TASK_CONCISE_FORMAT,
				"" + count++ , task.getName(),dateDetails));
			str.append("</tr>");
		}
		str.append("</html>");
		((TextDisplay) displayPane).displayHTMLFormattedText(str.toString());
	}
}
