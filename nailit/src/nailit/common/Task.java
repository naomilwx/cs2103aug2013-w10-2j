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
	public Task(String taskName,String desc, DateTime startTime, DateTime endTime, String tag, TASK_PRIORITY priority){
		ID = TASKID_NULL;
		name = taskName;
		description = ""; //TODO: replace with constant
		startTime = null;
		endTime = null;
		tag = "";
		isCompleted = false;
		this.priority = priority;
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
	public boolean setID(int ID){
		if(ID==Task.TASKID_NULL){
			this.ID = ID;
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
	public void setCompleted(boolean isCompleted){
		this.isCompleted = isCompleted;
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
		Task newTask = new Task(name,description, startTime, endTime, tag, priority);
		newTask.setID(ID);
		newTask.setCompleted(isCompleted);
		return newTask;
	}
	public String printNameAndID(){
		return String.format(BASIC_PRINTOUT_FORMAT, ID, name);
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
}
