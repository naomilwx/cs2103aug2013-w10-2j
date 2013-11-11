package nailit.storage;
/**
 * DataManager class is used to store the data in memory, i.e. store the data
 * passed from the hardDisk as well as the changes made in the upper layers
 * @author a0105683e
 * */
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


import nailit.common.Task;
import nailit.storage.NoTaskFoundException;

public class DataManager {

	private HashMap<Integer,Task> taskTable = new HashMap<Integer,Task>();
	private int nextValidID;
	
	/**
	 * Constructor
	 * @param nextValidID   the ID to give once a new task needs to be created
	 * @param taskTable     the task list to set up the memory
	 * */
	public DataManager(int nextValidID,HashMap<Integer,Task> taskTable){
		this.nextValidID =nextValidID;
		this.taskTable = taskTable;
	}
	
	
	/***************************
	 * Public Methods
	 ***************************/
	
	

	/**
	 * add or update a task to memory. whether it is add or update depends on whether
	 * the task is added before
	 * @param newTask       the task needs to be added or updated
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
	
	/**
	 * retrieve a task with a specific ID
	 * @param ID     The ID needed to specify a task
	 * @throws NoTaskFoundException  
	 * */
	public Task retrieve(int ID) throws NoTaskFoundException{
		if(!taskTable.containsKey(ID)){
			throw new NoTaskFoundException("The task cannot be found!");
		}else{
			Task task = taskTable.get(ID);
			return task;
		}
	}
	
	/**
	 * remove a task with a specific ID
	 * @param ID     The ID needed to specify a task
	 * @throws NoTaskFoundException  
	 * */
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
	
	public void setTaskList(HashMap<Integer,Task> anotherTaskTable){
		taskTable = anotherTaskTable;
	}
	
	public void setNextValidID(int ID){
		nextValidID = ID;
	}
	
	
	/***************************
	 * Private Methods
	 ***************************/
	
	
	
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
