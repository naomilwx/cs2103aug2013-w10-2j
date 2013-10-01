package nailit.storage;

import org.joda.time.DateTime;

import nailit.common.Task;

public class StorageManager {
	private FileManager hardDisk;
	private DataManager origInMemory;
	private DataManager currInMemory;
	private final String DATAPATH = "storage.txt";
	//
	public StorageManager(){
		hardDisk = new FileManager(DATAPATH);
		origInMemory = new DataManager();
		currInMemory = new DataManager();

	}
	public int add(Task task){
		
		int ID = task.getID();
		int priority = task.getPriority();
		String name = task.getName();
		DateTime startDate = task.getStartTime();
		DateTime endDate = task.getEndTime();
		String desc = task.getDescription();
		String tag = task.getTag();
		

		
		currInMemory.add(ID,name,startDate,endDate,priority,tag,desc);
		
		saveToFile();
		return ID;
	}

	//update stub
	public Task retrieve(int ID){
		return null;
	}
	public Task remove(int ID){
		return null;
	}
	public void saveToFile(){
		
	}
}
