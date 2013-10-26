package nailit.gui.renderer;

import nailit.common.Task;

public class TaskDetailsFormatter {
	private static final String TASK_DISPLAY_STYLE = 
			"<head><style type = \"text/css\">" 
			+ "p {font-family: HelveticaNeue;}"
			+ "p.name {font-size: 12px;}"
			+ "p.tag {font-size: 9px; color: gray;}"
			+ "p.date {font-size: 11px;}"
			+ "p.time {font-size: 10px;}"
			+ "p.status {font-size: 11px;}"
			+ "p.priority {font-size: 11px;}"
			+ "p.title {font-size: 12px; font-weight: bold}"
			+ "p.desc {font-size: 11px; vertical-align:middle; width: 250px}"
			+ "</style></head>";
	private static final String TASK_NAME_AND_TAG_FORMAT = 
			"<tr><td><p class = \"title\">Name: </p></td>"
			+ "<td><p class = \"name\">%1s</p>%2s</td>";
	private static final String TASK_PRIORITY_FORMAT = 
			"<tr><td><p class = \"title\">Priority: </p></td><td><p class = \"priority\">" 
			+ "%1s</p></td></tr>";
	private static final String TASK_STATUS_FORMAT = 
			"<tr><td><p class = \"title\">Status: </p></td><td><p class = \"status\">"
			+ "%1s</p></td></tr>";
	private static final String TASK_DESCRIPTION_FORMAT = "<td colspan = 1 rowspan = %1d>"
			+ "<p class = \"title\"> Description: </p></td>"
			+ "<td rowspan = %2d>"
			+ "<p class = \"desc\">%3s</p></td>";
	private static final String EVENT_DATETIME_FORMAT ="<tr><td><p class = \"title\">Start: </p></td><td>"
			 + "%1s</td></tr>"
			 +"<tr><td><p class = \"title\">End: </p></td><td>" 
			 + "%2s</td></tr>";
	private static final String DEADLINE_TASK_FORMAT = "<tr><td><p class = \"title\">Due: </p></td><td>"
			+ "<p class = \"datetime\">%1s</td></tr>";
	
	public static String formatTaskForDisplay(Task task){
		int rowCount = 2;
		String tagDetails = "";
		if(!task.getTag().isEmpty()){
			tagDetails += "<p class = \"tag\">" + task.getTag() + "<p>";
		}
		String details = "<html>" + TASK_DISPLAY_STYLE 
						+ "<table>" + String.format(TASK_NAME_AND_TAG_FORMAT, task.getName(), tagDetails);
		
		String otherDetails = formatTaskDateTimeForDisplay(task);
		
		otherDetails += String.format(TASK_PRIORITY_FORMAT, task.getPriority());
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
					details += String.format(DEADLINE_TASK_FORMAT, TaskDateTimeDisplayRenderer.formatTaskDateTimeCellDisplay(task.getStartTime()));
				}else{
					details += String.format(DEADLINE_TASK_FORMAT, TaskDateTimeDisplayRenderer.formatTaskDateTimeCellDisplay(task.getEndTime()));  
				}
			}
		}
		return details;
	}
	
	public static String formatTaskDescriptionForDisplay(String desc, int rowCount){
		String details = "<td style = \" width: 20px\" colspan = 1 rowspan = " 
						+ rowCount + "></td>";
		details += String.format(TASK_DESCRIPTION_FORMAT, rowCount, rowCount, desc);
		return details;
	}
}
