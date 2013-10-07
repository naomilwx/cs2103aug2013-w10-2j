package nailit.logic.command;
import org.joda.time.DateTime;

import test.storage.StorageStub;

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
	
	private final String Success_Msg_FirstPart = "The task with Task ID: "; 
	private final String Success_Msg_SecondPart	= "is updated successfully";
	private final String UnsuccessfulFeedback = "Sorry, there is no such task record in the storage, please check and try again.";
	private final String UpdateFeedback = "update the task and the content updated is: ";


	public CommandUpdate(ParserResult resultInstance, StorageStub storerToUse) {
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
		createResultObject();
		createCommandSummary();
		return executedResult;
	}
	
	private void createCommandSummary() {
		commandSummary = UpdateFeedback + updatedContent;
		
	}

	private void createResultObject() {
		executedResult = new Result(false, true, Result.NOTIFICATION_DISPLAY, 
				Success_Msg_FirstPart + taskToRetrieveID + Success_Msg_SecondPart);
		
	}
	
	// there is no such task record in the storage to display
	private void createUnsuccessfulResultObject() {
		executedResult = new Result(false, false, Result.NOTIFICATION_DISPLAY,
				UnsuccessfulFeedback);
	}

	private void addTheUpdatedTaskObjOnStorage() {
		storer.add(taskRetrieved);
	}


	private void updateTheRetrievedTask() {
		if(!parserResultInstance.isNullName()) {
			String newName = parserResultInstance.getName();
			taskRetrieved.setName(newName);
			updatedContent = updatedContent + newName + " ";
		}
		if(!parserResultInstance.isNullStartTime()) {
			DateTime newStartTime = parserResultInstance.getStartTime();
			taskRetrieved.setStartTime(newStartTime);
			updatedContent = updatedContent + newStartTime + " ";
		}
		if(!parserResultInstance.isNullEndTime()) {
			DateTime newEndTime = parserResultInstance.getEndTime();
			taskRetrieved.setEndTime(newEndTime);
			updatedContent = updatedContent + newEndTime + " ";
		}
		if(!parserResultInstance.isNullTag()) {
			String newTag = parserResultInstance.getTag();
			taskRetrieved.setTag(newTag);
			updatedContent = updatedContent + newTag + " ";
		}
		if(!parserResultInstance.isNullPriority()) {
			TaskPriority newPriority = parserResultInstance.getPriority();
			taskRetrieved.setPriority(newPriority);
			updatedContent = updatedContent + newPriority + " ";
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
