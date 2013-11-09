package nailit.logic.command;

//@author A0105789R

import java.util.Vector;
import org.joda.time.DateTime;
import nailit.common.NIConstants;
import nailit.common.Result;
import nailit.common.Task;
import nailit.common.Utilities;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

public class CommandAddReminder extends Command{
	
	private static final String REMINDER_ADDED_UNSUCCESSFULLY_FEEDBACK_INVALID_DISPLAYID = "Sorry, the reminder is not added " +
																		"successfully. The reason is: the display ID is invalid";
	
	private static final String REMINDER_ADDED_UNSUCCESSFULLY_FEEDBACK_INVALID_REMINDER_DATE = "Sorry, the reminder is not added " +
																								"successfully. The reason is: the reminder " +
																								"date is invalid";
	private static final String FEEDBACK_FOR_SUCCESSFULLY_ADD_REMINDER = "Add reminder to the Task: %s on %s";
	
	private int displayID;
	private Vector<Task> taskList;
	private DateTime reminderDateToAdd;
	private Task taskRelated;
	
	// indicate whether the add reminder command is successful
	private boolean isSuccess;
	
	// constructor
	public CommandAddReminder(ParserResult resultInstance,
			StorageManager storerToUse, Vector<Task> currentTaskList) {
		super(resultInstance, storerToUse);
		taskList = currentTaskList;
		executedResult = new Result();
		reminderDateToAdd = new DateTime();
		taskRelated = new Task();
		commandSummary = "";
		isSuccess = false;
		isUndoSuccess = false;
		isRedoSuccess = false;
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
				createResultForInvalidReminderDateFailure();
			}
		} else {
			createResultForInvalidDisplayIdFailure();
		} 
		return executedResult;
	}
	
	private void createResultForInvalidReminderDateFailure() {
		executedResult = new Result(false, false, Result.NOTIFICATION_DISPLAY, 
				REMINDER_ADDED_UNSUCCESSFULLY_FEEDBACK_INVALID_REMINDER_DATE);
		executedResult.setUpdateReminderList(false);
		
	}

	private void createResultForInvalidDisplayIdFailure() {
		executedResult = new Result(false, false, Result.NOTIFICATION_DISPLAY, 
				REMINDER_ADDED_UNSUCCESSFULLY_FEEDBACK_INVALID_DISPLAYID);
		executedResult.setUpdateReminderList(false);
	}

	private void createCommandSummary() {
		commandSummary = "Add reminder for " + taskRelated.getName() + 
				" on " + reminderDateToAdd.toString(NIConstants.DISPLAY_DATE_FORMAT);
	}

	private void createResult() {
		String notificationStr = String.format(FEEDBACK_FOR_SUCCESSFULLY_ADD_REMINDER, 
				taskRelated.getName(), reminderDateToAdd.toString(NIConstants.DISPLAY_DATE_FORMAT));
		executedResult = new Result(false, true, Result.NOTIFICATION_DISPLAY, notificationStr);
		
		// create a dateTime object representing today, used as comparison
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
		taskId = taskRelated.getID();
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
		if(reminderDateToAdd == null) { // handle the null reminder time situation
			return false;
		}
		return true;
	}

	private void setReminderDateToAdd() {
		// the dateTime to add is stored in the endTime
		if(parserResultInstance.getReminderTime() != null) {
			this.reminderDateToAdd = Utilities.getStartOfDay(parserResultInstance.getReminderTime());
		} else {
			this.reminderDateToAdd = null;
		}
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
	public int getTaskId() {
		return taskId;
	}

	@Override
	public CommandType getCommandType() {
		return commandType;
	}

	@Override
	public void undo() { // update it by setting reminder date as null
		taskRelated.setReminder(null); 
		storer.add(taskRelated);
		this.isUndoSuccess = true;
		this.isRedoSuccess = false;
	}

	@Override
	public void redo() {
		taskRelated.setReminder(this.reminderDateToAdd); 
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
	
	public DateTime getReminderDateToAdd() {
		return this.reminderDateToAdd;
	}
}
