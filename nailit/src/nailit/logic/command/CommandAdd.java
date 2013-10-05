package nailit.logic.command;

import java.util.Date;

import org.joda.time.DateTime;

import nailit.common.Result;
import nailit.common.TaskPriority;
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
	private TaskPriority taskPriority;
	private String taskTag;
	
	private final String Success_Msg = "The new task is added successfully, the Task ID for it is: ";
	
	// constructor
	public CommandAdd(ParserResult ResultInstance, StorageManager storerToUse) {
		super(ResultInstance, storerToUse);
	}

	@Override
	public Result executeCommand(ParserResult parserResultInstance) {
		getContentFromParserResult();
		createTaskObject();
		int taskID = storer.add(taskPassedToStorer);
		createResultObject(taskID);
		return executedResult;
	}

	private void createResultObject(int taskID) {
		// Shuzhi: currently do not know what is displayType,
		// so give a null first
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
