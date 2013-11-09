package nailit.logic.command;

//@author A0105789R

import java.util.Vector;

import nailit.common.Result;
import nailit.common.Task;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.storage.FileCorruptionException;
import nailit.storage.NoTaskFoundException;
import nailit.storage.StorageManager;

public class CommandDelete extends Command{
	
	// static final variables
	private static final String SUCCESS_MSG = "Task: %1s has been successfully deleted.";
	
	private static final String FEEDBACK_FOR_NOT_EXISTING_TASK = "Task [ID %1d] not found. " +
																"Cannot delete non-existant task.";
	
	private static final String FEEDBACK_FOR_NOT_EXISTING_TASK_IN_TASK_LIST = "Task [ID %1d] does not " +
																			"exist in the current task list. " +
																			"Cannot delete non-existant task.";
	
	private static final String COMMAND_SUMMARY_FOR_DELETING_TASK_NOT_EXISTING_IN_STORAGE = "This is a delete command, " +
																							"but the to-delete task does not " +
																							"exist in the storage.";
	
	private static final String COMMAND_SUMMARY_FOR_DELETING_TASK_NOT_EXISTING_IN_TASK_LIST = "This is a delete command, but " +
																							"the to-delete task does not exist in " +
	
																							"the task list.";
	
	// private fields
	private Task taskToRemove;
	private boolean isDeleteSuccessfully;
	private int taskToDeleteDisplayID;
	
	// task list, in which the task is to delete
	private Vector<Task> taskList;
	
	// constructor
	public CommandDelete(ParserResult resultInstance, StorageManager storerToUse, Vector<Task> taskList) {
		super(resultInstance, storerToUse);
		commandType = CommandType.DELETE;
		this.taskList = taskList;
		isUndoSuccess = false;
		isRedoSuccess = false;
	}

	@Override
	public Result executeCommand() throws Exception {
		taskToDeleteDisplayID = getTaskDisplayID();
		try {
			if (isExistToDeleteTaskInTaskList()) {
				removeTheTaskOnStorage(); // inside the method, taskToRemove is defined
			} else {
				isDeleteSuccessfully = false;
				createResultObjectForTaskToDeleteNotExistingInTaskList();
				createCommandSummaryForDeletingNotExistingTaskInTaskList();
				return executedResult;
			}
		} catch (Exception e) {
			isDeleteSuccessfully = false;
			createResultObjectForNotExistingTask();
			createCommandSummaryForDeletingNotExistingTask();
			return executedResult;
		}
		isDeleteSuccessfully = true;
		createResultObject();
		createCommandSummary();
		return executedResult;
	}

	public int getTaskDisplayID() {
		// currently displayID is still taskID
		int displayID = parserResultInstance.getTaskID();
		return displayID;
	}

	private boolean isExistToDeleteTaskInTaskList() {
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

	private void createResultObjectForTaskToDeleteNotExistingInTaskList() {
		String notificationStr = String.format(FEEDBACK_FOR_NOT_EXISTING_TASK_IN_TASK_LIST, taskToDeleteDisplayID);
		executedResult = new Result(false, false, Result.NOTIFICATION_DISPLAY, notificationStr);
	}

	private void createResultObjectForNotExistingTask() {
		String notificationStr = String.format(FEEDBACK_FOR_NOT_EXISTING_TASK, taskToDeleteDisplayID);
		executedResult = new Result(false, false, Result.NOTIFICATION_DISPLAY, notificationStr);
	}

	private void createCommandSummaryForDeletingNotExistingTask() {
		commandSummary = COMMAND_SUMMARY_FOR_DELETING_TASK_NOT_EXISTING_IN_STORAGE;		
	}
	
	private void createCommandSummaryForDeletingNotExistingTaskInTaskList() {
		commandSummary = COMMAND_SUMMARY_FOR_DELETING_TASK_NOT_EXISTING_IN_TASK_LIST;		
	}

	private void createResultObject() {
		String notificationStr = String.format(SUCCESS_MSG, taskToRemove.getName());
		executedResult = new Result(false, true, Result.EXECUTION_RESULT_DISPLAY, notificationStr, taskToRemove, null, null);	
		executedResult.setDeleteStatus(true);
	}

	private void createCommandSummary() {
		
		commandSummary = "delete: ";
		if(taskToRemove.getName() != null) {
			commandSummary = commandSummary + taskToRemove.toString();
		} 
	}

	private void removeTheTaskOnStorage() throws NoTaskFoundException,
			FileCorruptionException {
	
		taskId = retrieveTheTaskID();
		storer.remove(taskId, false);
	}

	private int retrieveTheTaskID() {
		// retrieve the task from the taskList according to display ID
		// then remove it from storage according to its task ID
		taskToRemove = taskList.get(taskToDeleteDisplayID - 1);
		return taskToRemove.getID();
	}

	public int getTaskId() {
		return taskId;
	}

	public boolean deleteSuccess() {
		return isDeleteSuccessfully;
	}

	public CommandType getCommandType() {
		return commandType;
	}
	
	public Task getTaskDeleted() {
		if(this.deleteSuccess()) {
			return taskToRemove;
		} else {
			return null;
		}
	}

	@Override
	public void undo() {
		if(this.deleteSuccess()) { // in fact cannot happen
			storer.add(taskToRemove);
			isUndoSuccess = true;
			this.isRedoSuccess = false;
		} else {
			isUndoSuccess = false;
		}
	}

	@Override
	public boolean isUndoSuccessfully() {
		return isUndoSuccess;
	}

	@Override
	public String getCommandString() {
		return commandSummary;
	}

	@Override
	public void redo() {
		try {
			storer.remove(taskId, false);
			this.isRedoSuccess = true;
			this.isUndoSuccess = false;
		} catch (NoTaskFoundException e) {
			isRedoSuccess = false;
		}
		
	}

	@Override
	public boolean isRedoSuccessfully() {
		return isRedoSuccess;
	}

	@Override
	public Task getTaskRelated() {
		return taskToRemove;
	}
}
