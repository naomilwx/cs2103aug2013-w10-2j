package nailit.storage;

public class DataManager {
	private final int TASKID_NULL = -1;
	private FileManager hardDisk;
	private ArrayList<Task> tasks;
	public int add(int ID, String name,String startDate,String endDate,int priority,String tag,String desc){
		
		if(!addedBefore(ID)){
			ID = generateNewID();
		}
		hardDisk.add(ID,name,startDate,endDate,priority,desc,tag);
		return ID;
	}
	public boolean addedBefore(int ID){
		return ID != TASKID_NULL;
	}
	public int generateNewID(){
		return -1;
	}
	//Constructor
	public DataManager(){
		hardDisk = new FileManager();
		tasks = new ArrayList<Task>();
	}
}
