package nailit.logic;

import java.util.*;

import org.joda.time.DateTime;

import nailit.common.TaskPriority;

public class ParserResult{
	//ArrayList<String> command = new ArrayList<String> ();

	private int taskID;
	private CommandType command;
	private String name;
	private TaskPriority priority;
	private String tag;
	private String description;
	
	private DateTime startTime;
	private DateTime endTime;
	
	private boolean isDisplayAll = false;
	private boolean isDisplayHistory = false;
	private boolean isSetPriority = false;
	
	public ParserResult(){
		taskID = 0;
		command = CommandType.INVALID;
		name = null;
		priority = TaskPriority.DEFAULT_TASK_PRIORITY;
		tag = null;
		startTime = null;
		endTime = null;
		isDisplayAll = false;
		isSetPriority = false;
		description = null;
	}
	
	public void setCommand(CommandType commandExternal){
		command = commandExternal;
	}
	
	public CommandType getCommand(){
		return command;
	}
	
	public void setDisplayAll (boolean flag){
		isDisplayAll = flag;
	}
	
	public boolean isDisplayAll(){
		return isDisplayAll;
	}
	
	public void setDisplayHistory (boolean flag){
		isDisplayHistory = flag;
	}
	
	public boolean isDisplayHistory(){
		return isDisplayHistory;
	}
	
	public void setSetPriority (boolean flag){
		isSetPriority = flag;
	}
	
	public boolean isSetPriority(){
		return isSetPriority;
	}
	
	public void setName(String nameExternal){
		name = nameExternal;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isNullName(){
		if (name == null){
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
	
	public boolean isNullTaskID (){
		if (taskID == 0)
			return true;
		else
			return false;
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
		isSetPriority = true;
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
		if (tag == "" || tag == null){
			return true;
		}else{
			return false;
		}
	}
	
	public void setDescription(String descriptionExternal){
		description = descriptionExternal;
	}
	
	public String getDescription(){
		return description;
	}
	
	public boolean isNullDescription(){
		if (description == "" || description == null){
			return true;
		}else{
			return false;
		}
	}
}
