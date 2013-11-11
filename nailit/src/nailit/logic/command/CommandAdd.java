package nailit.logic.command;

//@author A0105789R

import org.joda.time.DateTime;
import nailit.common.CommandType;
import nailit.common.Result;
import nailit.common.TaskPriority;
import nailit.common.Task;
import nailit.logic.ParserResult;
import nailit.storage.NoTaskFoundException;
import nailit.storage.StorageManager;

public class CommandAdd extends Command{
	// the new created task to add to the storage
	private Task taskPassedToStorer;
	
	// potential fields to add to the new field
	private String taskName;
	private DateTime startTime;
	private DateTime endTime;
	private TaskPriority taskPriority;
	private String taskTag;
	private String taskDescription;
	
	// final static fields
	private static final String SUCCESS_MSG = "Successfully ADD: %1s";
	
	// constructor
	public CommandAdd(ParserResult resultInstance, StorageManager storerToUse) {
		super(resultInstance, storerToUse);
		commandType = CommandType.ADD;
		isUndoSuccess = false;
		isRedoSuccess = false;
	}

	@Override
	public Result executeCommand() {
		getContentFromParserResult();
		createTaskObject();
		taskId = storer.add(taskPassedToStorer);
		taskPassedToStorer.setID(taskId);
		createResultObject();
		createCommandSummary();
		return executedResult;
	}

	private void createCommandSummary() {
		commandSummary = "add: ";
		if(parserResultInstance.getName() != null) {
			commandSummary = commandSummary + taskPassedToStorer.toString();
		} 
	}

	private void createResultObject() {
		String notificationStr = String.format(SUCCESS_MSG, taskName);
		executedResult = new Result(false, true, Result.EXECUTION_RESULT_DISPLAY, notificationStr, taskPassedToStorer, null, null);
	}

	private void createTaskObject() {
		taskPassedToStorer = new Task(taskName, startTime, endTime, taskTag, taskPriority);
		taskPassedToStorer.setDescription(taskDescription);
	}

	private void getContentFromParserResult() {
		taskName = parserResultInstance.getName();
		startTime = parserResultInstance.getStartTime();
		endTime = parserResultInstance.getEndTime();
		taskPriority = parserResultInstance.getPriority();
		taskTag = parserResultInstance.getTag();
		taskDescription = parserResultInstance.getDescription();
	}
	
	public int getTaskId() {
		return taskId;
	}
	
	public CommandType getCommandType() {
		return commandType;
	}

	@Override
	public void undo() {
		try {
			storer.remove(taskId, true);
			isUndoSuccess = true;
			isRedoSuccess = false; //means now you can redo again, if redone before
		} catch (NoTaskFoundException e) {
			isUndoSuccess = false;
		}
	}

	@Override
	public boolean isUndoSuccessfully() {
		return isUndoSuccess;
	}

	@Override
	public String getCommandString() {
		return commandSummary;
	}

	@Override
	public void redo() {
		taskPassedToStorer.setIDToNull();  
		storer.add(taskPassedToStorer);
		this.isRedoSuccess = true;
		this.isUndoSuccess = false;
	}

	@Override
	public boolean isRedoSuccessfully() {
		return isRedoSuccess;
	}
	
	public Task getTaskAdded() {
		taskPassedToStorer.setID(taskId);
		return taskPassedToStorer;
	}

	@Override
	public Task getTaskRelated() {
		return taskPassedToStorer;
	}
}
