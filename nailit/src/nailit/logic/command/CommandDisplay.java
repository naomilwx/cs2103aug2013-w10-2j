package nailit.logic.command;

import java.util.Vector;

import nailit.common.Result;
import nailit.common.Task;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

public class CommandDisplay extends Command{
	private Result executedResult;
	private Task taskRetrieved;
	private String commandSummary;
	private int taskToRetrieveID;

//	private final String Success_Msg = "The task is deleted successfully, the Task ID for it is: ";;
	public CommandDisplay(ParserResult resultInstance,
			StorageManager storerToUse) {
		super(resultInstance, storerToUse);
	}

	@Override
	public Result executeCommand() {
		retrieveTheTask();
		createResultObject();
		createCommandSummary();
		return executedResult;
	}

	private void createCommandSummary() {
		commandSummary = "display the task with the Task ID: " + taskToRetrieveID;		
	}

	private void createResultObject() {
		Vector<Task> vectorStoringTheTask = new Vector<Task>();
		vectorStoringTheTask.add(taskRetrieved);
		executedResult = new Result(false, true, Result.TASK_DISPLAY, null, vectorStoringTheTask, null);
	}

	private void retrieveTheTask() {
		taskToRetrieveID = parserResultInstance.getTaskID();
		taskRetrieved = storer.retrieve(taskToRetrieveID);
	}

}
