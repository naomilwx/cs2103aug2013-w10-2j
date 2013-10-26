package nailit.storage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import nailit.common.NIConstants;
import nailit.common.Reminder;
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
	
	public void addReminder(Reminder reminder){
		assert(isValidReminderToBeAdded(reminder));
		int ID = reminder.getID();
		Task task = taskTable.get(ID);
		task.setReminder(reminder);
		taskTable.put(ID, task);
	}
	
	public void delReminder(int ID){
		assert(isValidReminderToBeDelete(ID));
		
	}
	
	public void markAsCompleted(int ID){
		
	}
	public int getNextValidID(){
		return nextValidID;
	}
	public HashMap<Integer,Task> getTaskList(){
		return taskTable;
	}
	public HashMap<Integer,Reminder> getRemindersList(){
		Set<Integer> keys = taskTable.keySet();
		Iterator<Integer> iterator = keys.iterator();
		HashMap<Integer,Reminder> reminderTable = new HashMap<Integer,Reminder>();
		
		while(iterator.hasNext()){
			int key = iterator.next();
			
			Task task = taskTable.get(key);
			
			reminderTable.put(task.getID(),task.getReminder());
		}
		return reminderTable;
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
	
	private boolean isValidReminderToBeAdded(Reminder reminder){
		return taskTable.containsKey(reminder.getID());
	}
	
	private boolean isValidReminderToBeDelete(int ID){
		return taskTable.containsKey(ID)&&taskTable.get(ID)!=null&&taskTable.get(ID).getReminder()!=null;
	}
}
