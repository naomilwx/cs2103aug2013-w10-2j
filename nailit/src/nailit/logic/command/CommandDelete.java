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
	private boolean deleteSuccessfully;
	
	private int taskToDeleteDisplayID;
	
	// task list, in which the task is to delete
	private Vector<Task> taskList;

	private static final String SUCCESS_MSG = "Task: [ID %1s]has been successfully deleted.";
	private static final String FEEDBACK_FOR_NOT_EXISTING_TASK = "Task [ID %1d] not found. Cannot delete non-existant task.";
	private static final String FEEDBACK_FOR_NOT_EXISTING_TASK_IN_TASK_LIST = "Task [ID %1d] does not exist in the current task list. Cannot delete non-existant task."; 
	private static final String EXCEPTION_MESSAGE_FOR_DISPLAY_ID_IS_NULL = "Display ID in the parserResult instance is null."; 
	private static final String COMMAND_SUMMARY_FOR_DELETING_TASK_NOT_EXISTING_IN_STORAGE = "This is a delete command, but the to-delete task does not exist in the storage.";;
	private static final String COMMAND_SUMMARY_FOR_DELETING_TASK_NOT_EXISTING_IN_TASK_LIST = "This is a delete command, but the to-delete task does not exist in the task list."; 

	
	public CommandDelete(ParserResult resultInstance, StorageManager storerToUse, Vector<Task> taskList) {
		super(resultInstance, storerToUse);
		commandType = "delete";
		this.taskList = taskList;
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
					removeTheTaskOnStorage();
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

	private void removeTheTaskOnStorage() throws NoTaskFoundException,
			FileCorruptionException {
	
		taskToDeleteID = retrieveTheTaskID();
		storer.remove(taskToDeleteID);
	}

	private int retrieveTheTaskID() {
		// retrieve the task from the taskList according to display ID
		// then remove it from storage according to its task ID
		taskToRemove = taskList.get(taskToDeleteDisplayID - 1);
		return taskToRemove.getID();
	}

	public int getTaskID() {
		return taskToDeleteID;
	}

	public boolean deleteSuccess() {
		return deleteSuccessfully;
	}


	
	
}
