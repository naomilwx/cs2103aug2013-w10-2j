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

		Result executeResult = new Result();
		StorageManagerStub storer = new StorageManagerStub();

		return executeResult;
	}
	
}
