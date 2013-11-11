package nailit.common;

import org.joda.time.DateTime;
// @author a0105683e

public class FilterObject {
	private String  name;
	private DateTime startTime;
	private DateTime endTime;
	private Boolean isCompleted;
	private TaskPriority priority;
	private String tag;
	private boolean isSearchAll;
	
	public FilterObject(){
		name = null;
		startTime = null;
		endTime = null;
		this.isCompleted = null;
		priority = null;
		this.tag = null;
		this.isSearchAll = false;
	}
	public FilterObject(String taskName, DateTime start, DateTime end, String tag, TaskPriority p,Boolean isCompleted){
		name = taskName;
		startTime = start;
		endTime = end;
		this.isCompleted = isCompleted;
		priority = p;
		this.tag = tag;
		this.isSearchAll = false;
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
	
	public boolean getIsSearchAll(){
		return isSearchAll;
	}
	
	public void setIsSearchAll(boolean isSearchAll){
		this.isSearchAll = isSearchAll;
	}
	
}

