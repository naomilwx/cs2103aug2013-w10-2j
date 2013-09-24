package nailit;

import java.util.ArrayList;

public class Result {
	private boolean isExit;
	private boolean isSuccessful;
	private String commandType;
	private String executionPrintOut;
	private ArrayList<Task> taskList;
	//initialise "empty" Result object
	public Result(){
		isExit = false;
		isSuccessful = false;
		commandType = null;
		executionPrintOut = "";
		taskList = null;
	}
	public Result(boolean isExitCommand, boolean isSuccess, String commandType, String printOut){
		isExit = isExitCommand;
		isSuccessful = isSuccess;
		this.commandType = commandType;
		executionPrintOut = printOut;
		taskList = null;
	}
	public Result(boolean isExitCommand, boolean isSuccess, String commandType, String printOut, ArrayList<Task> taskList){
		isExit = isExitCommand;
		isSuccessful = isSuccess;
		this.commandType = commandType;
		executionPrintOut = printOut;
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
		return commandType;
	}
	public String getPrintOut(){
		return executionPrintOut;
	}
	public ArrayList<Task> getTaskList(){
		return taskList;
	}
}
