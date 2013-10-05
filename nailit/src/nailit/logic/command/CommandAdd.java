package nailit.logic.command;

import java.util.Date;

import org.joda.time.DateTime;

import nailit.common.Result;
import nailit.common.TASK_PRIORITY;
import nailit.common.Task;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

public class CommandAdd extends Command{
	private Result executedResult;
	private Task taskPassedToStorer;
	private CommandType command;
	private String taskName;
	private DateTime startTime;
	private DateTime endTime;
	private TASK_PRIORITY taskPriority;
	private String taskTag;
	
	private String commandSummary;
	
	private final String Success_Msg = "The new task is added successfully, the Task ID for it is: ";
	
	// constructor
	public CommandAdd(ParserResult resultInstance, StorageManager storerToUse) {
		super(resultInstance, storerToUse);
	}

	@Override
	public Result executeCommand(ParserResult parserResultInstance) {
		getContentFromParserResult();
		createTaskObject();
		int taskID = storer.add(taskPassedToStorer);
		createResultObject(taskID);
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

	private void createResultObject(int taskID) {
		executedResult = new Result(false, true, null, Success_Msg + taskID);
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
	
}
