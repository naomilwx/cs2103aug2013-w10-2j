package nailit.logic.command;

import nailit.Task;
import nailit.storage.DataManager;
import nailit.storage.FileManager;

public class StorageManagerStub {
	private DataManager inMemory;
	private FileManager hardDisk;
	
	public StorageManagerStub(){
		inMemory = new DataManager();
		hardDisk = new FileManager();
	}
	
	public int add(Task taskObj){
		return 0;
	}
	//update stub
	public boolean retrieve(Integer taskID){
		return true;
	}
	public boolean remove(Integer taskID){
		return true;
	}
}
