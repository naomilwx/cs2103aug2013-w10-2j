package nailit.logic.command;

//@author A0105789R

import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;
import org.joda.time.DateTime;
import nailit.common.CommandType;
import nailit.common.FilterObject;
import nailit.common.Result;
import nailit.common.Task;
import nailit.common.Utilities;
import nailit.storage.FileCorruptionException;
import nailit.storage.StorageManager;
import nailit.logic.ParserResult;


public class CommandManager {
	// static final fields
	private static String COMMAND_HISTORY_IS_EMPTY_FEEDBACK = "Sorry, no command has " +
															"been executed yet, so no undo " +
															"command can be done.";

	private static String NO_UNDOABLE_COMMNAD_FEEDBACK = "Sorry, no undoable command in the " +
														"command history. You can undo Add, Delete, " +
														"or Update command.";
	
	// private fields
	// the storage object that the commandManager works with 
	protected StorageManager storer;
	
	// the parserResult to use in the commandExcute
	private ParserResult parserResultInstance;
	
	// the vector object is used to store the done operations
	private Stack<Command> operationsHistory;
	
	// the current displaying task list in the GUI
	private Vector<Task> currentTaskList;
	
	// the content of the filter that filters the currentTaskList
	// eg. it can be "all", "CS2103, 2013-10-20, low"
	private FilterObject filterContentForCurrentTaskList;
	
	// store the commands that have been undone
	private Stack<Command> redoCommandsList;
	
	// constructor
	public CommandManager () throws FileCorruptionException {
		storer = new StorageManager();
		operationsHistory = new Stack<Command>();
		currentTaskList = new Vector<Task>();
		filterContentForCurrentTaskList = new FilterObject();
		parserResultInstance = null;
		redoCommandsList = new Stack<Command>();
	}
	
	public Vector<Task> getTodayReminder() {
		return storer.getReminderListForToday();
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
	
	/** 
	 * In each command execute method like add(), we will create the
	 * corresponding object and use them to execute the command.
	 * In addition, the command is added to operation History.
	*/
	private Result doExecution() throws Exception {
		CommandType commandType = parserResultInstance.getCommand();

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
		case DELETEREMINDER: {
			Result resultToReturn = deleteReminder();
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
	
	private Result add() {
		CommandAdd newAddCommandObj = new CommandAdd(parserResultInstance, storer); 
		
		// the resultToPassToGUI does not have the currentTaskList
		Result resultToPassToGUI = newAddCommandObj.executeCommand();
		addNewCommandObjToOperationsHistory(newAddCommandObj);
		
		// clear the redo command list
		redoCommandsList.clear();
		addTaskToCurrentTaskList(resultToPassToGUI);
		
		// sort the current task list 
		sortCurrentTaskList();
		
		// deal with the case that when user add a task while nothing in the task list.
		// in this situation, instead of giving a display type Execution_Display, we give task display
		resultToPassToGUI.setTaskList(currentTaskList);
		return resultToPassToGUI;
	}
	
	private Result delete() throws Exception {
		// delete the task according to its displayID in the taskList
		CommandDelete newDeleteCommandObj = new CommandDelete(parserResultInstance, storer, currentTaskList);
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
		sortCurrentTaskList();
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
		sortCurrentTaskList();
		resultToPassToGUI.setTaskList(currentTaskList);
		return resultToPassToGUI;
	}
	
	private Result search() {
		CommandSearch newSearchCommandObj = new CommandSearch(parserResultInstance, storer);
		Result resultToPassToGUI = newSearchCommandObj.executeCommand();
		// update the currentTaskList
		updateCurrentTaskList(newSearchCommandObj);
		updateCurrentFilterObj(newSearchCommandObj);
		// since want to sort the searched tasks list, we need to sort 
		// the current task list and then add it to the result object
		sortCurrentTaskList();
		resultToPassToGUI.setTaskList(currentTaskList);
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
	
	private Result deleteReminder() throws Exception {
		CommandDeleteReminder cdrObj = new CommandDeleteReminder(parserResultInstance, storer, currentTaskList);
		Result resultToPassToGUI = cdrObj.executeCommand(); // the result display 
		if(cdrObj.isSuccess()) {
			addNewCommandObjToOperationsHistory(cdrObj);
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
		// sort the currentTaskList
		sortCurrentTaskList();
	}
	
	private Result uncomplete() throws Exception {
		CommandMarkCompletedOrUncompleted newMOrUnMCompletedObj = new CommandMarkCompletedOrUncompleted(parserResultInstance, storer, currentTaskList, false);
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
		sortCurrentTaskList();
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
			if(commandToUndo.isUndoSuccessfully()) {
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
		Result resultToReturn = new Result(false, true, Result.EXECUTION_RESULT_DISPLAY, "Successfully UNDO " + commandSummary, null, currentTaskList, null);
		if (commandToUndo.getCommandType() != CommandType.ADD) {
			Task taskGivenToGui = commandToUndo.getTaskRelated();
			resultToReturn.setTaskToDisplay(taskGivenToGui);
		}
		return resultToReturn;
	}

	private void updateCurrentListAfterUndo(Command commandToUndo) {
		int taskID = commandToUndo.getTaskId();
		CommandType commandType = commandToUndo.getCommandType();
		if(commandType == CommandType.DELETE) {
			CommandDelete cd = (CommandDelete)commandToUndo;
			Task taskAddedBack = cd.getTaskDeleted();
			currentTaskList.add(taskAddedBack);
			sortCurrentTaskList();
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
							// add the reminder back if has before
							CommandMarkCompletedOrUncompleted mcou = (CommandMarkCompletedOrUncompleted)commandToUndo;
							DateTime rd = mcou.getReminderDate();
							currentTask.setReminder(rd);
							
						} else if(commandType == CommandType.UNCOMPLETE) {
							currentTask.setCompleted(true);
							currentTask.setReminder(null); // remove the reminder date if has
						} else if(commandType == CommandType.ADDREMINDER) {
							currentTask.setReminder(null);
						} else if(commandType == CommandType.DELETEREMINDER) {
							CommandDeleteReminder cdrObj = (CommandDeleteReminder)commandToUndo;
							DateTime reminderDateDeleted = cdrObj.getReminderDateDeleted();
							currentTask.setReminder(reminderDateDeleted);
						}
					}
					sortCurrentTaskList();
					break;
				} 
			}
			// add it to the task list if the task fit the filter after the undo
			if(commandType == CommandType.UPDATE) {
				CommandUpdate cu = (CommandUpdate)commandToUndo;
				currentTaskList.add(cu.getRetrievedTask());
				sortCurrentTaskList();
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
	
	private Result redo() {
		Command commandToRedo = getTheCommandToRedo();
		Result resultToPassToGUI = new Result();
		if(commandToRedo == null) {
			resultToPassToGUI = new Result(false, false, Result.EXECUTION_RESULT_DISPLAY, "Sorry, you haven't undone any command, so cannot redo.", null, currentTaskList, null);
		} else {
			commandToRedo.redo();
			if(commandToRedo.isRedoSuccessfully()) {
				operationsHistory.push(commandToRedo);
				updateCurrentListAfterRedo(commandToRedo);
				resultToPassToGUI = createResultForRndoSuccessfully(commandToRedo);
			} else {
				resultToPassToGUI = createResultForRedoFailure();
			}
		}
		return resultToPassToGUI;
	}

	private Result createResultForRedoFailure() {
		return new Result(false, false, Result.EXECUTION_RESULT_DISPLAY, 
						"Redo cannot be done.", null, currentTaskList, null);		

	}

	private Result createResultForRndoSuccessfully(Command commandToRedo) {
		String commandSummary = commandToRedo.getCommandString();
		Result resultReturnedToGui = new Result(false, true, Result.EXECUTION_RESULT_DISPLAY, "Successfully REDO " + 
				commandSummary, null, currentTaskList, null);
		Task taskReturnedToGui = commandToRedo.getTaskRelated();
		resultReturnedToGui.setTaskToDisplay(taskReturnedToGui);
		if(commandToRedo.getCommandType() == CommandType.DELETE) {
			resultReturnedToGui.setDeleteStatus(true);
		}
		return resultReturnedToGui;
	}

	private void updateCurrentListAfterRedo(Command commandToRedo) {
		int taskID = commandToRedo.getTaskId();
		CommandType commandType = commandToRedo.getCommandType();
		if(commandType == CommandType.ADD) {
			CommandAdd ca = (CommandAdd)commandToRedo;
			Task taskAddedBack = ca.getTaskAdded();
			currentTaskList.add(taskAddedBack);
			sortCurrentTaskList();
			
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
							currentTask.setReminder(null);
						} else if(commandType == CommandType.UNCOMPLETE) {
							currentTask.setCompleted(false);
							// add the reminder back if has before
							CommandMarkCompletedOrUncompleted mcou = (CommandMarkCompletedOrUncompleted)commandToRedo;
							DateTime rd = mcou.getReminderDate();
							currentTask.setReminder(rd);
						} else if(commandType == CommandType.ADDREMINDER) {
							CommandAddReminder car = (CommandAddReminder)commandToRedo;
							DateTime reminderDateToAdd = car.getReminderDateToAdd();
							currentTask.setReminder(reminderDateToAdd);
						} else if(commandType == CommandType.DELETEREMINDER) {
							currentTask.setReminder(null);
						}
					}
					sortCurrentTaskList();
					break;
				} 
			}
			
			// add it to the task list if the task fit the filter after the redo
			if(commandType == CommandType.UPDATE) {
				CommandUpdate cu = (CommandUpdate)commandToRedo;
				currentTaskList.add(cu.getUpdatedTask());
				sortCurrentTaskList();
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

	
	private Result createResultForNoUndoableCommand() {
		return new Result(false, false, Result.EXECUTION_RESULT_DISPLAY, NO_UNDOABLE_COMMNAD_FEEDBACK);
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
	
	public Vector<Command> getRedoableCommandList() {
		return redoCommandsList;
	}
	
	// sort the Tasks in the currentTaskList according to isCompleted, date, priority
	private void sortCurrentTaskList() {
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

	
	//@author A0091372H
	public Vector<Task> getTasksHappeningOnDay(DateTime date){
		DateTime startOfDay = Utilities.getStartOfDay(date);
		DateTime endOfDay =  Utilities.getEndOfDay(date);
		FilterObject dateFilter = new FilterObject("", startOfDay, endOfDay, null, null , null);
		Vector<Task> tasks = storer.filter(dateFilter);
		return tasks;
	}
	
	public Result getDefaultListOfTasks(){
		DateTime now = new DateTime();
		DateTime startOfDay = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 0, 0);
		FilterObject uncompletedFilter = new FilterObject("", null, startOfDay.minusSeconds(1), null, null, false); //1 sec before start of today
		Vector<Task> dateList = getTasksHappeningOnDay(now);
		Vector<Task> overdueList = storer.filter(uncompletedFilter);
		for(Task task: overdueList){
			if(!dateList.contains(task)){
				dateList.add(task);
			}
		}
		Result ret = new Result(false, true, Result.LIST_DISPLAY, "");
		currentTaskList = dateList;
		sortCurrentTaskList();
		ret.setTaskList(currentTaskList);
		return ret;
	}
	//@author A0105789R
	public Vector<String> getUndoableCommandStringList() {
		Vector<String> undoableCommandList = getCommandString(operationsHistory);
		return undoableCommandList;
	}
	
	public Vector<String> getRedoableCommandStringList() {
		Vector<String> redoableCommandList = getCommandString(redoCommandsList);
		return redoableCommandList;
	}
	
	private Vector<String> getCommandString(Vector<Command> commandList) {
		Vector<String> reversedCommandStringList = new Vector<String>();
		Iterator<Command> itr = commandList.iterator();
		
		while(itr.hasNext()) {
			reversedCommandStringList.add(itr.next().getCommandString());
		}
		
		Vector<String> commandStringList  = new Vector<String>();
		int size = reversedCommandStringList.size();
		for(int i = 0; i < size; i++) {
			commandStringList.add(reversedCommandStringList.get(size-i-1));
		}
		
		return commandStringList;
	}
}
