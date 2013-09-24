package nailit.storage;

import nailit.Task;

public class StorageManager {
	private DataManager inMemory;
	
	public StorageManager(){
		inMemory = new DataManager();
	}
	
	public int add(Task task){
		
		int ID = task.getID();
		int priority = task.getPriority();
		String name = task.getName();
		String startDate = task.getStartTime();
		String endDate = task.getEndTime();
		String desc = task.getDescription();
		String tag = task.getTag();
		
		ID = inMemory.add(ID,name,priority,startDate,endDate,desc,tag);
		return ID;
	}
	//update stub
	public boolean retrieve(int ID){
		return true;
	}
	public boolean remove(int ID){
		return true;
	}
}
