package nailit.logic.command;
import org.joda.time.DateTime;

import test.storage.StorageStub;
import nailit.common.Result;
import nailit.common.TaskPriority;
import nailit.common.Task;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

public class CommandAdd extends Command{
	private String commandType;
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
	
	private final String SuccessMsg = "The new task is added successfully, the Task ID for it is: ";
	
	// constructor
	public CommandAdd(ParserResult resultInstance, StorageStub storerToUse) {
		super(resultInstance, storerToUse);
		commandType = "add";
	}

	@Override
	public Result executeCommand() {
		getContentFromParserResult();
		createTaskObject();
		taskID = storer.add(taskPassedToStorer);
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
		executedResult = new Result(false, true, Result.NOTIFICATION_DISPLAY, SuccessMsg + taskID);
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
}
