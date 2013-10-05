package nailit.storage;

import java.util.ArrayList;

import org.joda.time.DateTime;

import nailit.common.Task;

import nailit.common.TaskPriority;

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
		
		String taskString = task.changeToDiskFormat();
				
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

	
	public static void main(String[] args){

	}
}
