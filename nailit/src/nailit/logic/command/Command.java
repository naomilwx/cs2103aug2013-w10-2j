package nailit.logic.command;
import nailit.logic.*;

public class Command {
	
	public static ParserResult ParserResultInstance;
	
	public Command (ParserResult ResultInstance) // constructor
	{
		ParserResultInstance = ResultInstance;
	}
	
	public Result executeCommand()
	{
		Result executeResult = new Result();
		// add the method
		
		return executeResult;
	}
	
}
