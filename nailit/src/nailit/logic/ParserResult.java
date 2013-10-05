package nailit.logic;

import java.util.*;

import org.joda.time.DateTime;

import nailit.common.TaskPriority;

public class ParserResult{
	//ArrayList<String> command = new ArrayList<String> ();

	// added by Shuzhi
	// in delete and addDescription command: need tasdID
	private int taskID;
	private static CommandType command;
	private static String name;
	
	private DateTime startTime;
	private DateTime endTime;
	
	private TaskPriority priority;
	private String tag;
	
	public void setCommand(CommandType commandExternal){
		command = commandExternal;
	}
	
	public CommandType getCommand(){
		return command;
	}
	
	public void setName(String nameExternal){
		name = nameExternal;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isNullName(){
		if (name == ""){
			return true;
		}else{ 
			return false;
		}
	}
	
	public void setTaskID(int taskIDExternal) {
		taskID = taskIDExternal;
	}
	
	public int getTaskID() {
		return taskID;
	}

	public void setStartTime(DateTime startTimeExternal){
		startTime = startTimeExternal;
	}
	
	public DateTime getStartTime(){
		return startTime;
	}
	
	public boolean isNullStartTime(){
		if (startTime == null){
			return true;
		}else{ 
			return false;
		}
	}
	
	public void setEndTime(DateTime endTimeExternal){
		endTime = endTimeExternal;
	}
	
	public DateTime getEndTime(){
		return endTime;
	}
	
	public boolean isNullEndTime(){
		if (endTime == null){
			return true;
		}else{ 
			return false;
		}
	}
	
	public void setPriority(TaskPriority priorityExternal){
		priority = priorityExternal;
	}
	
	public TaskPriority getPriority(){
		return priority;
	}
	
	public boolean isNullPriority(){
		if (priority == null){
			return true;
		}else{
			return false;
		}
	}
	
	public void setTag(String tagExternal){
		tag = tagExternal;
	}
	
	public String getTag(){
		return tag;
	}
	
	public boolean isNullTag(){
		if (tag == ""){
			return true;
		}else{
			return false;
		}
	}
}
