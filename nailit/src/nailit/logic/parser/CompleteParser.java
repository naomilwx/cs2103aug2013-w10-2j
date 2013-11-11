package nailit.logic.parser;

//@author A0105559B
import nailit.logic.CommandType;
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
		
		resultExecution.setCommand(CommandType.COMPLETE);
		if (userCommand.equals("")){
			throw new InvalidCommandFormatException(CommandType.COMPLETE,ParserExceptionConstants.EMPTY_INPUT_STRING_COMPLETE);
		}
		
		if (Parser.isNumber(userCommand)){
			resultExecution.setTaskId(Integer.parseInt(userCommand));
		}else{ 
			throw new InvalidCommandFormatException(CommandType.COMPLETE,ParserExceptionConstants.NO_TASK_ID);
		}
		return resultExecution;
	}
}
