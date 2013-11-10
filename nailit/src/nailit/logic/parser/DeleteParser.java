package nailit.logic.parser;

//@author A0105559B
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.logic.exception.InvalidCommandFormatException;

public class DeleteParser extends Parser {
	
	private String userCommand;
	
	public DeleteParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute() throws InvalidCommandFormatException{
		ParserResult resultExecution = new ParserResult();
	
		resultExecution.setCommand(CommandType.DELETE);
		if (userCommand.equals("")){
			throw new InvalidCommandFormatException(CommandType.DELETE,"Wrong Format: Cannot add an empth task, please specify your task name");
		}
		
		if (Parser.isNumber(userCommand)){
			resultExecution.setTaskId(Integer.parseInt(userCommand));
		}else{ 
			throw new InvalidCommandFormatException(CommandType.DELETE,"Wrong Format: the ID should be specified");
		}
		
		return resultExecution;
	}
}
