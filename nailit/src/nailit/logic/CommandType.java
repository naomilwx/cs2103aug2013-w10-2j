package nailit.logic;

import org.apache.commons.lang3.StringUtils;

public enum CommandType {
	ADD, ADDREMINDER, DELETEREMINDER, COMPLETE, DELETE, DISPLAY, REDO, SEARCH, UNCOMPLETE, UNDO, UPDATE, EXIT, INVALID;

	// @author A0105559B
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
	
	// @author A0105559B
	public static CommandType isApproximateCommandType(String p){
		int min = Integer.MAX_VALUE;
		CommandType answer = CommandType.INVALID;
		for(CommandType type: CommandType.values()){
			if (StringUtils.getLevenshteinDistance(type.toString(), p)<min){
				min = StringUtils.getLevenshteinDistance(type.toString(), p);
				answer = type;
			}
		}
		System.out.println(answer.toString());
		return answer;
	}
}