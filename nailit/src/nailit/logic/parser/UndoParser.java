package nailit.logic.parser;

import nailit.common.NIConstants;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class UndoParser extends Parser {
	
	private String userCommand;
	private String[] listOfCommand;
	
	public UndoParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
		listOfCommand = userCommand.split(NIConstants.NORMAL_FIELD_SPLITTER);
	
		if (userCommand.equalsIgnoreCase(""))
			resultExecution.setTaskID(-1);
		resultExecution.setCommand(CommandType.UNDO);
		resultExecution.setTaskID(Integer.parseInt(userCommand));
		
		return resultExecution;
	}
}