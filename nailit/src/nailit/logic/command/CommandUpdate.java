package nailit.logic.command;
import java.util.Vector;

import org.joda.time.DateTime;

import test.storage.StorageStub;
import nailit.common.NIConstants;
import nailit.common.Result;
import nailit.common.Task;
import nailit.common.TaskPriority;
import nailit.logic.ParserResult;
import nailit.storage.FileCorruptionException;
import nailit.storage.NoTaskFoundException;
import nailit.storage.StorageManager;

public class CommandUpdate extends Command{
	private String commandType;
	private Result executedResult;
	private int taskToRetrieveID;
	private Task taskRetrieved;
	
	// for the use of commandSummary
	private String updatedContent;
	private String commandSummary;
	
	private Vector<Task> taskList;
	private int taskToRetrieveDisplayID;
	
	private boolean updateSuccessfully;
	
	private static final String SUCCESS_MSG_FIRSTPART = "Task [ID: "; 
	private static final String SUCCESS_MSG_SECONDPART	= "] has been successfully updated";
	private static final String UPDATE_UNSUCCESSFUL_FEEDBACK = "Sorry, Task [ID: %1d] not found. Please check and try again.";
	private static final String UPDATE_UNSUCCESSFUL_FEEDBACK_FOR_TASK_NOT_EXIST_IN_CURRENT_LIST = "Sorry, Task [ID: %1d] is not in the current task list. Please check and try again.";

	private static final String UPDATE_SUCCESSFUL_FEEDBACK = 
			"Task [ID: %1d] has been successfully updated. The content updated is: \n";


	public CommandUpdate(ParserResult resultInstance, StorageManager storerToUse, Vector<Task> taskList) {
		super(resultInstance, storerToUse);
		updatedContent = "";
		commandType = "update";
		this.taskList = taskList;
	}

	@Override
	public Result executeCommand() {
		taskToRetrieveDisplayID = getDisplayID();
		try {
			if(isExistToUpdateTaskInTaskList()) {
				retrieveTheTask();
			} else {
				updateSuccessfully = false;
				return createResultObjectForTaskToUpdateNotExistingInTaskList(taskToRetrieveDisplayID);
			}
			
		} catch(Exception e) {
			createUnsuccessfulResultObject();
			return executedResult;
		}
		updateSuccessfully = true;
		updateTheRetrievedTask();
		addTheUpdatedTaskObjOnStorage();
		createCommandSummary();
		createResultObject();
		return executedResult;
	}
	
	private Result createResultObjectForTaskToUpdateNotExistingInTaskList(
			int taskToRetrieveDisplayID2) {
		String notificationStr = String.format(UPDATE_UNSUCCESSFUL_FEEDBACK_FOR_TASK_NOT_EXIST_IN_CURRENT_LIST, taskToRetrieveDisplayID);
		executedResult = new Result(false, false, Result.NOTIFICATION_DISPLAY,
				notificationStr);
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
		commandSummary = String.format(UPDATE_SUCCESSFUL_FEEDBACK, taskToRetrieveID) + updatedContent;
		
	}

	private void createResultObject() {
//		executedResult = new Result(false, true, Result.NOTIFICATION_DISPLAY, 
//				SUCCESS_MSG_FIRSTPART + taskToRetrieveID + SUCCESS_MSG_SECONDPART);
		executedResult = new Result(false, true, Result.EXECUTION_RESULT_DISPLAY, 
				commandSummary, taskRetrieved, null, null);
	}
	
	// there is no such task record in the storage to display
	private void createUnsuccessfulResultObject() {
		String notificationStr = String.format(UPDATE_UNSUCCESSFUL_FEEDBACK, taskToRetrieveID);
		executedResult = new Result(false, false, Result.NOTIFICATION_DISPLAY,
				notificationStr);
	}

	private void addTheUpdatedTaskObjOnStorage() {
		storer.add(taskRetrieved);
	}


	private void updateTheRetrievedTask() {
		if(!parserResultInstance.isNullName()) {
			String newName = parserResultInstance.getName();
			taskRetrieved.setName(newName);
			updatedContent = updatedContent + "Name: " + newName + " \n";
		}
		if(!parserResultInstance.isNullStartTime()) {
			DateTime newStartTime = parserResultInstance.getStartTime();
			taskRetrieved.setStartTime(newStartTime);
			updatedContent = updatedContent + "Starttime: "+newStartTime.toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT) + " \n";
		}
		if(!parserResultInstance.isNullEndTime()) {
			DateTime newEndTime = parserResultInstance.getEndTime();
			taskRetrieved.setEndTime(newEndTime);
			updatedContent = updatedContent + "Endtime: " +newEndTime.toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT) + " \n";
		}
		if(!parserResultInstance.isNullTag()) {
			String newTag = parserResultInstance.getTag();
			taskRetrieved.setTag(newTag);
			updatedContent = updatedContent + "Tag: " + newTag + " \n";
		}
		if(!parserResultInstance.isNullPriority()) {
			TaskPriority newPriority = parserResultInstance.getPriority();
			taskRetrieved.setPriority(newPriority);
			updatedContent = updatedContent + "Priority: " + newPriority + " ";
		}
	}

	private void retrieveTheTask() throws Exception {
		taskRetrieved = taskList.get(taskToRetrieveDisplayID - 1);
	}
	
	
	
	public int getTaskID() {
		return taskToRetrieveID;
	}

	public boolean updateSuccess() {
		return updateSuccessfully;
	}
}
