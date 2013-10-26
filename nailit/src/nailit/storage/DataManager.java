package nailit.storage;

import java.util.HashMap;
import nailit.common.Task;
import nailit.storage.NoTaskFoundException;

public class DataManager {
	
	/**
	 * Private Fields
	 * */
	private HashMap<Integer,Task> hashTable = new HashMap<Integer,Task>();
	private int nextValidID;
	
	/**
	 * Constructor
	 * */
	public DataManager(int nextValidID,HashMap<Integer,Task> hashTable){
		this.nextValidID =nextValidID;
		this.hashTable = hashTable;
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
			
			hashTable.put(ID, newTask);
		}else{

			hashTable.put(ID,newTask);
		}
		return ID;
	}
	
	public Task retrieve(int ID) throws NoTaskFoundException{
		if(!hashTable.containsKey(ID)){
			throw new NoTaskFoundException("The task cannot be found!");
		}else{
			Task task = hashTable.get(ID);
			return task;
		}
	}
	
	public Task remove(int ID) throws NoTaskFoundException{
		
		assert(isValidRemovableID(ID));
		
		if(!hashTable.containsKey(ID)){
			throw new NoTaskFoundException("The task cannot be found!");
		}
		return hashTable.remove(ID);
	}
		
	public int getNextValidID(){
		return nextValidID;
	}
	public HashMap<Integer,Task> getHashMap(){
		return hashTable;
	}
	
	public void setHashMap(HashMap<Integer,Task> h){
		hashTable = h;
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
