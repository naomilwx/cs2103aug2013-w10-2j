package nailit.logic.parser;

// @author A0105559B
import nailit.common.CommandType;
import nailit.logic.ParserResult;
import nailit.logic.exception.InvalidCommandFormatException;

public class CompleteParser extends Parser{

private String userCommand;
	
	public CompleteParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute() throws InvalidCommandFormatException{
		ParserResult resultExecution = new ParserResult();
		// Set commandType
		resultExecution.setCommand(CommandType.COMPLETE);
		// The input String cannot be empty
		if (userCommand.equals("")){
			throw new InvalidCommandFormatException(CommandType.COMPLETE,ParserExceptionConstants.EMPTY_INPUT_STRING_COMPLETE);
		}
		// Test whether the string contains number
		if (Parser.isNumber(userCommand)){
			resultExecution.setTaskId(Integer.parseInt(userCommand));
		}else{ 
			throw new InvalidCommandFormatException(CommandType.COMPLETE,ParserExceptionConstants.NO_TASK_ID);
		}
		return resultExecution;
	}
}
