package nailit.common;

import org.joda.time.DateTime;

public class FilterObject {
	private String  name;
	private DateTime startTime;
	private DateTime endTime;
	private Boolean isCompleted;
	private TaskPriority priority;
	private String tag;
	private Boolean isSearchAll;
	
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
	
	public Boolean getIsSearchAll(){
		return isSearchAll;
	}
	
	public void setIsSearchAll(Boolean isSearchAll){
		this.isSearchAll = isSearchAll;
	}
	
}

