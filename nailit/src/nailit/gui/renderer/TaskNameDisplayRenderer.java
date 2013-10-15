package nailit.gui.renderer;

import javax.swing.table.DefaultTableCellRenderer;

public class TaskNameDisplayRenderer extends DefaultTableCellRenderer{
	@Override
	protected void setValue(Object value){
		String output = "<html>" + (String) value + "</html>";
		setText(output);
	}
	public static String formatTaskNameCellDisplay(String taskName, String tag){
		String formattedString = "<p class = \"name\">" + taskName + "</p>";
		if(!tag.isEmpty()){
			formattedString = formattedString + "<p class = \"tag\">" + tag + "</p>";
		}
		return formattedString;
	}
}
