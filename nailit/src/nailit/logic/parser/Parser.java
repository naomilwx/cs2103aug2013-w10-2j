package nailit.logic.parser;

import nailit.logic.ParserResult;
import nailit.common.Task;
import org.joda.time.DateTime;
import com.joestelmach.natty.*;

public abstract class Parser {

	public abstract ParserResult execute();
	
	public static boolean isDateTime(String p){
		return true;
	}
	
	public static DateTime retrieveDateTime (String p){
		DateTime result = new DateTime();	
		return result;
	}
	
	public static boolean isTaskID(){
		return true;
	}
	
	public static boolean isTag(String p){
		if (p.charAt(0)=='#' && p.charAt(p.length()-1)=='#')
			return true;
		else
			return false;
	}
}
