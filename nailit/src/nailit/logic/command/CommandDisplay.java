package nailit.logic.command;

import java.util.Vector;


import nailit.common.Result;
import nailit.common.Task;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

public class CommandDisplay extends Command{
	private String commandType;
	private String commandSummary;
	private Result executedResult;
	private Task taskRetrieved;
	private int taskToRetrieveID;
	
	// the CommandManager instance that the CommandDisplay 
	// instance belongs to, since it may display the operations
	// history
	private CommandManager cm;
	
	private final String UnsuccessfulFeedback = "Sorry, there is no such task record in the storage, please check and try again.";

//	private final String Success_Msg = "The task is deleted successfully, the Task ID for it is: ";;
	public CommandDisplay(ParserResult resultInstance,
			StorageManager storerToUse, CommandManager cm) {
		super(resultInstance, storerToUse);
		this.cm = cm;
		commandType = "display";
	}

	@Override
	public Result executeCommand() {
		if(parserResultInstance.isDisplayAll()) {
			return displayAllTasks();
		} else if(parserResultInstance.isDisplayHistory()) {
			return displayOperationsHistory();
		} else {
			return displayTheTask();
		}
	}

	private Result displayAllTasks() {
		try {
			Vector<Task> vectorOfTasks = storer.retrieveAll();
			createResultObject(false, true, Result.TASK_DISPLAY, null, vectorOfTasks, null);
			return executedResult;
		} catch(Exception e) {
			createUnsuccessfulResultObject();
			return executedResult;
		}
	}

	private Result displayTheTask() {
		try {
			retrieveTheTask();
		} catch(Exception e) {
			createUnsuccessfulResultObject();
			return executedResult;
		}
		Vector<Task> vectorStoringTheTask = new Vector<Task>();
		vectorStoringTheTask.add(taskRetrieved);
		createResultObject(false, true, Result.TASK_DISPLAY, null, vectorStoringTheTask, null);
		createCommandSummary();
		return executedResult;
	}

	private Result displayOperationsHistory() {
		return executedResult;
		// TODO Auto-generated method stub
		
	}

	private void retrieveAllTheTasks() {
		// TODO Auto-generated method stub
		
	}

	private void createCommandSummary() {
		commandSummary = "display the task with the Task ID: " + taskToRetrieveID;		
	}

	private void createResultObject(boolean isExitCommand, boolean isSuccess, int displayType, 
			String printOut, Vector<Task> tasks, Vector<String> history) {
		executedResult = new Result(isExitCommand, isSuccess, displayType, printOut, tasks, history);
	}
	
	// there is no such task record in the storage to display
	private void createUnsuccessfulResultObject() {
		executedResult = new Result(false, false, Result.NOTIFICATION_DISPLAY, UnsuccessfulFeedback, null, null);
	}

	private void retrieveTheTask() throws Exception {
		taskToRetrieveID = parserResultInstance.getTaskID();
		taskRetrieved = storer.retrieve(taskToRetrieveID);
	}

	public int getTaskID() {
		return taskToRetrieveID;
	}
}
