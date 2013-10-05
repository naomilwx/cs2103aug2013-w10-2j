package nailit.logic;

import java.util.*;

import org.joda.time.DateTime;

import nailit.common.TASK_PRIORITY;

public class ParserResult{
	//ArrayList<String> command = new ArrayList<String> ();

	// added by Shuzhi
	// in delete and addDescription command: need tasdID
	private int taskID;
	private static CommandType command;
	private static String name;
	
	private DateTime startTime;
	private DateTime endTime;
	
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
	

	public int getTaskID() {
		return taskID;
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
