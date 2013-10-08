package nailit.gui.renderer;

import javax.swing.table.DefaultTableCellRenderer;

public class TaskNameDisplayRenderer extends DefaultTableCellRenderer{
	@Override
	protected void setValue(Object value){
		String output = "<html>" + (String) value + "</html>";
		setText(output);
	}
	public static String formatTaskNameCellDisplay(String taskName, String tag){
		String formattedString = "<p>" + taskName + "</p>";
		if(tag != null){
			formattedString = formattedString + "<p>" + tag + "</p>";
		}
		return formattedString;
	}
}
