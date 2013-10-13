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
	
	private Vector<Task> taskList;
	
	private int displayID;
	
	// the CommandManager instance that the CommandDisplay 
	// instance belongs to, since it may display the operations
	// history
	private CommandManager cm;
	
	private static final String UNSUCCESS_DISPLAY_FEEDBACK = "Sorry, task [ID: %1d] cannot be found. Please check and try again.";

	public CommandDisplay(ParserResult resultInstance,
			StorageManager storerToUse, CommandManager cm, Vector<Task> currentTaskList) {
		super(resultInstance, storerToUse);
		this.cm = cm;
		commandType = "display";
		taskList = currentTaskList;
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
			Vector<Task> vectorOfTasks = this.retrieveAllTheTasks();
			createResultObject(false, true, Result.LIST_DISPLAY, null, null, vectorOfTasks, null);
			return executedResult;
		} catch(Exception e) {
			createUnsuccessfulResultObject();
			return executedResult;
		}
	}

	private Result displayTheTask() {
		try {
			getDisplayID();
			retrieveTheTask();
		} catch(Exception e) {
			createUnsuccessfulResultObject();
			return executedResult;
		}
//		Vector<Task> vectorStoringTheTask = new Vector<Task>();
//		vectorStoringTheTask.add(taskRetrieved);
		createResultObject(false, true, Result.EXECUTION_RESULT_DISPLAY, null, taskRetrieved, taskList, null);
		createCommandSummary();
		return executedResult;
	}

	private void getDisplayID() {
		// currently, displayID is still taskID
		displayID = parserResultInstance.getTaskID();
	}

	private Result displayOperationsHistory() {
		return executedResult;
		// TODO Auto-generated method stub
		
	}

	private Vector<Task> retrieveAllTheTasks() {
		return storer.retrieveAll();
		
	}

	private void createCommandSummary() {
		commandSummary = "display the task with the Task ID: " + taskToRetrieveID;		
	}

	private void createResultObject(boolean isExitCommand, boolean isSuccess, int displayType, 
			String printOut, Task taskRetrieved, Vector<Task> tasks, Vector<String> history) {
		executedResult = new Result(isExitCommand, isSuccess, displayType, printOut, taskRetrieved, tasks, history);
	}
	
	// there is no such task record in the storage to display
	private void createUnsuccessfulResultObject() {
		String notificationStr = String.format(UNSUCCESS_DISPLAY_FEEDBACK, taskToRetrieveID);
		executedResult = new Result(false, false, Result.NOTIFICATION_DISPLAY, notificationStr, null, null);
	}

	private void retrieveTheTask() throws Exception {
		if(taskList == null || (taskList.size() < displayID) || (displayID < 1)) {
			throw new Exception("The task to display does not exist in the display list.");
		} else {
			taskRetrieved = taskList.get(displayID);
		} 
	}

	public int getTaskID() {
		return taskToRetrieveID;
	}
}
