//@author A0091372H
package nailit.gui.renderer;

import nailit.common.NIConstants;
import nailit.common.Task;

public class TaskDetailsFormatter {
	private static final String TASK_DISPLAY_STYLE = 
			"<head><style type = \"text/css\">" 
			+ "p {font-family: HelveticaNeue; font-size: 11px}"
			+ "p.name {font-size: 11px;}"
			+ "p.tag {font-size: 9px; color: gray;}"
			+ "p.date {font-size: 11px;}"
			+ "p.time {font-size: 10px;}"
			+ "p.status {font-size: 10px;}"
			+ "p.priority {font-size: 10px;}"
			+ "p.title {font-size: 11px; font-weight: bold}"
			+ "p.desc {font-size: 11px; text-align:top;}"
			+ "</style></head>";
	private static final String TASK_NAME_AND_TAG_FORMAT = 
			"<tr><td><p class = \"title\">Name: </p></td>"
			+ "<td colspan = 7><p class = \"name\">%1s</p>%2s</td><td></td><td></td></tr>";
	private static final String TASK_PRIORITY_FORMAT = 
			"<tr><td><p class = \"title\">Priority: </p></td><td><p class = \"priority\">" 
			+ "%1s</p></td>";
	private static final String TASK_STATUS_FORMAT = 
			"<td><p class = \"title\">Status: </p></td><td><p class = \"status\">"
			+ "%1s</p></td>";
	private static final String TASK_REMINDER_FORMAT = 
			"<td><p class = \"title\">Reminder: </p></td><p>%1s</p></td>";
//	private static final String TASK_DESCRIPTION_FORMAT = "<td colspan = 1 rowspan = %1d>"
//			+ "<p class = \"title\"> Description: </p></td>"
//			+ "<td rowspan = %2d>"
//			+ "<p class = \"desc\">%3s</p></td>";
	private static final String TASK_DESCRIPTION_FORMAT = "<td colspan = 5 rowspan = %1d>"
			+ "<p class = \"title\"> Description: </p>"
			+ "<p class = \"desc\">%3s</p></td>";
	private static final String EVENT_DATETIME_FORMAT ="<tr><td><p class = \"title\">Start: </p></td><td>"
			 + "%1s</td><td><p class = \"title\">End: </p></td><td>" 
			 + "%2s</td>";
	private static final String SINGLE_DATETIME_TASK_FORMAT = "<td><p class = \"title\">%1s: </p></td><td>"
			+ "%2s</td>";
	public static final String TASK_CONCISE_FORMAT = "<td><p class = \"id\">%1s</p></td>"
			+ "<td><p class = \"name\">%2s</p></td>"
			+ "%3s";
	public static final String TASK_SINGLE_CONCISE_DATE = "<td>%1s</td>";
	public static final String TASK_DOUBLE_CONCISE_DATE = "<td>%1s</td><td><p> - </p></td><td>%2s</td>";
	public static String formatTaskForDisplay(Task task){
		int rowCount = 2;
		String tagDetails = "";
		if(!task.getTag().isEmpty()){
			tagDetails += "<p class = \"tag\">" + task.getTag() + "<p>";
		}
		String details = "<html>" + TASK_DISPLAY_STYLE 
						+ "<table>" + String.format(TASK_NAME_AND_TAG_FORMAT, task.getName(), tagDetails);
		
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
		
		otherDetails += "</tr>" + String.format(TASK_PRIORITY_FORMAT, task.getPriority());
		if(task.getReminder() != null){
			String reminderDate = task.getReminder().toString(NIConstants.DISPLAY_DATE_FORMAT);
			otherDetails += String.format(TASK_REMINDER_FORMAT, reminderDate);
		}
		
		otherDetails += "</tr>";
		System.out.println(otherDetails);
	
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
	
	public static String formatTaskDescriptionForDisplay(String desc, int rowCount){
		String details = String.format(TASK_DESCRIPTION_FORMAT, rowCount, desc);
		return details;
	}
}
