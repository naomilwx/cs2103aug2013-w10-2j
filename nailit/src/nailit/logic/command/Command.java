package nailit.logic.command;
import nailit.Result;
import nailit.logic.*;
import nailit.storage.StorageManager;
import nailit.Result;

public class Command {
	
	private static ParserResult parserResultInstance;
	private static StorageManager storer;
	private static Result executedResult;
	
	public Command (ParserResult ResultInstance) // constructor
	{
		parserResultInstance = ResultInstance;
		storer = new StorageManager();
	}
	
	public Result executeCommand()
	{

		CommandResult executeResult = new CommandResult();
		doExecution();

		
		return executedResult;
	}

	private void doExecution() {
		String commandType = parserResultInstance.getCommand();
		Switch(commandType) {
			case 
		}
	}
	
	
}
