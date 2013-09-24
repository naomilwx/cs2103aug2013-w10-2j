package nailit.storage;

public class DataManager {
	private final int TASKID_NULL = -1;
	public int add(int ID, String name,String startDate,String endDate,int priority,String tag,String desc){
		if(addedBefore(ID)){
			
		}
		else{
			ID = generateNewID();
		}
	}
	public boolean addedBefore(int ID){
		return ID != TASKID_NULL;
	}
	public int generateNewID(){
		return -1;
	}
	//Constructor
	public DataManager(){
		
	}
}
