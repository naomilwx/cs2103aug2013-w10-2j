package nailit.logic.parser;

import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class CompleteParser extends Parser{

private String userCommand;
	
	public CompleteParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
		resultExecution.setCommand(CommandType.COMPLETE);
		if (Parser.isNumber(userCommand))
			resultExecution.setTaskID(Integer.parseInt(userCommand));
		else 
			throw new Error("Wrong Format");
		return resultExecution;
	}
}
