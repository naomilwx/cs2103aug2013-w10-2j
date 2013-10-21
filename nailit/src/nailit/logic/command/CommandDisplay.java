package nailit.logic.command;

import java.util.Vector;


import nailit.common.FilterObject;
import nailit.common.Result;
import nailit.common.Task;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

public class CommandDisplay extends Command{
	private CommandType commandType;
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
	private FilterObject currentFilterObj;
	
	private static final String UNSUCCESS_DISPLAY_FEEDBACK = "Sorry, task [ID: %1d] cannot be found in the task list. Please check and try again.";
	private static final String FEEDBACK_FOR_UNSUCCESSFUL_DISPLAY_ALL = "Sorry, the system fails to retrieve all the tasks in the storage. Please try again.";
	private static final String NO_DISPLAY_ID_WARNING = "The parserResult does not have DisplayID.";
	private static final String TASK_TO_DISPLAY_NOT_EXIST_ON_TASK_LIST = "The task to display does not exist in the display list.";
	
	public CommandDisplay(ParserResult resultInstance,
			StorageManager storerToUse, CommandManager cm) {
		super(resultInstance, storerToUse);
		this.cm = cm;
		commandType = CommandType.DISPLAY;
		taskList = cm.getCurrentTaskList();
		commandSummary = "Display operation";
		currentFilterObj = cm.getCurrentFilterObj();
	}

	@Override
	public Result executeCommand() throws Exception {
		if(parserResultInstance.isDisplayAll()) {
			displayAllTasks();
			return executedResult;
		} else if(parserResultInstance.isDisplayHistory()) {
			return displayOperationsHistory();
		} else {
			return displayTheTask();
		}
	}

	private void displayAllTasks() {
		try {
			Vector<Task> vectorOfTasks = this.retrieveAllTheTasks();
			// if the retrieved content is empty or null, return notification instead
			if(vectorOfTasks == null || vectorOfTasks.isEmpty()) {
				
			} else {
				createResultObject(false, true, Result.LIST_DISPLAY, Result.EMPTY_DISPLAY, null, vectorOfTasks, null);
				// since successfully retrieve all the task on the storage
				// update the current task list as the list of all tasks
				// and make the filter as all
				cm.setCurrentList(vectorOfTasks);
				cm.setCurrentFilterSearchAll();
			}
			
		} catch(Exception e) {
			createUnsuccessfulResultObjectForDisplayAll();
		}
	}

	private void createUnsuccessfulResultObjectForDisplayAll() {
		// in case GUI access the taskList 
		Vector<Task> emptyTaskList = new Vector<Task>();
		executedResult = new Result(false, false, Result.NOTIFICATION_DISPLAY, FEEDBACK_FOR_UNSUCCESSFUL_DISPLAY_ALL, null, emptyTaskList, null);
	}

	private Result displayTheTask() throws Exception {
		getDisplayID();
		if(displayID == 0) { // currently, 0 means no display ID, needs changes later
			throw new Exception(NO_DISPLAY_ID_WARNING);
		} else {
			try {
				retrieveTheTask(); // let retrievedTask = the task to display, which is gotten from task list
			} catch(Exception e) {
				createUnsuccessfulResultObject();
				return executedResult;
			}
			
			createResultObject(false, true, Result.TASK_DISPLAY, Result.EMPTY_DISPLAY, taskRetrieved, cm.getCurrentTaskList(), null);
			createCommandSummary();
			return executedResult;
		}
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
		commandSummary = "display the task with the Task ID: " + displayID;		
	}

	private void createResultObject(boolean isExitCommand, boolean isSuccess, int displayType, 
			String printOut, Task taskRetrieved, Vector<Task> tasks, Vector<String> history) {
		executedResult = new Result(isExitCommand, isSuccess, displayType, printOut, taskRetrieved, tasks, history);
	}
	
	// there is no such task record in the storage to display
	private void createUnsuccessfulResultObject() {
		String notificationStr = String.format(UNSUCCESS_DISPLAY_FEEDBACK, displayID);
		executedResult = new Result(false, false, Result.NOTIFICATION_DISPLAY, notificationStr, null, null);
	}

	private void retrieveTheTask() throws Exception {
		if(taskList == null || (taskList.size() < displayID) || (displayID < 1)) {
			throw new Exception(TASK_TO_DISPLAY_NOT_EXIST_ON_TASK_LIST);
		} else {
			taskRetrieved = taskList.get(displayID - 1);
		} 
	}

	public int getTaskID() {
		return taskToRetrieveID;
	}
	
	public CommandType getCommandType() {
		return commandType;
	}

	@Override
	public void undo() {
		// nothing
	}

	@Override
	public boolean undoSuccessfully() {
		// nothing to do
		return false;
	}
}
