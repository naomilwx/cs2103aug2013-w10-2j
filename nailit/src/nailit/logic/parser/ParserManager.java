package nailit.logic.parser;

//@author A0105559B
import nailit.common.CommandType;
import nailit.common.NIConstants;
import nailit.logic.ParserResult;
import nailit.logic.exception.InvalidCommandFormatException;
import nailit.logic.exception.InvalidCommandTypeException;

public class ParserManager {

	private String commandToExecute;

	public ParserManager() {
		commandToExecute = "";
	}

	public void passCommand(String command){
		commandToExecute = command.trim();
	}

	public ParserResult execute() throws InvalidCommandFormatException, InvalidCommandTypeException{
		// retrieve the first word of the input string
		String commandTypeString = getFirstWord(commandToExecute);
		// determine the command type
		CommandType commandType = determineCommandType(commandTypeString);
		// remove the first word in the input string
		commandToExecute = removeFirstWord(commandToExecute);
		// check whether the string contains reserved string
		if (commandToExecute.contains(NIConstants.HARDDISK_FIELD_SPLITTER)){
			throw new InvalidCommandFormatException(ParserExceptionConstants.RESERVE_WORD_CLASH);
		}
		// create different classes for different commands
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
		case DELETEREMINDER:
			DeleteReminderParser DeleteReminderParserManager = new DeleteReminderParser(commandToExecute);
			return DeleteReminderParserManager.execute();
		case DISPLAY:
			DisplayParser displayParserManager = new DisplayParser(commandToExecute);
			return displayParserManager.execute();
		case REDO:
			RedoParser redoParserManager = new RedoParser(commandToExecute);
			return redoParserManager.execute();
		case SEARCH:
			SearchParser searchParserManager = new SearchParser(commandToExecute);
			return searchParserManager.execute();
		case UNCOMPLETE:
			UncompleteParser uncompleteParserManager = new UncompleteParser(commandToExecute);
			return uncompleteParserManager.execute();
		case UNDO:
			UndoParser undoParserManager = new UndoParser(commandToExecute);
			return undoParserManager.execute();
		case UPDATE:
			UpdateParser updateParserManager = new UpdateParser(commandToExecute);
			return updateParserManager.execute();
		case ADDREMINDER:
			AddReminderParser AddReminderParserManager = new AddReminderParser(commandToExecute);
			return AddReminderParserManager.execute();
		case EXIT:
			ExitParser exitParserManager = new ExitParser();
			return exitParserManager.execute();
		default:
			throw new InvalidCommandTypeException(ParserExceptionConstants.INVLID_COMMAND);
		}
	}
	
	private static String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}
	
	private static String removeFirstWord(String userCommand){
		if (userCommand.trim().indexOf(' ') != -1){
			userCommand = userCommand.substring(userCommand.trim().indexOf(' ')+1);
			userCommand = userCommand.trim();
			return userCommand;
		}else{
			return "";
		}
	}
	
	private static CommandType determineCommandType(String commandTypeString) throws InvalidCommandTypeException {
		if (commandTypeString == null || commandTypeString.isEmpty()){
			throw new InvalidCommandTypeException(ParserExceptionConstants.EMPTY_COMMAND_TYPE);
		}
		if (CommandType.isCommandType(commandTypeString)){
			return CommandType.valueOf(commandTypeString.toUpperCase());
		}else{
			return CommandType.isApproximateCommandType(commandTypeString);
		}
	}
	
}
