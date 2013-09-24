package nailit.logic.parser;

import nailit.logic.ParserResult;

public class UpdateParser extends ParserManager {

	private String userCommand;
	
	public UpdateParser (String command)
	{
		userCommand = command;
	}
	
	public ParserResult execute()
	{
		ParserResult resultExecution = new ParserResult();
		return resultExecution;
	}
}
