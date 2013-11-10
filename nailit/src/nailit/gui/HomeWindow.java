//@author A0091372H
package nailit.gui;

import java.util.Vector;

import org.joda.time.DateTime;

import nailit.common.NIConstants;
import nailit.common.Task;


@SuppressWarnings("serial")
public class HomeWindow extends ExtendedWindow{
	private static final String TASK_REMINDER_DISPLAY_HEADER 
	= "<h1 style = \"padding-left: 9px\">Deadlines: </h1>";
	private static final String FLOATING_REMINDER_DISPLAY_HEADER
	= "<h1 style = \"padding-left: 9px\">Todo: </h1>";
	private static final String EVENT_REMINDER_DISPLAY_HEADER 
	= "<h1 style = \"padding-left: 9px\">Events: </h1>";
	
	private static final String TASK_CONCISE_FORMAT = "<td></td>%1s<td><p>%2s</p></td>";
	private static final String DONE_TASK_HEADER = "&#10004; ";
	private static final String UNDONE_TASK_HEADER = "";
	public static final String TASK_SINGLE_CONCISE_DATE = "<p>[%1s, %2s]</p>";
	public static final String TODAYS_DEADLINE_DISPLAY = "<p>[TODAY, %1s]</p>";
	public static final String ONE_DAY_EVENT_DATE_DISPLAY = "<p>[%1s,<br>"
															+ "%2s - %3s]</p>";
	public static final String EVENT_DATE_DISPLAY = "<p>[%1s  %2s - <br> %3s %4s]</p>";
	
	public HomeWindow(GUIManager GUIMain, int width) {
		super(GUIMain, width);
	}
	protected void formatAndAppendTaskList(StringBuilder str, String displayHeader, Vector<Task> tasks){
		str.append(displayHeader);
		str.append("<table>");
		for(Task task: tasks){
			str.append("<tr>");
			str.append(formatTasksForReminderDisplay(task));
			str.append("</tr>");
		}
		str.append("</table>");
	}
	protected boolean displayReminders(Vector<Vector <Task>> tasks){
		if(tasks == null){
			return false;
		}
		boolean notAllEmpty = false;
		Vector<Task> taskReminders = tasks.get(NIConstants.REMINDER_DEADLINE_TASKS_INDEX);
		Vector<Task> floatingTaskReminders = tasks.get(NIConstants.REMINDER_FLOATING_TASKS_INDEX);
		Vector<Task> eventReminders = tasks.get(NIConstants.REMINDER_EVENTS_INDEX);
		StringBuilder str = new StringBuilder();
		str.append("<html>");
		if(!taskReminders.isEmpty()){
			notAllEmpty = true;
			formatAndAppendTaskList(str, TASK_REMINDER_DISPLAY_HEADER, taskReminders);
		}
		
		if(! floatingTaskReminders.isEmpty()){
			notAllEmpty = true;
			formatAndAppendTaskList(str, FLOATING_REMINDER_DISPLAY_HEADER, floatingTaskReminders);
		}
		if(!eventReminders.isEmpty()){
			notAllEmpty = true;
			formatAndAppendTaskList(str, EVENT_REMINDER_DISPLAY_HEADER, eventReminders);
		}
		str.append("</html>");
		((TextDisplay) displayPane).displayHTMLFormattedText(str.toString());
		return notAllEmpty;
	}
	
	protected String formatTasksForReminderDisplay(Task task){
		String taskDetails;
		String dateDetails;
		if(task.isFloatingTask()){
			dateDetails = "";
		}else if(task.getStartTime() != null){
			dateDetails = "<td width = \"120px\">" + formatEventDate(task) + "</td>";
		}else{
			dateDetails = "<td width = \"120px\">" + formatDeadline(task) + "</td>";
		}
		taskDetails = String.format(TASK_CONCISE_FORMAT, dateDetails, getTaskStatusSymbol(task) + task.getName());
		return taskDetails;
	}
	private String getTaskStatusSymbol(Task task){
		if(task.checkCompletedOrOver()){
			return DONE_TASK_HEADER;
		}else{
			return UNDONE_TASK_HEADER;
		}
	}
	protected String formatEventDate(Task task){
		String dateString = "";
		
		DateTime start = task.getStartTime();
		DateTime end = task.getEndTime();
		
		if(end == null){
			dateString = String.format(EVENT_DATE_DISPLAY, start.toString(NIConstants.DISPLAY_DATE_SHORT_FORMAT),
					start.toString(NIConstants.DISPLAY_TIME_FORMAT), "", "");
		}else if(task.isOneDayEvent()){
			dateString = String.format(ONE_DAY_EVENT_DATE_DISPLAY, start.toString(NIConstants.DISPLAY_DATE_SHORT_FORMAT), 
					start.toString(NIConstants.DISPLAY_TIME_FORMAT), end.toString(NIConstants.DISPLAY_TIME_FORMAT));
		}else{
			dateString = String.format(EVENT_DATE_DISPLAY, start.toString(NIConstants.DISPLAY_DATE_SHORT_FORMAT),
									start.toString(NIConstants.DISPLAY_TIME_FORMAT), end.toString(NIConstants.DISPLAY_DATE_FORMAT),
									end.toString(NIConstants.DISPLAY_TIME_FORMAT));
		}
		if(task.isHappeningToday()){
			dateString = "<b>" + dateString + "</b>";
		}
		return dateString;
	}
	protected String formatDeadline(Task task){
		DateTime date = task.getEndTime();
		String deadline = "";
		if(date == null){
			return deadline;
		}
		if(task.isHappeningToday()){
			deadline = "<b>" + String.format(TODAYS_DEADLINE_DISPLAY,date.toString(NIConstants.DISPLAY_TIME_FORMAT)) + "</b>";
		}else{
			deadline = String.format(TASK_SINGLE_CONCISE_DATE, date.toString(NIConstants.DISPLAY_DATE_SHORT_FORMAT), date.toString(NIConstants.DISPLAY_TIME_FORMAT));
		}
		
		if(task.isOverDueTask()){
			deadline = "<font style = \"color: red; font-weight: bold\">" + deadline + "</font>";
		}
		return deadline;
	}
}
