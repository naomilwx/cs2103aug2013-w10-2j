package nailit.logic;

import java.util.Vector;
import nailit.common.Result;
import nailit.common.Task;
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
	
	// @author A0105559B
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
	public Vector<Task> getReminderList() {
		return commandInstance.getTodayReminder();
	}
}
