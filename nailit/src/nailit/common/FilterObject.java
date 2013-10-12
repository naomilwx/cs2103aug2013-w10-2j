package nailit.common;

import org.joda.time.DateTime;

public class FilterObject {
	String  name;
	DateTime startTime;
	DateTime endTime;
	Boolean isCompleted;
	TaskPriority priority;
	String tag;
	
	public FilterObject(String taskName, DateTime start, DateTime end, String tag, TaskPriority p,Boolean isCompleted){
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
	
	public String getName(){
		return name;
	}
	
	public DateTime getStartTime(){
		return startTime;
	}
	
	public DateTime getEndTime(){
		return endTime;
	}
	
	public Boolean isCompleted(){
		return isCompleted;
	}
	public TaskPriority getPriority(){
		return priority;
	}
	
	public String getTag(){
		return tag;
	}
	
}

