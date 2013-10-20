package nailit.logic.parser;

import nailit.common.NIConstants;
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
			resultExecution.setTaskID(Integer.parseInt(userCommand));
		else 
			System.out.println("Wrong Format");
		
		return resultExecution;
	}
}
