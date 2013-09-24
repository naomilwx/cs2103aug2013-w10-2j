package nailit.logic.parser;
import nailit.logic.COMMAND_TYPE;
import nailit.logic.command.*;
import nailit.logic.ParserResult;

public class Parser {

	public static String CommandToExecute;
	
	public Parser (String command)
	{
		CommandToExecute = command;
	}

	public ParserResult execute() {
		ParserResult ParserResultInstance = new ParserResult();
		String commandTypeString = getFirstWord(CommandToExecute);
		COMMAND_TYPE commandType = determineCommandType(commandTypeString);
		switch (commandType) {
		case ADD:
		{
			AddParser addParserManager = new AddParser();
			return AddParser.execute(CommandToExecute);
		}
		case COMPLETE:
		{
			//return completeCommand(CommandToExecute);
		}
		case DELETE:
		{
			//return deleteCommand(CommandToExecute);
		}
		case SEARCH:
		{
			//return searchCommand(uCommandToExecute);
		}
		case UPDATE:
		{
			
		}
		default:
			//throw an error if the command is not recognized
			//throw new Error("Unrecognized command type");
		}
		return ParserResultInstance;
	}
	
	private static String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}
	
	private static COMMAND_TYPE determineCommandType(String commandTypeString) {
		if (commandTypeString == null)
			throw new Error("command type string cannot be null!");

		if (commandTypeString.equalsIgnoreCase("add")) {
			return COMMAND_TYPE.ADD;
		} else if (commandTypeString.equalsIgnoreCase("complete")) {
			return COMMAND_TYPE.COMPLETE;
		} else if (commandTypeString.equalsIgnoreCase("delete")) {
			return COMMAND_TYPE.DELETE;
		} else if (commandTypeString.equalsIgnoreCase("search")){
			return COMMAND_TYPE.SEARCH;
		} else if (commandTypeString.equalsIgnoreCase("update")) {
			return COMMAND_TYPE.UPDATE;
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}
	
}
