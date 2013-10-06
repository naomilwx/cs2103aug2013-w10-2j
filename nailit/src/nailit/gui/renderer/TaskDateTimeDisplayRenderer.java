package nailit.gui.renderer;

import javax.swing.table.DefaultTableCellRenderer;

import org.joda.time.DateTime;

import nailit.common.Task;
import nailit.common.NIConstants;

public class TaskDateTimeDisplayRenderer extends DefaultTableCellRenderer{
	protected static final String ONE_DAY_EVENT_DATETIME_DISPLAY_FORMAT = 
			"<p>" + "Day: %1s" +"</p>"
					+ "<p>" + "Start: %2s + End: %3s" + "</p>";
	protected static final String EVENT_DATETIME_DISPLAY_FORMAT = 
			"<p>" + "Start: %1s" + "</p>"
			+"<p>" + "End: %2s" + "</p>";
	protected static final String TASK_DATETIME_DISPLAY_FORMAT = 
			"<p>" + "Day: %1s" + "</p>" + 
			"<p>" + "Time: %2s" + "</p>";
	protected static final String EMPTY_TIME_DISPLAY_FORMAT = "<p>" + "-" + "</p>";
	
	public TaskDateTimeDisplayRenderer(){
		setHorizontalTextPosition(DefaultTableCellRenderer.CENTER);
	}
	@Override
	protected void setValue(Object value){
		String dateTimeDisplayStr = (String) value;
		String outStr = "<html>" + dateTimeDisplayStr + "<html>";
		setText(outStr);
	}
	
	protected static String formatTaskDateTimeCellDisplay(Task task){
		String formattedTime = "";
		DateTime startTime = task.getStartTime();
		DateTime endTime = task.getEndTime();
		if(task.isEvent()){
			if(task.isOneDayEvent()){
				String start = startTime.toString(NIConstants.DISPLAY_TIME_FORMAT);
				String end = endTime.toString(NIConstants.DISPLAY_TIME_FORMAT);
				String day = startTime.toString(NIConstants.DISPLAY_DATE_FORMAT);
				formattedTime = String.format(ONE_DAY_EVENT_DATETIME_DISPLAY_FORMAT, day, start , end);
			}else{
				String start = startTime.toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT);
				String end = endTime.toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT);
				formattedTime = String.format(EVENT_DATETIME_DISPLAY_FORMAT, start, end);
			}
		}else if(!task.isFloatingTask()){
			assert startTime != null;
			String day = startTime.toString(NIConstants.DISPLAY_DATE_FORMAT);
			String time = startTime.toString(NIConstants.DISPLAY_TIME_FORMAT);
			formattedTime = String.format(TASK_DATETIME_DISPLAY_FORMAT, day, time);
		}else{
			formattedTime = EMPTY_TIME_DISPLAY_FORMAT;
		}
		return formattedTime;
	}
}
