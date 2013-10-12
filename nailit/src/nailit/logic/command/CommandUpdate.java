package nailit.logic.command;
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
	
	private static final String SUCCESS_MSG_FIRSTPART = "Task [ID: "; 
	private static final String SUCCESS_MSG_SECONDPART	= "] has been successfully updated";
	private static final String UPDATE_UNSUCCESSFUL_FEEDBACK = "Sorry, Task [ID: %1d] not found. Please check and try again.";
	private static final String UPDATE_SUCCESSFUL_FEEDBACK = 
			"Task [ID: %1d] has been successfully updated. The content updated is: \n";


	public CommandUpdate(ParserResult resultInstance, StorageManager storerToUse) {
		super(resultInstance, storerToUse);
		updatedContent = "";
		commandType = "update";
	}

	@Override
	public Result executeCommand() {
		try {
			retrieveTheTask();
		} catch(Exception e) {
			createUnsuccessfulResultObject();
			return executedResult;
		}

		updateTheRetrievedTask();
		addTheUpdatedTaskObjOnStorage();
		createCommandSummary();
		createResultObject();
		return executedResult;
	}
	
	private void createCommandSummary() {
		commandSummary = String.format(UPDATE_SUCCESSFUL_FEEDBACK, taskToRetrieveID) + updatedContent;
		
	}

	private void createResultObject() {
//		executedResult = new Result(false, true, Result.NOTIFICATION_DISPLAY, 
//				SUCCESS_MSG_FIRSTPART + taskToRetrieveID + SUCCESS_MSG_SECONDPART);
		executedResult = new Result(false, true, Result.NOTIFICATION_DISPLAY, 
				commandSummary);
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
		taskToRetrieveID = parserResultInstance.getTaskID();
		taskRetrieved = storer.retrieve(taskToRetrieveID);

	}
	
	public int getTaskID() {
		return taskToRetrieveID;
	}
}
