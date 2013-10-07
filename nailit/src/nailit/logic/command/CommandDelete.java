package nailit.logic.command;

import nailit.common.Result;
import nailit.common.Task;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

public class CommandDelete extends Command{
	private String commandType;
	private String commandSummary;
	private Result executedResult;
	private Task taskToRemove;
	private int taskToDeleteID;

	private final String Success_Msg = "The task is deleted successfully, the Task ID for it is: ";;
	
	public CommandDelete(ParserResult resultInstance, StorageManager storerToUse) {
		super(resultInstance, storerToUse);
		commandType = "delete";
	}

	@Override
	public Result executeCommand() {
		removeTheTaskOnStorage();
		createResultObject();
		createCommandSummary();
		return executedResult;
	}

	private void createResultObject() {
		executedResult = new Result(false, true, Result.NOTIFICATION_DISPLAY, Success_Msg + taskToDeleteID);		
	}

	private void createCommandSummary() {
		commandSummary = "delete " + taskToRemove.getName() + " " 
				+ taskToRemove.getStartTime() + " - " 
				+ taskToRemove.getEndTime() + " " 
				+ taskToRemove.getTag() 
				+ taskToRemove.getPriority();
	}

	private void removeTheTaskOnStorage() {
		try {
			taskToDeleteID = parserResultInstance.getTaskID();
			taskToRemove = storer.remove(taskToDeleteID);
		} catch (Exception e) {
			// do nothing, since the purpose is to remove the 
			// task and the storage feedbacks that there is no 
			// such task record
		}
		
	}
	
	public int getTaskID() {
		return taskToDeleteID;
	}

}
