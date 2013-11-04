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
	
	protected CommandType commandType;
	
	protected Result executedResult;
	
	// this is used for the command history
	protected String commandSummary;
	
	
	// constructor
	public Command(ParserResult resultInstance, StorageManager storerToUse) {
		parserResultInstance =  resultInstance;
		this.storer = storerToUse;
	}


	public abstract Result executeCommand() throws Exception;
	
	public abstract int getTaskID(); 
	
	public abstract CommandType getCommandType();	
	
	public abstract void undo();
	public abstract void redo();


	public abstract boolean undoSuccessfully();
	public abstract boolean isSuccessRedo();
	
	public abstract String getCommandString();
}
