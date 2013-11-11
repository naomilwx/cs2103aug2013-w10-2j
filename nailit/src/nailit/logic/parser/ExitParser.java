package nailit.logic.parser;

//@author A0105559B
import nailit.common.CommandType;
import nailit.logic.ParserResult;

public class ExitParser extends Parser {
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
		// Set the commandType
		resultExecution.setCommand(CommandType.EXIT);
		
		return resultExecution;
	}
}