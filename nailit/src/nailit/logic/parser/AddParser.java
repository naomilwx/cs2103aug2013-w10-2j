package nailit.logic.parser;

import nailit.logic.ParserResult;

public class AddParser extends Parser {

	private String userCommand;
	private String[] listOfCommand = new String [10];
	
	public AddParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
		listOfCommand = userCommand.split(",");
		return resultExecution;
	}
}
