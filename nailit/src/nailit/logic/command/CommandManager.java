package nailit.logic.command;

import java.util.Collections;
import java.util.Iterator;
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
	
	// store the commands that have been undoed
	private Vector<Command> redoCommandsList;
	
	private static String COMMAND_HISTORY_IS_EMPTY_FEEDBACK = "Sorry, no command has been executed yet, so no undo command can be done.";
	private static String NO_UNDOABLE_COMMNAD_FEEDBACK = "Sorry, no undoable command in the command history. You can undo Add, delete, or Update command.";
	private static String STORAGE_THROWING_EXCEPTION_FEEDBACK = "Sorry, storage throws exception. The command cannot be undone.";

	
	// constructor
	public CommandManager () throws FileCorruptionException 
	{
		//for testing
		//storer = new StorageStub();
		storer = new StorageManager();
		operationsHistory = new Vector<Command>();
		currentTaskList = new Vector<Task>();
		filterContentForCurrentTaskList = new FilterObject();
		parserResultInstance = null;
		redoCommandsList = new Vector<Command>();
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
			if(isTheTaskFitTheFilter(taskAddedBack)) {
				currentTaskList.add(taskAddedBack);
				sort();
			}
		} else {
			int count = -1;
			Iterator<Task> itr = currentTaskList.iterator();
			
			while(itr.hasNext()) {
				count++;
				Task currentTask = itr.next();
				int currentTaskID = currentTask.getID();
				if(currentTaskID == taskID) {
					currentTaskList.remove(count);
					if(commandType == CommandType.UPDATE) {
						CommandUpdate cu = (CommandUpdate)commandToRedo;
						currentTaskList.add(cu.getUpdatedTask());
						sort();
					}
				} 
			}
		}
		
	}

	private Command getTheCommandToRedo() {
		int size = redoCommandsList.size();
		if(size == 0) {
			return null;
		} else {
			Command commandToRedo = redoCommandsList.get(size-1);
			redoCommandsList.remove(size-1);
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
				redoCommandsList.add(commandToUndo); // add the undone command into the redo list
				updateCurrentListAfterUndo(commandToUndo); // since undo operation may change the current task list
				resultToPassToGUI = createResultForUndoSuccessfully();
			} else {
				resultToPassToGUI = createResultForUndoFailure();
			}
		}
		return resultToPassToGUI;
	}

	

	private Result createResultForUndoSuccessfully() {
		return new Result(false, true, Result.EXECUTION_RESULT_DISPLAY, "Undo successfully.", null, currentTaskList, null);
	}

	private void updateCurrentListAfterUndo(Command commandToUndo) {
		int taskID = commandToUndo.getTaskID();
		CommandType commandType = commandToUndo.getCommandType();
		if(commandType == CommandType.DELETE) {
			CommandDelete cd = (CommandDelete)commandToUndo;
			Task taskAddedBack = cd.getTaskDeleted();
			if(isTheTaskFitTheFilter(taskAddedBack)) {
				currentTaskList.add(taskAddedBack);
				sort();
			}
		} else {
			int count = -1;
			Iterator<Task> itr = currentTaskList.iterator();
			
			while(itr.hasNext()) {
				count++;
				Task currentTask = itr.next();
				int currentTaskID = currentTask.getID();
				if(currentTaskID == taskID) {
					currentTaskList.remove(count);
					if(commandType == CommandType.UPDATE) {
						CommandUpdate cu = (CommandUpdate)commandToUndo;
						currentTaskList.add(cu.getRetrievedTask());
						sort();
					}
				} 
			}
		}
	}

	

	private Result createResultForUndoFailure() {
		return new Result(false, false, Result.EXECUTION_RESULT_DISPLAY, "Undo cannot be done.", null, currentTaskList, null);		
	}

	private Command getTheCommandToUndo() {
		
//		Iterator<Command> itr = operationsHistory.iterator();
//		while(itr.hasNext()) {
//			Command currentCommand = itr.next();
//			CommandType currentCommandType = currentCommand.getCommandType();
//			if((currentCommandType == CommandType.ADD) || (currentCommandType == CommandType.DELETE) || (currentCommandType == CommandType.UPDATE)) {
//				return currentCommand;
//			} 
//		}
		int size = operationsHistory.size();
		for(int i = size-1; i >= 0; i--) {
			Command currentCommand = operationsHistory.get(i);
			CommandType currentCommandType = currentCommand.getCommandType();
			// this gurantee that only conmmand add, delete and update will be popped
			if((currentCommandType == CommandType.ADD) || (currentCommandType == CommandType.DELETE) || (currentCommandType == CommandType.UPDATE)) {
				return currentCommand;
			}
		}
		// no undoable command
		return null;
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
		// the new added task may or may not should exist in the currentTaskList,
		// check whether the added task fit the filterContentForCurrentTaskList
		// if fit, add it and sort the current task list
		if(isTheTaskFitTheFilter(resultToPassToGUI)) {
			addTaskToCurrentTaskList(resultToPassToGUI);
			sort();
		}
		
		// deal with the case that when user add a task while nothing in the task list.
		// in this situation, instead of giving a display type Execution_Display, we give task display
		if(currentTaskList.isEmpty()) {
			resultToPassToGUI.setDisplayType(Result.TASK_DISPLAY);
		} else {
			// add the searchResult into the resultToPassToGUI
			resultToPassToGUI.setTaskList(currentTaskList);
		}
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
		return resultToPassToGUI;
	}
	
	private Result display() throws Exception {
		CommandDisplay newDisplayCommandObj = new CommandDisplay(parserResultInstance, storer, this);
		Result resultToPassToGUI = newDisplayCommandObj.executeCommand();
		// should sort the currentTaskList and then set the sorted 
		// one on the returned result object
		sort();
		resultToPassToGUI.setTaskList(currentTaskList);
//		addNewCommandObjToOperationsHistory(newDisplayCommandObj);
		return resultToPassToGUI;
	}
	
	private Result search() {
		CommandSearch newSearchCommandObj = new CommandSearch(parserResultInstance, storer);
		Result resultToPassToGUI = newSearchCommandObj.executeCommand();
//		addNewCommandObjToOperationsHistory(newSearchCommandObj);
		// update the currentTaskList and filter Object
		updateCurrentTaskList(newSearchCommandObj);
		updateCurrentFilterObj(newSearchCommandObj);
		// since want to sort the searched tasks list, we need to sort 
		// the current task list and then add it to the result obj
		sort();
		resultToPassToGUI.setTaskList(currentTaskList);
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
	
	private boolean isTheTaskFitTheFilter(Result resultToPassToGUI) {
		Task taskToCompare = resultToPassToGUI.getTaskToDisplay();
		// date in filter
		DateTime filterST = filterContentForCurrentTaskList.getStartTime();
		DateTime filterET = filterContentForCurrentTaskList.getEndTime();
		
		// date in the task
		DateTime taskST = taskToCompare.getStartTime();
		DateTime taskET = taskToCompare.getEndTime();
		
		if (filterContentForCurrentTaskList.getIsSearchAll()) {
			return true;
		} 
		
		if (filterContentForCurrentTaskList.getName() != null) {
			if(taskToCompare.getName() == filterContentForCurrentTaskList.getName()) {
				return true;
			} 
		} 
		
		if(filterContentForCurrentTaskList.getPriority() != null) {
			if(taskToCompare.getPriority() == filterContentForCurrentTaskList.getPriority()) {
				return true;
			}
		} 
		
		if(filterContentForCurrentTaskList.getTag() != null) {
			if(taskToCompare.getTag() == filterContentForCurrentTaskList.getTag()) {
				return true;
			}
		}


		if (filterContentForCurrentTaskList.isCompleted() != null) { // means the field is searched
			if(taskToCompare.checkCompleted() == filterContentForCurrentTaskList.isCompleted()){
				return true;
			}
		}

		if (filterST != null && filterET != null) { // a time range, meaning
													// that start time needs to
													// be within the range
			if ((filterST.compareTo(taskST) < 0) && (filterET.compareTo(taskST) > 0)) {
				return true;
			}
		} 
		
		if(filterST != null && filterET == null) { // from a time, taskST needs to after it
			if(filterST.compareTo(taskST) < 0) {
				return true;
			}
		}
		
		if(filterST == null && filterET != null) { // to a time, taskST needs to before it
			if(filterET.compareTo(taskST) > 0) {
				return true;
			}
		} 
		return false;
	}
	
	private boolean isTheTaskFitTheFilter(Task taskAddedBack) {
		Task taskToCompare = taskAddedBack;
		// date in filter
		DateTime filterST = filterContentForCurrentTaskList.getStartTime();
		DateTime filterET = filterContentForCurrentTaskList.getEndTime();
		
		// date in the task
		DateTime taskST = taskToCompare.getStartTime();
		DateTime taskET = taskToCompare.getEndTime();
		
		if (filterContentForCurrentTaskList.getIsSearchAll()) {
			return true;
		} 
		
		if (filterContentForCurrentTaskList.getName() != null) {
			if(taskToCompare.getName() == filterContentForCurrentTaskList.getName()) {
				return true;
			} 
		} 
		
		if(filterContentForCurrentTaskList.getPriority() != null) {
			if(taskToCompare.getPriority() == filterContentForCurrentTaskList.getPriority()) {
				return true;
			}
		} 
		
		if(filterContentForCurrentTaskList.getTag() != null) {
			if(taskToCompare.getTag() == filterContentForCurrentTaskList.getTag()) {
				return true;
			}
		}


		if (filterContentForCurrentTaskList.isCompleted() != null) { // means the field is searched
			if(taskToCompare.checkCompleted() == filterContentForCurrentTaskList.isCompleted()){
				return true;
			}
		}

		if (filterST != null && filterET != null) { // a time range, meaning
													// that start time needs to
													// be within the range
			if ((filterST.compareTo(taskST) < 0) && (filterET.compareTo(taskST) > 0)) {
				return true;
			}
		} 
		
		if(filterST != null && filterET == null) { // from a time, taskST needs to after it
			if(filterST.compareTo(taskST) < 0) {
				return true;
			}
		}
		
		if(filterST == null && filterET != null) { // to a time, taskST needs to before it
			if(filterET.compareTo(taskST) > 0) {
				return true;
			}
		} 
		return false;
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
