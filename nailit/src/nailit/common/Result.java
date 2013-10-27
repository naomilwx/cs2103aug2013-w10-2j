//@author A0091372H
package nailit.common;
import java.util.Vector;

public class Result {
	public static final int NULL_DISPLAY = -1;
	public static final int NOTIFICATION_DISPLAY = 0;
	public static final int TASK_DISPLAY = 1;
	public static final int LIST_DISPLAY = 2;
	public static final int HISTORY_DISPLAY = 3;
	public static final int EXECUTION_RESULT_DISPLAY = 4;
	public static final String EMPTY_DISPLAY = "";
	
	private boolean isExit;
	private boolean isSuccessful;
	private boolean isDelete = false;
	private boolean updateReminderList = false;
	private int displayType = NULL_DISPLAY;
	private String notificationPrintOut = EMPTY_DISPLAY;
	private Vector<Task> taskList = new Vector<Task>();
	private Vector<Task> reminderList = new Vector<Task>();
	private Vector<String> historyList = new Vector<String>();
	private Task task = null;
	
	//initialise "empty" Result object
	public Result(){
		isExit = false;
		isSuccessful = false;
	}
	
	public Result(boolean isExitCommand, boolean isSuccess, int displayType, String printOut){
		isExit = isExitCommand;
		isSuccessful = isSuccess;
		this.displayType = displayType;
		notificationPrintOut = printOut;
	}
	public Result(boolean isExitCommand, boolean isSuccess, int displayType, 
			Task taskToDisplay){
		isExit = isExitCommand;
		isSuccessful = isSuccess;
		this.displayType = displayType;
		task = taskToDisplay;
	}
	public Result(boolean isExitCommand, boolean isSuccess, int displayType, 
			String printOut, Task taskToDisplay, Vector<Task> tasks, Vector<String> history){
		isExit = isExitCommand;
		isSuccessful = isSuccess;
		this.displayType = displayType;
		notificationPrintOut = printOut;
		taskList = tasks;
		historyList = history;
		task = taskToDisplay;
	}
	public Result(boolean isExitCommand, boolean isSuccess, int displayType, 
			String printOut, Vector<Task> tasks, Vector<String> history){
		isExit = isExitCommand;
		isSuccessful = isSuccess;
		this.displayType = displayType;
		notificationPrintOut = printOut;
		taskList = tasks;
		historyList = history;
	}
	//setters
	public void setNotification(String printOut){
		notificationPrintOut = printOut;
	}
	public void setIsSuccessful(boolean isSuccess){
		isSuccessful = isSuccess;
	}
	public void setDisplayType(int displayType){
		this.displayType = displayType;
	}
	public void setExitStatus(boolean isExitCommand){
		isExit = isExitCommand;
	}
	public void setTaskList(Vector<Task> taskList){
		this.taskList = taskList;
	}
	public void setHistoryList(Vector<String> historyList){
		this.historyList = historyList;
	}
	public void setTaskToDisplay(Task taskToDisplay){
		task = taskToDisplay;
	}
	public void setDeleteStatus(boolean isDeleteCommand){
		isDelete = isDeleteCommand;
	}
	public void setUpdateReminderList(boolean updateReminder){
		updateReminderList = updateReminder;
	}
	public void setReminderList(Vector<Task> reminders){
		reminderList = reminders;
	}
	//getters
	public boolean getExitStatus(){
		return isExit;
	}
	public boolean getDeleteStatus(){
		return isDelete;
	}
	public boolean getExecutionSuccess(){
		return isSuccessful;
	}
	public boolean isUpdateReminderList(){
		return updateReminderList;
	}
	public int getCommandType(){
		return displayType;
	}
	public String getPrintOut(){
		return notificationPrintOut;
	}
	public Vector<Task> getTaskList(){
		return taskList;
	}
	public Vector<Task> getReminderList(){
		return reminderList;
	}
	public Vector<String> getHistoryList(){
		return historyList;
	}
	public int getDisplayType(){
		return displayType;
	}
	public Task getTaskToDisplay(){
		return task;
	}
}
