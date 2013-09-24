package nailit.logic.command;
import nailit.logic.*;
import nailit.storage.StorageManager;

public class Command {
	
	public static ParserResult ParserResultInstance;
	
	public Command (ParserResult ResultInstance) // constructor
	{
		ParserResultInstance = ResultInstance;
	}
	
	public CommandResult executeCommand()
	{
		CommandResult executeResult = new CommandResult();
		StorageManager storer = new StorageManager();
		
		
		return executeResult;
	}
	
}
