package nailit.logic.command;

import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

import org.joda.time.DateTime;

import test.storage.StorageManagerStub;

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
	protected StorageManager storer; //Note from Naomi: changed to protected so it is visible to child stub
//	private StorageManager storer;
	
	// the parserResult to use in the commandExcute
	private ParserResult parserResultInstance;
	
	// the vector object is used to store the done operations
	private Stack<Command> operationsHistory;
	
	// the current displaying task list in the GUI
	private Vector<Task> currentTaskList;
	
	// the content of the filter that filters the currentTaskList
	// eg. it can be "all", "CS2103, 2013-10-20, low"
	private FilterObject filterContentForCurrentTaskList;
	
	// store the commands that have been undoed
	private Stack<Command> redoCommandsList;
	
	private static String COMMAND_HISTORY_IS_EMPTY_FEEDBACK = "Sorry, no command has been executed yet, so no undo command can be done.";
	private static String NO_UNDOABLE_COMMNAD_FEEDBACK = "Sorry, no undoable command in the command history. You can undo Add, delete, or Update command.";
	private static String STORAGE_THROWING_EXCEPTION_FEEDBACK = "Sorry, storage throws exception. The command cannot be undone.";

	
	// constructor
	public CommandManager () throws FileCorruptionException 
	{
		//for testing
		//storer = new StorageStub();
		storer = new StorageManager();
		operationsHistory = new Stack<Command>();
		currentTaskList = new Vector<Task>();
		filterContentForCurrentTaskList = new FilterObject();
		parserResultInstance = null;
		redoCommandsList = new Stack<Command>();
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

	private Result doExecution() throws Exception {
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
		case UNCOMPLETE: {
			Result resultToReturn = uncomplete();
			return resultToReturn;
		}
		case ADDREMINDER: {
			Result resultToReturn = addReminder();
			return resultToReturn;
		}
		case UNDO: {
			Result resultToReturn = undo();
			return resultToReturn;
		}
		case REDO: {
			Result resultToReturn = redo();
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
	
	
	private Result redo() {
		Command commandToRedo = getTheCommandToRedo();
		Result resultToPassToGUI = new Result();
		if(commandToRedo == null) {
			resultToPassToGUI = new Result(false, false, Result.EXECUTION_RESULT_DISPLAY, "Sorry, you haven't undone any command, so cannot redo.", null, currentTaskList, null);
		} else {
			commandToRedo.redo();
			if(commandToRedo.isSuccessRedo()) {
				operationsHistory.push(commandToRedo);
				updateCurrentListAfterRedo(commandToRedo);
				resultToPassToGUI = createResultForRndoSuccessfully();
			} else {
				resultToPassToGUI = createResultForRedoFailure();
			}
		}
		return resultToPassToGUI;
	}

	private Result createResultForRedoFailure() {
		return new Result(false, false, Result.EXECUTION_RESULT_DISPLAY, "Redo cannot be done.", null, currentTaskList, null);		

	}

	private Result createResultForRndoSuccessfully() {
		return new Result(false, true, Result.EXECUTION_RESULT_DISPLAY, "Redo successfully.", null, currentTaskList, null);

	}

	private void updateCurrentListAfterRedo(Command commandToRedo) {
		int taskID = commandToRedo.getTaskID();
		CommandType commandType = commandToRedo.getCommandType();
		if(commandType == CommandType.ADD) {
			CommandAdd ca = (CommandAdd)commandToRedo;
			Task taskAddedBack = ca.getTaskAdded();
			currentTaskList.add(taskAddedBack);
			sort();
			
		} else {
			int count = -1;
			Iterator<Task> itr = currentTaskList.iterator();
			while(itr.hasNext()) {
				count++;
				Task currentTask = itr.next();
				int currentTaskID = currentTask.getID();
				if(currentTaskID == taskID) {
					if((commandType == CommandType.DELETE) || commandType == (CommandType.UPDATE)) {
						currentTaskList.remove(count);
					} else {
						if(commandType == CommandType.COMPLETE) {
							currentTask.setCompleted(true);
						} else if(commandType == CommandType.UNCOMPLETE) {
							currentTask.setCompleted(false);
						} else if(commandType == CommandType.ADDREMINDER) {
							CommandAddReminder car = (CommandAddReminder)commandToRedo;
							DateTime reminderDateToAdd = car.getReminderDateToAdd();
							currentTask.setReminder(reminderDateToAdd);
						}
					}
					break;
				} 
			}
			
			// add it to the task list if the task fit the filter after the redo
			if(commandType == CommandType.UPDATE) {
				CommandUpdate cu = (CommandUpdate)commandToRedo;
				currentTaskList.add(cu.getUpdatedTask());
				sort();
			} 
		}
		
	}

	private Command getTheCommandToRedo() {
		if(redoCommandsList.isEmpty()) {
			return null;
		} else {
			Command commandToRedo = redoCommandsList.pop();
			return commandToRedo;
		}
	}

	private Result undo() { // do not put into the command history
		Command commandToUndo = getTheCommandToUndo();
		Result resultToPassToGUI = new Result(); // means there is no command can be undone
		if(commandToUndo == null) { // two situations
			if(operationsHistory.isEmpty()) { // no command done yet
				resultToPassToGUI = createResultForEmptyCommandsHistory();
				resultToPassToGUI.setTaskList(currentTaskList); // the task list is unchanged
			} else {
				resultToPassToGUI = createResultForNoUndoableCommand();
				resultToPassToGUI.setTaskList(currentTaskList); // the task list is unchanged
			}
		} else { // three situations
			commandToUndo.undo();
			if(commandToUndo.undoSuccessfully()) {
				redoCommandsList.push(commandToUndo); // add the undone command into the redo list
				updateCurrentListAfterUndo(commandToUndo); // since undo operation may change the current task list
				resultToPassToGUI = createResultForUndoSuccessfully(commandToUndo);
			} else {
				resultToPassToGUI = createResultForUndoFailure();
			}
		}
		return resultToPassToGUI;
	}

	

	private Result createResultForUndoSuccessfully(Command commandToUndo) {
		String commandSummary = commandToUndo.getCommandString();
		return new Result(false, true, Result.EXECUTION_RESULT_DISPLAY, "Undo " + commandSummary +  " successfully.", null, currentTaskList, null);
	}

	private void updateCurrentListAfterUndo(Command commandToUndo) {
		int taskID = commandToUndo.getTaskID();
		CommandType commandType = commandToUndo.getCommandType();
		if(commandType == CommandType.DELETE) {
			CommandDelete cd = (CommandDelete)commandToUndo;
			Task taskAddedBack = cd.getTaskDeleted();
			currentTaskList.add(taskAddedBack);
			sort();
		} else {
			int count = -1;
			Iterator<Task> itr = currentTaskList.iterator();
			while(itr.hasNext()) {
				count++;
				Task currentTask = itr.next();
				int currentTaskID = currentTask.getID();
				if(currentTaskID == taskID) {
					if(commandType == (CommandType.UPDATE) || commandType == (CommandType.ADD)) {
						currentTaskList.remove(count);
					} else {
						if(commandType == CommandType.COMPLETE) {
							currentTask.setCompleted(false);
						} else if(commandType == CommandType.UNCOMPLETE) {
							currentTask.setCompleted(true);
						} else if(commandType == CommandType.ADDREMINDER) {
							currentTask.setReminder(null);
						}
					}
					break;
				} 
			}
			// add it to the task list if the task fit the filter after the undo
			if(commandType == CommandType.UPDATE) {
				CommandUpdate cu = (CommandUpdate)commandToUndo;
				currentTaskList.add(cu.getRetrievedTask());
				sort();
			} 
		}
	}

	

	private Result createResultForUndoFailure() {
		return new Result(false, false, Result.EXECUTION_RESULT_DISPLAY, "Undo cannot be done.", null, currentTaskList, null);		
	}

	private Command getTheCommandToUndo() {
		// it is guaranteed that in the operation 
		// history, only add, delete and update command objects will be contained
		if(operationsHistory.isEmpty()) {
			return null;
		} else {
			Command commandToUndo = operationsHistory.pop();
			return commandToUndo;
		}
	}
	
	private Result createResultForEmptyCommandsHistory() {
		return new Result(false, false, Result.EXECUTION_RESULT_DISPLAY, COMMAND_HISTORY_IS_EMPTY_FEEDBACK);
	}
	
	private Result createResultForNoUndoableCommand() {
		return new Result(false, false, Result.EXECUTION_RESULT_DISPLAY, NO_UNDOABLE_COMMNAD_FEEDBACK);
	}

	private Result add() {
		CommandAdd newAddCommandObj = new CommandAdd(parserResultInstance, storer); 
		// the resultToPassToGUI does not have the currentTaskList
		Result resultToPassToGUI = newAddCommandObj.executeCommand();
		addNewCommandObjToOperationsHistory(newAddCommandObj);
		// clear the redo command list
		redoCommandsList.clear();
		// the new added task may or may not should exist in the currentTaskList,
		// check whether the added task fit the filterContentForCurrentTaskList
		// if fit, add it and sort the current task list
		addTaskToCurrentTaskList(resultToPassToGUI);
		sort();
		// deal with the case that when user add a task while nothing in the task list.
		// in this situation, instead of giving a display type Execution_Display, we give task display
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
			addNewCommandObjToOperationsHistory(newDeleteCommandObj); // only add the sucessful command to the command stack
			// clear the redo command list
			redoCommandsList.clear();
		}
		resultToPassToGUI.setTaskList(currentTaskList);
		return resultToPassToGUI;
	}
	
	private Result update() throws Exception {
		CommandUpdate newUpdateCommandObj = new CommandUpdate(parserResultInstance, storer, currentTaskList);
		Result resultToPassToGUI = newUpdateCommandObj.executeCommand();
		if(newUpdateCommandObj.updateSuccess()) {
			int taskUpdatedDisplayID = newUpdateCommandObj.getDisplayID();
			deleteTaskFromCurrentTaskList(taskUpdatedDisplayID);
			addTaskToCurrentTaskList(resultToPassToGUI);
		}
		// the display ID for the task may change, so sort again
		sort();
		resultToPassToGUI.setTaskList(currentTaskList);
		addNewCommandObjToOperationsHistory(newUpdateCommandObj);
		// clear the redo command list
		redoCommandsList.clear();
		return resultToPassToGUI;
	}
	
	private Result display() throws Exception {
		CommandDisplay newDisplayCommandObj = new CommandDisplay(parserResultInstance, storer, this);
		Result resultToPassToGUI = newDisplayCommandObj.executeCommand();
		// should sort the currentTaskList and then set the sorted 
		// one on the returned result object
		sort();
		resultToPassToGUI.setTaskList(currentTaskList);
		// clear the redo command list
		redoCommandsList.clear();
		return resultToPassToGUI;
	}
	
	private Result search() {
		CommandSearch newSearchCommandObj = new CommandSearch(parserResultInstance, storer);
		Result resultToPassToGUI = newSearchCommandObj.executeCommand();
		// update the currentTaskList and filter Object
		updateCurrentTaskList(newSearchCommandObj);
		updateCurrentFilterObj(newSearchCommandObj);
		// since want to sort the searched tasks list, we need to sort 
		// the current task list and then add it to the result obj
		sort();
		resultToPassToGUI.setTaskList(currentTaskList);
		// clear the redo command list
		redoCommandsList.clear();
		return resultToPassToGUI;
	}
	
	private Result addReminder() throws Exception { // no need to update the currentTaskList, since no influence
		CommandAddReminder carObj = new CommandAddReminder(parserResultInstance, storer, currentTaskList);
		Result resultToPassToGUI = carObj.executeCommand(); // the result display 
		if(carObj.isSuccess()) {
			addNewCommandObjToOperationsHistory(carObj);
			// clear the redo command list
			redoCommandsList.clear();
		}
		return resultToPassToGUI;
	}

	private void updateCurrentFilterObj(CommandSearch newSearchCommandObj) {
		if(newSearchCommandObj.isFilterNothing()) { // filterObj becomes a new original filterObj
			filterContentForCurrentTaskList = new FilterObject();
		} else {
			filterContentForCurrentTaskList = newSearchCommandObj.getLatestFilteredResult();
		}
	}

	private void updateCurrentTaskList(CommandSearch newSearchCommandObj) {
		Vector<Task> tl = newSearchCommandObj.getLatestTaskList();
		if(tl == null ) { // this situation shouldn't happen; in case happen, do not change currentTaskList
			// do nothing
		} else if(newSearchCommandObj.isFilterNothing()){ // change the currentTaskList as a empty vector
			currentTaskList = new Vector<Task>();
		} else {
			currentTaskList = tl;
		}
	}

	private Result complete() throws Exception {
		CommandMarkCompletedOrUncompleted newMOrUnMCompletedObj = new CommandMarkCompletedOrUncompleted(parserResultInstance, storer, currentTaskList, true);
		Result resultToPassToGUI = newMOrUnMCompletedObj.executeCommand();
		// need to attach currentTaskList to the resultToPassToGUI, if success
		if(newMOrUnMCompletedObj.isSuccess()) { // successfully mark as completed, the task must be in the 
			updateCurrentTaskListAfterMarkCompleted(newMOrUnMCompletedObj); // update the related task in the currentTaskList
			// add to history
			addNewCommandObjToOperationsHistory(newMOrUnMCompletedObj);
			// clear the redo command list
			redoCommandsList.clear();
		}
		resultToPassToGUI.setTaskList(currentTaskList);
		return resultToPassToGUI;
	}
	
	// guaranteed that the task display ID is valid
	private void updateCurrentTaskListAfterMarkCompleted(
			CommandMarkCompletedOrUncompleted newMOrUnMCompletedObj) {
		int displayID = newMOrUnMCompletedObj.getDisplayID();
		Task taskToMarkAsCompleted = currentTaskList.get(displayID - 1);
		taskToMarkAsCompleted.setCompleted(true);
		sort();
	}
	
	private Result uncomplete() throws Exception {
		CommandMarkCompletedOrUncompleted newMOrUnMCompletedObj = new CommandMarkCompletedOrUncompleted(parserResultInstance, storer, currentTaskList, true);
		Result resultToPassToGUI = newMOrUnMCompletedObj.executeCommand();
		// need to attach currentTaskList to the resultToPassToGUI, if success
		if(newMOrUnMCompletedObj.isSuccess()) { // successfully mark as completed, the task must be in the 
			updateCurrentTaskListAfterMarkUncompleted(newMOrUnMCompletedObj); // update the related task in the currentTaskList
			// add to history
			addNewCommandObjToOperationsHistory(newMOrUnMCompletedObj);
			// clear the redo command list
			redoCommandsList.clear();
		}
		resultToPassToGUI.setTaskList(currentTaskList);
		return resultToPassToGUI;
	}
	
	// guaranteed that the task display ID is valid
	private void updateCurrentTaskListAfterMarkUncompleted(
			CommandMarkCompletedOrUncompleted newMOrUnMCompletedObj) {
		int displayID = newMOrUnMCompletedObj.getDisplayID();
		Task taskToMarkAsCompleted = currentTaskList.get(displayID - 1);
		taskToMarkAsCompleted.setCompleted(false);
		sort();
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
		operationsHistory.push(commandObjToAdd);
	}
	
	public Vector<Command> getOperationsHistory() {
		return operationsHistory;
	}
	
	// sort the Tasks in the currentTaskList according to isCompleted, date, priority
	private void sort() {
		ComparatorForTwoTaskObj newComparator = new ComparatorForTwoTaskObj();
		Collections.sort(currentTaskList, newComparator);
	}
	
	public Vector<Task> getCurrentTaskList() {
		return currentTaskList;
	}
	
	public FilterObject getCurrentFilterObj() {
		return filterContentForCurrentTaskList;
	}

	public void setCurrentList(Vector<Task> vectorOfTasks) {
		currentTaskList = vectorOfTasks;
	}

	public void setCurrentFilterSearchAll() {
		filterContentForCurrentTaskList.setIsSearchAll(true);
	}
	
}
