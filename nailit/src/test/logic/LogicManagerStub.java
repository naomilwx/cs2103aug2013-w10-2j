package test.logic;

import java.util.Vector;

import org.joda.time.DateTime;

import nailit.common.Result;
import nailit.common.Task;
import nailit.common.TaskPriority;
import nailit.logic.LogicManager;
import nailit.logic.command.CommandManager;

public class LogicManagerStub{//NOTE should extend LogicManager. standalone for now because of bugs downstream
	private DateTime startTime = new DateTime(2013,9,30,10,20);
	private DateTime endTime = new DateTime();
	private Task event = new Task("New Event Test", startTime, endTime, "#stub", TaskPriority.MEDIUM);
	private Task task = new Task("New Task Test", startTime, null, "#test", TaskPriority.HIGH);
	private int displayType;
	private Vector<Task> taskList = new Vector<Task>();
	private Result res = new Result();
	
	public LogicManagerStub(int displayType){
		this.displayType = displayType;
		taskList.add(event);
		taskList.add(task);
	}
	public Result executeCommand(String command){
		res.setTaskList(taskList);
		res.setDisplayType(displayType);
		res.setNotification("test");
		return res;
	}
}
