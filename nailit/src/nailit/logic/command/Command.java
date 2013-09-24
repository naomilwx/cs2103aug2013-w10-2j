package nailit.logic.command;
import nailit.Result;
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

	private void doExecution() {
		COMMAND_TYPE commandType = parserResultInstance.getCommand();
		switch(commandType) {
			case ADD: {
				add();
				break;
			}
			case COMPLETE: {
				complete();
				break;
			} 
			case DELETE: {
				delete();
				break;
			}
			case SEARCH: {
				search();
				break;
			}
			case UPDATE: {
				update();
				break;
			}
			case INVALID: {
				break;
			}
			default: {
				
			}
		}
	}

	private void update() {
		// TODO Auto-generated method stub
		
	}

	private void search() {
		// TODO Auto-generated method stub
		
	}

	private void delete() {
		// TODO Auto-generated method stub
		
	}

	private void complete() {
		// TODO Auto-generated method stub
		
	}

	private void add() {
		// TODO Auto-generated method stub
		
	}
	
	
}
