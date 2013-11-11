package nailit.common;

import nailit.common.NIConstants;

import org.joda.time.DateTime;
//@author A0091372H
public class Task {
	public static final int TASKID_NULL = -1;
	private static final String BASIC_PRINTOUT_FORMAT = "[%1d] Name: %2s";
	
	private int ID = TASKID_NULL;
	private String name = NIConstants.EMPTY_STRING;
	private String description = NIConstants.EMPTY_STRING;
	private DateTime startTime;
	private DateTime endTime;
	private String tag = NIConstants.EMPTY_STRING;
	private boolean isCompleted = false;
	private TaskPriority priority;
	private DateTime reminderStartDate = null;

	public static final int COMPLETED_IN_HARDDISK = 1;
	public static final int INCOMPLETE_IN_HARDDISK = 0;

	public Task(){
		startTime = null;
		endTime = null;
		priority = TaskPriority.DEFAULT_TASK_PRIORITY;
	}
	public Task(String taskName){
		name = taskName;
		startTime = null;
		endTime = null;
		priority = TaskPriority.DEFAULT_TASK_PRIORITY;
	}
	
	public Task(String taskName, DateTime start, DateTime end, String t, TaskPriority p){
		ID = TASKID_NULL;
		name = taskName;
		startTime = start;
		endTime = end;
		priority = p;
		if(t == null){
			tag = "";
		}else{
			tag = Utilities.formatTaskTag(t);
		}
	}
	
	public Task(int ID, String taskName, DateTime start, DateTime end, TaskPriority p, String t, String desc, boolean isCompleted){
		this.ID = ID;
		name = taskName;
		startTime = start;
		endTime = end;
		priority = p;
		if(desc != null){
			description = desc;
		}
		this.isCompleted = isCompleted;
		if(t == null){
			tag = "";
		}else{
			tag = Utilities.formatTaskTag(t);
		}
	}
	public Task(int ID, String taskName, DateTime start, DateTime end, TaskPriority p, String t, String desc, boolean isCompleted,DateTime reminder){
		this.ID = ID;
		name = taskName;
		startTime = start;
		endTime = end;
		priority = p;
		if(desc != null){
			description = desc;
		}
		this.isCompleted = isCompleted;
		if(t == null){
			tag = "";
		}else{
			tag = Utilities.formatTaskTag(t);
		}
		this.reminderStartDate = reminder;
	}
	//getters
	public int getID(){
		return ID;
	}
	public TaskPriority getPriority(){
		return priority;
	}
	public String getName(){
		return name;
	}
	
	public String getDescription(){
		return description;
	}
	public String getTag(){
		return tag;
	}
	public boolean checkCompletedOrOver(){
		if(isEvent()){
			return isPastEvent();
		}else{
			return isCompleted;
		}
	}
	
	public boolean isCompleted(){
		return isCompleted;
	}
	public DateTime getStartTime(){
		return startTime;
	}
	public DateTime getEndTime(){
		return endTime;
	}
	
	public DateTime getReminder(){
		return reminderStartDate;
	}
	
	public boolean isDeadlineTask(){
		if(startTime == null && endTime != null){
			return true;
		}
		else{
			return false;
		}
	}
	public boolean isFloatingTask(){
		if(startTime==null && endTime==null){
			return true;
		}else{
			return false;
		}
	}
	//Checks if task is an event. returns true if both startTime and endTime are not null
	public boolean isEvent(){
		if(startTime != null && endTime != null){
			return true;
		}else{
			return false;
		}
	}
	//check if event is over
	public boolean isPastEvent(){
		if(isEvent()){
			DateTime now = new DateTime();
			return (endTime.compareTo(now) < 0);
		}else{
			return false;
		}
	}
	//check if task is overdue
	public boolean isOverDueTask(){
		if(isEvent() || isFloatingTask() || !isDeadlineTask()){
			return false;
		}else if(!isCompleted){
			return isBeforeDateTime(new DateTime());
		}else{
			return false;
		}
	}
	//setters
	public boolean setID(int newID){
		if(ID==Task.TASKID_NULL){
			ID = newID;
			return true;
		}else{
			return false;
		}
	}
	public void setIDToNull(){
		ID = Task.TASKID_NULL;
	}
	public void setName(String taskName){
		name = taskName;
	}
	public void setDescription(String taskDesc){
		if(taskDesc != null){
			description = taskDesc;
		}
	}
	public void setTag(String taskTag){
		if(taskTag != null){
			tag = Utilities.formatTaskTag(taskTag);
		}else{
			tag = "";
		}
	}
	public void setCompleted(boolean completed){
		isCompleted = completed;
	}
	public void setPriority(TaskPriority taskPriority){
		priority = taskPriority;
	}
	public void setStartTime(DateTime start){
		startTime = start;
	}
	public void setEndTime(DateTime end){
		endTime = end;
	}
	
	public void setReminder(DateTime reminder){
		this.reminderStartDate = reminder;
	}
	
	//Functions to format Task details for printout
	public String formatID(){
		String formattedID = "ID: "+ ID;
		return formattedID;
	}
	
	public String formatNameAndTag(){
		String formatted = name;
		if(!tag.isEmpty()){
			formatted += " " + tag;
		}
		return formatted;
	}
	
	
	public String formatPriority(){
		String formattedPriority = "Priority: " + priority.toString();
		return formattedPriority;
	}
	
	public String formatDateDetails(){
		String formattedDateDetails = "";
		if(isEvent()){
			formattedDateDetails = 
					"from " + startTime.toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT) + "\n"
					+"to " + endTime.toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT);
		}else if(!isFloatingTask() && endTime != null){
			formattedDateDetails = "at " + endTime.toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT);
		}else if(startTime != null){
			formattedDateDetails = "from " + startTime.toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT);
		}
		return formattedDateDetails;
	}
	
	public String formatStatus(){
		String formattedStatus = "Status: ";
		if(isCompleted){
			formattedStatus += "Completed";
		}else{
			formattedStatus += "Not Completed";
		}
		return formattedStatus;
	}
	
	//method to create copy of Task object
	public Task copy(){
		Task newTask = new Task(name, startTime, endTime, tag, priority);
		newTask.setDescription(description);
		newTask.setID(ID);
		newTask.setCompleted(isCompleted);
		newTask.setReminder(reminderStartDate);
		return newTask;
	}
	
	public String printNameAndID(){
		return String.format(BASIC_PRINTOUT_FORMAT, ID, name);
	}
	//start of task utiliy functions
	public boolean isAtSameStartTime(Task other){
		if(startTime == null || other.startTime == null){
			return ((startTime == null) && (other.startTime ==null));
		}
		return startTime.equals(other.startTime);
	}
	public boolean isAtSameEndTime(Task other){
		if(endTime == null || other.endTime == null){
			return ((endTime == null) && (other.endTime ==null));
		}
		return endTime.equals(other.endTime);
	}
	public boolean isAtSameTime(Task other){
		return (isAtSameStartTime(other) && isAtSameEndTime(other));
	}
	//check if task is an event and startTime and endTime is on the same day
	public boolean isOneDayEvent(){
		if(!isEvent()){
			return false;
		}else{
			String startDay = startTime.toString(NIConstants.DISPLAY_DATE_FORMAT);
			String endDay = endTime.toString(NIConstants.DISPLAY_DATE_FORMAT);
			return startDay.equals(endDay);
		}
	}
	public boolean isAfterDateTime(DateTime dateTime){
		//true if event starts AFTER (exclusive) date or task is due after date
		if(dateTime == null){
			return true;
		}
		if(startTime != null){
			return (startTime.compareTo(dateTime) > 0);//startTime is after given date
		}else if(endTime != null){
			return (endTime.compareTo(dateTime) > 0);
		}else{
			return false;
		}
	}
	public boolean isBeforeDateTime(DateTime dateTime){
		//returns true if event starts BEFORE (exclusive) date or task is due before date
		if(dateTime == null){
			return true;
		}
		if(startTime != null){
			return (startTime.compareTo(dateTime) < 0);
		}else if(endTime != null){
			return (endTime.compareTo(dateTime) < 0);
		}else{
			return false;
		}
	}
	public boolean isHappeningToday(){
		DateTime now = new DateTime();
		return isInDateRange(Utilities.getStartOfDay(now), Utilities.getEndOfDay(now));
	}
	public boolean isInDateRange(DateTime start, DateTime end){
		//checks if event or deadline is within date range (inclusive)
		//boundary case: ie event or task time is exactly the same as start or end, should return true
		if(start != null && end != null){
			if(isEvent()){
				if(isAfterDateTime(start) && isAfterDateTime(end)){
					return false;
				}else if((endTime.compareTo(start) < 0) && (endTime.compareTo(end) < 0)){
					return false;
				}else{
					return true;
				}
			}else{
				if(isAfterDateTime(start) && isAfterDateTime(end)){
					return false;
				}else if(endTime == null){ //start and no end: we treat as an event that goes on indefinitely
					return (!isAfterDateTime(end));
				}else if(isBeforeDateTime(start) && isBeforeDateTime(end)){
					return false;
				}else{
					return true;
				}
			}
		}else if(start == null){
			if(isBeforeDateTime(end)){
				return true;
			}else if(startTime != null && endTime != null){
				return (startTime.equals(end));
			}else if(startTime != null){
				//treat as event which goes on indefinitely
				return startTime.equals(end);
			}else if(endTime != null){
				return endTime.equals(end);
			}else{
				return false;
			}
		}else{
			if(isAfterDateTime(start)){
				return true;
			}else if(startTime != null && endTime != null){
				return (startTime.compareTo(start) <= 0 && endTime.compareTo(start) >= 0);
			}else if(startTime != null){
				//start time null and end time is not null: treat as event which goes on indefinitely
				return (startTime.compareTo(start) <= 0);
			}else if(endTime != null){
				return endTime.equals(start);
			}else{
				return false;
			}
		}
	}
	//end of task utility functions
	@Override
	public String toString(){
		String output = formatNameAndTag() +" ";
		if(!isFloatingTask()){
			output += formatDateDetails();
		}
		
		return output;
	}
	
	//Two tasks are considered to be the same task if their IDs are the same
	@Override
	public boolean equals(Object otherTask){
		if(otherTask instanceof Task){
			int otherTaskID = ((Task) otherTask).getID();
			if(otherTaskID == Task.TASKID_NULL){
				return false;
			}else{
				return otherTaskID == ID;
			}
		}else{
			return false;
		}
	}

	/**
	 * This method is used to change a task into the harddisk format, which
	 * is a string
	 * @author a0105683e
	 * */
	public String changeToDiskFormat(){
		int priority = this.getPriority().getPriorityCode();
		assert(isValidPriority(priority));
		
		String name = this.getName();
		String startDate;
		if(this.getStartTime() == null){
			startDate = null;
		}else{
			startDate = this.getStartTime().toString();
		}
		
		String endDate;
		if(this.getEndTime() == null){
			endDate = null;
		}else{
			endDate = this.getEndTime().toString();
		}
		String desc = this.getDescription();
		String tag = this.getTag();
		
		int completeStatus = parseCompleteStatus(this.isCompleted);
		assert(isValidCompleteStatus(completeStatus));
		
		String reminderDate;
		if(this.getReminder()==null){
			reminderDate = null;
		}else{
			reminderDate = reminderStartDate.toString();
		}
		
		String taskString = name + NIConstants.HARDDISK_FIELD_SPLITTER + startDate + NIConstants.HARDDISK_FIELD_SPLITTER + endDate + NIConstants.HARDDISK_FIELD_SPLITTER + priority + NIConstants.HARDDISK_FIELD_SPLITTER + tag + NIConstants.HARDDISK_FIELD_SPLITTER +desc +NIConstants.HARDDISK_FIELD_SPLITTER + completeStatus + NIConstants.HARDDISK_FIELD_SPLITTER + reminderDate;
		
		return taskString;
	}
	
	private int parseCompleteStatus(boolean complete){
		if(complete){
			return COMPLETED_IN_HARDDISK;
		}else{
			return INCOMPLETE_IN_HARDDISK;
		}
	}
	private boolean isValidPriority(int p){
		return (p>=TaskPriority.LOW.getPriorityCode()&&p<=TaskPriority.HIGH.getPriorityCode());
	}
	private boolean isValidCompleteStatus(int completeStatus){
		return ((completeStatus == COMPLETED_IN_HARDDISK) || (completeStatus == INCOMPLETE_IN_HARDDISK));
	}

}
