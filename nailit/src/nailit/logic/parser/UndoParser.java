package nailit.logic.parser;

import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class UndoParser extends Parser {
	
	private String userCommand;
	
	public UndoParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
		
		resultExecution.setCommand(CommandType.UNDO);
		if (userCommand.equalsIgnoreCase("")){
			resultExecution.setTaskID(-1);
			return resultExecution;
		}
		return resultExecution;
	}
}