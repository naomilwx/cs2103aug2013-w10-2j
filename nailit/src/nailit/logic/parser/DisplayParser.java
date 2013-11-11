package nailit.logic.parser;

//@author A0105559B
import nailit.common.NIConstants;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.logic.exception.InvalidCommandFormatException;

public class DisplayParser extends Parser {
	
	private String userCommand;
	private String[] listOfCommands;
	
	public DisplayParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute() throws InvalidCommandFormatException{
		ParserResult resultExecution = new ParserResult();
		listOfCommands = userCommand.split(NIConstants.NORMAL_FIELD_SPLITTER);
	
		resultExecution.setCommand(CommandType.DISPLAY);
		if (userCommand.equals("")){
			throw new InvalidCommandFormatException(CommandType.DISPLAY,ParserExceptionConstants.EMPTY_INPUT_STRING_DISPLAY);
		}
		
		if (listOfCommands[0].equalsIgnoreCase("ALL")){
			resultExecution.setDisplayAll(true);
		}else if (listOfCommands[0].equalsIgnoreCase("HISTORY")){
			resultExecution.setDisplayHistory(true);
		}else if (listOfCommands[0].equalsIgnoreCase("COMPLETE")){
			resultExecution.setDisplayComplete(true);
		}else if (listOfCommands[0].equalsIgnoreCase("UNCOMPLETE")){
			resultExecution.setDisplayUncomplete(true);
		}else if (Parser.isTaskID(userCommand)){
			resultExecution.setTaskId(Integer.parseInt(userCommand));
		}else if (Parser.isDateTime(userCommand)){
			resultExecution.setEndTime(Parser.retrieveDateTime(userCommand));
		}else{
			throw new InvalidCommandFormatException(CommandType.DISPLAY,ParserExceptionConstants.INVALID_STRING);
		}
		
		return resultExecution;
	}
}
