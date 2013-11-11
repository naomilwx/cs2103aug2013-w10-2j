package test.storage;

import java.util.Vector;

import org.joda.time.DateTime;

import nailit.common.CommandType;
import nailit.common.FilterObject;
import nailit.common.Task;
import nailit.common.TaskPriority;
import nailit.logic.ParserResult;
import nailit.storage.FileCorruptionException;
import nailit.storage.StorageManager;

public class StorageManagerStub extends StorageManager{
	Vector<Task> tasks = new Vector<Task>();
	// constructor
	public StorageManagerStub() throws FileCorruptionException{
		
	}
	
	public int add(Task newTask) {
		return 123;
	}
	
	// for test normal situations
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
	
	@Override
	public Vector<Task> retrieveAll(){
		return tasks;
	}
	
	@Override
	public Vector<Task> filter(FilterObject ftobj){
		return tasks;
	}
	
	@Override
	public void clear(){}
	// for testing throw exception
//	public Task retrieve(int taskID) throws Exception {
//		throw new Exception();
//	}
//	
//	public Task remove(int taskID) throws Exception {
//		throw new Exception();
//	}
	
	
}
