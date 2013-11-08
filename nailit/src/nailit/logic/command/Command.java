package nailit.logic.command;

//@author A0105789R

import nailit.common.Result;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

public abstract class Command {
	// the parserResutl instance given by parser
	protected ParserResult parserResultInstance;
	
	// used for connecting with storage
	protected StorageManager storer;
	
	// represent the commandType of the sub Command class
	protected CommandType commandType;
	
	// the task ID for the task related to the Command object
	protected int taskId;
	
	// the final Result object passed to GUI component
	protected Result executedResult;
	
	// this is used for the command history, it summarize the command
	protected String commandSummary;
	
	protected boolean isUndoSuccess;
	protected boolean isRedoSuccess;
	
	
	// constructor
	public Command(ParserResult resultInstance, StorageManager storerToUse) {
		parserResultInstance =  resultInstance;
		this.storer = storerToUse;
	}

	// the method that does the execution 
	public abstract Result executeCommand() throws Exception;
	
	// get the task ID for the task related to that command object
	public abstract int getTaskId(); 
	
	public abstract CommandType getCommandType();	
	
	// the command object is able to undo or redo itself
	public abstract void undo();
	public abstract void redo();

	
	public abstract boolean isUndoSuccessfully();
	public abstract boolean isRedoSuccessfully();
	
	// the method return the command summary for the command object
	public abstract String getCommandString();
}
