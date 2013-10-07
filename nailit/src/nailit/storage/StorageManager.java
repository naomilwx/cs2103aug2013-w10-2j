package nailit.storage;

import java.util.Vector;

import org.joda.time.DateTime;

import nailit.common.NIConstants;
import nailit.common.Task;
import nailit.common.TaskPriority;
 
public class StorageManager {
	private FileManager hardDisk;
	private DataManager origInMemory;
	private DataManager currInMemory;
	private final String DATAPATH = "storage.txt";

	/**
	 * Constructor
	 * @throws FileCorruptionException 
	 * */
	public StorageManager() throws FileCorruptionException{
		hardDisk = new FileManager(DATAPATH);
		origInMemory = new DataManager();
		currInMemory = new DataManager();
		currInMemory.setDataList(hardDisk.getDataList());

	}
	public int add(Task task){
		
		int ID = task.getID();
		
		String taskString = task.changeToDiskFormat();
				
		ID = currInMemory.add(ID,taskString);
		
		assert(ID>=0);
		
		saveToFile();
		
		return ID;
	}

	public Task retrieveNew(int ID){
		return null;//TODO : reformat DataManager and FileManager
	}
	//update stub
	public Task retrieve(int ID) throws NoTaskFoundException, FileCorruptionException {
		
		String taskString;
		taskString = currInMemory.retrieve(ID);
		String[] result = taskString.split("\\"+NIConstants.NORMAL_FIELD_SPLITTER);
		try{
			int task_ID = Integer.parseInt(result[0]);
			String name = result[1];
			DateTime startTime;
			if(result[2].compareTo("null") == 0){
				startTime = null;
			}
			else{
				startTime = new DateTime(result[2]);
			}
			DateTime endTime;
			if(result[3].compareTo("null") == 0){
				endTime = null;
			}
			else{
				endTime = new DateTime(result[3]);
			}
			
			TaskPriority priority = TaskPriority.getPriority(Integer.parseInt(result[4]));
			String tag = result[5];
			String desc = result[6];
			boolean isCompleted = Integer.parseInt(result[7]) == 1;
			
			return new Task(task_ID, name,startTime,endTime,priority, tag,desc,isCompleted);
		}
		catch(Exception e){
			throw new FileCorruptionException(DATAPATH+" is corrupted. Please delete "+DATAPATH+" and restart NailIt");
		}
		
		
	}
	public Task remove(int ID) throws NoTaskFoundException, FileCorruptionException{
		Task task = retrieve(ID);
		currInMemory.remove(ID);
		saveToFile();
		return task;
		
	}
	
	public Vector<Task> retrieveAll() throws FileCorruptionException{
		Vector<String> dataList = currInMemory.getDataList();
		Vector<Task> taskList = new Vector<Task>();
		for(int i=1;i<=dataList.size();i++){
			try {
				Task task = retrieve(i);
				taskList.add(task);
			} catch (NoTaskFoundException e) {

			} 
		}
		
		return taskList;
	}
	
	public void clear(){
		currInMemory.setDataList(new Vector<String>());
		saveToFile();
	}
	/**
	 * Private Methods
	 * */

	private void saveToFile(){
		
		Vector<String> dataList = currInMemory.getDataList();
		
		hardDisk.writingProcessInit();
		
		for(int i=0;i<dataList.size();i++){
			
			String stringToBeStored = dataList.get(i);
			
			if(stringToBeStored != null){
				hardDisk.add(stringToBeStored);
			}
		}
		
		hardDisk.save();
	}
	
	
	
	public static void main(String[] args){
		String s = "";
		String[] results = s.split("\\,");
		String str = null;
		System.out.println(str == null);
//		System.out.print(results[0]);
	}
}
