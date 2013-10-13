package nailit.logic.command;

import java.util.Vector;

import test.storage.StorageStub;

import nailit.common.Result;
import nailit.logic.*;

import nailit.storage.FileCorruptionException;
import nailit.storage.StorageManager;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;


public class CommandManager {
	// the storage object that the commandManager works with 
	
	// for testing
	private StorageManager storer;
//	private StorageManager storer;
	
	// the parserResult to use in the commandExcute
	private ParserResult parserResultInstance;
	
	// the vector object is used to store the done operations
	private Vector<Command> operationsHistory;
	
	// constructor
	public CommandManager () throws FileCorruptionException 
	{
		//for testing
		//storer = new StorageStub();
		storer = new StorageManager();
		operationsHistory = new Vector<Command>();
	}
	
	public Result executeCommand(ParserResult parserResultInstance)
	{
		this.parserResultInstance = parserResultInstance;
		Result executedResult = doExecution();
		return executedResult;
	}

	private Result doExecution() {
		CommandType commandType = parserResultInstance.getCommand();
		// in each command execute method like add(), we will create the 
		// corresponding object and use them to execute the command.
		// In addition, the command is added to operation History.
		switch (commandType) {
		case ADD: {
			Result resultToReturn = add();
			return resultToReturn;
		}
		case COMPLETE: {
			Result resultToReturn = complete();
			return resultToReturn;
		}
		case DELETE: {
			Result resultToReturn = delete();
			return resultToReturn;
		}
		case SEARCH: {
			Result resultToReturn = search();
			return resultToReturn;
		}
		case UPDATE: {
			Result resultToReturn = update();
			return resultToReturn;
		}
		case INVALID: {
			break;
		}
		case DISPLAY: {
			Result resultToReturn = display();
			return resultToReturn;
		}
		case EXIT: {
			Result resultToReturn = exit();
			return resultToReturn;
		}
		default: {

		}
		}
		return null;
	}

	private Result exit() {
		CommandExit newExitCommandObj = new CommandExit(parserResultInstance, storer);
		Result resultToPassToGUI = newExitCommandObj.executeCommand();
		return resultToPassToGUI;
	}

	private Result display() {
		CommandDisplay newDisplayCommandObj = new CommandDisplay(parserResultInstance, storer, this);
		addNewCommandObjToOperationsHistory(newDisplayCommandObj);
		Result resultToPassToGUI = newDisplayCommandObj.executeCommand();
		return resultToPassToGUI;
	}

	private Result update() {
		CommandUpdate newUpdateCommandObj = new CommandUpdate(parserResultInstance, storer);
		addNewCommandObjToOperationsHistory(newUpdateCommandObj);
		Result resultToPassToGUI = newUpdateCommandObj.executeCommand();
		return resultToPassToGUI;
	}

	private Result search() {
		return null;
		// TODO Auto-generated method stub
	}

	private Result delete() {
		CommandDelete newDeleteCommandObj = new CommandDelete(parserResultInstance, storer);
		addNewCommandObjToOperationsHistory(newDeleteCommandObj);
		Result resultToPassToGUI = newDeleteCommandObj.executeCommand();
		return resultToPassToGUI;
	}

	private Result complete() {
		return null;
		// TODO Auto-generated method stub
	}

	private Result add() {
		CommandAdd newAddCommandObj = new CommandAdd(parserResultInstance, storer);
		addNewCommandObjToOperationsHistory(newAddCommandObj);
		Result resultToPassToGUI = newAddCommandObj.executeCommand();
		return resultToPassToGUI;
	}

	private void addNewCommandObjToOperationsHistory(Command commandObjToAdd) {
		operationsHistory.add(commandObjToAdd);
	}
	
	public Vector<Command> getOperationsHistory() {
		return operationsHistory;
	}
}
