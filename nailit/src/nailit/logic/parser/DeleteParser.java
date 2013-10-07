package nailit.logic.parser;

import nailit.common.NIConstants;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class DeleteParser extends Parser {
	
	private String userCommand;
	private String[] listOfCommand;
	
	public DeleteParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
		listOfCommand = userCommand.split(NIConstants.NORMAL_FIELD_SPLITTER);
	
		resultExecution.setCommand(CommandType.DELETE);
		resultExecution.setTaskID(Integer.parseInt(userCommand));
		
		return resultExecution;
	}
}
