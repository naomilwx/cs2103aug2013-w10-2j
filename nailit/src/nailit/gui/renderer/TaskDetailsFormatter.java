//@author A0091372H
package nailit.gui.renderer;

import nailit.common.Task;

public class TaskDetailsFormatter {
	private static final String TASK_DISPLAY_STYLE = 
			"<head><style type = \"text/css\">" 
			+ "p {font-family: HelveticaNeue;}"
			+ "p.name {font-size: 11px;}"
			+ "p.tag {font-size: 9px; color: gray;}"
			+ "p.date {font-size: 11px;}"
			+ "p.time {font-size: 10px;}"
			+ "p.status {font-size: 10px;}"
			+ "p.priority {font-size: 10px;}"
			+ "p.title {font-size: 11px; font-weight: bold}"
			+ "p.desc {font-size: 11px; vertical-align:middle; width: 230px}"
			+ "</style></head>";
	private static final String TASK_NAME_AND_TAG_FORMAT = 
			"<tr><td><p class = \"title\">Name: </p></td>"
			+ "<td><p class = \"name\">%1s</p>%2s</td><td></td><td></td>";
	private static final String TASK_PRIORITY_FORMAT = 
			"<tr><td><p class = \"title\">Priority: </p></td><td><p class = \"priority\">" 
			+ "%1s</p></td><td></td><td></td>";
	private static final String TASK_STATUS_FORMAT = 
			"<td><p class = \"title\">Status: </p></td><td><p class = \"status\">"
			+ "%1s</p></td><td></td><td></td>";
	private static final String TASK_REMINDER_FORMAT = 
			"<td><p class = \"title\">Reminder Set On: </p></td>%1s</td>";
	private static final String TASK_DESCRIPTION_FORMAT = "<td colspan = 1 rowspan = %1d>"
			+ "<p class = \"title\"> Description: </p></td>"
			+ "<td rowspan = %2d>"
			+ "<p class = \"desc\">%3s</p></td>";
	private static final String EVENT_DATETIME_FORMAT ="<tr><td><p class = \"title\">Start: </p></td><td>"
			 + "%1s</td><td><p class = \"title\">End: </p></td><td>" 
			 + "%2s</td></tr>";
	private static final String SINGLE_DATETIME_TASK_FORMAT = "<tr><td><p class = \"title\">%1s: </p></td><td>"
			+ "%2s</td><td></td><td></td>";
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
			if(task.isFloatingTask()){
				rowCount += 1;
			}else{
				rowCount += 2; //including row for due date
			}
			
			if(task.checkCompleted()){
				otherDetails += String.format(TASK_STATUS_FORMAT, "Done");
			}else{
				otherDetails += String.format(TASK_STATUS_FORMAT, "Not Done");
			}
		}else{
			rowCount += 2; //for start and end time
		}
		
		otherDetails += "</tr>" + String.format(TASK_PRIORITY_FORMAT, task.getPriority());
		if(task.getReminder() != null){
			String reminderDate = TaskDateTimeDisplayRenderer.formatTaskDateTimeCellDisplay(task.getReminder());
			otherDetails += String.format(TASK_REMINDER_FORMAT, reminderDate);
		}
		
		otherDetails += "</tr>";
		
		String taskDesc = task.getDescription();
		if(taskDesc != null && !taskDesc.isEmpty()){
			details += formatTaskDescriptionForDisplay(taskDesc, rowCount);
		}
	
		details += "</tr>" + otherDetails + "</table></html>";
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
		String details = String.format(TASK_DESCRIPTION_FORMAT, rowCount, rowCount, desc);
		return details;
	}
}
