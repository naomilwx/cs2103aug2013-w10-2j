package nailit.storage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.joda.time.DateTime;

import nailit.common.NIConstants;
import nailit.common.Task;
import nailit.common.TaskPriority;
import nailit.storage.FileCorruptionException;
import nailit.storage.NoTaskFoundException;
 
public class StorageManager {
	private FileManager hardDisk;
	private DataManager inMemory;
	private final String DATAPATH = "database.txt";
	private int nextValidIDWhenSessionStarts;
	private HashMap<Integer,Task> originalTaskList;
	/**
	 * Constructor
	 * @throws FileCorruptionException 
	 * */
	public StorageManager() throws FileCorruptionException{
		hardDisk = new FileManager(DATAPATH);
		interpretFileContents(hardDisk.getFileContents());
		inMemory = new DataManager(nextValidIDWhenSessionStarts, originalTaskList);
	}
	
	public int add(Task task){
				
		Task taskToBeAdded = task.copy();
				
		int ID = inMemory.add(taskToBeAdded);
				
		saveToFile();
		
		return ID;
	}


	//update stub
	public Task retrieve(int ID) throws NoTaskFoundException{
		Task task = inMemory.retrieve(ID);
		
		if(TaskNotFound(task)){
			throw new NoTaskFoundException("The task cannot be found");
		}
		
		return task;
	}
	
	public Task remove(int ID) throws NoTaskFoundException{
		Task task = inMemory.remove(ID);
		if(TaskNotFound(task)){
			throw new NoTaskFoundException("The task cannot be found");
		}
		saveToFile();
		return task;
		
	}
	
	public Vector<Task> retrieveAll() {
		
		HashMap<Integer,Task> hashTable = inMemory.getHashMap();
		
		return toTaskVector(hashTable);
	}
	
	public void clear(){
//		currInMemory.setDataList(new Vector<String>());
		saveToFile();
	}
	/**
	 * Private Methods
	 * */

	private void saveToFile(){
				
		hardDisk.writingProcessInit();
		
		prepareWritingContents();
		
		hardDisk.save();
	}
	
	private Vector<Task> toTaskVector(HashMap<Integer,Task> hashTable){
		
		Vector<Task> taskList = new Vector<Task>();
		
		Set<Integer> keys = hashTable.keySet();
		
		Iterator<Integer> iterator = keys.iterator();
		
		while(iterator.hasNext()){
			
			int key = iterator.next();
			
			Task task = hashTable.get(key).copy();
			
			taskList.add(task);
		}
		return taskList;

	}
	
	private void prepareWritingContents(){
		
		Vector<String> dataList = new Vector<String>();

		HashMap<Integer,Task> hashTable = inMemory.getHashMap();

		dataList.add(""+inMemory.getNextValidID());
		
		toStringVector(hashTable,dataList);
		
		hardDisk.setDataListForWriting(dataList);
	}
	
	private void toStringVector(HashMap<Integer,Task> hashTable,Vector<String> dataList){
		
		Set<Integer> keys = hashTable.keySet();
		
		Iterator<Integer> iterator = keys.iterator();
		
		while(iterator.hasNext()){
			
			int key = iterator.next();
			
			Task task = hashTable.get(key).copy();
			
			dataList.add(task.getID()+NIConstants.NORMAL_FIELD_SPLITTER+task.changeToDiskFormat());
		}
		
	}
	private boolean TaskNotFound(Task task){
		return task == null;
	}
	
	private void interpretFileContents(Vector<String> fileContents) throws FileCorruptionException{
		originalTaskList = new HashMap<Integer,Task>();
		try{
			nextValidIDWhenSessionStarts = Integer.parseInt(fileContents.get(0));
			
			for(int i=1;i<fileContents.size();i++){
				Task task = stringToTask(fileContents.get(i));
				originalTaskList.put(task.getID(), task);
			}

		}
		catch(Exception e){
			throw new FileCorruptionException("The file is corrupted");
		}
				
	}
	
	private Task stringToTask(String taskString) throws Exception{
		String[] result = taskString.split("\\" + NIConstants.NORMAL_FIELD_SPLITTER);
		
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
		
		Task task = new Task(task_ID, name,startTime,endTime,priority, tag,desc,isCompleted);
		return task;
	}
	public static void main(String[] args) throws FileCorruptionException{
		String s = "";
		String[] results = s.split("\\,");
		String str = null;
//		System.out.println(str == null);
//		System.out.print(results[0]);
		StorageManager sto =new StorageManager();
		Task task1 = new Task("frist task");
		Task task2 = new Task("second task");
		sto.add(task1);
	}
}
