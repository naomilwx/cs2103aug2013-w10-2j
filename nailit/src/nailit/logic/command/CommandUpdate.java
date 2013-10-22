package nailit.logic.command;
import java.util.Vector;

import org.joda.time.DateTime;

import test.storage.StorageManagerStub;
import nailit.common.NIConstants;
import nailit.common.Result;
import nailit.common.Task;
import nailit.common.TaskPriority;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.storage.FileCorruptionException;
import nailit.storage.NoTaskFoundException;
import nailit.storage.StorageManager;

public class CommandUpdate extends Command{
	private CommandType commandType;
	private Result executedResult;
	private int taskToRetrieveID;
	private Task taskRetrieved; // previous version
	
	private Task updatedTask; // updated version
	
	// for the use of commandSummary
	private String updatedContent;
	private String commandSummary;
	
	private Vector<Task> taskList;
	private int taskToRetrieveDisplayID;
	
	private boolean updateSuccessfully;
	
	private boolean isUndoSuccess;
	private boolean isRedoSuccess;
	
	private static final String SUCCESS_MSG_FIRSTPART = "Task [ID: "; 
	private static final String SUCCESS_MSG_SECONDPART	= "] has been successfully updated";
	private static final String UPDATE_UNSUCCESSFUL_FEEDBACK = "Sorry, Task [ID: %1d] not found. Please check and try again.";
	private static final String UPDATE_UNSUCCESSFUL_FEEDBACK_FOR_TASK_NOT_EXIST_IN_CURRENT_LIST = "Sorry, Task [ID: %1d] is not in the current task list. Please check and try again.";
	private static final String EXCEPTION_MESSAGE_FOR_DISPLAY_ID_IS_NULL = "Display ID in the parserResult instance is null.";
	private static final String COMMAND_SUMMARY_FOR_UPDATING_TASK_NOT_IN_TASK_LIST = "The task you are trying to update does not exist in the task list. Please try again."; 
	private static final String COMMAND_SUMMARY_FOR_UPDATING_TASK_NOT_IN_STORAGE = "The task you are trying to update does not exist in the storage. Please try again.";


	private static final String UPDATE_SUCCESSFUL_FEEDBACK = 
			"Task [ID: %1d] has been successfully updated. The content updated is: \n";


	public CommandUpdate(ParserResult resultInstance, StorageManager storerToUse, Vector<Task> taskList) {
		super(resultInstance, storerToUse);
		updatedContent = "";
		commandType = CommandType.UPDATE;
		this.taskList = taskList;
		updatedTask = new Task();
		isUndoSuccess = false;
		isRedoSuccess = false;
	}

	@Override
	public Result executeCommand() throws Exception {
		taskToRetrieveDisplayID = getDisplayID();
		if(taskToRetrieveDisplayID == 0) { // 0 means no display ID but the original one. Later need changes
			throw new Exception(EXCEPTION_MESSAGE_FOR_DISPLAY_ID_IS_NULL);
		} else {
			try {
				if(isExistToUpdateTaskInTaskList()) {
					retrieveTheTask();
					setTaskID(); // set taskToRetrieveID
				} else {
					updateSuccessfully = false;
					createResultObjectForTaskToUpdateNotExistingInTaskList();
					createCommandSummaryForUpdatingTaskNotInTaskList();
					return executedResult;
				}
				
			} catch(Exception e) {
				updateSuccessfully = false;
				createUnsuccessfulResultObject();
				createCommandSummaryForUpdatingTaskNotInStorage();
				return executedResult;
			}
			updateSuccessfully = true;
			updateTheRetrievedTask();
			addTheUpdatedTaskObjOnStorage();
			createCommandSummary();
			createResultObject();
			return executedResult;
		}
	}
	
	private void setTaskID() {
		taskToRetrieveID = taskRetrieved.getID();
	}

	private void createCommandSummaryForUpdatingTaskNotInStorage() {
		commandSummary = COMMAND_SUMMARY_FOR_UPDATING_TASK_NOT_IN_STORAGE;
	}

	private void createCommandSummaryForUpdatingTaskNotInTaskList() {
		commandSummary = COMMAND_SUMMARY_FOR_UPDATING_TASK_NOT_IN_TASK_LIST;
	}

	private Result createResultObjectForTaskToUpdateNotExistingInTaskList() {
		String notificationStr = String.format(UPDATE_UNSUCCESSFUL_FEEDBACK_FOR_TASK_NOT_EXIST_IN_CURRENT_LIST, taskToRetrieveDisplayID);
		executedResult = new Result(false, false, Result.NOTIFICATION_DISPLAY, notificationStr);
		return executedResult;
	}

	private boolean isExistToUpdateTaskInTaskList() {
		if(taskList == null) {
			return false;
		} else if(taskList.size() < taskToRetrieveDisplayID) {
			return false;
		} else if(taskToRetrieveDisplayID < 1) {
			return false;
		} else {
			return true;
		}
	}

	public int getDisplayID() {
		// currently, displayID is still taskID
		return parserResultInstance.getTaskID();
	}

	private void createCommandSummary() {
//		commandSummary = String.format(UPDATE_SUCCESSFUL_FEEDBACK, taskToRetrieveID) + updatedContent;
		commandSummary = taskRetrieved.getName() + " is updated to " + updatedContent + "\n";
		
	}

	private void createResultObject() {
//		executedResult = new Result(false, true, Result.NOTIFICATION_DISPLAY, 
//				SUCCESS_MSG_FIRSTPART + taskToRetrieveID + SUCCESS_MSG_SECONDPART);
		executedResult = new Result(false, true, Result.EXECUTION_RESULT_DISPLAY, 
				commandSummary, updatedTask, null, null);
	}
	
	// there is no such task record in the storage to display
	private void createUnsuccessfulResultObject() {
		String notificationStr = String.format(UPDATE_UNSUCCESSFUL_FEEDBACK, taskToRetrieveDisplayID);
		executedResult = new Result(false, false, Result.NOTIFICATION_DISPLAY,
				notificationStr);
	}

	private void addTheUpdatedTaskObjOnStorage() {
		// since there is taskID in the taskRetrieved, storer knows that it is update operation
		storer.add(updatedTask);
	}


	private void updateTheRetrievedTask() {
		if(!parserResultInstance.isNullName()) {
			String newName = parserResultInstance.getName();
			updatedTask.setName(newName);
			updatedContent = updatedContent + "Name: " + newName + " \n";
		} else { // means name use the original one, since no update
			updatedTask.setName(taskRetrieved.getName());
		}
		
		if(!parserResultInstance.isNullStartTime()) {
			DateTime newStartTime = parserResultInstance.getStartTime();
			updatedTask.setStartTime(newStartTime);
			updatedContent = updatedContent + "Starttime: "+newStartTime.toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT) + " \n";
		} else { // means name use the original one, since no update
			updatedTask.setStartTime(taskRetrieved.getStartTime());
		}
		
		if(!parserResultInstance.isNullEndTime()) {
			DateTime newEndTime = parserResultInstance.getEndTime();
			updatedTask.setEndTime(newEndTime);
			updatedContent = updatedContent + "Endtime: " +newEndTime.toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT) + " \n";
		} else { // means name use the original one, since no update
			updatedTask.setEndTime(taskRetrieved.getEndTime());
		}
		
		if(!parserResultInstance.isNullTag()) {
			String newTag = parserResultInstance.getTag();
			updatedTask.setTag(newTag);
			updatedContent = updatedContent + "Tag: " + newTag + " \n";
		} else { // means name use the original one, since no update
			updatedTask.setTag(taskRetrieved.getTag());
		}
		
		if(parserResultInstance.isSetPriority()) { // currently use this method, although the name is not suitable
			TaskPriority newPriority = parserResultInstance.getPriority();
			updatedTask.setPriority(newPriority);
			updatedContent = updatedContent + "Priority: " + newPriority + " ";
		} else { // means name use the original one, since no update
			updatedTask.setPriority(taskRetrieved.getPriority());
		}
		
		updatedTask.setID(taskRetrieved.getID());
	}

	private void retrieveTheTask() {
		taskRetrieved = taskList.get(taskToRetrieveDisplayID - 1);
	}
	
	
	
	public int getTaskID() {
		return taskToRetrieveID;
	}

	public boolean updateSuccess() {
		return updateSuccessfully;
	}
	
	public CommandType getCommandType() {
		return commandType;
	}

	@Override
	public void undo() {
		storer.add(taskRetrieved); // update back to the previous version
		isUndoSuccess = true;
		this.isRedoSuccess = false;
	}

	@Override
	public boolean undoSuccessfully() {
		return isUndoSuccess;
	}
	
	public Task getRetrievedTask() { // get the previous version of task
		return taskRetrieved; 
	}
	
	public Task getUpdatedTask() {
		return updatedTask;
	}

	@Override
	public String getCommandString() {
		return commandSummary;
	}

	@Override
	public void redo() {
		storer.add(updatedTask);
		isRedoSuccess = true;
		this.isUndoSuccess = false;
	}

	@Override
	public boolean isSuccessRedo() {
		return isRedoSuccess;
	}

}
