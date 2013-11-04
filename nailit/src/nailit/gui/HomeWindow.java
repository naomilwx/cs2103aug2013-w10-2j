//@author A0091372H
package nailit.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JPanel;

import nailit.common.NIConstants;
import nailit.common.Task;
import nailit.gui.renderer.TaskDateTimeDisplayRenderer;
import nailit.gui.renderer.TaskDetailsFormatter;

public class HomeWindow extends ExtendedWindow{
	private static final String TASK_REMINDER_DISPLAY_HEADER 
	= "<h1 style = \"padding-left: 5px\">Tasks: </h1>";
	private static final String EVENT_REMINDER_DISPLAY_HEADER 
	= "<h1 style = \"padding-left: 5px\">Events: </h1>";
	private final HomeWindow selfRef = this;
	
	public HomeWindow(GUIManager GUIMain, int width) {
		super(GUIMain, width);
	}
	
	protected void displayReminders(Vector<Vector <Task>> tasks){
		if(tasks == null){
			return;
		}
		Vector<Task> taskReminders = tasks.get(NIConstants.REMINDER_TASKS_INDEX);
		Vector<Task> eventReminders = tasks.get(NIConstants.REMINDER_EVENTS_INDEX);
		StringBuilder str = new StringBuilder();
		str.append("<html>");
		str.append(TASK_REMINDER_DISPLAY_HEADER);
		str.append("<table>");
		for(Task task: taskReminders){
			str.append("<tr>");
			str.append(formatTasksForReminderDisplay(task));
			str.append("</tr>");
		}
		str.append("</table>");
		str.append(EVENT_REMINDER_DISPLAY_HEADER);
		str.append("<table>");
		for(Task task: eventReminders){
			str.append("<tr>");
			str.append(formatTasksForReminderDisplay(task));
			str.append("</tr>");
		}
		str.append("</table>");
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
