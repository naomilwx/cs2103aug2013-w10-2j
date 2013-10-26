package nailit.storage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.joda.time.DateTime;

import nailit.common.FilterObject;
import nailit.common.NIConstants;
import nailit.common.Reminder;
import nailit.common.Task;
import nailit.common.TaskPriority;
import nailit.storage.FileCorruptionException;
import nailit.storage.NoTaskFoundException;
 
public class StorageManager {
	private FileManager taskFile;
	private FileManager reminderFile;
	private DataManager inMemory;
	private final String TASK_PATH = "database.txt";
	private final String REMINDER_PATH = "reminders.txt";
	private int nextValidIDWhenSessionStarts;
	private HashMap<Integer,Task> originalTaskList;
	private HashMap<Integer,Reminder> originalRemindersList; 
	/**
	 * Constructor
	 * @throws FileCorruptionException 
	 * */
	public StorageManager() throws FileCorruptionException{
		taskFile = new FileManager(TASK_PATH);
		reminderFile = new FileManager(REMINDER_PATH);
		interpretTaskFileContents(taskFile.getFileContents());
		interpretReminderFileContents(reminderFile.getFileContents());
		inMemory = new DataManager(nextValidIDWhenSessionStarts, originalTaskList);
	}
	
	
	public int add(Task task,boolean isUndoRemove){
		if(task == null){
			return Task.TASKID_NULL;
		}
		
		Task taskToBeAdded = task.copy();
		
//		reformatTaskDescription(task);
				
		int ID = inMemory.add(taskToBeAdded);		
		
		if(isUndoRemove){
//			reminderRecovering(ID);
		}
		
		saveToFile(taskFile);
		
		return ID;
	}


	public Task retrieve(int ID) throws NoTaskFoundException{
		Task task = inMemory.retrieve(ID);
		
		if(TaskNotFound(task)){
			throw new NoTaskFoundException("The task cannot be found");
		}
		
		return task;
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
	
	
		
	public Vector<Task> retrieveAll() {
		
		HashMap<Integer,Task> hashTable = inMemory.getTaskList();
		
		return toTaskVector(hashTable);
	}
	
	public Vector<Task> filter(FilterObject ftobj){
		
		if(ftobj == null){
			return new Vector<Task>();
		}
		
		Vector<Task> taskList = retrieveAll();
		Vector<Task> filteredTaskList = new Vector<Task>();
		
		for(int i=0;i<taskList.size();i++){
			Task task = taskList.get(i);
			if(matchTask(task,ftobj)){
				filteredTaskList.add(taskList.get(i));
			}
		}
		
		return filteredTaskList;
	}
	
	public void addReminder(int ID,DateTime date){
		Reminder reminder = new Reminder(ID,date);
		inMemory.addReminder(reminder);
		saveToFile(reminderFile);
	}
	
	public void delReminder(int ID){
		inMemory.delReminder(ID);
		saveToFile(reminderFile);
	}
	
	public void clear(){
		inMemory.setTaskList(new HashMap<Integer,Task>());
		inMemory.setNextValidID(1);
		saveToFile(taskFile);
		saveToFile(reminderFile);//TODO: clear reminders
		
	}
	/**
	 * Private Methods
	 * */

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
	
	private void prepareWritingContents(FileManager file){
		
		if(isTaskFile(file)){
			prepareWritingContentsForTaskFile();
		}
		else{
			prepareWritingContentsForReminderFile();
		}
		
	}
	
	private boolean isTaskFile(FileManager file){
		return file.equals(taskFile);
	}
	
	private void prepareWritingContentsForTaskFile(){
		Vector<String> dataList = new Vector<String>();

		HashMap<Integer,Task> taskList = inMemory.getTaskList();

		dataList.add(""+inMemory.getNextValidID());
		
		taskListToStringVector(taskList,dataList);
		
		taskFile.setDataListForWriting(dataList);
	}
	
	private void prepareWritingContentsForReminderFile(){
		Vector<String> dataList = new Vector<String>();
		HashMap<Integer,Task> taskList = inMemory.getTaskList();
		reminderListToStringVector(taskList, dataList);
	}
	private void taskListToStringVector(HashMap<Integer,Task> hashTable,Vector<String> dataList){
		
		Set<Integer> keys = hashTable.keySet();
		
		Iterator<Integer> iterator = keys.iterator();
		
		while(iterator.hasNext()){
			
			int key = iterator.next();
			
			Task task = hashTable.get(key);
			
			dataList.add(task.getID()+NIConstants.NORMAL_FIELD_SPLITTER+task.changeToDiskFormat());
		}
		
	}
	
	private void reminderListToStringVector(HashMap<Integer,Task> taskTable, Vector<String> dataList){
		Set<Integer> keys = taskTable.keySet();
		
		Iterator<Integer> iterator = keys.iterator();
		
		while(iterator.hasNext()){
			
			int key = iterator.next();
			
			Reminder reminder = taskTable.get(key).getReminder();
			
			dataList.add(reminder.changeToDiskFormat());
		}
	}
	
	private void reminderRecovering(int ID){
		HashMap<Integer,Reminder> reminderList = inMemory.getRemindersList();
		if(containsReminder(reminderList,ID)){
			recoverReminder(reminderList,ID);
		}
	}
	
	private boolean containsReminder(HashMap<Integer,Reminder> reminderList,int ID){
		return reminderList.containsKey(ID);
	}
	
	private void recoverReminder(HashMap<Integer,Reminder> reminderList,int ID){
		Reminder reminder = reminderList.get(ID);
		reminder.recover();
	}
	
	private boolean TaskNotFound(Task task){
		return task == null;
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
				Task task = stringToTask(fileContents.get(i));
				originalTaskList.put(task.getID(), task);
			}

		}
		catch(Exception e){
			throw new FileCorruptionException("The file " + TASK_PATH +" is corrupted");
		}
				
	}
	private void interpretReminderFileContents(Vector<String> fileContents) throws FileCorruptionException{
		originalRemindersList = new HashMap<Integer,Reminder>();
		try{
			for(int i=1;i<fileContents.size();i++){
				Reminder reminder = stringToReminder(fileContents.get(i));
				originalRemindersList.put(reminder.getID(), reminder);
			}

		}catch(Exception e){
			throw new FileCorruptionException("The file " + REMINDER_PATH +" is corrupted");
		}
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
	private Task stringToTask(String taskString) throws Exception{
		String[] result = taskString.split("\\" + NIConstants.NORMAL_FIELD_SPLITTER);
		
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
		
		Task task = new Task(task_ID, name,startTime,endTime,priority, tag,desc,isCompleted);
		return task;
	}
	
	private Reminder stringToReminder (String reminderString) throws Exception{
		String[] result = reminderString.split("\\" + NIConstants.NORMAL_FIELD_SPLITTER);	
		
		int task_ID = Integer.parseInt(result[0]);
				
		DateTime date;
		
		if(result[1].compareTo("null") == 0){
			date = null;
		}
		else{
			date = new DateTime(result[1]);
		}
		
		return new Reminder(task_ID,date);
	}
	private boolean isEmptyFile(Vector<String> fileContents){
		return fileContents.size() == 0;
	}
	
	private boolean isNameEmpty (FilterObject ftobj){
		return ftobj.getName() == null;
	}
	
	private boolean isStartTimeEmpty(FilterObject ftobj){
		return ftobj.getStartTime() == null;
	}
	
	private boolean isEndTimeEmpty(FilterObject ftobj){
		return ftobj.getEndTime() == null;
	}
	
	private boolean isPriorityEmpty(FilterObject ftobj){
		return ftobj.getPriority() == null;
	}
	
	private boolean isTagEmpty(FilterObject ftobj){
		return ftobj.getTag() == null;
	}
	
	private boolean isCompleteStatusEmpty(FilterObject ftobj){
		return ftobj.isCompleted() == null;
	}
	
	private boolean nameNotMatch(Task task, FilterObject ftobj){
		return !isNameEmpty(ftobj)&&!task.getName().toLowerCase().contains(ftobj.getName().toLowerCase());
	}
	
	private boolean priorityNotMatch(Task task,FilterObject ftobj){
		return !isPriorityEmpty(ftobj)&&!task.getPriority().equals(ftobj.getPriority());//TODO: check whether the enum has the right the equal function
	}
	
	private boolean tagNotMatch(Task task,FilterObject ftobj){
		return !isTagEmpty(ftobj)&&!task.getTag().equalsIgnoreCase(ftobj.getTag());
	}
	private boolean completeStatusNotMatch(Task task,FilterObject ftobj){
		return !isCompleteStatusEmpty(ftobj)&&task.checkCompleted() != ftobj.isCompleted();
	}
	

	private boolean TimeNotMatch(Task task,FilterObject ftobj){
		
		//For event when startTime and endTime, we only check whether TaskStartTime is in the time period
		//For tasks, we only check whether taskEndTime is in the time period
		
		if(task.isEvent()){
			if(!isStartTimeEmpty(ftobj)&&!isEndTimeEmpty(ftobj)){
				if(task.getStartTime().compareTo(ftobj.getStartTime())==-1){
					return true;
				}else if(task.getStartTime().compareTo(ftobj.getEndTime())==1){
					return true;
				}else{
					return false;
				}
			}else if(!isStartTimeEmpty(ftobj)&&isEndTimeEmpty(ftobj)){
				if(task.getStartTime().compareTo(ftobj.getStartTime())==-1){
					return true;
				}else{
					return false;
				}
				
			}else if(isStartTimeEmpty(ftobj)&&!isEndTimeEmpty(ftobj)){
				if(task.getStartTime().compareTo(ftobj.getEndTime())==1){
					return true;
				}
				else{	return false;
				}
			}else{
				return false;
			}

		}else if(task.isFloatingTask()){
			if(!isStartTimeEmpty(ftobj)||!isEndTimeEmpty(ftobj)){
				return true;
			}else{
				return false;
			}
		}else if(task.isNormalTask()){
			if(!isStartTimeEmpty(ftobj)&&!isEndTimeEmpty(ftobj)){
				if(task.getStartTime().compareTo(ftobj.getStartTime())==-1){
					return true;
				}else if(task.getStartTime().compareTo(ftobj.getEndTime())==1){
					return true;
				}else{
					return false;
				}
			}else if(!isStartTimeEmpty(ftobj)&&isEndTimeEmpty(ftobj)){
				if(task.getStartTime().compareTo(ftobj.getStartTime())==-1){
					return true;
				}else{
					return false;
				}
				
			}else if(isStartTimeEmpty(ftobj)&&!isEndTimeEmpty(ftobj)){
				if(task.getStartTime().compareTo(ftobj.getEndTime())==1){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}

		}else{
			return true;//TODO: check the standard for those tasks that does not belong to any category
		}
	}

	private boolean matchTask(Task task, FilterObject ftobj){
		if(nameNotMatch(task,ftobj)){
			return false;
		}
		if(TimeNotMatch(task,ftobj)){
			return false;
		}
		if(priorityNotMatch(task,ftobj)){
			return false;
		}
		if(tagNotMatch(task,ftobj)){
			return false;
		}
		if(completeStatusNotMatch(task,ftobj)){
			return false;
		}
		
		return true;
	}
	
	private void reformatTaskDescription(Task task){
		String originalDescription = task.getDescription();
		String newDescription = originalDescription.replaceAll("\n", NIConstants.DESCRIPTION_LINE_SPLITER);
		task.setDescription(newDescription);
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
	}
}
