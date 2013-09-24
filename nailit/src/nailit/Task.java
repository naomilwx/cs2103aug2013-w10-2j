package nailit;

public class Task {
	private static int TASKID_NULL = -1;
	
	private int ID;
	private String name;
	private String description;
	//private DateTime startTime;
	//private DateTime endTime;
	private String tag;
	private boolean isCompleted;
	private int priority; //takes values 0,1,2,3
	public Task(){
		ID = TASKID_NULL;
		name = "";
		description = ""; //TODO: replace with constant
//		startTime = null;
//		endTime = null;
		tag = "";
		isCompleted = false;
	}
	public Task(String taskName){
		ID = TASKID_NULL;
		name = taskName;
		description = ""; //TODO: replace with constant
//		startTime = null;
//		endTime = null;
		tag = "";
		isCompleted = false;
		
	}
//	public Task(String taskName,String desc, DateTime startTime, DateTime endTime, String tag){
//		ID = TASKID_NULL;
//		name = taskName;
//		description = ""; //TODO: replace with constant
//		startTime = null;
//		endTime = null;
//		tag = "";
//		isDone = false;
//	}
	//getters
	public int getID(){
		return ID;
	}
	public int getPriority(){
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
//	public DateTime getStartTime(){
//		return startTime;
//	}
//	public DateTime getEndTime(){
//		return endTime;
//	}
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
	public void setPriority(int taskPriority){
		priority = taskPriority;
	}
//	public void setStartTime(DateTime start){
//		startTime = start;
//	}
//	public void setEndTime(DateTime end){
//		endTime = end;
//	}
	
	//method to create copy of Task object
	public Task copy(){
		Task newTask = new Task(name);
//		Task newTask = new Task(name,description, startTime, endTime, tag); actual object
		newTask.setID(ID);
		newTask.setCompleted(isCompleted);
		return newTask;
	}
}
