package nailit.logic.parser;

import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class UncompleteParser extends Parser{

private String userCommand;
	
	public UncompleteParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
		resultExecution.setCommand(CommandType.UNCOMPLETE);
		if (Parser.isNumber(userCommand))
			resultExecution.setTaskId(Integer.parseInt(userCommand));
		else 
			throw new Error("Wrong Format");
		return resultExecution;
	}

}
