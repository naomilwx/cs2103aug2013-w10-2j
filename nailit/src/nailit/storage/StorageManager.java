package nailit.storage;

public class StorageManager {
	private DataManager inMemory;
	private FileManager hardDisk;
	
	public StorageManager(){
		inMemory = new DataManager();
		hardDisk = new FileManager();
	}
	
	public int add(){
		return 0;
	}
	//update stub
	public boolean retrieve(){
		return true;
	}
	public boolean remove(){
		return true;
	}
}
