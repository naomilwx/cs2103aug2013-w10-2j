package nailit.gui.renderer;

import javax.swing.table.DefaultTableCellRenderer;

public class TaskNameDisplayRenderer extends DefaultTableCellRenderer{
	@Override
	protected void setValue(Object value){
		setText((String) value);
	}
	public static String formatTaskNameCellDisplay(String taskName, String tag){
		String formattedString = "<p>" + taskName + "</p>" + "<p>" + tag + "</p>";
		return formattedString;
	}
}
