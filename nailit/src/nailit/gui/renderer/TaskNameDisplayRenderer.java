package nailit.gui.renderer;

import javax.swing.table.DefaultTableCellRenderer;

import nailit.common.Task;

public class TaskNameDisplayRenderer extends DefaultTableCellRenderer{
	private static final String cellStyle = "<head><style type = \"text/css\">" 
			+ "p.name {font-size: 15px;}"
			+ "p.tag {font-size: 10px; color: gray;}"
			+ "</style></head>";
	private static final String TASK_DONE_CODE = "&#10004  ";
	private static final String TASK_NOT_DONE_CODE = "&nbsp;   ";
	private static final String ROW_FILLER = "<p></p>";

	@Override
	protected void setValue(Object value){
		String output = "<html>" + cellStyle + (String) value + "</html>";
		setText(output);
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
		String nameAndTag = "<p class = \"name\">" + taskName + "</p>";
		if(!tag.isEmpty()){
			nameAndTag += "<p class = \"tag\">" + tag + "</p>";
			tagRowFiller = ROW_FILLER;
		}
		String formattedString = "<tr><td width = \"15px\"><p>"+statusFlag + "</p>"+ tagRowFiller +"</td><td>" 
		+ nameAndTag + "<td></tr>";
		
		return formattedString;
	}

}
