package test.common;

import org.joda.time.DateTime;

import nailit.common.Task;
import nailit.common.TaskPriority;

public class TaskStub extends Task{
	public TaskStub(){
		super("hello World", new DateTime(2013, 4, 5, 10 , 10), new DateTime(2013, 5, 5, 10 , 10), "#lalaland#", TaskPriority.LOW);
	}
}
