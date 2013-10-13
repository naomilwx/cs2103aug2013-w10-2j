package test.logic;

import java.util.Vector;

import org.joda.time.DateTime;

import nailit.common.Result;
import nailit.common.Task;
import nailit.common.TaskPriority;
import nailit.logic.LogicManager;
import nailit.logic.command.CommandManager;
import nailit.storage.FileCorruptionException;

public class LogicManagerStub extends LogicManager{
	private DateTime startTime = new DateTime(2013,9,30,10,20);
	private DateTime endTime = new DateTime();
	private Task event = new Task("New Event Test", startTime, endTime, "#stub", TaskPriority.MEDIUM);
	private Task task = new Task("New Task Test", startTime, null, "#test", TaskPriority.HIGH);
	private Vector<Task> taskList = new Vector<Task>();
	private Result res = new Result();
	
	public LogicManagerStub() throws FileCorruptionException{
		event.setID(1);
		task.setID(2);
		taskList.add(event);
		taskList.add(task);
	}
	public Result executeCommand(String command){
		res.setTaskList(taskList);
		res.setDisplayType(Result.NOTIFICATION_DISPLAY);
		res.setTaskToDisplay(task);
		res.setNotification("test");
		if(command.equals("exit")){
			res.setExitStatus(true);
		}else if(command.equals("task")){
			res.setDisplayType(Result.TASK_DISPLAY);
		}else if(command.equals("list")){
			res.setDisplayType(Result.LIST_DISPLAY);
		}else if(command.equals("new")){
			res.setDisplayType(Result.EXECUTION_RESULT_DISPLAY);
		}
		return res;
	}
}
