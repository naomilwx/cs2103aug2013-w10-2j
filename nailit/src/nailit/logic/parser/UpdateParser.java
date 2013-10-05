package nailit.logic.parser;

import nailit.common.NIConstants;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class UpdateParser extends Parser {

	private String userCommand;
	private String[] listOfCommand;
	
	public UpdateParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
		
		resultExecution.setCommand(CommandType.UPDATE);
		listOfCommand = userCommand.split(NIConstants.FIELD_SPLITTER);
		
		if (userCommand.equalsIgnoreCase("ALL")){
			resultExecution.setDisplayAll(true);
		}else if (Parser.isTaskID(userCommand)){
			resultExecution.setTaskID(Integer.parseInt(userCommand));
		}else{
			resultExecution.setStartTime(Parser.retrieveDateTime(userCommand));
		}
		return resultExecution;
	}
}
