package nailit.logic.parser;
import TextBuddy.COMMAND_TYPE;
import nailit.logic.*;

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
			return addCommand(CommandToExecute);
		case CLEAR:
			return clearCommand(CommandToExecute);
		case DELETE:
			return deleteCommand(CommandToExecute);
		case DISPLAY:
			return displayCommand(CommandToExecute);
		case SORT:
			return sortCommand(CommandToExecute);
		case SEARCH:
			return searchCommand(uCommandToExecute);
		default:
			//throw an error if the command is not recognized
			throw new Error("Unrecognized command type");
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
		} else if (commandTypeString.equalsIgnoreCase("delete")) {
			return COMMAND_TYPE.DELETE;
		} else if (commandTypeString.equalsIgnoreCase("clear")){
			return COMMAND_TYPE.CLEAR;
		} else if (commandTypeString.equalsIgnoreCase("display")){
			return COMMAND_TYPE.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase("sort")){
			return COMMAND_TYPE.SORT;
		} else if (commandTypeString.equalsIgnoreCase("search")){
			return COMMAND_TYPE.SEARCH;
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}
	
}
