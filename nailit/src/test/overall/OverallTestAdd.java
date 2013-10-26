//@author A0091372H
package test.overall;

import static org.junit.Assert.*;
import nailit.common.NIConstants;
import nailit.common.Result;
import nailit.common.Task;
import nailit.logic.LogicManager;
import nailit.storage.FileCorruptionException;
import nailit.storage.StorageManager;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import test.common.TaskStub;

public class OverallTestAdd {
	LogicManager logic;
	int count;
	@Before
	public void clearStorageAndInitialise(){
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
	@Test
	public void run(){
		test();
		test2();
	}
	public void test() {
		Task expectedTask = new TaskStub();
		String commandString = "add "+ expectedTask.getName() +"," 
				+expectedTask.getPriority() + "," 
				+expectedTask.getStartTime().toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT)+","
				+expectedTask.getEndTime().toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT)+ ","
				+expectedTask.getTag();
		executeAddAndCheckResult(expectedTask, commandString);
	}

	public void test2() {
		try {
			Task expectedTask = new TaskStub();
			String commandString = "add "
					+expectedTask.getPriority() + "," 
					+expectedTask.getTag() + ","
					+expectedTask.getStartTime().toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT)+","
					+expectedTask.getEndTime().toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT)+","
					+expectedTask.getName();
		
			executeAddAndCheckResult(expectedTask, commandString);
		}catch(Exception e){
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
			assertEquals(expectedTask.getName(), addedTask.getName());
			assertEquals(expectedTask.getStartTime(), addedTask.getStartTime());
			assertEquals(expectedTask.getEndTime(), addedTask.getEndTime());
			assertEquals(expectedTask.getPriority(), addedTask.getPriority());
			assertEquals(expectedTask.getTag(), addedTask.getTag());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
