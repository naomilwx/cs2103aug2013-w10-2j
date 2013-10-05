package nailit.common;

import org.joda.time.DateTime;
public class Task {
	public static final int TASKID_NULL = -1;
	public static final String DEFAULT_TASK_DATETIME_PRINT_FORMAT = "dd-MM-YYYY h:m";
	private static final String BASIC_PRINTOUT_FORMAT = "[%1d] Name: %2s";
	private int ID;
	private String name;
	private String description;
	private DateTime startTime;
	private DateTime endTime;
	private String tag;
	private boolean added = false; //variable to indicate if task has been added to task list
	private boolean isCompleted;

	private TaskPriority priority;

	public static final int COMPLETED_IN_HARDDISK = 1;
	public static final int INCOMPLETE_IN_HARDDISK = 0;

	public Task(){
		ID = TASKID_NULL;
		name = "";
		description = ""; //TODO: replace with constant
		startTime = null;
		endTime = null;
		tag = "";
		isCompleted = false;
		priority = TaskPriority.DEFAULT_TASK_PRIORITY;
	}
	public Task(String taskName){
		ID = TASKID_NULL;
		name = taskName;
		description = ""; //TODO: replace with constant
		startTime = null;
		endTime = null;
		tag = "";
		isCompleted = false;
		added = true;
		priority = TaskPriority.DEFAULT_TASK_PRIORITY;
	}
	
	public Task(String taskName, DateTime start, DateTime end, String t, TaskPriority p){
		ID = TASKID_NULL;
		name = taskName;
		startTime = start;
		endTime = end;
		tag = t;
		isCompleted = false;
		priority = p;
	}
	
	public Task(int ID, String taskName, DateTime start, DateTime end, TaskPriority p, String tag, String desc, boolean isCompleted){
		this.ID = ID;
		name = taskName;
		startTime = start;
		endTime = end;
		priority = p;
		this.tag = tag;
		description = desc;
		this.isCompleted = isCompleted;
	}
	public boolean isAdded(){
		return added;
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
	public boolean checkCompleted(){
		return isCompleted;
	}
	public DateTime getStartTime(){
		return startTime;
	}
	public DateTime getEndTime(){
		return endTime;
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
	//setters
	public boolean setID(int newID){
		if(ID==Task.TASKID_NULL){
			ID = newID;
			return true;
		}else{
			return false;
		}
	}
	public void setName(String taskName){
		name = taskName;
	}
	public void setDescription(String taskDesc){
		description = taskDesc;
	}
	public void setTag(String taskTag){
		tag = taskTag;
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
	//Functions to format Task details for printout
	public String formatID(){
		String formattedID = "ID: "+ ID;
		return formattedID;
	}
	public String formatName(){
		String formattedName = "Name: " + name;
		return formattedName;
	}
	public String formatTag(){
		String formattedTag = "";
		if(!tag.isEmpty()){
			formattedTag = "Tag: " + tag;
		}
		return formattedTag;
	}
	public String formatPriority(){
		String formattedPriority = "Priority: " + priority.toString();
		return formattedPriority;
	}
	public String formatDateDetails(){
		String formattedDateDetails = "";
		if(isEvent()){
			formattedDateDetails = 
					"Start Time: " + startTime.toString(DEFAULT_TASK_DATETIME_PRINT_FORMAT) + "\n"
					+"End Time: " + endTime.toString(DEFAULT_TASK_DATETIME_PRINT_FORMAT);
		}else if(!isFloatingTask()){
			formattedDateDetails = "Due: " + startTime.toString(DEFAULT_TASK_DATETIME_PRINT_FORMAT);
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
		// change from Shuzhi
		// the same, delete description,
		Task newTask = new Task(name, startTime, endTime, tag, priority);
		newTask.setDescription(description);
		newTask.setID(ID);
		newTask.setCompleted(isCompleted);
		return newTask;
	}
	public String printNameAndID(){
		return String.format(BASIC_PRINTOUT_FORMAT, ID, name);
	}
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
	@Override
	public String toString(){ //TODO: figure out whether to include description
		String output = formatID()+"\n"
						+formatName() +"\n";
		if(!isFloatingTask()){
			output += formatDateDetails() + "\n";
		}
		output += formatPriority() +"\n"
				 + formatStatus() + "\n";
		if(!tag.isEmpty()){
			output += formatTag() + "\n";
		}
		return output;
	}
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
	public boolean isClone(Object otherTask){
		if(this.equals(otherTask)){
			String otherTaskString = otherTask.toString();
			return otherTaskString.equals(this.toString());
		}else{
			return false;
		}
	}
	
	public String changeToDiskFormat(){
		int priority = this.getPriority().getPriorityCode();
		assert(isValidPriority(priority));
		
		String name = this.getName();
		String startDate = this.getStartTime().toString();
		String endDate = this.getEndTime().toString();
		String desc = this.getDescription();
		String tag = this.getTag();
		
		int completeStatus = parseCompleteStatus(this.isCompleted);
		assert(isValidCompleteStatus(completeStatus));
		
		
		String taskString = name + NIConstants.NORMAL_FIELD_SPLITTER + startDate + NIConstants.NORMAL_FIELD_SPLITTER + endDate + NIConstants.NORMAL_FIELD_SPLITTER + priority + NIConstants.NORMAL_FIELD_SPLITTER + tag + NIConstants.NORMAL_FIELD_SPLITTER +desc +NIConstants.NORMAL_FIELD_SPLITTER + completeStatus;
		
		return taskString;
	}
	
	private int parseCompleteStatus(boolean complete){
		if(complete){
			return COMPLETED_IN_HARDDISK;
		}
		else{
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
