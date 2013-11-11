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
 
/**
 * StorageManager class is the control unit of the Storage Component. It takes charge of CRUD (create,
 * read, update and delete) as well as filtering as the bottom level of the software. As a control unit,
 * it gets data and store data not by itself, but by DataManager and FileManager.
 * @author a0105683e
 * */
public class StorageManager {
	private final String TASK_PATH = "database.txt";
	
	private int nextValidIDWhenSessionStarts;
	private HashMap<Integer,Task> originalTaskList;

	private FileManager taskFile;
	private DataManager inMemory;
	
	/**
	 * Constructor
	 * @throws FileCorruptionException 
	 * */
	public StorageManager() throws FileCorruptionException{
		taskFile = new FileManager(TASK_PATH);
		interpretTaskFileContents(taskFile.getFileContents());
		inMemory = new DataManager(nextValidIDWhenSessionStarts, originalTaskList);
	}
	
	/***************************
	 * Public Methods
	 ***************************/
	
	/**
	 * add or update a task. Whether is is add or update depends whether the
	 * task is added before.
	 * @param task
	 * */
	public int add(Task task){
		if(task == null){
			return Task.TASKID_NULL;
		}
		
		Task taskToBeAdded = task.copy();
						
		int ID = inMemory.add(taskToBeAdded);
		
		saveToFile(taskFile);
		
		return ID;
	}

	
	/**
	 * remove a task permanently both from in memory and hardDisk
	 * @param ID			indicates which task to remove
	 * @param isUndoAdd 	indicates whether this function is called because 
	 * undo add so that the system will know whether to release task ID to maintain
	 * the consistency.
	 * @throws NoTaskFoundException
	 * */
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

	/**
	 * retrieve the copy of the task with the specific ID from memory
	 * @param ID
	 * @throws NoTaskFoundException
	 * */
	public Task retrieve(int ID) throws NoTaskFoundException{
		Task task = inMemory.retrieve(ID);
		
		if(TaskNotFound(task)){
			throw new NoTaskFoundException("The task cannot be found");
		}
		
		return task.copy();
	}
	
	/**
	 * filter out a vector of tasks that satisfy the criteria
	 * @param ftobj			a filter object which contains the criteria information
	 * */
	public Vector<Task> filter(FilterObject ftobj){
		
		if(ftobj == null){
			return new Vector<Task>();
		}
		
		Vector<Task> tasks = retrieveAll();
		Vector<Task> filteredTasks = new Vector<Task>();
		
		for(int i=0;i<tasks.size();i++){
			Task task = tasks.get(i);
			
			if(matchTask(task,ftobj)){
				filteredTasks.add(tasks.get(i));
			}
		}
		
		return filteredTasks;
	}


	public void clear(){
		inMemory.setTaskList(new HashMap<Integer,Task>());
		inMemory.setNextValidID(1);
		saveToFile(taskFile);		
	}

	/**
	 * This method will return back a vector of tasks which should be reminded today
	 * */
	public Vector<Task> getReminderListForToday(){
		Vector<Task> tasks = new Vector<Task>();
		
		HashMap<Integer,Task> taskList = getTaskInMemory();
		
		Set<Integer> keys = taskList.keySet();
		
		Iterator<Integer> i = keys.iterator();
		
		while(i.hasNext()){
			
			int key = i.next();
			
			Task task = taskList.get(key).copy();
			
			if(haveReminder(task)&&isReminderForToday(task)){
				tasks.add(task);
			}
		}
	
		return tasks;
	}


	public Vector<Task> retrieveAll() {
		
		HashMap<Integer,Task> hashTable = inMemory.getTaskList();
		
		return changeHashTableToTaskVector(hashTable);
	}
	
	
	
	
	
	/***************************
	 * Private Methods
	 ***************************/
	
	
	
	/**
	 * This method is used to interpret the string-form file contents into task-object-form
	 * @param fileContents
	 * @throws FileCorruptionException 
	 * */
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
				Task task = changeStringToTask(taskString);
				originalTaskList.put(task.getID(), task);
			}
	
		}catch(Exception e){
			throw new FileCorruptionException("The file " + TASK_PATH +" is corrupted");
		}
				
	}

	
	/**
	 * @param file			The file to be written
	 * */
	private void prepareWritingContents(FileManager file){
		
		Vector<String> dataList = new Vector<String>();
	
		HashMap<Integer,Task> tasks = inMemory.getTaskList();
	
		dataList.add(""+inMemory.getNextValidID());
		
		changeHashTableToStringVector(tasks,dataList);
		
		taskFile.setDataListForWriting(dataList);
		
	}


	private void saveToFile(FileManager file){
				
		file.writingProcessInit();
		
		prepareWritingContents(file);
		
		file.save();
	}


	private Vector<Task> changeHashTableToTaskVector(HashMap<Integer,Task> hashTable){
		
		Vector<Task> tasks = new Vector<Task>();
		
		Set<Integer> keys = hashTable.keySet();
		
		Iterator<Integer> i = keys.iterator();
		
		while(i.hasNext()){
			
			int key = i.next();
			
			Task task = hashTable.get(key).copy();
			
			tasks.add(task);
		}
		return tasks;
	
	}


	private Task changeStringToTask(String taskString) throws Exception{
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


	private void changeHashTableToStringVector(HashMap<Integer,Task> hashTable,Vector<String> dataList){
		
		Set<Integer> keys = hashTable.keySet();
		
		Iterator<Integer> i = keys.iterator();
		
		while(i.hasNext()){
			
			int key = i.next();
			
			Task task = hashTable.get(key);
			
			dataList.add(task.getID()+NIConstants.HARDDISK_FIELD_SPLITTER+task.changeToDiskFormat());
		}
		
	}


	private boolean TaskNotFound(Task task){
		return task == null;
	}

	/**
	 * This method will only release those task which is added by last operations
	 * @param ID
	 * */
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
	
	/**
	 * This method is used to filter out those tag fits to the name field criteria
	 * @param task
	 * @param ftobj
	 * */
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
		return !isCompleteStatusEmpty(ftobj)&&task.checkCompletedOrOver() != ftobj.isCompleted();
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
}
