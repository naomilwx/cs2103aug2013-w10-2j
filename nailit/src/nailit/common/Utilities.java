package nailit.common;

import org.joda.time.DateTime;

public class Utilities {
	public static String formatTaskTag(String tag){
		if(tag.equals("")){
			return tag;
		}else{
			if(getFirstChar(tag) != '#'){
				tag = "#" + tag;
			}
			if(getLastChar(tag) != '#'){
				tag = tag + "#";
			}
			return tag;
		}
	}
	public static DateTime getStartOfDay(DateTime date){
		return new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), 0, 0);
	}
	public static DateTime getEndOfDay(DateTime date){
		return new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), 23, 59);
	}
	public static char getFirstChar(String s){
		if(s.length() > 0){
			return s.charAt(0);
		}else{
			return ' ';
		}
	}
	public static char getLastChar(String s){
		return s.charAt(s.length() - 1);
	}
}
