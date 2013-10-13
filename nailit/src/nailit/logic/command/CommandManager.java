package nailit.logic.command;

import java.util.Collections;
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
		parserResultInstance = null;
	}
	
	public Result executeCommand(ParserResult parserResultInstance) throws Exception
	{
		if(parserResultInstance == null) {
			throw new Exception("The parserResult Instance is a null object.");
		} else {
			this.parserResultInstance = parserResultInstance;
			Result executedResult = doExecution();
			return executedResult;
		}
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
		case DELETE: {
			Result resultToReturn = delete();
			return resultToReturn;
		}
		case UPDATE: {
			Result resultToReturn = update();
			return resultToReturn;
		}
		case DISPLAY: {
			Result resultToReturn = display();
			return resultToReturn;
		}
		case SEARCH: {
			Result resultToReturn = search();
			return resultToReturn;
		}
		case COMPLETE: {
			Result resultToReturn = complete();
			return resultToReturn;
		}
		case INVALID: {
			break;
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
	
	private Result add() {
		CommandAdd newAddCommandObj = new CommandAdd(parserResultInstance, storer); 
		// the resultToPassToGUI does not have the currentTaskList
		Result resultToPassToGUI = newAddCommandObj.executeCommand();
		addNewCommandObjToOperationsHistory(newAddCommandObj);
		// the new added task may or may not should exist in the currentTaskList,
		// so create a commandSearch object to do the retrieving job
		CommandSearch newCommandSearchObj = new CommandSearch(storer);
		// the searchResult is not sorted
		currentTaskList = newCommandSearchObj.search(filterContentForCurrentTaskList);
		// sort the searchResult
		sort();
		// add the searchResult into the resultToPassToGUI
		resultToPassToGUI.setTaskList(currentTaskList);
		return resultToPassToGUI;
	}
	
	private Result delete() throws Exception {

		// delete the task according to its displayID in the taskList
		CommandDelete newDeleteCommandObj = new CommandDelete(
				parserResultInstance, storer, currentTaskList);
		Result resultToPassToGUI = newDeleteCommandObj.executeCommand();
		// if successfully deleted, update the currentTaskList by removing that
		// task from the list
		if (newDeleteCommandObj.deleteSuccess()) {
			int taskDeletedDisplayID = newDeleteCommandObj.getTaskDisplayID();
			deleteTheTaskInCurrentTaskList(taskDeletedDisplayID);
		}
		resultToPassToGUI.setTaskList(currentTaskList);
		addNewCommandObjToOperationsHistory(newDeleteCommandObj);
		return resultToPassToGUI;

	}
	
	private Result update() {
		CommandUpdate newUpdateCommandObj = new CommandUpdate(parserResultInstance, storer, currentTaskList);
		Result resultToPassToGUI = newUpdateCommandObj.executeCommand();
		if(newUpdateCommandObj.updateSuccess()) {
			int taskUpdatedDisplayID = newUpdateCommandObj.getDisplayID();
			deleteTaskFromCurrentTaskList(taskUpdatedDisplayID);
			addTaskToCurrentTaskList(resultToPassToGUI);
		}
		resultToPassToGUI.setTaskList(currentTaskList);
		addNewCommandObjToOperationsHistory(newUpdateCommandObj);
		return resultToPassToGUI;
	}
	
	private Result display() {
		CommandDisplay newDisplayCommandObj = new CommandDisplay(parserResultInstance, storer, this, currentTaskList);
		Result resultToPassToGUI = newDisplayCommandObj.executeCommand();
		addNewCommandObjToOperationsHistory(newDisplayCommandObj);
		return resultToPassToGUI;
	}
	
	private Result search() {
		return null;
		// TODO Auto-generated method stub
	}

	private Result complete() {
		return null;
		// TODO Auto-generated method stub
	}
	
	private Result exit() {
		CommandExit newExitCommandObj = new CommandExit(parserResultInstance, storer);
		Result resultToPassToGUI = newExitCommandObj.executeCommand();
		return resultToPassToGUI;
	}

	private void addTaskToCurrentTaskList(Result result) {
		Task taskUpdated = result.getTaskToDisplay();
		currentTaskList.add(taskUpdated);
	}

	private void deleteTaskFromCurrentTaskList(int taskUpdatedDisplayID) {
		currentTaskList.remove(taskUpdatedDisplayID - 1);
	}

	private void deleteTheTaskInCurrentTaskList(int taskDeletedDisplayID) {
		currentTaskList.remove(taskDeletedDisplayID - 1);
	}

	private void addNewCommandObjToOperationsHistory(Command commandObjToAdd) {
		operationsHistory.add(commandObjToAdd);
	}
	
	public Vector<Command> getOperationsHistory() {
		return operationsHistory;
	}
	
	// sort the Tasks in the currentTaskList according to isCompleted, date, priority
	private void sort() {
		ComparatorForTwoTaskObj newComparator = new ComparatorForTwoTaskObj();
		Collections.sort(currentTaskList, newComparator);
	}
}
