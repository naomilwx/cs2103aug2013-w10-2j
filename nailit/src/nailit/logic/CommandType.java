package nailit.logic;

import nailit.common.TaskPriority;
import org.apache.commons.lang3.StringUtils;

public enum CommandType {
	ADD, ADDREMINDER, COMPLETE, DELETE, DISPLAY, REDO, SEARCH, SHOWHISTORY, UNCOMPLETE, UNDO, UPDATE, EXIT, INVALID;

	public static final int DELETE_COST = 1;
	public static final int INSERT_COST = 1;
	public static final int REPLACE_COST = 2;
	public static final int TWIDDLE_COST = 2;
	
	
	public static boolean isCommandType(String p){
		for(CommandType type: CommandType.values()){
			if(p.equalsIgnoreCase(type.toString())){
				return true;
			}
		}
		return false;
	}
	
	public static int calculateDistance(String a, String b){
		int answer=0;
		StringUtils.getLevenshteinDistance(a,b)
		return answer;
	}
	
	public static CommandType isApproximateCommandType(String p){
		int min = Integer.MAX_VALUE;
		CommandType answer = CommandType.INVALID;
		for(CommandType type: CommandType.values()){
			if (StringUtils.getLevenshteinDistance(type.values().toString(), p) < min){
				min = StringUtils.getLevenshteinDistance(type.values().toString(), p);
			}
		}
		return answer;
	}
}