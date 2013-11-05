// @author A0091372H
package nailit.logic.exception;

import nailit.logic.CommandType;

public class InvalidCommandFormatException extends Exception{
	public static final String DEFAULT_ERROR_MESSAGE = "Wrong command format.";
	private String commandTypeEntered;
	public InvalidCommandFormatException(){
		super(DEFAULT_ERROR_MESSAGE);
	}
	public InvalidCommandFormatException(String message){
		super(message);
	}
	//The commandtype is needed to trigger the relevant help window display by GUI
	public InvalidCommandFormatException(CommandType command, String message){
		super(message);
		commandTypeEntered = command.toString();
	}
	public String getCommandType(){
		return commandTypeEntered;
	}
}
