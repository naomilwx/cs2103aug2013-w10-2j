package nailit.logic.command;

import java.util.Vector;

import nailit.common.NIConstants;
import nailit.common.Result;
import nailit.common.Task;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

public class CommandMarkCompletedOrUncompleted extends Command{
	
	private static final String NOTIFICATION_STRING_FOR_MARK_COMPLETED_SUCCESS = "The task: %1s has been successfully marked as completed.";
	private static final String NOTIFICATION_STRING_FOR_MARK_UNCOMPLETED_SUCCESS = "The task: %1s has been successfully marked as uncompleted.";

	private static final String NOTIFICATION_STRING_FOR_MARK_COMPLETED_UNSUCCESS = "The marking operation fails. This may be due to: the task " +
																					"does not exist in the display task list; the task does not exist " +
																					"in the storage.";

	// isCompleted marks whether the task related has already been completed
	// this helps the undo and redo double check
	private boolean isSuccess;
	
	// the taskID of the task related
	private int taskID;
	
	private Result executedResult;
	
	private Vector<Task> taskList;
	
	private int displayID;
	
	private Task taskRelated;
	
	private String commandSummary;
	
	private boolean undoSuccess;
	
	private boolean redoSuccess;
	
	private boolean isCommandMarkAsCompleted;
	
	private CommandType CommandType;

	public CommandMarkCompletedOrUncompleted(ParserResult resultInstance,
			StorageManager storerToUse, Vector<Task> currentTaskList, boolean isMarkAsCompleted) {
		super(resultInstance, storerToUse);
		isSuccess = false;
		executedResult = new Result();
		taskList = currentTaskList;
		taskRelated = new Task();
		commandSummary = "";
		undoSuccess = false;
		redoSuccess = false;
		isCommandMarkAsCompleted = isMarkAsCompleted;
		if(isCommandMarkAsCompleted) {
			CommandType = CommandType.COMPLETE;
		} else {
			CommandType = CommandType.UNCOMPLETE;
		}
	}

	@Override
	public Result executeCommand() throws Exception {
		if(isValidDisplayID()) {
			setTaskRelated();
			setTaskID();
			markAsCompletedOrUncompleted();
			createExecutedResult();
			createCommandSummary(); // only need to create command summary for success
		} else {
			createResultForFailure(); 
		}
		return executedResult;
	}

	private void createCommandSummary() {
		if(isCommandMarkAsCompleted) {
			commandSummary = "mark completed";
			if(taskRelated.getName() != null) {
				commandSummary = commandSummary + " Name: " + taskRelated.getName() + "\n";
			} 
			
			if(taskRelated.getStartTime() != null) {
				commandSummary = commandSummary + " Start time: " + 
						taskRelated.getStartTime().toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT) + "\n";
			} 
			
			if(taskRelated.getEndTime() != null) {
				commandSummary = commandSummary + " End time: " + 
						taskRelated.getEndTime().toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT) + "\n";
			} 
		} else {
			commandSummary = "mark uncompleted";
			if(taskRelated.getName() != null) {
				commandSummary = commandSummary + " Name: " + taskRelated.getName() + "\n";
			} 
			
			if(taskRelated.getStartTime() != null) {
				commandSummary = commandSummary + " Start time: " + 
						taskRelated.getStartTime().toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT) + "\n";
			} 
			
			if(taskRelated.getEndTime() != null) {
				commandSummary = commandSummary + " End time: " + 
						taskRelated.getEndTime().toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT) + "\n";
			} 
		}
	}

	private void createResultForFailure() {
		String notificationStr = NOTIFICATION_STRING_FOR_MARK_COMPLETED_UNSUCCESS;
		executedResult = new Result(false, true, Result.EXECUTION_RESULT_DISPLAY, notificationStr);
	}

	private void setTaskRelated() {
		taskRelated = taskList.get(displayID - 1);
	}

	private boolean isValidDisplayID() {
		int displayID = parserResultInstance.getTaskID();
		int size = taskList.size();
		if((size == 0) || (displayID < 1) || (displayID > size) ) {
			return false;
		} else {
			return true;
		}
	}

	private void markAsCompletedOrUncompleted() {
		if(isCommandMarkAsCompleted) {
			taskRelated.setCompleted(true);
			storer.add(taskRelated);
			isSuccess = true;
		} else {
			taskRelated.setCompleted(false);
			storer.add(taskRelated);
			isSuccess = true;
		}
	}

	private void createExecutedResult() {
		String notificationStr;
		if(isCommandMarkAsCompleted) {
			notificationStr = String.format(NOTIFICATION_STRING_FOR_MARK_COMPLETED_SUCCESS, taskRelated.getName());
		} else {
			notificationStr = String.format(NOTIFICATION_STRING_FOR_MARK_UNCOMPLETED_SUCCESS, taskRelated.getName());
		}
		executedResult = new Result(false, true, Result.EXECUTION_RESULT_DISPLAY, notificationStr);
	}

	private void setTaskID() {
		taskID = taskRelated.getID();
	}

	@Override
	public int getTaskID() {
		return taskID;
	}

	@Override
	public CommandType getCommandType() {
		return CommandType;
	}

	@Override
	public void undo() { // I need to set the isComplete field here, because when undo, will be used directly
		if(isCommandMarkAsCompleted) { // undo the marking as completed operation
			taskRelated.setCompleted(false);
			storer.add(taskRelated);
		} else { // undo the marking as uncompleted operation
			taskRelated.setCompleted(true);
			storer.add(taskRelated);
		}
		redoSuccess = false;
		undoSuccess = true;
	}

	@Override
	public void redo() {
		if(isCommandMarkAsCompleted) { // undo the marking as completed operation
			taskRelated.setCompleted(true);
			storer.add(taskRelated);
		} else { // undo the marking as uncompleted operation
			taskRelated.setCompleted(false);
			storer.add(taskRelated);
		}
		redoSuccess = true;
		undoSuccess = false;
	}

	@Override
	public boolean undoSuccessfully() {
		return undoSuccess;
	}

	@Override
	public boolean isSuccessRedo() {
		return redoSuccess;
	}

	@Override
	public String getCommandString() {
		return commandSummary;
	}
	
	public boolean isSuccess() {
		return isSuccess;
	}

	public int getDisplayID() {
		return displayID;
	}
	
	public Task getTaskRelated() {
		return taskRelated;
	}

}