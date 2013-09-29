package nailit;

import java.util.ArrayList;

public class Result {
	private boolean isExit;
	private boolean isSuccessful;
	private String displayType;
	private String notificationPrintOut;
	private ArrayList<Task> taskList;
	//initialise "empty" Result object
	public Result(){
		isExit = false;
		isSuccessful = false;
		displayType = null;
		notificationPrintOut = "";
		taskList = null;
	}
	public Result(boolean isExitCommand, boolean isSuccess, String displayType, String printOut){
		isExit = isExitCommand;
		isSuccessful = isSuccess;
		this.displayType = displayType;
		notificationPrintOut = printOut;
		taskList = null;
	}
	public Result(boolean isExitCommand, boolean isSuccess, String displayType, String printOut, ArrayList<Task> taskList){
		isExit = isExitCommand;
		isSuccessful = isSuccess;
		this.displayType = displayType;
		notificationPrintOut = printOut;
		this.taskList = taskList;
	}
	//setters
	public void setNotification(String printOut){
		notificationPrintOut = printOut;
	}
	public void setIsSuccessful(boolean isSuccess){
		isSuccessful = isSuccess;
	}
	public void setDisplayType(String displayType){
		this.displayType = displayType;
	}
	public void setExitStatus(boolean isExitCommand){
		isExit = isExitCommand;
	}
	public void setTaskList(ArrayList<Task> taskList){
		this.taskList = taskList;
	}
	//getters
	public boolean getExitStatus(){
		return isExit;
	}
	public boolean getExecutionSuccess(){
		return isSuccessful;
	}
	public String getCommandType(){
		return displayType;
	}
	public String getPrintOut(){
		return notificationPrintOut;
	}
	public ArrayList<Task> getTaskList(){
		return taskList;
	}
}
