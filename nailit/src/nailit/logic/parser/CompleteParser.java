package nailit.logic.parser;

import nailit.logic.ParserResult;

public class CompleteParser extends ParserManager{

private String userCommand;
	
	public CompleteParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
		return resultExecution;
	}

}
