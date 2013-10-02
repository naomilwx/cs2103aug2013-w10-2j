package nailit.logic;

import java.util.*;

public class ParserResult{
	//ArrayList<String> command = new ArrayList<String> ();
	
	private static CommandType Command;
	private static String Name;
	private static Date StartTime;
	private static Date EndTime;
	private static Integer Priority;
	private static String Tag;
	
	public CommandType getCommand(){
		return Command;
	}
	
	public String getName(){
		return Name;
	}
	
	public boolean isNullName(){
		if (Name == ""){
			return true;
		}else{ 
			return false;
		}
	}
	
	public Date getStartTime()
	{
		return StartTime;
	}
	
	/*public boolean isNull_StartTime()
	{
		if (StartTime == )
	}*/
	
	public Date getEndTime()
	{
		return EndTime;
	}
	
	public Integer getPriority()
	{
		return Priority;
	}
	
	public boolean isNullPriority()
	{
		if (Priority == 0){
			return true;
		}else{
			return false;
		}
	}
	
	public String getTag()
	{
		return Tag;
	}
	
	public boolean isNullTag()
	{
		if (Tag == ""){
			return true;
		}else{
			return false;
		}
	}
}
