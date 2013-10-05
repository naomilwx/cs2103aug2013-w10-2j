package nailit.logic.parser;

import nailit.logic.ParserResult;
import nailit.common.Task;
import org.joda.time.DateTime;

public abstract class Parser {

	public abstract ParserResult execute();
	
	public static boolean isDateTime(String p){
		DateTime result;
		return true;
	}
	
	public static DataTime retrieveDateTime (String p){
		
	}
}
