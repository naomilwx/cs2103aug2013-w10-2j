package nailit.logic;

import nailit.common.TaskPriority;

public enum CommandType {
	ADD, COMPLETE, DELETE, DISPLAY,REDO, SEARCH, SHOWHISTORY, UNCOMPLETE, UNDO, UPDATE, EXIT, INVALID;


	public static boolean isCommandType(String p){
		for(CommandType type: CommandType.values()){
			if(p.equalsIgnoreCase(type.toString())){
				return true;
			}
		}
		return false;
	}
}