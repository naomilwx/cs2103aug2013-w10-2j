package nailit.logic.command;

//@author A0105789R

import nailit.common.Result;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

public class CommandExit extends Command{

	
	
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
	public int getTaskID() {
		// TODO Auto-generated method stub
		return 0;
	}
	public CommandType getCommandType() {
		return commandType;
	}

	@Override
	public void undo() {
		// nothing to do
	}

	@Override
	public boolean isUndoSuccessfully() {
		// nothing
		return false;
	}

	@Override
	public String getCommandString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void redo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isRedoSuccessfully() {
		// TODO Auto-generated method stub
		return false;
	}


}
