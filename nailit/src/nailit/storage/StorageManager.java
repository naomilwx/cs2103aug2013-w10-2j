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
	public boolean retrieve(int ID){
		return true;
	}
	public boolean remove(int ID){
		return true;
	}
}
