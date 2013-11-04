//@author A0091372H
package nailit.gui.renderer;

import nailit.common.NIConstants;
import nailit.common.Task;
import nailit.common.TaskPriority;

public class TaskDetailsFormatter {
	private static final String HIGH_PRIORITY_CODE = "&nbsp &#x25B2 &#x25B2 &#x25B2";
	private static final String MED_PRIORITY_CODE = "&nbsp &#x25B2 &#x25B2";
	private static final String LOW_PRIORITY_CODE = "&nbsp &#x25B2";
	
	private static final String TASK_DISPLAY_STYLE = 
			"<head><style type = \"text/css\">" 
			+ "p {font-family: HelveticaNeue; font-size: 11px;}"
			+ "p.name {font-size: 15px; font-weight:bold}"
			+ "p.date {font-size: 12x; font-weight:bold}"
			+ "p.time {font-size: 10px;}"
			+ "p.status {font-size: 11px;}"
			+ "p.priority {font-size: 10px;}"
			+ "p.title {font-size: 11px; font-weight: bold}"
			+ "p.desc {font-size: 11px; text-align:top;}"
			+ "</style></head>";
	private static final String TASK_NAME_AND_TAG_FORMAT = 
//			"<tr><td><p class = \"title\">Name: </p></td>"
			"<tr>"
			+ "<td colspan = 7><p class = \"name\">%1s %2s</p></td><td></td><td></td></tr>";
	private static final String TASK_PRIORITY_FORMAT = 
			"<tr><td><p class = \"title\">Priority: </p></td><td><p class = \"priority\">%1s</p></td>";
	private static final String TASK_STATUS_FORMAT = 
			"<td><p class = \"title\">Status: </p></td><td><p class = \"status\">%1s</p></td>";
	private static final String TASK_REMINDER_FORMAT = 
			"<td><p class = \"title\">Reminder: </p></td><p>%1s</p></td>";
	private static final String NO_REMINDERS = "None";
//	private static final String TASK_DESCRIPTION_FORMAT = "<td colspan = 1 rowspan = %1d>"
//			+ "<p class = \"title\"> Description: </p></td>"
//			+ "<td rowspan = %2d>"
//			+ "<p class = \"desc\">%3s</p></td>";
	private static final String TASK_DESCRIPTION_FORMAT = "<td colspan = 5 rowspan = %1d style = \"padding-left:5px; padding-top: 0px\">"
			+ "<p class = \"title\">Description: </p>"
			+ "<p class = \"desc\">%3s</p></td>";
	private static final String EVENT_DATETIME_FORMAT ="<tr><td><p class = \"title\">Start: </p></td>"
			+ "<td width = \"70px\">%1s</td><td><p class = \"title\">End: </p></td>"
			+ "<td width = \"70px\">%2s</td>";
	private static final String SINGLE_DATETIME_TASK_FORMAT = "<td><p class = \"title\">%1s: </p></td>"
			+ "<td width = \"70px\">%2s</td>";
	public static final String TASK_CONCISE_FORMAT = "<td></td>"
			+ "<td><p class = \"name\">%1s</p></td>"
			+ "%2s";
	public static final String TASK_SINGLE_CONCISE_DATE = "<td>%1s</td>";
	public static final String TASK_DOUBLE_CONCISE_DATE = "<td>%1s</td><td><p> - </p></td><td>%2s</td>";
	public static String formatTaskForDisplay(Task task){
		int rowCount = 2;
		String tagDetails = "";
		if(!task.getTag().isEmpty()){
			tagDetails += "<font color = \"gray\">" + task.getTag() + "</font>";
		}
		String details = "<html>" + TASK_DISPLAY_STYLE 
						+ "<table>" + String.format(TASK_NAME_AND_TAG_FORMAT, task.getName(), tagDetails) + "<hr>";
		
		String otherDetails = formatTaskDateTimeForDisplay(task);
		
		if(!task.isEvent()){
			if(task.checkCompleted()){
				otherDetails += String.format(TASK_STATUS_FORMAT, "Done");
			}else{
				otherDetails += String.format(TASK_STATUS_FORMAT, "Not Done");
			}
			if(task.isFloatingTask()){
				otherDetails += "<td></td><td></td>";
			}
		}

		String taskDesc = task.getDescription();
		if(taskDesc != null && !taskDesc.isEmpty() && !taskDesc.equals("null")){
			otherDetails += formatTaskDescriptionForDisplay(taskDesc, rowCount);
		}
		String taskPriorityDisp = formatPriorityForDisplay(task.getPriority());
		otherDetails += "</tr>" + String.format(TASK_PRIORITY_FORMAT, taskPriorityDisp);
		if(task.getReminder() != null){
			String reminderDate = task.getReminder().toString(NIConstants.DISPLAY_DATE_FORMAT);
			otherDetails += String.format(TASK_REMINDER_FORMAT, reminderDate);
		}else{
			otherDetails += String.format(TASK_REMINDER_FORMAT, NO_REMINDERS);
		}
		
		otherDetails += "</tr>";

		details += otherDetails + "</table></html>";
		return details;
	}
	
	public static String formatTaskDateTimeForDisplay(Task task){
		String details = "";
		if(!task.isFloatingTask()){
			if(task.isEvent()){
				details += String.format(EVENT_DATETIME_FORMAT, 
						TaskDateTimeDisplayRenderer.formatTaskDateTimeCellDisplay(task.getStartTime()),
						TaskDateTimeDisplayRenderer.formatTaskDateTimeCellDisplay(task.getEndTime()));
			}else{
				if(task.getStartTime() != null){
					details += String.format(SINGLE_DATETIME_TASK_FORMAT, "Start",
							TaskDateTimeDisplayRenderer.formatTaskDateTimeCellDisplay(task.getStartTime()));
				}else{
					details += String.format(SINGLE_DATETIME_TASK_FORMAT, "Due",
							TaskDateTimeDisplayRenderer.formatTaskDateTimeCellDisplay(task.getEndTime()));  
				}
			}
		}
		return details;
	}
	public static String formatPriorityForDisplay(TaskPriority priority){
		String ret = priority.toString();
		switch(priority){
		case LOW:
			ret += LOW_PRIORITY_CODE;
			break;
		case MEDIUM:
			ret += MED_PRIORITY_CODE;
			break;
		case HIGH:
			ret += HIGH_PRIORITY_CODE;
			break;
		}
		return ret;	
	}
	public static String formatTaskDescriptionForDisplay(String desc, int rowCount){
		String details = String.format(TASK_DESCRIPTION_FORMAT, rowCount, desc);
		return details;
	}
}
