package nailit.logic;

import nailit.common.NIConstants;
import nailit.common.Result;
import nailit.logic.*;
import nailit.logic.command.*;
import nailit.logic.parser.*;
import nailit.storage.FileCorruptionException;


public class LogicManager{
	
	private ParserManager parserInstance;
	private CommandManager commandInstance;
	
	public LogicManager() throws FileCorruptionException {
		parserInstance = new ParserManager();
		commandInstance = new CommandManager();
	}
	public Result executeCommand(String OriginalCommand) throws Exception{
		Result executeCommandResult = new Result();
		parserInstance.passCommand(OriginalCommand);
		ParserResult parserResultInstance = parserInstance.execute();
//		System.out.println(parserResultInstance.getStartTime().toString(NIConstants.DISPLAY_DATE_FORMAT));
		executeCommandResult = commandInstance.executeCommand(parserResultInstance);
		return  executeCommandResult;
	}
}
