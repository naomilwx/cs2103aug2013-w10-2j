package nailit.logic;

import nailit.logic.*;
import nailit.logic.command.*;
import nailit.logic.parser.*;

public enum COMMAND_TYPE {
	ADD, COMPLETE, DELETE, SEARCH, UPDATE
}

public class LogicManager {
	
	private ParserResult ParserResultInstance;
	private Parser ParserInstance;
	private Command CommandInstance;
	
	protected CommandResult executeCommand(String OriginalCommand)
	{
		CommandResult executeCommandResult = new CommandResult();
		ParserInstance = new Parser(OriginalCommand);
		ParserResultInstance = ParserInstance.execute();
		CommandInstance = new Command(ParserResultInstance);
		executeCommandResult = CommandInstance.executeCommand();
		return  executeCommandResult;
	}
	
}
