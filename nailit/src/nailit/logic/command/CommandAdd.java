package nailit.logic.command;
import org.joda.time.DateTime;

import test.storage.StorageManagerStub;
import nailit.common.NIConstants;
import nailit.common.Result;
import nailit.common.TaskPriority;
import nailit.common.Task;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.storage.NoTaskFoundException;
import nailit.storage.StorageManager;

public class CommandAdd extends Command{
	private CommandType commandType;
	private Result executedResult;
	private Task taskPassedToStorer;
	private CommandType command;
	private String taskName;
	private DateTime startTime;
	private DateTime endTime;
	private TaskPriority taskPriority;
	private String taskTag;
	private int taskID;
	
	// this is used for the command history
	private String commandSummary;
	
	private boolean isUndoSuccess;
	private boolean isRedoSuccess;
	
	private static final String SUCCESS_MSG = "Task: %1s has been successfully added";
	
	// constructor
	public CommandAdd(ParserResult resultInstance, StorageManager storerToUse) {
		super(resultInstance, storerToUse);
		commandType = CommandType.ADD;
		isUndoSuccess = false;
		isRedoSuccess = false;
	}

	@Override
	public Result executeCommand() {
		System.out.println("execute");
		getContentFromParserResult();
		createTaskObject();
		taskID = storer.add(taskPassedToStorer);
		taskPassedToStorer.setID(taskID); //temp fix here. will it be better if storage returns task object instead of id?
		createResultObject();
		createCommandSummary();
		
		System.out.println(taskID);
		
		return executedResult;
	}

	private void createCommandSummary() {
		commandSummary = "add";
		if(parserResultInstance.getName() != null) {
			commandSummary = commandSummary + " Name: " + parserResultInstance.getName() + "\n";
		} 
		
		if(parserResultInstance.getStartTime() != null) {
			commandSummary = commandSummary + " Start time: " + 
		parserResultInstance.getStartTime().toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT) + "\n";
		} 
		
		if(parserResultInstance.getEndTime() != null) {
			commandSummary = commandSummary + " End time: " + 
		parserResultInstance.getEndTime().toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT) + "\n";
		} 
		
//		if(parserResultInstance.getTag() != null) {
//			commandSummary = commandSummary + " Tag: " + parserResultInstance.getTag() + "\n";
//		} 
//		
//		if(parserResultInstance.getPriority() != null) {
//			commandSummary = commandSummary + " Priority: " + parserResultInstance.getPriority() + "\n";
//		} 
	}

	private void createResultObject() {
		String notificationStr = String.format(SUCCESS_MSG, taskName);
		executedResult = new Result(false, true, Result.EXECUTION_RESULT_DISPLAY, notificationStr, taskPassedToStorer, null, null);
	}

	private void createTaskObject() {
		taskPassedToStorer = new Task(taskName, startTime, endTime, taskTag, taskPriority);
	}

	private void getContentFromParserResult() {
		command = parserResultInstance.getCommand();
		taskName = parserResultInstance.getName();
		startTime = parserResultInstance.getStartTime();
		endTime = parserResultInstance.getEndTime();
		taskPriority = parserResultInstance.getPriority();
		taskTag = parserResultInstance.getTag();
	}
	
	public int getTaskID() {
		return taskID;
	}
	
	public CommandType getCommandType() {
		return commandType;
	}

	@Override
	public void undo() {
		try {
			storer.remove(taskID, true);
			isUndoSuccess = true;
			isRedoSuccess = false; //means now you can redo again, if redone before
		} catch (NoTaskFoundException e) {
			isUndoSuccess = false;
		}
	}

	@Override
	public boolean undoSuccessfully() {
		return isUndoSuccess;
	}

	@Override
	public String getCommandString() {
		return commandSummary;
	}

	@Override
	public void redo() {
		taskPassedToStorer.setIDToNull();  
		storer.add(taskPassedToStorer); // will the storer redo a delete here?
		this.isRedoSuccess = true;
		this.isUndoSuccess = false;
	}

	@Override
	public boolean isSuccessRedo() {
		return isRedoSuccess;
	}
	
	public Task getTaskAdded() {
		taskPassedToStorer.setID(taskID);
		return taskPassedToStorer;
	}
}
