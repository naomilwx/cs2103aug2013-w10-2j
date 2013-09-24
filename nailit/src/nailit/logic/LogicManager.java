package nailit.logic;

import nailit.logic.*;
import nailit.logic.command.*;
import nailit.logic.parser.*;
import nailit.Result;


public class LogicManager {
	
	private ParserResult ParserResultInstance;
	private Parser ParserInstance;
	private Command CommandInstance;
	
	protected Result executeCommand(String OriginalCommand)
	{
		Result executeCommandResult = new Result();
		ParserInstance = new Parser(OriginalCommand);
		ParserResultInstance = ParserInstance.execute();
		CommandInstance = new Command(ParserResultInstance);
		executeCommandResult = CommandInstance.executeCommand();
		return  executeCommandResult;
	}
	
}
