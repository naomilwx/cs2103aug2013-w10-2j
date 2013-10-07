package nailit.logic;

import nailit.common.Result;
import nailit.logic.*;
import nailit.logic.command.*;
import nailit.logic.parser.*;
import nailit.storage.FileCorruptionException;


public class LogicManager{
	
	private ParserManager ParserInstance;
	private CommandManager commandInstance;
	
	public LogicManager() throws FileCorruptionException {
		ParserInstance = new ParserManager();
		commandInstance = new CommandManager();
	}
	public Result executeCommand(String OriginalCommand){
		Result executeCommandResult = new Result();
		ParserInstance.passCommand(OriginalCommand);
		ParserResult parserResultInstance = ParserInstance.execute();
		executeCommandResult = commandInstance.executeCommand(parserResultInstance);
		return  executeCommandResult;
	}
	
}
