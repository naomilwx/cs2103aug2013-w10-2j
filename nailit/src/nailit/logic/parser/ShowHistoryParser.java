package nailit.logic.parser;

import nailit.common.NIConstants;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class ShowHistoryParser extends Parser {
	
	private String userCommand;
	
	public ShowHistoryParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
	
		resultExecution.setCommand(CommandType.SHOWHISTORY);
		
		return resultExecution;
	}
}