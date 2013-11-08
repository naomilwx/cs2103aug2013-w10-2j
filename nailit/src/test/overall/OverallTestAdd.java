//@author A0091372H
package test.overall;

import static org.junit.Assert.*;
import nailit.common.NIConstants;
import nailit.common.Result;
import nailit.common.Task;
import nailit.logic.LogicManager;
import nailit.storage.FileCorruptionException;
import nailit.storage.StorageManager;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import test.common.TaskStub;
//Must be run after overall test add
public class OverallTestAdd {
	public static LogicManager logic;
	static int count;
	@BeforeClass
	public static void clearStorageAndInitialise(){
		StorageManager storage;
		try {
			storage = new StorageManager();
			storage.clear();
			logic = new LogicManager();
			count = 0;
		} catch (FileCorruptionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//	@Test
	public void run() throws Exception{
		testEvent1();
		testEvent2();
		testFloatingAdd();
		testTaskAdd();
	}
	@Test
	public void testEvent1() throws Exception {
		Task expectedTask = new TaskStub(TaskStub.GENERATE_EVENT);
		String commandString = "add "+ expectedTask.getName() +"," 
				+expectedTask.getPriority() + "," 
				+expectedTask.getStartTime().toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT)+","
				+expectedTask.getEndTime().toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT)+ ","
				+expectedTask.getTag();
		executeAddAndCheckResult(expectedTask, commandString);
	}
	@Test
	public void testEvent2() {
		try {
			Task expectedTask = new TaskStub(TaskStub.GENERATE_EVENT);
			String commandString = "add "
					+expectedTask.getPriority() + "," 
					+expectedTask.getTag() + ","
					+"from " + expectedTask.getStartTime().toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT)+" to "
					+expectedTask.getEndTime().toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT)+","
					+expectedTask.getName();
		
			executeAddAndCheckResult(expectedTask, commandString);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Test
	public void testFloatingAdd(){
		Task expectedTask;
		try {
			expectedTask = new TaskStub(TaskStub.GENERATE_FLOATING);
			String commandString = "add "
					+expectedTask.getPriority() + "," 
					+expectedTask.getTag() + ","
					+expectedTask.getName();
			executeAddAndCheckResult(expectedTask, commandString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Test
	public void testTaskAdd(){
		Task expectedTask;
		try {
			expectedTask = new TaskStub(TaskStub.GENERATE_TASK);
			String commandString = "add "
					+expectedTask.getEndTime().toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT)+ ","
					+expectedTask.getPriority() + "," 
					+expectedTask.getTag() + ","
					+expectedTask.getName();
			executeAddAndCheckResult(expectedTask, commandString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testTaskWithDescAdd(){
		Task expectedTask;
		try {
			expectedTask = new TaskStub(TaskStub.GENERATE_TASK);
			expectedTask.setDescription(TaskStub.getRandomDescription());
			String commandString = "add "
					+expectedTask.getEndTime().toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT)+ ","
					+expectedTask.getPriority() + "," 
					+"(" + expectedTask.getDescription() + ")" + ","
					+expectedTask.getTag() + ","
					+expectedTask.getName();
			executeAddAndCheckResult(expectedTask, commandString);
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	public void executeAddAndCheckResult(Task expectedTask, String commandString){
		try {
			count += 1;
			expectedTask.setID(count);
			Result addResult;
			addResult = logic.executeCommand(commandString);
			Task addedTask = addResult.getTaskToDisplay();
			OverallTestSuite.compareTasksAttributes(expectedTask, addedTask);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
