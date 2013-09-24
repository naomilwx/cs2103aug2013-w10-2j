package nailit.logic.parser;

import nailit.logic.ParserResult;

public class AddParser extends ParserManager {

	private String userCommand;
	
	public AddParser (String command)
	{
		userCommand = command;
	}
	
	@Override
	public ParserResult execute()
	{
		ParserResult resultExecution = new ParserResult();
		
		return resultExecution;
	}
}
