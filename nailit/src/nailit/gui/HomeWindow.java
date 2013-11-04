//@author A0091372H
package nailit.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JPanel;

import nailit.common.Task;
import nailit.gui.renderer.TaskDateTimeDisplayRenderer;
import nailit.gui.renderer.TaskDetailsFormatter;

public class HomeWindow extends ExtendedWindow{
	private static final String REMINDER_DISPLAY_HEADER 
	= "<h1 style = \"padding-left: 5px\">Reminders: </h1>";
	private final HomeWindow selfRef = this;
	
	public HomeWindow(GUIManager GUIMain, int width) {
		super(GUIMain, width);
	}
	
	protected void displayReminders(Vector<Task> tasks){
		if(tasks == null){
			return;
		}
		StringBuilder str = new StringBuilder();
		str.append("<html>");
		str.append(REMINDER_DISPLAY_HEADER);
		for(Task task: tasks){
			str.append("<tr>");
			str.append(formatTasksForReminderDisplay(task));
			str.append("</tr>");
		}
		str.append("</html>");
		((TextDisplay) displayPane).displayHTMLFormattedText(str.toString());
	}
	protected String formatTasksForReminderDisplay(Task task){
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
		taskDetails = String.format(TaskDetailsFormatter.TASK_CONCISE_FORMAT, task.getName(), dateDetails);
		return taskDetails;
	}
}
