package nailit.logic.command;

//@author A0105789R

import java.util.Vector;
import org.joda.time.DateTime;
import nailit.common.CommandType;
import nailit.common.Result;
import nailit.common.Task;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

public class CommandMarkCompletedOrUncompleted extends Command{
	// static final fields
	private static final String NOTIFICATION_STRING_FOR_MARK_COMPLETED_SUCCESS = "Marked %1s " +
																				"as completed.";
	
	private static final String NOTIFICATION_STRING_FOR_MARK_UNCOMPLETED_SUCCESS = "Marked %1s " +
																				"as uncompleted.";

	private static final String NOTIFICATION_STRING_FOR_MARK_COMPLETED_UNSUCCESS = "The marking operation " +
																					"fails. This may be due to: the task " +
																					"does not exist in the display task list; " +
																					"the task does not exist " +
																					"in the storage.";
	
	private static final String NOTIFICATION_STRING_FOR_UNCOMPLETE_NOT_COMPLETED_TASK = "Task is already marked as uncompleted.";
	
	private static final String NOTIFICATION_STRING_FOR_COMPLETE_COMPLETED_TASK = "Task is already marked as completed.";

	// private fields
	// isCompleted marks whether the task related has already been completed
	// this helps the undo and redo double check
	private boolean isSuccess;
	
	// the taskID of the task related
	private int taskID;
	
	
	private Vector<Task> taskList;
	private int displayID;
	private Task taskRelated;
	private boolean isCommandMarkAsCompleted;
	
	// the description, used to future undo and redo
	private DateTime taskReminderDate;

	// constructor
	public CommandMarkCompletedOrUncompleted(ParserResult resultInstance,
			StorageManager storerToUse, Vector<Task> currentTaskList, boolean isMarkAsCompleted) {
		super(resultInstance, storerToUse);
		isSuccess = false;
		executedResult = new Result();
		taskList = currentTaskList;
		taskRelated = new Task();
		commandSummary = "";
		isUndoSuccess = false;
		isRedoSuccess = false;
		isCommandMarkAsCompleted = isMarkAsCompleted;
		if(isCommandMarkAsCompleted) {
			commandType = CommandType.COMPLETE;
		} else {
			commandType = CommandType.UNCOMPLETE;
		}
		taskReminderDate = null;
	}

	@Override
	public Result executeCommand() throws Exception {
		if(isValidDisplayID()) { // also set the displayID
			setTaskRelated(); // also set the task description
			setTaskID();
			if(isInvalidCommandComplete()) {
				createResultForCompleteCompletedTask();
			} else if(isInvalidCommandUncomplete()) {
				createResultForUncompleteNotCompletedTask();
			} else {
				markAsCompletedOrUncompleted();
				createExecutedResult();
				createCommandSummary(); // only need to create command summary for success
			}
		} else {
			createResultForFailure(); 
		}
		return executedResult;
	}

	private void createResultForUncompleteNotCompletedTask() {
		String notificationStr = NOTIFICATION_STRING_FOR_UNCOMPLETE_NOT_COMPLETED_TASK;
		executedResult = new Result(false, false, Result.EXECUTION_RESULT_DISPLAY, notificationStr);
	}

	private void createResultForCompleteCompletedTask() {
		String notificationStr = NOTIFICATION_STRING_FOR_COMPLETE_COMPLETED_TASK;
		executedResult = new Result(false, false, Result.EXECUTION_RESULT_DISPLAY, notificationStr);
	}

	private boolean isInvalidCommandUncomplete() {
		if(isCommandMarkAsCompleted) {
			return false;
		} else if(!taskRelated.isCompleted()) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isInvalidCommandComplete() {
		if(!isCommandMarkAsCompleted) {
			return false;
		} else if(taskRelated.isCompleted()) {
			return true;
		} else {
			return false;
		}
	}

	private void createCommandSummary() {
		if(isCommandMarkAsCompleted) {
			commandSummary = "mark completed: ";
			commandSummary += taskRelated.toString();
		} else {
			commandSummary = "mark uncompleted: ";
			commandSummary += taskRelated.toString();
		}
	}

	private void createResultForFailure() {
		String notificationStr = NOTIFICATION_STRING_FOR_MARK_COMPLETED_UNSUCCESS;
		executedResult = new Result(false, false, Result.EXECUTION_RESULT_DISPLAY, notificationStr);
	}

	private void setTaskRelated() {
		taskRelated = taskList.get(displayID - 1);
		taskReminderDate = taskRelated.getReminder();
	}

	private boolean isValidDisplayID() {
		displayID = parserResultInstance.getTaskID();
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
			// the task does not need reminder date
			taskRelated.setReminder(null);
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
		executedResult.setTaskToDisplay(taskRelated);
	}

	private void setTaskID() {
		taskID = taskRelated.getID();
	}

	@Override
	public int getTaskId() {
		return taskID;
	}

	@Override
	public CommandType getCommandType() {
		return commandType;
	}

	@Override
	public void undo() { // I need to set the isComplete field here, because when undo, will be used directly
		if(isCommandMarkAsCompleted) { // undo the marking as completed operation
			taskRelated.setCompleted(false);
			// add the reminder date back if has before
			taskRelated.setReminder(taskReminderDate);
			storer.add(taskRelated);
		} else { // undo the marking as uncompleted operation
			taskRelated.setCompleted(true);
			taskRelated.setReminder(null);
			storer.add(taskRelated);
		}
		isRedoSuccess = false;
		isUndoSuccess = true;
	}

	@Override
	public void redo() {
		if(isCommandMarkAsCompleted) { // undo the marking as completed operation
			taskRelated.setCompleted(true);
			taskRelated.setReminder(null); // the reminder is gone also
			storer.add(taskRelated);
		} else { // undo the marking as uncompleted operation
			taskRelated.setCompleted(false);
			taskRelated.setReminder(taskReminderDate);
			storer.add(taskRelated);
		}
		isRedoSuccess = true;
		isUndoSuccess = false;
	}

	@Override
	public boolean isUndoSuccessfully() {
		return isUndoSuccess;
	}

	@Override
	public boolean isRedoSuccessfully() {
		return isRedoSuccess;
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
	
	public boolean hasReminderDate() {
		return taskReminderDate != null;
	}

	public DateTime getReminderDate() {
		return taskReminderDate;
	}
}
