package nailit.logic.parser;

//@author A0105559B
import nailit.logic.CommandType;
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
		String commandTypeString = getFirstWord(commandToExecute);
		CommandType commandType = determineCommandType(commandTypeString);
		commandToExecute = commandToExecute.substring(commandToExecute.trim().indexOf(' ')+1);
		commandToExecute = commandToExecute.trim();
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
			if (commandToExecute.equalsIgnoreCase("REDO")){
				commandToExecute = "";
			}
			RedoParser redoParserManager = new RedoParser(commandToExecute);
			return redoParserManager.execute();
		case SEARCH:
			SearchParser searchParserManager = new SearchParser(commandToExecute);
			return searchParserManager.execute();
		case UNCOMPLETE:
			UncompleteParser uncompleteParserManager = new UncompleteParser(commandToExecute);
			return uncompleteParserManager.execute();
		case UNDO:
			if (commandToExecute.equalsIgnoreCase("UNDO")){
				commandToExecute = "";
			}
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
			throw new InvalidCommandTypeException("This is an invalid command");
		}
	}
	
	private static String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}
	
	private static CommandType determineCommandType(String commandTypeString) throws InvalidCommandTypeException {
		if (commandTypeString == null){
			throw new InvalidCommandTypeException("Command type string cannot be null!");
		}
		if (CommandType.isCommandType(commandTypeString)){
			return CommandType.valueOf(commandTypeString.toUpperCase());
		}else{
			return CommandType.isApproximateCommandType(commandTypeString);
		}
	}
	
}
