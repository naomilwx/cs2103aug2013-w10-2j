package nailit.logic;

import nailit.logic.command.Command;
import nailit.logic.parser.Parser;
import java.util.*;


public class LogicManager {
	
	protected ParserResult ParserResultInstance;
	protected Parser ParserInstance;
	protected Command CommandInstance;
	
	protected Result executeCommand(String OriginalCommand)
	{
		Result executeCommandResult = new Result();
		ParserInstance = new Parser(OriginalCommand);
		
		return  executeCommandResult;
	}
	
}
