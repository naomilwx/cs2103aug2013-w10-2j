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
	
	public static Vector<DateTime> retrieveDateTimesList(String p){
		Vector<DateTime> results = new Vector<DateTime>();
		List<DateGroup> dateGroups = nattyParser.parse(p);
		List<Date> dates = new Vector<Date>();
		if(!dateGroups.isEmpty()){
			dates = dateGroups.get(0).getDates();
		}
		if(dates.size() == 1){
			DateTime start = new DateTime(dates.get(0));
			results.add(start);
		}else if(dates.size() == 2){
			DateTime first = new DateTime(dates.get(0));
			DateTime second = new DateTime(dates.get(1));
			if(first.compareTo(second) == -1){
				results.add(first);
				results.add(second);
			}else{
				results.add(second);
				results.add(first);
			}
		}
		return results;
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
			DateGroup date = parseResult.get(0);
			return date.getText().equalsIgnoreCase(p);
		}
	}
	
	public static boolean checkStringAfterHasDateTime(String str, String token){
		int pos = str.indexOf(token);
		int afterPos = pos + token.length();
		if((pos != -1) && (str.length() > afterPos)){
			String testStr = str.substring(afterPos).trim();
			if(isNumber(testStr)){
				return false;
			}else{
				return isDateTime(testStr);
			}
		}else{
			return false;
		}
	}
	
	public static boolean hasDateTime(String p){
		String parseStr = p.toLowerCase();
		return (checkStringAfterHasDateTime(parseStr, "at") || checkStringAfterHasDateTime(parseStr, "from"));
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
