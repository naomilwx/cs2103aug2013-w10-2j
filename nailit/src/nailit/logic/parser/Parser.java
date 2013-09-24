package nailit.logic.parser;
import nailit.logic.*;

public class Parser {

	public static String CommandToExecute;
	
	public Parser (String command)
	{
		CommandToExecute = command;
	}

	public ParserResult execute() {
		ParserResult ParserResultInstance = new ParserResult();
		return ParserResultInstance;
	}
	
	
}
