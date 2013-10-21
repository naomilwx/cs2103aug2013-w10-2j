package nailit.logic;

import nailit.common.TaskPriority;

public enum CommandType {
	ADD, ADDDESCRIPTION, COMPLETE, DELETE, DISPLAY, EXIT, SEARCH, SHOWHISTORY, UNDO, UPDATE, INVALID, REDO;


	public static boolean isCommandType(String p){
		for(CommandType type: CommandType.values()){
			if(p.equalsIgnoreCase(type.toString())){
				return true;
			}
		}
		return false;
	}
}