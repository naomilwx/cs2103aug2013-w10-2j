package nailit.logic;

import java.util.Vector;

import nailit.common.NIConstants;
import nailit.common.Result;
import nailit.common.Task;
import nailit.common.Utilities;
import nailit.logic.*;
import nailit.logic.command.*;
import nailit.logic.parser.*;
import nailit.storage.FileCorruptionException;


public class LogicManager{
	
	private ParserManager parserInstance;
	private CommandManager commandInstance;
	
	public LogicManager() throws FileCorruptionException {
		parserInstance = new ParserManager();
		commandInstance = new CommandManager();
	}
	public Result executeCommand(String OriginalCommand) throws Exception{
		Result executeCommandResult = new Result();
		parserInstance.passCommand(OriginalCommand);
		ParserResult parserResultInstance = parserInstance.execute();
//		System.out.println(parserResultInstance.getStartTime().toString(NIConstants.DISPLAY_DATE_FORMAT));
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
	public Vector<Vector <Task>> getReminderList() {
		Vector<Task> reminders = commandInstance.getTodayReminder();
		Vector<Task> taskReminders = Utilities.filterAndSortTasks(reminders);
		Vector<Task> eventReminders = Utilities.filterAndSortEvents(reminders);
		Vector<Vector <Task>> reminderList = new Vector<Vector <Task>>();
		reminderList.add(NIConstants.REMINDER_TASKS_INDEX, taskReminders);
		reminderList.add(NIConstants.REMINDER_EVENTS_INDEX, eventReminders);
		return reminderList;
	}
}
