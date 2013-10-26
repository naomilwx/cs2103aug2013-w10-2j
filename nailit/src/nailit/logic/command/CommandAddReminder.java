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

	public CommandAddReminder(ParserResult resultInstance,
			StorageManager storerToUse, Vector<Task> currentTaskList) {
		super(resultInstance, storerToUse);
		taskList = currentTaskList;
		executedResult = new Result();
		reminderDateToAdd = new DateTime();
		taskRelated = new Task();
		commandSummary = "";
		isSuccess = false;
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
			}
		} else {
			createResultForFailure();
		} 
		return executedResult;
	}
	
	private void createResultForFailure() {
		executedResult = new Result(false, true, Result.NOTIFICATION_DISPLAY, REMINDER_ADDED_UNSUCCESSFULLY_FEEDBACK);
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
			Vector<Task> todayReminderList = storer.getTodayReminderList();
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
		
		DateTime endTimeOfTask = taskRelated.getEndTime();
		if(reminderDateToAdd.compareTo(endTimeOfTask) > 0) { // means reminderDateToAdd is later than task due Date
			return false;
		}
		
		return true;
	}

	private void setReminderDateToAdd() {
		// the dateTime to add is stored in the endTime
		this.reminderDateToAdd = parserResultInstance.getEndTime();
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

	private void setDisplayID() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getTaskID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CommandType getCommandType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void redo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean undoSuccessfully() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSuccessRedo() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getCommandString() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean isSuccess() {
		return isSuccess;
	}

}
