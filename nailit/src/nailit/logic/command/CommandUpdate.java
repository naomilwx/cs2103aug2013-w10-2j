package nailit.logic.command;

import org.joda.time.DateTime;

import nailit.common.Result;
import nailit.common.Task;
import nailit.common.TaskPriority;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

public class CommandUpdate extends Command{
	private Result executedResult;
	private Task taskPassedToStorer;
	private CommandType command;
	private String taskName;
	private DateTime startTime;
	private DateTime endTime;
	private TaskPriority taskPriority;
	private String taskTag;
	private int taskToRetrieveID;
	private Task taskRetrieved;
	
	public CommandUpdate(ParserResult resultInstance, StorageManager storerToUse) {
		super(resultInstance, storerToUse);
	}

	@Override
	public Result executeCommand() {
		getContentFromParserResult();
		retrieveTheTask();
		updateTheRetrievedTask();
		return null;
	}
	
	private void updateTheRetrievedTask() {
		if(!parserResultInstance.isNullName()) {
			taskRetrieved.setName(parserResultInstance.getName());
		}
		if(!parserResultInstance.isNullStartTime()) {
			taskRetrieved.setStartTime(parserResultInstance.getStartTime());
		}
		if(!parserResultInstance.isNullEndTime()) {
			taskRetrieved.setEndTime(parserResultInstance.getEndTime());
		}
		if(!parserResultInstance.isNullTag()) {
			taskRetrieved.setTag(parserResultInstance.getTag());
		}
		if(!parserResultInstance.isNullPriority()) {
			taskRetrieved.setPriority(parserResultInstance.getPriority());
		}
		
	}

	private void getContentFromParserResult() {
		command = parserResultInstance.getCommand();
		taskName = parserResultInstance.getName();
		startTime = parserResultInstance.getStartTime();
		endTime = parserResultInstance.getEndTime();
		taskPriority = parserResultInstance.getPriority();
		taskTag = parserResultInstance.getTag();
	}
	
	private void retrieveTheTask() {
		taskToRetrieveID = parserResultInstance.getTaskID();
		taskRetrieved = storer.retrieve(taskToRetrieveID);
	}

}
