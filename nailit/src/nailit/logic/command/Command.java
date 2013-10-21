package nailit.logic.command;

import test.storage.StorageManagerStub;
import nailit.common.Result;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

public abstract class Command {

	protected ParserResult parserResultInstance;
	
	protected StorageManager storer;
	
	private Result executedResult;
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
