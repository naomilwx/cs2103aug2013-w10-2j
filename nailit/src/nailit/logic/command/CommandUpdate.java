package nailit.logic.command;
import org.joda.time.DateTime;

import nailit.common.Result;
import nailit.common.Task;
import nailit.common.TaskPriority;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

public class CommandUpdate extends Command{
	private String commandType;
	private Result executedResult;
	private int taskToRetrieveID;
	private Task taskRetrieved;
	private int updatedTaskID;
	
	// for the use of commandSummary
	private String updatedContent;
	private String commandSummary;
	
	private final String Success_Msg_FirstPart = "The task with Task ID: "; 
	private final String Success_Msg_SecondPart	= "is updated successfully, the new Task ID for it is: ";

	public CommandUpdate(ParserResult resultInstance, StorageManager storerToUse) {
		super(resultInstance, storerToUse);
		updatedContent = "";
		commandType = "update";
	}

	@Override
	public Result executeCommand() {
		retrieveTheTask();
		updateTheRetrievedTask();
		removeTheOriginalTaskObjOnStorage();
		addTheUpdatedTaskObjOnStorage();
		createResultObject();
		createCommandSummary();
		return executedResult;
	}
	
	private void createCommandSummary() {
		commandSummary = "update the task and the content updated is: "
				+ updatedContent;
		
	}

	private void createResultObject() {
		executedResult = new Result(false, true, Result.NOTIFICATION_DISPLAY, 
				Success_Msg_FirstPart + taskToRetrieveID + Success_Msg_SecondPart + updatedTaskID);
		
	}

	private void addTheUpdatedTaskObjOnStorage() {
		updatedTaskID = storer.add(taskRetrieved);
	}

	private void removeTheOriginalTaskObjOnStorage() {
		storer.remove(taskToRetrieveID);
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

	private void retrieveTheTask() {
		taskToRetrieveID = parserResultInstance.getTaskID();
		taskRetrieved = storer.retrieve(taskToRetrieveID);
	}

}
