package nailit.logic;

import java.util.*;

public class ParserResult
{
	//ArrayList<String> command = new ArrayList<String> ();
	
	private static String Name;
	private static Date StartTime;
	private static Date EndTime;
	private static Integer Priority;
	private static String Tag;
	
	public String get_Name()
	{
		return Name;
	}
	
	public boolean isNull_Name()
	{
		if (Name == "")
			return true;
		else 
			return false;
	}
	
	
	
	
}
