package nailit.logic.parser;

import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class ExitParser extends Parser {
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
	
		resultExecution.setCommand(CommandType.EXIT);
		
		return resultExecution;
	}
}