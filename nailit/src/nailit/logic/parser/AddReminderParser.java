package nailit.logic.parser;

// @author A0105559B
// This is the parser for addReminder command

import nailit.common.NIConstants;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.logic.exception.InvalidCommandFormatException;

public class AddReminderParser extends Parser{

	private String userCommand;
	private String[] listOfCommands;
	
	public AddReminderParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute() throws InvalidCommandFormatException{
		ParserResult resultExecution = new ParserResult();
		//Set the commandType
		resultExecution.setCommand(CommandType.ADDREMINDER);
		//Input string cannot be null
		if (userCommand.equals("")){
			throw new InvalidCommandFormatException(CommandType.ADDREMINDER,ParserExceptionConstants.EMPTY_INPUT_STRING_ADDREMINDER);
		}
		
		boolean isContainsId = false;
		// Split the string using comma
		listOfCommands = userCommand.split(NIConstants.NORMAL_FIELD_SPLITTER);
		for (int i=0; i<listOfCommands.length; i++) {
			listOfCommands[i] = listOfCommands[i].trim();
			if (Parser.isNumber(listOfCommands[i])){
				isContainsId = true;
				resultExecution.setTaskId(Integer.parseInt(listOfCommands[i]));
				System.out.println(listOfCommands[i]);
			}else if (Parser.isDateTime(listOfCommands[i])) {
				resultExecution.setReminderTime(Parser.retrieveDateTime(listOfCommands[i]));
			} 
		}
		// If the string does not contain ID, it will throw wrong form
		if (!isContainsId){
			throw new InvalidCommandFormatException(CommandType.ADDREMINDER,"Wrong Format: The ID should be specified");
		}
		return resultExecution;
	}

}
