package test.overall;

import static org.junit.Assert.*;
import nailit.common.NIConstants;
import nailit.common.Result;
import nailit.common.Task;
import nailit.logic.LogicManager;
import nailit.storage.FileCorruptionException;
import nailit.storage.StorageManager;

import org.junit.Before;
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
	public void executeAddAndCheckResult(Task expectedTask, String commandString){
		try {
			count += 1;
			expectedTask.setID(count);
			Result addResult;
			addResult = logic.executeCommand(commandString);
			Task addedTask = addResult.getTaskToDisplay();
			assertEquals(expectedTask, addedTask);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Test
	public void test() {
		Task expectedTask = new TaskStub();
		String commandString = "add "+ expectedTask.getName() +"," 
				+expectedTask.getPriority() + "," 
				+expectedTask.getStartTime().toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT)+","
				+expectedTask.getStartTime().toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT);
		executeAddAndCheckResult(expectedTask, commandString);
	}
	@Test
	public void test2() {
		try {
			Task expectedTask = new TaskStub();
			String commandString = "add "
					+expectedTask.getPriority() + "," 
					+expectedTask.getStartTime().toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT)+","
					+expectedTask.getStartTime().toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT)+","
					+expectedTask.getName();
		
			executeAddAndCheckResult(expectedTask, commandString);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
