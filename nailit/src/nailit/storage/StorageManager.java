package nailit.storage;

import java.util.Vector;

import org.joda.time.DateTime;

import nailit.common.Task;

import nailit.common.TASK_PRIORITY;

public class StorageManager {
	private FileManager hardDisk;
	private DataManager origInMemory;
	private DataManager currInMemory;
	private final String DATAPATH = "storage.txt";

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
		Vector<String> dataList = currInMemory.getDataList();
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
		String s = "";
		String[] results = s.split("\\,");
//		System.out.print(results[0]);
	}
}
