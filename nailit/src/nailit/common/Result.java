package nailit.common;
import java.util.Vector;

public class Result {
	public static final int NULL_DISPLAY = -1;
	public static final int NOTIFICATION_DISPLAY = 0;
	public static final int TASK_DISPLAY = 1;
	public static final int LIST_DISPLAY = 2;
	public static final int HISTORY_DISPLAY = 3;
	
	private boolean isExit;
	private boolean isSuccessful;
	private int displayType;
	private String notificationPrintOut;
	private Vector<Task> taskList;
	private Vector<String> historyList;
	//initialise "empty" Result object
	public Result(){
		isExit = false;
		isSuccessful = false;
		displayType = NULL_DISPLAY;
		notificationPrintOut = "";
		taskList = null;
	}
	public Result(boolean isExitCommand, boolean isSuccess, int displayType, String printOut){
		isExit = isExitCommand;
		isSuccessful = isSuccess;
		this.displayType = displayType;
		notificationPrintOut = printOut;
		taskList = null;
		historyList = null;
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
	//getters
	public boolean getExitStatus(){
		return isExit;
	}
	public boolean getExecutionSuccess(){
		return isSuccessful;
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
	public Vector<String> getHistoryList(){
		return historyList;
	}
}
