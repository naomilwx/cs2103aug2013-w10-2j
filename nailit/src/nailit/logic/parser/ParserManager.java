package nailit.logic.parser;
import nailit.logic.CommandType;
import nailit.logic.command.*;
import nailit.logic.ParserResult;

public class ParserManager {

	private String commandToExecute;
	
	public void passCommand(String command){
		commandToExecute = command;
	}

	public ParserResult execute() {
		String commandTypeString = getFirstWord(commandToExecute);
		CommandType commandType = determineCommandType(commandTypeString);
		switch (commandType) {
		case ADD:
			AddParser addParserManager = new AddParser(commandToExecute);
			return addParserManager.execute();
		case COMPLETE:
			CompleteParser completeParserManager = new CompleteParser(commandToExecute);
			return completeParserManager.execute();
		case DELETE:
			DeleteParser deleteParserManager = new DeleteParser(commandToExecute);
			return deleteParserManager.execute();
		case DISPLAY:
			DisplayParser displayParserManager = new DisplayParser(commandToExecute);
			return displayParserManager.execute();
		case SEARCH:
			SearchParser searchParserManager = new SearchParser(commandToExecute);
			return searchParserManager.execute();
		case UPDATE:
			UpdateParser updateParserManager = new UpdateParser(commandToExecute);
			return updateParserManager.execute();
		case EXIT:
			ExitParser exitParserManager = new ExitParser(commandToExecute);
			return exitParserManager.execute();
		default:
			//throw an error if the command is not recognized
			throw new Error("Unrecognized command type");
		}
	}
	
	private static String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}
	
	private static CommandType determineCommandType(String commandTypeString) {
		if (commandTypeString == null)
			throw new Error("command type string cannot be null!");

		if (commandTypeString.equalsIgnoreCase("add")) {
			return CommandType.ADD;
		} else if (commandTypeString.equalsIgnoreCase("complete")) {
			return CommandType.COMPLETE;
		} else if (commandTypeString.equalsIgnoreCase("delete")) {
			return CommandType.DELETE;
		} else if (commandTypeString.equalsIgnoreCase("display")) {
			return CommandType.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase("exit")) {
			return CommandType.EXIT;
		} else if (commandTypeString.equalsIgnoreCase("search")){
			return CommandType.SEARCH;
		} else if (commandTypeString.equalsIgnoreCase("showhistory")) {
			return CommandType.SHOWHISTORY;
		} else if (commandTypeString.equalsIgnoreCase("undo")) {
			return CommandType.UNDO;
		} else if (commandTypeString.equalsIgnoreCase("update")) {
			return CommandType.UPDATE;
		} else {
			return CommandType.INVALID;
		}
	}
	
}
