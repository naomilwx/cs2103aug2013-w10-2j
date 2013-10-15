package nailit.gui.renderer;

import javax.swing.table.DefaultTableCellRenderer;

public class TaskNameDisplayRenderer extends DefaultTableCellRenderer{
	private static final String cellStyle = "<head><style type = \"text/css\">" 
			+ "p.name {font-size: 15px;}"
			+ "</style></head>";
	private static final String TASK_DONE_CODE = "&#9745";
	private static final String TASK_NOT_DONE_CODE = "&#9744 ";
	@Override
	protected void setValue(Object value){
		String output = "<html>" + cellStyle + (String) value + "</html>";
		setText(output);
	}
	public static String formatTaskNameCellDisplay(String taskName, String tag){
		String formattedString = "<p class = \"name\">"+ taskName + "</p>";
		if(!tag.isEmpty()){
			formattedString = formattedString + "<p class = \"tag\">" + tag + "</p>";
		}
		return formattedString;
	}
}
