package nailit.logic.command;

import java.util.Vector;

import test.storage.StorageStub;
import nailit.common.Result;
import nailit.common.Task;
import nailit.logic.ParserResult;
import nailit.storage.FileCorruptionException;
import nailit.storage.NoTaskFoundException;
import nailit.storage.StorageManager;

public class CommandDelete extends Command{
	private String commandType;
	private String commandSummary;
	private Result executedResult;
	private Task taskToRemove;
	private int taskToDeleteID;
	
	// task list, in which the task is to delete
	private Vector<Task> taskList;

	private static final String SUCCESS_MSG = "Task: [ID %1s]has been successfully deleted.";
	private static final String FEEDBACK_FOR_NOT_EXISTING_TASK = "Task [ID %1d] not found. Cannot delete non-existant task.";
	private static final String FEEDBACK_FOR_NOT_EXISTING_TASK_IN_TASK_LIST = "Task [ID %1d] does not exist in the current task list. Cannot delete non-existant task."; 

	
	public CommandDelete(ParserResult resultInstance, StorageManager storerToUse, Vector<Task> taskList) {
		super(resultInstance, storerToUse);
		commandType = "delete";
		this.taskList = taskList;
	}

	@Override
	public Result executeCommand() {
		int taskToDeleteDisplayID = getTaskDisplayID();
		try {
			if(isExistToDeleteTaskInTaskList(taskToDeleteDisplayID)) {
				removeTheTaskOnStorage(taskToDeleteDisplayID);
			} else {
				return createResultObjectForTaskToDeleteNotExistingInTaskList(taskToDeleteDisplayID);
			}
			
		} catch(Exception e) {
			createResultObjectForNotExistingTask(taskToDeleteDisplayID);
			createCommandSummaryForDeletingNotExistingTask();
			return executedResult;
		}
		
		createResultObject(taskToDeleteDisplayID);
		createCommandSummary();
		return executedResult;
	}

	private int getTaskDisplayID() {
		// currently displayID is still taskID
		int displayID = parserResultInstance.getTaskID();
		return displayID;
	}

	private boolean isExistToDeleteTaskInTaskList(int taskToDeleteDisplayID) {
		if(taskList == null) {
			return false;
		} else if(taskList.size() < taskToDeleteDisplayID) {
			return false;
		} else if(taskToDeleteDisplayID < 1) {
			return false;
		} else {
			return true;
		}
	}

	private Result createResultObjectForTaskToDeleteNotExistingInTaskList(int taskToDeleteDisplayID) {
		String notificationStr = String.format(FEEDBACK_FOR_NOT_EXISTING_TASK_IN_TASK_LIST, taskToDeleteDisplayID);
		executedResult = new Result(false, true, Result.NOTIFICATION_DISPLAY, notificationStr);
		return executedResult;
	}

	private void createResultObjectForNotExistingTask(int taskToDeleteDisplayID) {
		String notificationStr = String.format(FEEDBACK_FOR_NOT_EXISTING_TASK, taskToDeleteDisplayID);
		executedResult = new Result(false, true, Result.NOTIFICATION_DISPLAY, notificationStr);
	}

	private void createCommandSummaryForDeletingNotExistingTask() {
		commandSummary = "This is a delete command, but the to-delete task does not exist in the storage.";		
	}

	private void createResultObject(int taskToDeleteDisplayID) {
		String notificationStr = String.format(SUCCESS_MSG, taskToDeleteDisplayID);
		executedResult = new Result(false, true, Result.NOTIFICATION_DISPLAY, notificationStr, taskToRemove, null, null);		
	}

	private void createCommandSummary() {
		commandSummary = "delete " + taskToRemove.getName() + " " 
				+ taskToRemove.getStartTime() + " - " 
				+ taskToRemove.getEndTime() + " " 
				+ taskToRemove.getTag() 
				+ taskToRemove.getPriority();
	}

	private void removeTheTaskOnStorage(int taskToDeleteDisplayID) throws NoTaskFoundException,
			FileCorruptionException {
	
		taskToDeleteID = retrieveTheTaskID(taskToDeleteDisplayID);
		storer.remove(taskToDeleteID);
	}

	private int retrieveTheTaskID(int taskToDeleteDisplayID) {
		taskToRemove = taskList.get(taskToDeleteDisplayID - 1);
		return taskToRemove.getID();
	}

	public int getTaskID() {
		return taskToDeleteID;
	}

	
	
}
