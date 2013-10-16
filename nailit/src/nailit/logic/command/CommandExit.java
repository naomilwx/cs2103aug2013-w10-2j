package nailit.logic.command;

import nailit.common.Result;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

public class CommandExit extends Command{

	private Result executedResult;
	
	public CommandExit(ParserResult resultInstance, StorageManager storerToUse) {
		super(resultInstance, storerToUse);
		// TODO Auto-generated constructor stub
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


}
