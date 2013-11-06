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
	private Task taskToRemove;
	private boolean deleteSuccessfully;
	
	private int taskToDeleteDisplayID;
	
	// task list, in which the task is to delete
	private Vector<Task> taskList;
	

	private static final String SUCCESS_MSG = "Task: %1s has been successfully deleted.";
	private static final String FEEDBACK_FOR_NOT_EXISTING_TASK = "Task [ID %1d] not found. Cannot delete non-existant task.";
	private static final String FEEDBACK_FOR_NOT_EXISTING_TASK_IN_TASK_LIST = "Task [ID %1d] does not exist in the current task list. Cannot delete non-existant task."; 
	private static final String EXCEPTION_MESSAGE_FOR_DISPLAY_ID_IS_NULL = "Display ID in the parserResult instance is null."; 
	private static final String COMMAND_SUMMARY_FOR_DELETING_TASK_NOT_EXISTING_IN_STORAGE = "This is a delete command, but the to-delete task does not exist in the storage.";;
	private static final String COMMAND_SUMMARY_FOR_DELETING_TASK_NOT_EXISTING_IN_TASK_LIST = "This is a delete command, but the to-delete task does not exist in the task list."; 

	
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
		// if displayID is TASKID_NULL, throw exception
		if(taskToDeleteDisplayID == 0) { // 0 means no display ID but the original one. later need change
			throw new Exception(EXCEPTION_MESSAGE_FOR_DISPLAY_ID_IS_NULL);
		} else {
			try {
				if(isExistToDeleteTaskInTaskList()) {
					removeTheTaskOnStorage(); // inside the method, taskToRemove is defined
				} else {
					deleteSuccessfully = false;
					createResultObjectForTaskToDeleteNotExistingInTaskList();
					createCommandSummaryForDeletingNotExistingTaskInTaskList();
					return executedResult;
				}
				
			} catch(Exception e) {
				deleteSuccessfully = false;
				createResultObjectForNotExistingTask();
				createCommandSummaryForDeletingNotExistingTask();
				return executedResult;
			}
			deleteSuccessfully = true;
			createResultObject();
			createCommandSummary();
			return executedResult;
		}
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
			commandSummary = commandSummary + taskToRemove.getName() + "\n";
		} 
		
//		if(taskToRemove.getStartTime() != null) {
//			commandSummary = commandSummary + " Start time: " + 
//		taskToRemove.getStartTime().toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT) + "\n";
//		} 
//		
//		if(taskToRemove.getEndTime() != null) {
//			commandSummary = commandSummary + " End time: " + 
//		taskToRemove.getEndTime().toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT) + "\n";
//		} 
		
//		if(taskToRemove.getTag() != null) {
//			commandSummary = commandSummary + " Tag: " + taskToRemove.getTag() + "\n";
//		} 
//		
//		if(taskToRemove.getPriority() != null) {
//			commandSummary = commandSummary + " Priority: " + taskToRemove.getPriority() + "\n";
//		} 
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
		return deleteSuccessfully;
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
	
	
}
