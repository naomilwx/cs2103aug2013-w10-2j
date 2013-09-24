package nailit.logic;

import nailit.logic.command.Command;
import nailit.logic.parser.Parser;
import java.util.*;


public class LogicManager {
	
	private ParserResult ParserResultInstance;
	private Parser ParserInstance;
	private Command CommandInstance;
	
	protected Result executeCommand(String OriginalCommand)
	{
		Result executeCommandResult = new Result();
		ParserInstance = new Parser(OriginalCommand);
		CommandInstance = new Command(ParserInstance.execute());
		
		return  executeCommandResult;
	}
	
}
