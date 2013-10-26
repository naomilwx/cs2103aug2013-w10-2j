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
	
	private int displayID;
	
	private int taskID;
	
	private Vector<Task> taskList;
	
	private Result executedResult;
	
	private DateTime reminderDateToAdd;
	
	private Task taskRelated;
	
	private String commandSummary;

	public CommandAddReminder(ParserResult resultInstance,
			StorageManager storerToUse, Vector<Task> currentTaskList) {
		super(resultInstance, storerToUse);
		taskList = currentTaskList;
		executedResult = new Result();
		reminderDateToAdd = new DateTime();
		taskRelated = new Task();
		commandSummary = "";
	}

	@Override
	public Result executeCommand() throws Exception {
		setReminderDateToAdd();
		if(isValidDisplayID()) {
			setTaskRelated();
			setTaskID();
			if(isValidReminderDateTime()) { // the added reminder can be set
				addReminderDate(); // add reminder in storage
				createResult();
				createCommandSummary();
			}
		} else {
			createResultForFailure();
		} 
		return executedResult;
	}
	
	private void createResultForFailure() {
		// TODO Auto-generated method stub
		
	}

	private void createCommandSummary() {
		commandSummary = "Add reminder to the Task: " + taskRelated.getName() + 
				" to the date " + reminderDateToAdd.toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT);
	}

	private void createResult() {
		// TODO Auto-generated method stub
		
	}

	private void setTaskID() {
		taskID = taskRelated.getID();
	}

	private void addReminderDate() {
		// set the reminder date in the task
		storer.addReminder(taskID, reminderDateToAdd); // add reminder to the storage
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
		int displayID = parserResultInstance.getTaskID();
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

}
