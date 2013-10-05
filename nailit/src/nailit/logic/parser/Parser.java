package nailit.logic.parser;

import nailit.logic.ParserResult;
import nailit.common.Task;
import org.joda.time.DateTime;
import com.joestelmach.natty.*;
import java.util.*;

public abstract class Parser {

	public abstract ParserResult execute();
	
	public static boolean isDateTime(String p){
		return true;
	}
	
	public static DateTime retrieveDateTime (String p){
		DateTime result = new DateTime();
		
		List<DateGroup> parseResult;
		com.joestelmach.natty.Parser nattyParser = new com.joestelmach.natty.Parser();
		
		parseResult= nattyParser.parse(p);
		result = parseResult.get(0);
		
		return result;
	}
	
	public static boolean isTaskID(String p){
		for (int i=0; i<p.length(); i++)
			if (p.charAt(i)<'0' && p.charAt(i)>'9')
				return false;
		return true;
	}
	
	public static boolean isTag(String p){
		if (p.charAt(0)=='#' && p.charAt(p.length()-1)=='#')
			return true;
		else
			return false;
	}
}
