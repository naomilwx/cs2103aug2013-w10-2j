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
		return 0;
	}
	//update stub
	public boolean retrieve(int ID){
		return true;
	}
	public boolean remove(int ID){
		return true;
	}
}
