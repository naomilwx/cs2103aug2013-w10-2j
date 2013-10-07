package nailit.logic.command;

import test.storage.StorageStub;
import nailit.common.Result;
import nailit.common.Task;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

public class CommandDelete extends Command{
	private String commandType;
	private String commandSummary;
	private Result executedResult;
	private Task taskToRemove;
	private int taskToDeleteID;

	private final String Success_Msg = "The task is deleted successfully, the Task ID for it is: ";
	private final String FeedbackForNotExistingTask = "The to-delete task does not exist in the storage."; 
	
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
		executedResult = new Result(false, true, Result.NOTIFICATION_DISPLAY, FeedbackForNotExistingTask);
	}

	private void createCommandSummaryForDeletingNotExistingTask() {
		commandSummary = "This is a delete command, but the to-delete task does not exist in the storage.";		
	}

	private void createResultObject() {
		executedResult = new Result(false, true, Result.NOTIFICATION_DISPLAY, Success_Msg + taskToDeleteID);		
	}

	private void createCommandSummary() {
		commandSummary = "delete " + taskToRemove.getName() + " " 
				+ taskToRemove.getStartTime() + " - " 
				+ taskToRemove.getEndTime() + " " 
				+ taskToRemove.getTag() 
				+ taskToRemove.getPriority();
	}

	private void removeTheTaskOnStorage() {
		try {
			taskToDeleteID = parserResultInstance.getTaskID();
			taskToRemove = storer.remove(taskToDeleteID);
		} catch (Exception e) {
			// no need to remove, since already not there.
		}
		
	}
	
	public int getTaskID() {
		return taskToDeleteID;
	}
	
}
