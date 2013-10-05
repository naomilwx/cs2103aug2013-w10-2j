package nailit.logic.parser;

import nailit.logic.ParserResult;

public class DeleteParser extends Parser {
	
	private String userCommand;
	
	public DeleteParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
		return resultExecution;
	}
}
