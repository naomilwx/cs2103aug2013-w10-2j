package nailit.logic.command;

import java.util.Vector;

import nailit.common.Result;
import nailit.logic.*;

import nailit.storage.StorageManager;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;


public class CommandManager {
	// the storage object that the commandManager works with 
	private StorageManager storer;
	
	// the Result to pass back to the GUI component
	private Result executedResult;
	
	// the parserResult to use in the commandExcute
	private ParserResult parserResultInstance;
	
	// the vector object is used to store the done operations
	private Vector<Command> operationsHistory;
	
	// constructor
	public CommandManager () 
	{
		storer = new StorageManager();
		executedResult = new Result();
		operationsHistory = new Vector<Command>();
	}
	
	public Result executeCommand(ParserResult parserResultInstance)
	{
		this.parserResultInstance = parserResultInstance;
		doExecution();
		return executedResult;
	}

	private void doExecution() {
		CommandType commandType = parserResultInstance.getCommand();
		// in each command execute method like add(), we will create the 
		// corresponding object and use them to execute the command.
		// In addition, the command is added to operation History.
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
		return null;
	}

	private Result search() {
		// TODO Auto-generated method stub
		return null;
	}

	private Result delete() {
		// TODO Auto-generated method stub
		return null;
	}

	private Result complete() {
		// TODO Auto-generated method stub
		return null;
	}

	private Result add() {
		// TODO Auto-generated method stub
		return null;
	}

}
