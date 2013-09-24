package nailit.storage;

import nailit.Task;

public class StorageManager {
	private DataManager inMemory;
	private FileManager hardDisk;
	
	public StorageManager(){
		inMemory = new DataManager();
		hardDisk = new FileManager();
	}
	
	public int add(Task task){
		
		int ID = task.getID();
		int priority = task.getPriority();
		String name = task.getName();
		String startDate = task.getStartTime();
		String engDate = task.getEndTime();
		String desc = task.getDescription();
		String tag = task.getTag();
	}
	//update stub
	public boolean retrieve(int ID){
		return true;
	}
	public boolean remove(int ID){
		return true;
	}
}
