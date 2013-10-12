package nailit.common;

import org.joda.time.DateTime;

public class FilterObject {
	String  name;
	DateTime startTime;
	DateTime endTime;
	boolean isCompleted;
	TaskPriority priority;
	String tag;
	
	public FilterObject(String taskName, DateTime start, DateTime end, String tag, TaskPriority p,boolean isCompleted){
		name = taskName;
		startTime = start;
		endTime = end;
		this.isCompleted = isCompleted;
		priority = p;
		if(tag == null){
			this.tag = "";
		}else{
			this.tag = tag;
		}
	}
}
