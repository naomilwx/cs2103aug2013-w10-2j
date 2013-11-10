package nailit.logic.command;

//@author A0105789R

import nailit.common.Result;
import nailit.common.Task;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

public class CommandExit extends Command{
	// constructor
	public CommandExit(ParserResult resultInstance, StorageManager storerToUse) {
		super(resultInstance, storerToUse);
		commandType = CommandType.EXIT;
	}

	@Override
	public Result executeCommand() {
		createResult();
		return executedResult;
	}

	private void createResult() {
		executedResult = new Result(true, false, Result.NOTIFICATION_DISPLAY, "Exit");
	}

	@Override
	public int getTaskId() {
		return 0;
	}
	public CommandType getCommandType() {
		return commandType;
	}

	@Override
	public void undo() {
		// no undo for command exit
	}

	@Override
	public boolean isUndoSuccessfully() {
		// nothing
		return false;
	}

	@Override
	public String getCommandString() {
		return null;
	}

	@Override
	public void redo() {
		// no redo for command exit
	}

	@Override
	public boolean isRedoSuccessfully() {
		return false;
	}

	@Override
	public Task getTaskRelated() {
		// nothing to do
		return null;
	}
}
