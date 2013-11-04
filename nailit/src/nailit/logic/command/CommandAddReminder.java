package nailit.logic.command;

import java.util.Vector;

import org.joda.time.DateTime;

import nailit.common.NIConstants;
import nailit.common.Reminder;
import nailit.common.Result;
import nailit.common.Task;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

public class CommandAddReminder extends Command{
	
	private static final String REMINDER_ADDED_UNSUCCESSFULLY_FEEDBACK = "Sorry, the reminder is not added successfully. " +
																		"The reason may be: the display ID is invalid" +
																		" or the reminder date is invalid";

	private int displayID;
	
	private int taskID;
	
	private Vector<Task> taskList;
	
	private Result executedResult;
	
	private DateTime reminderDateToAdd;
	
	private Task taskRelated;
	
	private String commandSummary;
	
	private boolean isSuccess;
	
	private boolean undoSuccess;
	private boolean redoSuccess;
	
	private CommandType commandType;

	public CommandAddReminder(ParserResult resultInstance,
			StorageManager storerToUse, Vector<Task> currentTaskList) {
		super(resultInstance, storerToUse);
		taskList = currentTaskList;
		executedResult = new Result();
		reminderDateToAdd = new DateTime();
		taskRelated = new Task();
		commandSummary = "";
		isSuccess = false;
		undoSuccess = false;
		redoSuccess = false;
		commandType = CommandType.ADDREMINDER;
	}

	@Override
	public Result executeCommand() throws Exception {
		setReminderDateToAdd(); // the added reminder date set here
		if(isValidDisplayID()) { // set displayID here
			setTaskRelated();
			setTaskID();
			if(isValidReminderDateTime()) { 
				addReminderDate(); // add reminder in storage
				createResult();
				createCommandSummary();
				isSuccess = true;
			} else {
				createResultForFailure();
			}
		} else {
			createResultForFailure();
		} 
		return executedResult;
	}
	
	private void createResultForFailure() {
		executedResult = new Result(false, false, Result.NOTIFICATION_DISPLAY, REMINDER_ADDED_UNSUCCESSFULLY_FEEDBACK);
		executedResult.setUpdateReminderList(false);
	}

	private void createCommandSummary() {
		commandSummary = "Add reminder to the Task: " + taskRelated.getName() + 
				" to the date " + reminderDateToAdd.toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT);
	}

	private void createResult() {
		String notificationStr = "Add reminder to the Task: " + taskRelated.getName() + 
				" to the date " + reminderDateToAdd.toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT);
		executedResult = new Result(false, true, Result.NOTIFICATION_DISPLAY, notificationStr);
		// create a dateTime obj representing today
		DateTime today = new DateTime();
		if(today.compareTo(reminderDateToAdd) == 0) { // means that the reminder is today, reminder list needs update
			Vector<Task> todayReminderList = storer.getReminderListForToday();
			executedResult.setUpdateReminderList(true);
			executedResult.setReminderList(todayReminderList);
		} else {
			executedResult.setUpdateReminderList(false);
		}
	}

	private void setTaskID() {
		taskID = taskRelated.getID();
	}

	private void addReminderDate() {
		// set the reminder date in the task
		taskRelated.setReminder(this.reminderDateToAdd);
		storer.add(taskRelated); // add reminder to the storage
	}

	private void setTaskRelated() {
		taskRelated = taskList.get(displayID - 1);
	}

	private boolean isValidReminderDateTime() {
		if(taskRelated.checkCompleted()) { // cannot add a reminder to a completed task
			return false;
		} 
		
		if(reminderDateToAdd == null) { // handle the null reminder time situation
			return false;
		}
		
		DateTime endTimeOfTask = taskRelated.getEndTime();
		if(endTimeOfTask == null){
			return true;
		}
		if(reminderDateToAdd.compareTo(endTimeOfTask) > 0) { // means reminderDateToAdd is later than task due Date
			return false;
		}
		
		return true;
	}

	private void setReminderDateToAdd() {
		// the dateTime to add is stored in the endTime
		this.reminderDateToAdd = parserResultInstance.getReminderTime();
	}

	private boolean isValidDisplayID() {
		setDisplayID();
		int size = taskList.size();
		if((size == 0) || (displayID < 1) || (displayID > size) ) {
			return false;
		} else {
			return true;
		}
	}

	private void setDisplayID() {
		displayID = parserResultInstance.getTaskID();
	}

	@Override
	public int getTaskID() {
		return taskID;
	}

	@Override
	public CommandType getCommandType() {
		return commandType;
	}

	@Override
	public void undo() { // update it by setting reminder date as null
		taskRelated.setReminder(null); 
		storer.add(taskRelated);
		this.undoSuccess = true;
		this.redoSuccess = false;
	}

	@Override
	public void redo() {
		taskRelated.setReminder(this.reminderDateToAdd); 
		storer.add(taskRelated);
		this.undoSuccess = false;
		this.redoSuccess = true;
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
	
	public DateTime getReminderDateToAdd() {
		return this.reminderDateToAdd;
	}

}
