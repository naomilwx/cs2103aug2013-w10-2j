package nailit.logic.parser;
import nailit.common.TaskPriority;
import nailit.logic.CommandType;
import nailit.logic.command.*;
import nailit.logic.ParserResult;

public class ParserManager {

	private String commandToExecute;

	public ParserManager() {
		commandToExecute = "";
	}

	public void passCommand(String command){
		commandToExecute = command;
	}

	public ParserResult execute() {
		String commandTypeString = getFirstWord(commandToExecute);
		CommandType commandType = determineCommandType(commandTypeString);
		commandToExecute = commandToExecute.substring(commandToExecute.trim().indexOf(' ')+1);
		commandToExecute = commandToExecute.trim();
		switch (commandType) {
		case ADD:
			try {
				AddParser addParserManager = new AddParser(commandToExecute);
				return addParserManager.execute();
			}
			catch (Exception e) {
				System.out.print(e);
			}
		case COMPLETE:
			CompleteParser completeParserManager = new CompleteParser(commandToExecute);
			return completeParserManager.execute();
		case DELETE:
			try {
				DeleteParser deleteParserManager = new DeleteParser(commandToExecute);
				return deleteParserManager.execute();
			}
			catch (Exception e) {
				System.out.println(e);
			}
		case DISPLAY:
			DisplayParser displayParserManager = new DisplayParser(commandToExecute);
			return displayParserManager.execute();
		case REDO:
			if (commandToExecute.equalsIgnoreCase("REDO"))
				commandToExecute = "";
			RedoParser redoParserManager = new RedoParser(commandToExecute);
			return redoParserManager.execute();
		case SEARCH:
			SearchParser searchParserManager = new SearchParser(commandToExecute);
			return searchParserManager.execute();
		case SHOWHISTORY:
			ShowHistoryParser showHistoryParserManager = new ShowHistoryParser(commandToExecute);
			return showHistoryParserManager.execute();
		case UNCOMPLETE:
			UncompleteParser uncompleteParserManager = new UncompleteParser(commandToExecute);
			return uncompleteParserManager.execute();
		case UNDO:
			if (commandToExecute.equalsIgnoreCase("UNDO"))
				commandToExecute = "";
			UndoParser undoParserManager = new UndoParser(commandToExecute);
			return undoParserManager.execute();
		case UPDATE:
			UpdateParser updateParserManager = new UpdateParser(commandToExecute);
			return updateParserManager.execute();
		case ADDREMINDER:
			AddReminderParser AddReminderParserManager = new AddReminderParser(commandToExecute);
			return AddReminderParserManager.execute();
		case EXIT:
			ExitParser exitParserManager = new ExitParser(commandToExecute);
			return exitParserManager.execute();
		default:
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
		if (CommandType.isCommandType(commandTypeString))
			return CommandType.valueOf(commandTypeString.toUpperCase());
		else
			return CommandType.isApproximateCommandType(commandTypeString);
	}
	
}
