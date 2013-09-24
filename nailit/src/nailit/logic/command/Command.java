package nailit.logic.command;
import nailit.Result;
import nailit.logic.*;
import nailit.storage.StorageManager;

public class Command {
	
	private static ParserResult parserResultInstance;
	private static StorageManager storer;
	private static Result excutedResult;
	
	public Command (ParserResult ResultInstance) // constructor
	{
		parserResultInstance = ResultInstance;
		storer = new StorageManager();
	}
	
	public CommandResult executeCommand()
	{

		CommandResult executeResult = new CommandResult();
		doExecution();
		
		return executeResult;
	}

	private void doExecution() {
		String commandType = parserResultInstance.getCommand();
		Switch(commandType) {
			case 
		}
	}
	
	private void
	
}
