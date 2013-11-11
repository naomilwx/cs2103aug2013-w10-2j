package nailit.logic.parser;

//@author A0105559B
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.logic.exception.InvalidCommandFormatException;

public class UncompleteParser extends Parser{

private String userCommand;
	
	public UncompleteParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute() throws InvalidCommandFormatException{
		ParserResult resultExecution = new ParserResult();
		resultExecution.setCommand(CommandType.UNCOMPLETE);
		
		if (userCommand.equals("")){
			throw new InvalidCommandFormatException(CommandType.UNCOMPLETE,ParserExceptionConstants.EMPTY_INPUT_STRING_UNCOMPLETE);
		}
		
		if (Parser.isNumber(userCommand)){
			resultExecution.setTaskId(Integer.parseInt(userCommand));
		}else{ 
			throw new InvalidCommandFormatException(CommandType.UNCOMPLETE, ParserExceptionConstants.NO_TASK_ID);
		}
		return resultExecution;
	}

}
