package nailit.logic.command;
import nailit.logic.*;

public class Command {
	
	public static ParserResult ParserResultInstance;
	
	public Command (ParserResult ResultInstance) // constructor
	{
		ParserResultInstance = ResultInstance;
	}
	
	public CommandResult executeCommand()
	{
		CommandResult executeResult = new CommandResult();
		StorageManagerStub storer = new StorageManagerStub();
		
		return executeResult;
	}
	
}
