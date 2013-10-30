//@author A0091372H
package nailit.gui.renderer;

import javax.swing.table.DefaultTableCellRenderer;

import nailit.common.Task;
import nailit.common.TaskPriority;

public class TaskNameDisplayRenderer extends DefaultTableCellRenderer{
	private static final String cellStyle = "<head><style type = \"text/css\">" 
			+ "p.name {font-size: 14px;}"
			+ "p.overdue {font-size: 14px; color: red;}"
			+ "p.tag {font-size: 10px; color: gray;}"
			+ "</style></head>";
	private static final String PRIORITY_DISPLAY_FORMAT = "<td width = \" 40px\">"
			+ "<p class = \"priority\" style = \"color: %1s\">%2s</p><p></p</td>";
	private static final String TASK_DONE_CODE = "&#10004  ";
	private static final String TASK_NOT_DONE_CODE = "&nbsp;   ";
	private static final String HIGH_PRIORITY_CODE = "&#x25B2 &#x25B2 &#x25B2";
	private static final String MED_PRIORITY_CODE = "&#x25B2 &#x25B2";
	private static final String LOW_PRIORITY_CODE = "&#x25B2";
	private static final String HIGH_PRIORITY_COLOUR = "red";
	private static final String MED_PRIORITY_COLOUR = "FDA205";
	private static final String LOW_PRIORITY_COLOUR = "E8FD05";
	private static final String ROW_FILLER = "<p class = \"tag \">&nbsp;</p>";

	@Override
	protected void setValue(Object value){
		String output = "<html>" + cellStyle + (String) value + "</html>";
		setText(output);
	}
	private static String getPriorityDisplay(TaskPriority priority){
		if(priority.equals(TaskPriority.HIGH)){
			return String.format(PRIORITY_DISPLAY_FORMAT, HIGH_PRIORITY_COLOUR, HIGH_PRIORITY_CODE);
		}else if(priority.equals(TaskPriority.MEDIUM)){
			return String.format(PRIORITY_DISPLAY_FORMAT, MED_PRIORITY_COLOUR, MED_PRIORITY_CODE);
		}else{
			return String.format(PRIORITY_DISPLAY_FORMAT, LOW_PRIORITY_COLOUR, LOW_PRIORITY_CODE);
		}
	}
	private static String getNameHTMLTag(Task task){
		String htmlTag;
		if(task.isOverDueTask()){
			htmlTag = "<p class = \"overdue\">";
		}else{
			htmlTag = "<p class = \"name\">";
		}
		return htmlTag;
	}
	public static String formatTaskNameCellDisplay(Task task){
		String taskName = task.getName();
		String tag = task.getTag();
		boolean isCompleted = task.checkCompleted();
		String statusFlag = "";
		String tagRowFiller = "";
		if(!task.isEvent()){
			if(isCompleted){
				statusFlag = TASK_DONE_CODE;
			}else{
				statusFlag = TASK_NOT_DONE_CODE;
			}
		}
		String nameAndTag = getNameHTMLTag(task) + taskName + "</p>";
		if(!tag.isEmpty()){
			nameAndTag += "<p class = \"tag\">" + tag + "</p>";
			tagRowFiller = ROW_FILLER;
		}
		
		String formattedString = "<tr><td width = \"15px\"><p>"+statusFlag + "</p>"+ tagRowFiller +"</td><td>" 
		+ nameAndTag + "</td><td>"
				+ getPriorityDisplay(task.getPriority())+ tagRowFiller + "</td></tr>";
		
		return formattedString;
	}

}
