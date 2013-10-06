package nailit.logic.parser;

import nailit.common.NIConstants;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class ShowHistoryParser extends Parser {
	
	private String userCommand;
	private String[] listOfCommand;
	
	public ShowHistoryParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
		listOfCommand = userCommand.split(NIConstants.NORMAL_FIELD_SPLITTER);
	
		resultExecution.setCommand(CommandType.SHOWHISTORY);
		resultExecution.setTaskID(Integer.parseInt(userCommand));
		
		return resultExecution;
	}
}