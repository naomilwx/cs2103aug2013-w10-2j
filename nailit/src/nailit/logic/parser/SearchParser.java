package nailit.logic.parser;

import nailit.logic.ParserResult;

public class SearchParser extends Parser {

	private String userCommand;
	
	public SearchParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
		return resultExecution;
	}
}
