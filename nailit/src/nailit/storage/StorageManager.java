package nailit.storage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.joda.time.DateTime;

import nailit.common.FilterObject;
import nailit.common.NIConstants;
import nailit.common.Task;
import nailit.common.TaskPriority;
import nailit.storage.FileCorruptionException;
import nailit.storage.NoTaskFoundException;
 
public class StorageManager {
	private FileManager taskFile;
	private DataManager inMemory;
	private final String TASK_PATH = "database.txt";
	private int nextValidIDWhenSessionStarts;
	private HashMap<Integer,Task> originalTaskList;
	/**
	 * Constructor
	 * @throws FileCorruptionException 
	 * */
	public StorageManager() throws FileCorruptionException{
		taskFile = new FileManager(TASK_PATH);
		interpretTaskFileContents(taskFile.getFileContents());
		inMemory = new DataManager(nextValidIDWhenSessionStarts, originalTaskList);
	}
	
	
	public int add(Task task){
		if(task == null){
			return Task.TASKID_NULL;
		}
		
		Task taskToBeAdded = task.copy();
						
		int ID = inMemory.add(taskToBeAdded);
		
		saveToFile(taskFile);
		
		return ID;
	}


	public Task remove(int ID,boolean isUndoAdd) throws NoTaskFoundException{
		
		Task task = inMemory.remove(ID);
		if(TaskNotFound(task)){
			throw new NoTaskFoundException("The task cannot be found");
		}	
		
		if(isUndoAdd){
			releaseID(ID);
		}
		saveToFile(taskFile);
		return task;
	}


	public Task retrieve(int ID) throws NoTaskFoundException{
		Task task = inMemory.retrieve(ID);
		
		if(TaskNotFound(task)){
			throw new NoTaskFoundException("The task cannot be found");
		}
		
		return task;
	}
	
	public Vector<Task> filter(FilterObject ftobj){
		
		if(ftobj == null){
			return new Vector<Task>();
		}
		
		Vector<Task> taskList = retrieveAll();
		Vector<Task> filteredTaskList = new Vector<Task>();
		
		for(int i=0;i<taskList.size();i++){
			Task task = taskList.get(i);
			if(task.getID()==20){
				int j=222;
			}
			if(matchTask(task,ftobj)){
				filteredTaskList.add(taskList.get(i));
			}
		}
		
		return filteredTaskList;
	}


	public void clear(){
		inMemory.setTaskList(new HashMap<Integer,Task>());
		inMemory.setNextValidID(1);
		saveToFile(taskFile);		
	}


	public Vector<Task> getReminderListForToday(){
		Vector<Task> v = new Vector<Task>();
		
		HashMap<Integer,Task> taskList = getTaskInMemory();
		
		Set<Integer> keys = taskList.keySet();
		
		Iterator<Integer> iterator = keys.iterator();
		
		while(iterator.hasNext()){
			
			int key = iterator.next();
			
			Task task = taskList.get(key).copy();
			
			if(haveReminder(task)&&isReminderForToday(task)){
				v.add(task);
			}
		}
	
		return v;
	}


	public Vector<Task> retrieveAll() {
		
		HashMap<Integer,Task> hashTable = inMemory.getTaskList();
		
		return toTaskVector(hashTable);
	}
	
	private void interpretTaskFileContents(Vector<String> fileContents) throws FileCorruptionException{
		originalTaskList = new HashMap<Integer,Task>();
		try{
			if(isEmptyFile(fileContents)){
				nextValidIDWhenSessionStarts = 1;
			}else{
				nextValidIDWhenSessionStarts = Integer.parseInt(fileContents.get(0));
			}
			
			for(int i=1;i<fileContents.size();i++){
				String taskString = fileContents.get(i);
				Task task = stringToTask(taskString);
				originalTaskList.put(task.getID(), task);
			}
	
		}
		catch(Exception e){
			throw new FileCorruptionException("The file " + TASK_PATH +" is corrupted");
		}
				
	}


	private void prepareWritingContents(FileManager file){
		
		Vector<String> dataList = new Vector<String>();
	
		HashMap<Integer,Task> taskList = inMemory.getTaskList();
	
		dataList.add(""+inMemory.getNextValidID());
		
		taskListToStringVector(taskList,dataList);
		
		taskFile.setDataListForWriting(dataList);
		
	}


	private void saveToFile(FileManager file){
				
		file.writingProcessInit();
		
		prepareWritingContents(file);
		
		file.save();
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


	private Task stringToTask(String taskString) throws Exception{
		String[] result = taskString.split("\\" + NIConstants.HARDDISK_FIELD_SPLITTER);
		
		int task_ID = Integer.parseInt(result[0]);
		
		String name = result[1];
		
		DateTime startTime;
		if(result[2].compareTo("null") == 0){
			startTime = null;
		}else{
			startTime = new DateTime(result[2]);
		}
		
		DateTime endTime;
		if(result[3].compareTo("null") == 0){
			endTime = null;
		}else{
			endTime = new DateTime(result[3]);
		}
		
		TaskPriority priority = TaskPriority.getPriority(Integer.parseInt(result[4]));
		String tag = result[5];
		String desc = result[6];
		boolean isCompleted = Integer.parseInt(result[7]) == 1;
		
		DateTime reminder;
		if(result[8].compareTo("null") == 0){
			reminder = null;
		}else{
			reminder = new DateTime(result[8]);
		}
		Task task = new Task(task_ID, name,startTime,endTime,priority, tag,desc,isCompleted,reminder);
		return task;
	}


	private void taskListToStringVector(HashMap<Integer,Task> hashTable,Vector<String> dataList){
		
		Set<Integer> keys = hashTable.keySet();
		
		Iterator<Integer> iterator = keys.iterator();
		
		while(iterator.hasNext()){
			
			int key = iterator.next();
			
			Task task = hashTable.get(key);
			
			dataList.add(task.getID()+NIConstants.HARDDISK_FIELD_SPLITTER+task.changeToDiskFormat());
		}
		
	}


	private boolean TaskNotFound(Task task){
		return task == null;
	}


	private void releaseID(int ID){
		int nextValidID = inMemory.getNextValidID();
		if(isJustAdded(ID,nextValidID)){
			inMemory.setNextValidID(nextValidID-1);
		}
	}


	private boolean isJustAdded(int ID,int nextValidID){
		return ID == nextValidID-1;
	}


	private boolean haveReminder(Task task){
		return task!=null&&task.getReminder()!=null;
	}


	/**
	 * Private Methods
	 * */
	private boolean isReminderForToday(Task task){
		DateTime startOfToday = DateTime.now().withTimeAtStartOfDay();
		DateTime endOfToday = startOfToday.minusDays(-1).minusMillis(1);
		DateTime reminder = task.getReminder();
		return reminder.compareTo(endOfToday)<=0;
	}
	private HashMap<Integer,Task> getTaskInMemory(){
		return inMemory.getTaskList();
	}
	private boolean matchTask(Task task, FilterObject ftobj){
		
		boolean nameTagMatch = nameTagMatching(task,ftobj);
		if(nameNotMatch(task,ftobj)&&!nameTagMatch){
			return false;
		}
		
		if(tagNotMatch(task,ftobj)){
			return false;
		}
		
		if(TimeNotMatch(task,ftobj)){
			return false;
		}
		if(priorityNotMatch(task,ftobj)){
			return false;
		}
		
		if(completeStatusNotMatch(task,ftobj)){
			return false;
		}
		
		return true;
	}

	private boolean nameNotMatch(Task task, FilterObject ftobj){
		return (!isNameEmpty(ftobj)&&!task.getName().toLowerCase().contains(ftobj.getName().toLowerCase()));
	}
	private boolean nameTagMatching(Task task,FilterObject ftobj){
		if(isTagEmpty(ftobj)){
			if(!isNameEmpty(ftobj)){
				if(task.getTag().toLowerCase().contains(ftobj.getName().toLowerCase())){
					return true;
				}
			}
		}
		return false;
	}
	private boolean priorityNotMatch(Task task,FilterObject ftobj){
		return !isPriorityEmpty(ftobj)&&!task.getPriority().equals(ftobj.getPriority());//TODO: check whether the enum has the right the equal function
	}
	

	private boolean tagNotMatch(Task task,FilterObject ftobj){		
		return !isTagEmpty(ftobj)&&!task.getTag().toLowerCase().contains(ftobj.getTag().toLowerCase());		
	}
	
	private boolean completeStatusNotMatch(Task task,FilterObject ftobj){
		return !isCompleteStatusEmpty(ftobj)&&task.checkCompleted() != ftobj.isCompleted();
	}
	
	
	private boolean TimeNotMatch(Task task,FilterObject ftobj){
		DateTime start = ftobj.getStartTime();
		DateTime end = ftobj.getEndTime();
		
		return !task.isInDateRange(start,end);
	}


	private boolean isTagEmpty(FilterObject ftobj){
		return ftobj.getTag() == null;
	}


	private boolean isPriorityEmpty(FilterObject ftobj){
		return ftobj.getPriority() == null;
	}


	private boolean isNameEmpty (FilterObject ftobj){
		return ftobj.getName() == null;
	}


	private boolean isCompleteStatusEmpty(FilterObject ftobj){
		return ftobj.isCompleted() == null;
	}


	private boolean isEmptyFile(Vector<String> fileContents){
		return fileContents.size() == 0;
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
		sto.getReminderListForToday();
	}
}
