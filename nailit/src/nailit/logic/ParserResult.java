package nailit.logic;

import java.util.*;

import org.joda.time.DateTime;

import nailit.common.TASK_PRIORITY;

public class ParserResult{
	//ArrayList<String> command = new ArrayList<String> ();
	
	private static CommandType Command;
	private static String Name;
	
	// change from Shuzhi
	// to be consistent with class Task, we use DateTime here
	// can be changed back later after discussion
	private static DateTime StartTime;
	private static DateTime EndTime;
	
	// change from Shuzhi
	// should use TASK_PRIORITY
	private static TASK_PRIORITY Priority;
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
	
	public DateTime getStartTime()
	{
		return StartTime;
	}
	
	/*public boolean isNull_StartTime()
	{
		if (StartTime == )
	}*/
	
	public DateTime getEndTime()
	{
		return EndTime;
	}
	
	public TASK_PRIORITY getPriority()
	{
		return Priority;
	}
	
	public boolean isNullPriority()
	{
		if (Priority == null){
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
