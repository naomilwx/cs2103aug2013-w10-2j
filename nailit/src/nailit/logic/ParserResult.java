package nailit.logic;

import java.util.*;

import org.joda.time.DateTime;

import nailit.common.TASK_PRIORITY;

public class ParserResult{
	//ArrayList<String> command = new ArrayList<String> ();
	
	private CommandType command;
	private String name;
	
	// change from Shuzhi
	// to be consistent with class Task, we use DateTime here
	// can be changed back later after discussion
	private DateTime startTime;
	private DateTime endTime;
	
	// change from Shuzhi
	// should use TASK_PRIORITY
	private TASK_PRIORITY priority;
	private String tag;
	
	public CommandType getCommand(){
		return command;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isNullName(){
		if (name == ""){
			return true;
		}else{ 
			return false;
		}
	}
	
	public DateTime getStartTime(){
		return startTime;
	}
	
	public DateTime getEndTime(){
		return endTime;
	}
	
	public TASK_PRIORITY getPriority(){
		return priority;
	}
	
	public boolean isNullPriority(){
		if (priority == null){
			return true;
		}else{
			return false;
		}
	}
	
	public String getTag(){
		return tag;
	}
	
	public boolean isNullTag(){
		if (tag == ""){
			return true;
		}else{
			return false;
		}
	}
}
