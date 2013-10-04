package nailit.logic.command;

import nailit.common.Result;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

public class Command {

	private static ParserResult parserResultInstance;
	
	private static StorageManager storer;
	
	private static Result executedResult;
	// constructor
	public Command(ParserResult resultInstance, StorageManager storerToUse) {
		parserResultInstance =  resultInstance;
		this.storer = storerToUse;
	}
	
}
