package nailit.logic.parser;

//@author A0105559B
import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class DeleteParser extends Parser {
	
	private String userCommand;
	
	public DeleteParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
	
		resultExecution.setCommand(CommandType.DELETE);
		if (Parser.isNumber(userCommand))
			resultExecution.setTaskId(Integer.parseInt(userCommand));
		else 
			throw new Error("Wrong Format");
		
		return resultExecution;
	}
}
