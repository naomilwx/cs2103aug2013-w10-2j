package nailit.logic;

import java.util.Vector;

import nailit.common.CommandType;
import nailit.common.NIConstants;
import nailit.common.Result;
import nailit.common.Task;
import nailit.common.Utilities;
import nailit.logic.command.CommandManager;
import nailit.logic.parser.ParserManager;
import nailit.storage.FileCorruptionException;

public class LogicManager{
	
	private ParserManager parserInstance;
	private CommandManager commandInstance;
	
	public LogicManager() throws FileCorruptionException {
		parserInstance = new ParserManager();
		commandInstance = new CommandManager();
	}
	
	//@author A0105559B
	//API to parse and execute commands
	public Result executeCommand(String OriginalCommand) throws Exception{
		Result executeCommandResult = new Result();
		parserInstance.passCommand(OriginalCommand);

		ParserResult parserResultInstance = parserInstance.execute();
		executeCommandResult = commandInstance.executeCommand(parserResultInstance);
		
		return  executeCommandResult;
	}
	
	
	public Result getListOfTasksForTheDay(){
		return commandInstance.getDefaultListOfTasks();
	}
	//@author A0091372H
	//API to execute delete/display commands from GUI
	public Result executeDirectIDCommand(CommandType command, int ID) throws Exception{
		Result executeCommandResult = new Result();
		ParserResult parserResultInstance = new ParserResult();
		parserResultInstance.setCommand(command);
		parserResultInstance.setTaskId(ID);
		executeCommandResult = commandInstance.executeCommand(parserResultInstance);
		return  executeCommandResult;
	}
	
	// api for giving the reminder on today
	public Vector<Vector <Task>> getReminderList() {
		Vector<Task> reminders = commandInstance.getTodayReminder();
		return Utilities.filterAndSortTaskList(reminders);
	}
	
	//@author A0105789R 
	// api for giving command list, undoable and redoable
	public Vector<Vector<String>> getCommandList() {
		Vector<Vector<String>> commandList = new Vector<Vector<String>>();
		Vector<String> undoableCommandList = commandInstance.getUndoableCommandStringList();
		Vector<String> redoableCommandList = commandInstance.getRedoableCommandStringList();
		commandList.add(NIConstants.HISTORY_UNDO_INDEX, undoableCommandList);
		commandList.add(NIConstants.HISTORY_REDO_INDEX, redoableCommandList);
		return commandList;
	}
}
