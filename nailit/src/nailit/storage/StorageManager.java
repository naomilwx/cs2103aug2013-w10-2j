package nailit.storage;

import org.joda.time.DateTime;

import nailit.Task;

public class StorageManager {
	private DataManager inMemory;
	//
	public StorageManager(){
		inMemory = new DataManager();
	}
	
	public int add(Task task){
				
		int ID = inMemory.add(task);
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
