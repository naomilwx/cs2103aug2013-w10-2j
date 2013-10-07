package nailit.logic.parser;

import nailit.common.NIConstants;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class DisplayParser extends Parser {
	
	private String userCommand;
	private String[] listOfCommand;
	
	public DisplayParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
		listOfCommand = userCommand.split(NIConstants.NORMAL_FIELD_SPLITTER);
	
		resultExecution.setCommand(CommandType.DISPLAY);
		
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
