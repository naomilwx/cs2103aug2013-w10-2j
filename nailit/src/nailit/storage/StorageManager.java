package nailit.storage;

import java.util.ArrayList;

import org.joda.time.DateTime;

import nailit.common.Task;

import nailit.common.TASK_PRIORITY;

public class StorageManager {
	private FileManager hardDisk;
	private DataManager origInMemory;
	private DataManager currInMemory;
	private final String DATAPATH = "storage.txt";
	private final String FIELD_SPLITTER = ",";
	/**
	 * Constructor
	 * */
	public StorageManager(){
		hardDisk = new FileManager(DATAPATH);
		origInMemory = new DataManager();
		currInMemory = new DataManager();

	}
	public int add(Task task){
		
		int ID = task.getID();
		
		String taskString = combinedIntoString(task);
				
		ID = currInMemory.add(ID,taskString);
		
		saveToFile();
		
		return ID;
	}

	//update stub
	public Task retrieve(int ID){
		return null;
	}
	public Task remove(int ID){
		return null;
	}
	
	//TO BE DECIDED WHETHER SET TO PUBLIC OR PRIVATE
	public void saveToFile(){
		ArrayList<String> dataList = currInMemory.getDataList();
		for(int i=0;i<dataList.size();i++){
			String stringToBeStored = dataList.get(i);
			if(stringToBeStored != null){
				hardDisk.add(stringToBeStored);
			}
		}
		
		hardDisk.save();
	}
	
	/**
	 * Private Methods
	 * */
	private String combinedIntoString(Task task){
		
		int priority = parsePriority(task.getPriority());
		assert(isValidPriority(priority));
		
		String name = task.getName();
		String startDate = task.getStartTime().toString();
		String endDate = task.getEndTime().toString();
		String desc = task.getDescription();
		String tag = task.getTag();
		
		String taskString = name + FIELD_SPLITTER + startDate + FIELD_SPLITTER + endDate + FIELD_SPLITTER + priority + FIELD_SPLITTER + tag + FIELD_SPLITTER +desc;
		
		return taskString;
	}
	
	private int parsePriority(TASK_PRIORITY p){
		switch(p){
			case LOW: return 0;
			case MEDIUM: return 1;
			case HIGH: return 2;
			default: return -1;
		}
	}
	
	private boolean isValidPriority(int p){
		return p>=0&&p<=2;
	}
	
	public static void main(String[] args){

	}
}
