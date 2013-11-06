package nailit.logic.command;

//@author A0105789R

import java.util.Vector;
import org.joda.time.DateTime;
import nailit.common.NIConstants;
import nailit.common.Result;
import nailit.common.Task;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

public class CommandDeleteReminder extends Command {

	private static final String REMINDER_ADDED_UNSUCCESSFULLY_FEEDBACK = "Sorry, the reminder is " +
																		"not deleted successfully. " + 
																		"The display ID is invalid";
	
	private static final String NO_REMINDER_TO_DELETE_FEEDBACK = "Sorry, the task you are accessing does " +
																"not have a reminder. No need to delete.";

	private int displayID;


	private Vector<Task> taskList;

	// the reminder date deleted, used for undo and redo
	// it can be null
	private DateTime reminderDateDeleted;

	private Task taskRelated;

	private boolean isSuccess;


	public CommandDeleteReminder(ParserResult resultInstance,
			StorageManager storerToUse, Vector<Task> currentTaskList) {
		super(resultInstance, storerToUse);
		taskList = currentTaskList;
		executedResult = new Result();
		reminderDateDeleted = new DateTime();
		taskRelated = new Task();
		commandSummary = "";
		isSuccess = false;
		isUndoSuccess = false;
		isRedoSuccess = false;
		commandType = CommandType.DELETEREMINDER;
	}

	@Override
	public Result executeCommand() throws Exception {
		if (isValidDisplayID()) { // set displayID here 
			setTaskRelated();
			setTaskID();
			saveReminderDate();
			
			if(taskHasReminder()) {
				deleteReminderAndUpdateStorage();
				createCommandSummary(); 
				createResult();
				isSuccess = true;
			} else {
				createResultForNoReminderToDelete();
			}
		} else {
			createResultForFailure(); 
		}
		return executedResult;
	}

	private void createResultForNoReminderToDelete() {
		executedResult = new Result(false, false, Result.NOTIFICATION_DISPLAY,
				NO_REMINDER_TO_DELETE_FEEDBACK);
		executedResult.setUpdateReminderList(false);
	}

	private void deleteReminderAndUpdateStorage() {
		taskRelated.setReminder(null);
		storer.add(taskRelated);
	}

	private boolean taskHasReminder() {
		return reminderDateDeleted != null;
	}

	// this method is used to store the reminder in case of the 
	// undo operation
	private void saveReminderDate() {
		reminderDateDeleted = taskRelated.getReminder(); 
	}

	private void createResultForFailure() {
		executedResult = new Result(false, false, Result.NOTIFICATION_DISPLAY,
				REMINDER_ADDED_UNSUCCESSFULLY_FEEDBACK);
		executedResult.setUpdateReminderList(false);
	}

	private void createCommandSummary() {
//		commandSummary = "Deleted the reminder: " 
//				+ reminderDateDeleted.toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT)
//				+ "for the Task: "
//				+ taskRelated.getName();
		commandSummary = "Deleted reminder for " + taskRelated.getName();
	}

	private void createResult() {
		String notificationStr = "Delete the reminder: " 
				+ reminderDateDeleted.toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT)
				+ "for the Task: "
				+ taskRelated.getName();
		executedResult = new Result(false, true, Result.NOTIFICATION_DISPLAY,
				notificationStr);
		// create a dateTime obj representing today
		DateTime today = new DateTime();
		if (today.compareTo(reminderDateDeleted) == 0) { // means that the reminder deleted is today, reminder list needs update
			Vector<Task> todayReminderList = storer.getReminderListForToday();
			executedResult.setUpdateReminderList(true);
			executedResult.setReminderList(todayReminderList);
		} else {
			executedResult.setUpdateReminderList(false);
		}
	}

	private void setTaskID() {
		taskId = taskRelated.getID();
	}


	private void setTaskRelated() {
		taskRelated = taskList.get(displayID - 1);
	}

	private boolean isValidDisplayID() {
		setDisplayID();
		int size = taskList.size();
		if ((size == 0) || (displayID < 1) || (displayID > size)) {
			return false;
		} else {
			return true;
		}
	}

	private void setDisplayID() {
		displayID = parserResultInstance.getTaskID();
	}

	@Override
	public int getTaskId() {
		return taskId;
	}

	@Override
	public CommandType getCommandType() {
		return commandType;
	}

	@Override
	public void undo() { // update it by setting reminder date as null
		taskRelated.setReminder(reminderDateDeleted);
		storer.add(taskRelated);
		this.isUndoSuccess = true;
		this.isRedoSuccess = false;
	}

	@Override
	public void redo() {
		taskRelated.setReminder(null);
		storer.add(taskRelated);
		this.isUndoSuccess = false;
		this.isRedoSuccess = true;
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
	
	public DateTime getReminderDateDeleted() {
		return reminderDateDeleted;
	}
}
