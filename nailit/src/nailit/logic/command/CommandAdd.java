package nailit.logic.command;

import nailit.common.Result;
import nailit.logic.ParserResult;

public class CommandAdd extends Command{
	private Result executedResult;
	
	// constructor
	public CommandAdd(ParserResult ResultInstance) {
		super(ResultInstance);
	}
	
	public Result execute() {
		doExecution();
		return executedResult;
	}
	
	private void doExecution() {
		
	}
	
}
