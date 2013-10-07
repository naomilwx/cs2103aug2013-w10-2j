package test.storage;

import org.joda.time.DateTime;

import nailit.common.Task;
import nailit.common.TaskPriority;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class StorageStub {
	// constructor
	public StorageStub() {
		
	}
	
	public int add(Task newTask) {
		return 123;
	}
	
	public Task retrieve(int taskID) {
		DateTime startTime = new DateTime(2013, 10, 9, 10, 0);
		DateTime endTime = new DateTime(2013, 10, 9, 11, 0);
		return new Task("CS2103", startTime, endTime, "school work", TaskPriority.HIGH);
	}
	
	public Task remove(int taskID) {
		DateTime startTime = new DateTime(2013, 10, 9, 10, 0);
		DateTime endTime = new DateTime(2013, 10, 9, 11, 0);
		return new Task("CS2103", startTime, endTime, "school work", TaskPriority.HIGH);
	}
	
	
	
}
