//@author A0091372H
package nailit.logic.exception;

public class InvalidCommandTypeException extends Exception{
	private static final long serialVersionUID = 3913494559009124535L;
	
	public static final String DEFAULT_INVALID_COMMAND_ERROR_MESSAGE = "Unrecognised command type.";
	public static final String EMPTY_COMMAND_TYPE_ERROR_MESSAGE = "Command type string cannot be null!";
	
	public InvalidCommandTypeException(){
		super(DEFAULT_INVALID_COMMAND_ERROR_MESSAGE);
	}
	public InvalidCommandTypeException(String message){
		super(message);
	}
}
