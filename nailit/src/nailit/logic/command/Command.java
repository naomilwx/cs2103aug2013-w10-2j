package nailit.logic.command;
import nailit.common.Result;
import nailit.logic.*;
import nailit.storage.StorageManager;

public class Command {
	
	public static ParserResult ParserResultInstance;
	
	public Command (ParserResult ResultInstance) // constructor
	{
		ParserResultInstance = ResultInstance;
	}
	
	public Result executeCommand()
	{

		Result executeResult = new Result();
		StorageManager storer = new StorageManager();
		
		return executeResult;
	}
	
}
