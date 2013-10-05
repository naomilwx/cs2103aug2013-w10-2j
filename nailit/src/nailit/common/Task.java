package nailit.common;

import org.joda.time.DateTime;
public class Task {
	public static final int TASKID_NULL = -1;
	private static final String BASIC_PRINTOUT_FORMAT = "[%1d] Name: %2s";
	private int ID;
	private String name;
	private String description;
	private DateTime startTime;
	private DateTime endTime;
	private String tag;
	private boolean added = false; //variable to indicate if task has been added to task list
	private boolean isCompleted;
	private TASK_PRIORITY priority;
	private final String FIELD_SPLITTER = ",";
	public Task(){
		ID = TASKID_NULL;
		name = "";
		description = ""; //TODO: replace with constant
		startTime = null;
		endTime = null;
		tag = "";
		isCompleted = false;
		priority = null;
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
		priority = TASK_PRIORITY.MEDIUM;
	}
	// change from Shuzhi
	// do not think that here should have desc, since desc is 
	// added to it later. so delete ,String desc
	public Task(String taskName, DateTime start, DateTime end, String t, TASK_PRIORITY p){
		ID = TASKID_NULL;
		name = taskName;
//		description = desc;
		startTime = start;
		endTime = end;
		tag = t;
		isCompleted = false;
		priority = p;
	}
	
	public boolean isAdded(){
		return added;
	}
	//getters
	public int getID(){
		return ID;
	}
	public TASK_PRIORITY getPriority(){
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
	public void setPriority(TASK_PRIORITY taskPriority){
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
			formattedDateDetails = "Start Time: " + startTime + "\n"
									+"End Time: " + endTime;
		}else{
			formattedDateDetails = "Due: " + startTime;
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
	
	public String changeToDiskFormat(Task task){
		int priority = parsePriority(task.getPriority());
		assert(isValidPriority(priority));
		
		String name = task.getName();
		String startDate = task.getStartTime().toString();
		String endDate = task.getEndTime().toString();
		String desc = task.getDescription();
		String tag = task.getTag();
		
		String taskString = name + FIELD_SPLITTER + startDate + FIELD_SPLITTER + endDate + FIELD_SPLITTER + priority + FIELD_SPLITTER + tag + FIELD_SPLITTER +desc;
		
		return taskString;
	}
	
	private int parsePriority(TASK_PRIORITY p){
		switch(p){
			case LOW: return 0;
			case MEDIUM: return 1;
			case HIGH: return 2;
			default: return -1;
		}
	}
	
	private boolean isValidPriority(int p){
		return p>=0&&p<=2;
	}

}
