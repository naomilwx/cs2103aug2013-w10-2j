package nailit.logic.parser;

import nailit.logic.ParserResult;
import nailit.common.NIConstants;
import nailit.common.Task;
import org.joda.time.DateTime;
import com.joestelmach.natty.DateGroup;
import java.util.*;

public abstract class Parser {

	public abstract ParserResult execute();
	
	public static DateTime retrieveDateTime (String p){
		DateTime result;
		com.joestelmach.natty.Parser nattyParser;
		
		nattyParser = new com.joestelmach.natty.Parser();
		DateGroup resultDateGroup = nattyParser.parse(p).get(0);
		result = new DateTime(resultDateGroup.getDates().get(0));
	
		return result;
	}
	
	public static boolean isTaskID(String p){
		for (int i=0; i<p.length(); i++)
			if (p.charAt(i)<'0' && p.charAt(i)>'9')
				return false;
		return true;
	}
	
	public static boolean isDateTime(String p){
		com.joestelmach.natty.Parser nattyParser = new com.joestelmach.natty.Parser();
		
		if (nattyParser.parse(p).get(0).getText().equalsIgnoreCase(p))
			return true;
		else
			return false;
	}
	
	public static boolean isTag(String p){
		if (p.charAt(0)=='#' && p.charAt(p.length()-1)=='#')
			return true;
		else
			return false;
	}
}
