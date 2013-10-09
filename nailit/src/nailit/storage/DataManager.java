package nailit.storage;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import nailit.common.NIConstants;
import nailit.common.TaskPriority;
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
		if(!addedBefore(ID)){
			
			ID = generateNewID();
			newTask.setID(ID);
			
			hashTable.put(ID, newTask);
		}
		else{
			assert(ID>=1);

			hashTable.put(ID,newTask);
		}
		return ID;
	}
	
	public Task retrieve(int ID) throws NoTaskFoundException{
		Task task = hashTable.get(ID);
		if(task==null){
			throw new NoTaskFoundException("The task cannot be found!");
		}
		else{
			return task;
		}
	}
	
	public Task remove(int ID){
		assert(isValidID(ID));
		return hashTable.remove(ID);
	}
		
	public int getNextValidID(){
		return nextValidID;
	}
	public HashMap<Integer,Task> getHashMap(){
		return hashTable;
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
		return true;
	}
}
