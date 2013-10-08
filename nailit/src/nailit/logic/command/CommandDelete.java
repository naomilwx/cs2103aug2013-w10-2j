package nailit.logic.command;

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

	private static final String SUCCESS_MSG = "Task: [ID %1s]has been successfully deleted.";
	private static final String FEEDBACK_FOR_NOT_EXISTING_TASK = "Task [ID %1d] not found. Cannot delete non-existant task."; 
	
	public CommandDelete(ParserResult resultInstance, StorageManager storerToUse) {
		super(resultInstance, storerToUse);
		commandType = "delete";
	}

	@Override
	public Result executeCommand() {
		try {
			removeTheTaskOnStorage();
		} catch(Exception e) {
			createResultObjectForNotExistingTask();
			createCommandSummaryForDeletingNotExistingTask();
			return executedResult;
		}
		
		createResultObject();
		createCommandSummary();
		return executedResult;
	}

	private void createResultObjectForNotExistingTask() {
		String notificationStr = String.format(FEEDBACK_FOR_NOT_EXISTING_TASK, taskToDeleteID);
		executedResult = new Result(false, true, Result.NOTIFICATION_DISPLAY, notificationStr);
	}

	private void createCommandSummaryForDeletingNotExistingTask() {
		commandSummary = "This is a delete command, but the to-delete task does not exist in the storage.";		
	}

	private void createResultObject() {
		String notificationStr = String.format(SUCCESS_MSG, taskToDeleteID);
		executedResult = new Result(false, true, Result.NOTIFICATION_DISPLAY, notificationStr);		
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

		taskToDeleteID = parserResultInstance.getTaskID();
		taskToRemove = storer.remove(taskToDeleteID);
	}

	public int getTaskID() {
		return taskToDeleteID;
	}
	
}
