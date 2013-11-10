package nailit.storage;
/**
 * @author a0105683e
 * */
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


import nailit.common.Task;
import nailit.storage.NoTaskFoundException;

public class DataManager {
	
	/**
	 * Private Fields
	 * */
	private HashMap<Integer,Task> taskTable = new HashMap<Integer,Task>();
	private int nextValidID;
	
	/**
	 * Constructor
	 * */
	public DataManager(int nextValidID,HashMap<Integer,Task> taskTable){
		this.nextValidID =nextValidID;
		this.taskTable = taskTable;
	}

	/**
	 * Public Methods
	 * */
	public int add(Task newTask){
		int ID = newTask.getID();
		
		assert(isValidID(ID));

		if(!addedBefore(ID)){
			
			ID = generateNewID();
			newTask.setID(ID);
			
			taskTable.put(ID, newTask);
		}else{

			taskTable.put(ID,newTask);
		}
		return ID;
	}
	
	public Task retrieve(int ID) throws NoTaskFoundException{
		if(!taskTable.containsKey(ID)){
			throw new NoTaskFoundException("The task cannot be found!");
		}else{
			Task task = taskTable.get(ID);
			return task;
		}
	}
	
	public Task remove(int ID) throws NoTaskFoundException{
		
		assert(isValidRemovableID(ID));
		
		if(!taskTable.containsKey(ID)){
			throw new NoTaskFoundException("The task cannot be found!");
		}
		return taskTable.remove(ID);
	}
	
	
	public int getNextValidID(){
		return nextValidID;
	}
	public HashMap<Integer,Task> getTaskList(){
		return taskTable;
	}
	
	public void setTaskList(HashMap<Integer,Task> h){
		taskTable = h;
	}
	
	public void setNextValidID(int ID){
		nextValidID = ID;
	}
	
	
	/**
	 * Private Methods
	 * */
	private int generateNewID(){
		int newID = nextValidID;
		nextValidID++;
		return newID;
	}
	
	private boolean addedBefore(int ID){
		return ID != Task.TASKID_NULL;
	}
	
	private boolean isValidID(int ID){
		return (ID == Task.TASKID_NULL) || (ID>=1&&ID<nextValidID);
	}
	
	private boolean isValidRemovableID(int ID){
		return ID>=1&&ID<nextValidID;
	}
	
}
