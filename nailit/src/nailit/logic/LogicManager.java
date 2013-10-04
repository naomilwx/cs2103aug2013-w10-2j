package nailit.logic;

import nailit.common.Result;
import nailit.logic.*;
import nailit.logic.command.*;
import nailit.logic.parser.*;


public class LogicManager{
	
//	private ParserResult ParserResultInstance;
	private Parser ParserInstance;
	private CommandManager commandInstance;
	
	// constructor
	public LogicManager() {
//		ParserInstance = new Parser();
		commandInstance = new CommandManager();
	}
	public Result executeCommand(String OriginalCommand){
		Result executeCommandResult = new Result();
		
		// Shuzhi: commands for Shuzhi and Binbin
		// my thought of the work flow is this: we only have one 
		// instance for ParserManager and CommandManager, which act 
		// like two proxies. And everytime there is a Original command,
		// the only instance manager will handle it, and then pass the
		// parser result to the only instance of CommmandManager.
		// For the history of done operations, we store it in the only 
		// instance of the CommandManager.
		// This is why we need a constructor
		ParserInstance = new Parser(OriginalCommand);
		ParserResult parserResultInstance = ParserInstance.execute();
		executeCommandResult = commandInstance.executeCommand(parserResultInstance);
		return  executeCommandResult;
	}
	
}
