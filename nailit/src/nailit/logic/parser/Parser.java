package nailit.logic.parser;

import nailit.logic.ParserResult;
import nailit.common.NIConstants;
import nailit.common.Task;

import org.joda.time.DateTime;

import com.joestelmach.natty.DateGroup;

import java.util.*;

public abstract class Parser {
	public static com.joestelmach.natty.Parser nattyParser = new com.joestelmach.natty.Parser();
	
	public abstract ParserResult execute();
	
	public static DateTime retrieveDateTime (String p){
		DateTime result;
		
		DateGroup resultDateGroup = nattyParser.parse(p).get(0);
		result = new DateTime(resultDateGroup.getDates().get(0));
	
		return result;
	}
	
	public static int numberOfTime(String p){		
		List<DateGroup> resultDateGroup;
		int answer;
		
		resultDateGroup = nattyParser.parse(p);
		answer = resultDateGroup.size();
	    
		return anwer;
	}
	
	public static boolean isTaskID(String p){
		for (int i=0; i<p.length(); i++)
			if ((p.charAt(i)<48) || (p.charAt(i)>57))
				return false;
		if (Integer.parseInt(p)>10000000)
			return false;
		return true;
	}
	
	public static boolean isDateTime(String p){
		List<DateGroup> parseResult = nattyParser.parse(p);
		if(parseResult.isEmpty()){
			return false;
		}else{
			if(isNumber(p)){
				return false;
			}else{
				DateGroup date = parseResult.get(0);
				return date.getText().equalsIgnoreCase(p);
			}
		}
	}
	
	public static boolean isNumber(String p){
		if(p.isEmpty()){
			return false;
		}else{
			String numRegex = "^[0-9]+$";
			return p.matches(numRegex);
		}
	}
	public static boolean isTag(String p){
		if (p.charAt(0)=='#' && p.charAt(p.length()-1)=='#')
			return true;
		else
			return false;
	}
}
