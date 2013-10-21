package nailit.logic.command;
import org.joda.time.DateTime;

import test.storage.StorageManagerStub;
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
	
	private static final String SUCCESS_MSG = "Task: %1s [ID: %2d] has been successfully added";
	
	// constructor
	public CommandAdd(ParserResult resultInstance, StorageManager storerToUse) {
		super(resultInstance, storerToUse);
		commandType = CommandType.ADD;
		isUndoSuccess = false;
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
		return executedResult;
	}

	private void createCommandSummary() {
		commandSummary = "add " + parserResultInstance.getName() + " " 
				+ parserResultInstance.getStartTime() + " - " 
				+ parserResultInstance.getEndTime() + " " 
				+ parserResultInstance.getTag() 
				+ parserResultInstance.getPriority();
	}

	private void createResultObject() {
		String notificationStr = String.format(SUCCESS_MSG, taskName, taskID);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSuccessRedo() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public Task getTaskAdded() {
		taskPassedToStorer.setID(taskID);
		return taskPassedToStorer;
	}
}
