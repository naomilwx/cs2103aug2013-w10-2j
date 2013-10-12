package nailit.logic.command;

import java.util.Vector;

import test.storage.StorageStub;

import nailit.common.FilterObject;
import nailit.common.Result;
import nailit.common.Task;
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
	
	// the current displaying task list in the GUI
	private Vector<Task> currentTaskList;
	
	// the content of the filter that filters the currentTaskList
	// eg. it can be "all", "CS2103, 2013-10-20, low"
	private FilterObject filterContentForCurrentTaskList;
	
	// constructor
	public CommandManager () throws FileCorruptionException 
	{
		//for testing
		//storer = new StorageStub();
		storer = new StorageManager();
		operationsHistory = new Vector<Command>();
		currentTaskList = null;
		filterContentForCurrentTaskList = null;
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
		Result resultToPassToGUI = newDisplayCommandObj.executeCommand();
		addNewCommandObjToOperationsHistory(newDisplayCommandObj);
		return resultToPassToGUI;
	}

	private Result update() {
		CommandUpdate newUpdateCommandObj = new CommandUpdate(parserResultInstance, storer);
		Result resultToPassToGUI = newUpdateCommandObj.executeCommand();
		addNewCommandObjToOperationsHistory(newUpdateCommandObj);
		return resultToPassToGUI;
	}

	private Result search() {
		return null;
		// TODO Auto-generated method stub
	}

	private Result delete() {
		CommandDelete newDeleteCommandObj = new CommandDelete(parserResultInstance, storer, currentTaskList);
		Result resultToPassToGUI = newDeleteCommandObj.executeCommand();
		addNewCommandObjToOperationsHistory(newDeleteCommandObj);
		return resultToPassToGUI;
	}

	private Result complete() {
		return null;
		// TODO Auto-generated method stub
	}

	private Result add() {
		CommandAdd newAddCommandObj = new CommandAdd(parserResultInstance, storer); 
		// the resultToPassToGUI does not have the currentTaskList
		Result resultToPassToGUI = newAddCommandObj.executeCommand();
		addNewCommandObjToOperationsHistory(newAddCommandObj);
		// the new added task may or may not should exist in the currentTaskList,
		// so create a commandSearch object to do the retrieving job
		CommandSearch newCommandSearchObj = new CommandSearch(storer);
		// the searchResult is not sorted
		Vector<Task> searchResult = newCommandSearchObj.search(filterContentForCurrentTaskList);
		// sort the searchResult
		Vector<Task> sortedSearchResult = sort(searchResult);
		// add the searchResult into the resultToPassToGUI
		resultToPassToGUI.setTaskList(sortedSearchResult);
		return resultToPassToGUI;
	}

	private void addNewCommandObjToOperationsHistory(Command commandObjToAdd) {
		operationsHistory.add(commandObjToAdd);
	}
	
	public Vector<Command> getOperationsHistory() {
		return operationsHistory;
	}
	
	// sort the Tasks in the currentTaskList according to isCompleted, date, priority
	private Vector<Task> sort(Vector<Task> searchResult) {
		
		
		return searchResult;
		
	}
}
