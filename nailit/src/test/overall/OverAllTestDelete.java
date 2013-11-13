//@author A0091372H
package test.overall;

import static org.junit.Assert.fail;

import java.util.Random;

import org.junit.Test;

import test.common.TaskStub;
import nailit.common.CommandType;
import nailit.common.NIConstants;
import nailit.common.Task;
import nailit.logic.LogicManager;

public class OverAllTestDelete {
	Random gen = new Random();
	LogicManager logic = OverallTestAdd.logic;
	int overallTaskNum = OverallTestAdd.count;
	@Test
	public void runTest(){
		deleteTest();
		addAfterDelelete();
	}
	public void deleteTest(){
		int taskID = gen.nextInt(overallTaskNum) + 1;
		try {
			logic.executeCommand(CommandType.DISPLAY + " all");
			Task task = logic.executeDirectIDCommand(CommandType.DISPLAY, taskID).getTaskToDisplay();
			Task deletedTask = logic.executeCommand("delete " + taskID).getTaskToDisplay();
			OverallTestSuite.compareTasksAttributes(task, deletedTask);
		} catch (Exception e) {
			fail("delete failed");
			e.printStackTrace();
		}
	}
	
	public void addAfterDelelete(){
		Task expectedTask;
		try {
			expectedTask = new TaskStub(TaskStub.GENERATE_TASK);
			expectedTask.setID(overallTaskNum + 1);
			String commandString = "add "
					+expectedTask.getEndTime().toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT)+ ","
					+expectedTask.getPriority() + "," 
					+expectedTask.getTag() + ","
					+expectedTask.getName();
			Task addedTask = logic.executeCommand(commandString).getTaskToDisplay();
			OverallTestSuite.compareTasksAttributes(expectedTask, addedTask);
		} catch (Exception e) {
			fail("add after delete failed");
			e.printStackTrace();
		}
		
	}
}
