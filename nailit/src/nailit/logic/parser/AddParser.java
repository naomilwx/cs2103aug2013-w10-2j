package nailit.logic.parser;

//@author A0105559B
// This is the parser for add commands

import nailit.logic.ParserResult;
import nailit.common.CommandType;
import nailit.common.NIConstants;
import nailit.common.TaskPriority;
import nailit.logic.exception.InvalidCommandFormatException;

public class AddParser extends Parser {

	private String userCommand;
	private String[] listOfCommands;
	private String name="";
	
	public AddParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute() throws InvalidCommandFormatException {
		ParserResult resultExecution = new ParserResult();
		// Set commandType
		resultExecution.setCommand(CommandType.ADD);
		// Input string cannot be null
		if (userCommand.equals("")){
			throw new InvalidCommandFormatException(CommandType.ADD,ParserExceptionConstants.EMPTY_INPUT_STRING_ADD);
		}
		// Set Description
		int startIndex = -1, endIndex = 0;
		startIndex = userCommand.indexOf('(');
		if (startIndex!=-1){
			for (int i=startIndex+1; i<userCommand.length(); i++){
				if (userCommand.charAt(i)==')'){
					endIndex = i;
				}
			}
			if (endIndex == 0){
				throw new InvalidCommandFormatException (CommandType.ADD,ParserExceptionConstants.BRACKET_UNMATCHED);
			}
			
			resultExecution.setDescription(userCommand.substring(startIndex+1, endIndex));
			userCommand = userCommand+" ";
			
			String temp = userCommand.substring(endIndex+1, userCommand.length());
			int index = -1;
			for (int i=startIndex; i>0; i--){
				if (userCommand.charAt(i) == ','){
					index = i;
					break;
				}
			}
			
			if (index != -1){
				userCommand = userCommand.substring(0,index);
			}
			
			userCommand += temp;
		}
		// Split the string using comma
		listOfCommands = userCommand.split(NIConstants.NORMAL_FIELD_SPLITTER);
		for (int i=0; i<listOfCommands.length; i++) {
			listOfCommands[i] = listOfCommands[i].trim();
			// Check whether the string represents priority
			if (TaskPriority.isTaskPriority(listOfCommands[i])) {
				resultExecution.setPriority(TaskPriority.valueOf(listOfCommands[i].toUpperCase()));
			// Check whether the string represents tag
			}else if (Parser.isTag(listOfCommands[i])) {
				resultExecution.setTag(listOfCommands[i]);
			// Check whether the string represents start time or end time
			}else if (Parser.isDateTime(listOfCommands[i])) {
				if (resultExecution.getStartTime() == null) {
					if (Parser.numberOfTime(listOfCommands[i]) == 2) {
						resultExecution.setStartTime(Parser.retrieveDateTimeFirst(listOfCommands[i]));
						resultExecution.setEndTime(Parser.retrieveDateTimeSecond(listOfCommands[i]));
					} else {
						resultExecution.setStartTime(Parser.retrieveDateTime(listOfCommands[i]));
					}
				} else { 
					resultExecution.setEndTime(Parser.retrieveDateTime(listOfCommands[i]));
				}
			// Set name of the task
			} else{ 
				name+= listOfCommands[i]+" ";
			}
		}
		// The name of the task cannot be null
		if (name.equals("")){
			throw new InvalidCommandFormatException (CommandType.ADD,ParserExceptionConstants.NO_NAME);
		}
		name = name.substring(0,name.length()-1);
		resultExecution.setName(name);
		
		if (!resultExecution.isNullStartTime() && resultExecution.isNullEndTime()){
			resultExecution.setEndTime(resultExecution.getStartTime());
			resultExecution.setStartTime(null);
		}
		return resultExecution;
	}
}
