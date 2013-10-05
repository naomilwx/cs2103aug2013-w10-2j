package nailit.logic.parser;

import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class UpdateParser extends Parser {

	private String userCommand;
	
	public UpdateParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
		
		resultExecution.setCommand(CommandType.UPDATE);
		
		if (userCommand.equalsIgnoreCase("ALL")){
			resultExecution.setDisplayAll(true);
		}else if (Parser.isTaskID(userCommand)){
			resultExecution.setTaskID(Integer.parseInt(userCommand));
		}else if (Parser.isDateTime(userCommand)){
			resultExecution.setStartTime(Parser.retrieveDateTime(userCommand));
		}
		return resultExecution;
	}
}
