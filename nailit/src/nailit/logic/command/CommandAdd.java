package nailit.logic.command;

import nailit.common.Result;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

public class CommandAdd extends Command{
	private Result executedResult;
	
	// constructor
	public CommandAdd(ParserResult ResultInstance, StorageManager storerToUse) {
		super(ResultInstance, storerToUse);
	}
	
	public Result execute() {
		doExecution();
		return executedResult;
	}
	
	private void doExecution() {
		
	}

	@Override
	public Result executeCommand(ParserResult parserResultInstance) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
