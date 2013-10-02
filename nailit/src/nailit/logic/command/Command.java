package nailit.logic.command;

import nailit.common.Result;
import nailit.logic.*;

import nailit.storage.StorageManager;
import nailit.logic.COMMAND_TYPE;
import nailit.logic.ParserResult;


public class Command {
	
	private static ParserResult parserResultInstance;
	private static StorageManager storer;
	private static Result executedResult;
	
	public Command (ParserResult ResultInstance) // constructor
	{
		parserResultInstance = ResultInstance;
		storer = new StorageManager();
		executedResult = new Result();
	}
	
	public Result executeCommand()
	{
		doExecution();
		return executedResult;
	}

	protected void doExecution() {
		COMMAND_TYPE commandType = parserResultInstance.getCommand();
		switch(commandType) {
			case ADD: {
				executedResult = add();
				break;
			}
			case COMPLETE: {
				executedResult = complete();
				break;
			} 
			case DELETE: {
				executedResult = delete();
				break;
			}
			case SEARCH: {
				executedResult = search();
				break;
			}
			case UPDATE: {
				executedResult = update();
				break;
			}
			case INVALID: {
				break;
			}
			default: {
				
			}
		}
	}

	private Result update() {
		// TODO Auto-generated method stub
		
	}

	private Result search() {
		// TODO Auto-generated method stub
		
	}

	private Result delete() {
		// TODO Auto-generated method stub
		
	}

	private Result complete() {
		// TODO Auto-generated method stub
		
	}

	private Result add() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
