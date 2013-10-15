package nailit.gui.renderer;

import javax.swing.table.DefaultTableCellRenderer;

import org.joda.time.DateTime;

import nailit.common.Task;
import nailit.common.NIConstants;

public class TaskDateTimeDisplayRenderer extends DefaultTableCellRenderer{
	private static final String cellStyle = "<head><style type = \"text/css\">" 
			+ "p.date {font-size: 15px; text-align:center;}"
			+ "p.time {font-size: 12px; text-align:center;}"
			+ "</style></head>";

	protected static final String TASK_DATETIME_DISPLAY_FORMAT = 
			"<p class = \"date\"> %1s" + "</p>" + 
			"<p class = \"time\"> %2s" + "</p>";
	protected static final String EMPTY_TIME_DISPLAY_FORMAT = "<p>" + "--" + "</p>";
	
	public TaskDateTimeDisplayRenderer(){
		setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
	}
	@Override
	protected void setValue(Object value){
		String dateTimeDisplayStr = (String) value;
		String outStr = "<html>" + cellStyle + dateTimeDisplayStr + "<html>";
		setText(outStr);
	}
	
	public static String formatTaskDateTimeCellDisplay(DateTime dateTime){
		String formattedTime = "";
		
		if(dateTime != null){
			String day = dateTime.toString(NIConstants.DISPLAY_DATE_FORMAT);
			String time = dateTime.toString(NIConstants.DISPLAY_TIME_FORMAT);
			formattedTime = String.format(TASK_DATETIME_DISPLAY_FORMAT, day, time);
		}else{
			formattedTime = EMPTY_TIME_DISPLAY_FORMAT;
		}
		return formattedTime;
	}
}
