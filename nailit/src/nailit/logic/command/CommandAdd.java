package nailit.logic.command;

import java.util.Date;

import nailit.common.Result;
import nailit.common.Task;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

public class CommandAdd extends Command{
	private Result executedResult;
	private Task taskPassedToStorer;
	private CommandType command;
	private String taskName;
	private Date startTime;
	private Date endTime;
	private Integer taskPriority;
	private String taskTag;
	
	// constructor
	public CommandAdd(ParserResult ResultInstance, StorageManager storerToUse) {
		super(ResultInstance, storerToUse);
	}

	@Override
	public Result executeCommand(ParserResult parserResultInstance) {
		getContentFromParserResult();
		createTaskObject();
		return null;
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
