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
		StringBuilder str = new StringBuilder();
		str.append("<html>");
		int count = 1;
		for(Task task: tasks){
			str.append("<tr>");
			str.append(formatTasksForReminderDisplay(task, count));
			str.append("</tr>");
			count = count + 1;
		}
		str.append("</html>");
		((TextDisplay) displayPane).displayHTMLFormattedText(str.toString());
	}
	protected String formatTasksForReminderDisplay(Task task, int number){
		String taskDetails;
		String dateDetails;
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
		taskDetails = String.format(TaskDetailsFormatter.TASK_CONCISE_FORMAT,
				"" + number , task.getName(), dateDetails);
		return taskDetails;
	}
}
