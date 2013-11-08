package nailit.logic.command;

//@author A0105789R

import java.util.Iterator;
import java.util.Vector;
import org.joda.time.DateTime;
import nailit.common.FilterObject;
import nailit.common.NIConstants;
import nailit.common.Result;
import nailit.common.Task;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

public class CommandDisplay extends Command{
	private Task taskRetrieved;
	
	private Vector<Task> taskList;
	
	private int displayId;
	
	// the CommandManager instance that the CommandDisplay 
	// instance belongs to, since it may display the operations
	// history
	private CommandManager cm;
	
	private static final String UNSUCCESS_DISPLAY_FEEDBACK = "Sorry, there is no task with display ID as %d in the list";
	
	private static final String FEEDBACK_FOR_UNSUCCESSFUL_DISPLAY_ALL = "Sorry, the system fails " +
																		"to retrieve all the tasks in " +
																		"the storage. Please try again.";
	
	private static final String NO_DISPLAY_ID_WARNING = "The parserResult does not have DisplayID.";
	
	private static final String NO_TASKS_FOUND_MSG = "There are no tasks to display!";
	
	private static final String TASK_TO_DISPLAY_NOT_EXIST_ON_TASK_LIST = "The task does " +
																		"not exist in the display list.";
	
	public CommandDisplay(ParserResult resultInstance,
			StorageManager storerToUse, CommandManager cm) {
		super(resultInstance, storerToUse);
		this.cm = cm;
		commandType = CommandType.DISPLAY;
		taskList = cm.getCurrentTaskList();
		commandSummary = "Display operation";
	}

	@Override
	public Result executeCommand() throws Exception {
		if(parserResultInstance.isDisplayAll()) {
			displayAllTasks();
		} else if(parserResultInstance.isDisplayHistory()) {
			displayOperationsHistory();
		} else if(parserResultInstance.isDisplayComplete()) { 
			displayCompletedTasks();
		} else if(parserResultInstance.isDisplayUncomplete()) {
			displayUncompletedTasks();
		} else if(isDisplayDay()) {
			displayTasksOnTheDay();
		} else {
			displayTheTask();
		}
		return executedResult;
	}

	private void displayTasksOnTheDay() {
		DateTime chosenDay = parserResultInstance.getEndTime();
		Vector<Task> tasksOnTheDay = cm.getTasksHappeningOnDay(chosenDay);
		executedResult = new Result(false, true, Result.LIST_DISPLAY, Result.EMPTY_DISPLAY, null, tasksOnTheDay, null);
		cm.setCurrentList(tasksOnTheDay);
	}

	private boolean isDisplayDay() {
		return !parserResultInstance.isNullEndTime();
	}

	private void displayUncompletedTasks() {
		FilterObject filterObjectForUncompletedTasks = new FilterObject(null, null, null, null, null, false);
		Vector<Task> uncompletedTasks = storer.filter(filterObjectForUncompletedTasks);
		executedResult = new Result(false, true, Result.LIST_DISPLAY, Result.EMPTY_DISPLAY, null, uncompletedTasks, null);
		cm.setCurrentList(uncompletedTasks);
	}

	private void displayCompletedTasks() {
		FilterObject filterObjectForCompletedTasks = new FilterObject(null, null, null, null, null, true);
		Vector<Task> completedTasks = storer.filter(filterObjectForCompletedTasks);
		executedResult = new Result(false, true, Result.LIST_DISPLAY, Result.EMPTY_DISPLAY, null, completedTasks, null);
		cm.setCurrentList(completedTasks);
	}
	
	private void displayAllTasks() {
		try {
			Vector<Task> vectorOfTasks = this.retrieveAllTheTasks();
			// if the retrieved content is empty or null, return notification instead
			if(vectorOfTasks == null || vectorOfTasks.isEmpty()) {
				createResultObject(false, false, Result.LIST_DISPLAY, NO_TASKS_FOUND_MSG, null, new Vector<Task>(), null);
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

	private void displayTheTask() throws Exception {
		getDisplayID();
		if(displayId == 0) { // currently, 0 means no display ID, needs changes later
			throw new Exception(NO_DISPLAY_ID_WARNING);
		} else {
			try {
				// let retrievedTask = the task to display, which is gotten from task list
				retrieveTheTask(); 
				createResultObject(false, true, Result.TASK_DISPLAY, Result.EMPTY_DISPLAY, 
						taskRetrieved, cm.getCurrentTaskList(), null);
//				executedResult.setd
				createCommandSummary();
			} catch(Exception e) {
				createUnsuccessfulResultObject();
			}
		}
	}

	private void getDisplayID() {
		// currently, displayID is still taskID
		displayId = parserResultInstance.getTaskID();
	}

	private void displayOperationsHistory() {
		createResultForDisplayOperationshistory();
	}

	private void createResultForDisplayOperationshistory() {
		Vector<String> undoableCommandStringList = getCommandString(cm.getOperationsHistory());
		Vector<String> redoableCommandStringList = getCommandString(cm.getRedoableCommandList());
		Vector<Vector<String>> twoCommandStringList = new Vector<Vector<String>>();
		twoCommandStringList.add(NIConstants.HISTORY_UNDO_INDEX, undoableCommandStringList); // undoable list is the first
		twoCommandStringList.add(NIConstants.HISTORY_REDO_INDEX, redoableCommandStringList); // redoable list is the second
		executedResult = new Result(false, true, Result.HISTORY_DISPLAY, "", null, null, twoCommandStringList);
		
	}

	private Vector<String> getCommandString(Vector<Command> commandList) {
		Vector<String> reversedCommandStringList = new Vector<String>();
		Iterator<Command> itr = commandList.iterator();
		
		while(itr.hasNext()) {
			reversedCommandStringList.add(itr.next().getCommandString());
		}
		
		Vector<String> commandStringList  = new Vector<String>();
		int size = reversedCommandStringList.size();
		for(int i = 0; i < size; i++) {
			commandStringList.add(reversedCommandStringList.get(size-i-1));
		}
		
		return commandStringList;
	}

	private Vector<Task> retrieveAllTheTasks() {
		return storer.retrieveAll();
		
	}

	private void createCommandSummary() {
		commandSummary = "display the task with the Task ID: " + displayId;		
	}

	private void createResultObject(boolean isExitCommand, boolean isSuccess, int displayType, 
			String printOut, Task taskRetrieved, Vector<Task> tasks, Vector<Vector<String>> history) {
		executedResult = new Result(isExitCommand, isSuccess, displayType, printOut, taskRetrieved, tasks, history);
	}
	
	// there is no such task record in the storage to display
	private void createUnsuccessfulResultObject() {
		String notifiStr = String.format(UNSUCCESS_DISPLAY_FEEDBACK, displayId);
		executedResult = new Result(false, false, Result.NOTIFICATION_DISPLAY, notifiStr, null, null);
	}

	private void retrieveTheTask() throws Exception {
		if(taskList == null || (taskList.size() < displayId) || (displayId < 1)) {
			throw new Exception(TASK_TO_DISPLAY_NOT_EXIST_ON_TASK_LIST);
		} else {
			taskRetrieved = taskList.get(displayId - 1);
		} 
	}

	public int getTaskId() {
		return taskId;
	}
	
	public CommandType getCommandType() {
		return commandType;
	}

	@Override
	public void undo() {
		// nothing
	}

	@Override
	public boolean isUndoSuccessfully() {
		// nothing to do
		return false;
	}

	@Override
	public String getCommandString() {
		return commandSummary;
	}

	@Override
	public void redo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isRedoSuccessfully() {
		// TODO Auto-generated method stub
		return false;
	}
}
