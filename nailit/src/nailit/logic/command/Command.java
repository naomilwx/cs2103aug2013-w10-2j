package nailit.logic.command;

import test.storage.StorageStub;
import nailit.common.Result;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

public abstract class Command {

	protected ParserResult parserResultInstance;
	
	protected StorageStub storer;
	
	private Result executedResult;
	// constructor
	public Command(ParserResult resultInstance, StorageStub storerToUse) {
		parserResultInstance =  resultInstance;
		this.storer = storerToUse;
	}


	public abstract Result executeCommand();
	
	public abstract int getTaskID();
	
}
