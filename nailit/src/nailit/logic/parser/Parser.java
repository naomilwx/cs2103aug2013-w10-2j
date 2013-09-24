package nailit.logic.parser;
import nailit.logic.*;

public class Parser {

	public static String executeCommand;
	
	public Parser (String command)
	{
		executeCommand = command;
	}
	
	public ParserResult execute()
	{
		ParserResult ParserResultInstance = new ParserResult();
		return ParserResultInstance;
	}
	
	
}
