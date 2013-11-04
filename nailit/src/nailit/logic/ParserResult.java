package nailit.logic;

import org.joda.time.DateTime;
import nailit.common.TaskPriority;

public class ParserResult{
	//ArrayList<String> command = new ArrayList<String> ();

	private int taskId;
	private CommandType command;
	private String name;
	private TaskPriority priority;
	private String tag;
	private String description;
	
	private DateTime startTime;
	private DateTime endTime;
	private DateTime reminderTime;
	
	private boolean isDisplayAll = false;
	private boolean isDisplayComplete = false;
	private boolean isDisplayHistory = false;
	private boolean isDisplayUncomplete = false;
	private boolean isSetPriority = false;
	
	public ParserResult(){
		taskId = 0;
		command = CommandType.INVALID;
		name = null;
		priority = TaskPriority.DEFAULT_TASK_PRIORITY;
		tag = null;
		startTime = null;
		endTime = null;
		reminderTime = null;
		description = null;
		
		isDisplayAll = false;
		isDisplayComplete = false;
		isDisplayHistory = false;
		isDisplayUncomplete = false;
		isSetPriority = false;
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
	
	public void setDisplayComplete (boolean flag){
		isDisplayComplete = flag;
	}
	
	public boolean isDisplayComplete(){
		return isDisplayComplete;
	}
	
	public void setDisplayUncomplete (boolean flag){
		isDisplayUncomplete = flag;
	}
	
	public boolean isDisplayUncomplete(){
		return isDisplayUncomplete;
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
	
	public void setTaskId(int taskIdExternal) {
		taskId = taskIdExternal;
	}
	
	public int getTaskID() {
		return taskId;
	}
	
	public boolean isNullTaskID (){
		if (taskId == 0)
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
	public void setReminderTime(DateTime reminderTimeExternal){
		reminderTime = reminderTimeExternal;
	}
	
	public DateTime getReminderTime(){
		return reminderTime;
	}
	
	public boolean isNullReminderTime(){
		if (reminderTime == null){
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
	
	public void resetPriority(){
		isSetPriority = false;
	}
	
	public boolean isNullPriority(){
		if (isSetPriority == false){
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
