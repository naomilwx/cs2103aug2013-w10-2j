package test.overall;

import java.util.Random;
import java.util.Vector;

import nailit.common.NIConstants;
import nailit.common.Result;
import nailit.common.Task;
import nailit.common.TaskPriority;
import nailit.logic.CommandType;
import nailit.logic.LogicManager;

import org.joda.time.DateTime;
import org.junit.Test;

import test.common.TaskStub;

public class OverAllTestUpdate {
	Random gen = new Random();
	LogicManager logic = OverallTestAdd.logic;
	int totalTasksNum = OverallTestAdd.count;
	@Test
	public void testUpdateDesc(){
		int taskID = gen.nextInt(totalTasksNum) + 1;
		try {
			Task task = displayAllAndGetTaskToTest(taskID);
			String desc = TaskStub.getRandomString();
			task.setDescription(desc);
			Task updatedTask = logic.executeCommand("update " + taskID + ", description, "+ desc).getTaskToDisplay();
			OverallTestSuite.compareTasksAttributes(task, updatedTask);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//	@Test
	public void testUpdateTag(){
		int taskID = gen.nextInt(totalTasksNum) + 1;
		try {
			Task task = displayAllAndGetTaskToTest(taskID);
			String tag = TaskStub.getRandomString();
			task.setTag(tag);
			Task updatedTask = logic.executeCommand("update " + taskID + ", tag ,"+ tag).getTaskToDisplay();
			OverallTestSuite.compareTasksAttributes(task, updatedTask);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testUpdateReminder(){
		int taskID = gen.nextInt(totalTasksNum) + 1;
		try {
			Task task = displayAllAndGetTaskToTest(taskID);
			DateTime rem = task.getEndTime();
			if(rem == null){
				rem = TaskStub.getRandomEndDate();
			}
			task.setReminder(rem);
			Task updatedTask = logic.executeCommand("update " + taskID + ", reminder ,"+ rem.toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT)).getTaskToDisplay();
			OverallTestSuite.compareTasksAttributes(task, updatedTask);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testUpdateDue(){
		int taskID = gen.nextInt(totalTasksNum) + 1;
		try {
			Task task = displayAllAndGetTaskToTest(taskID);
			DateTime date = task.getEndTime();
			if(date == null){
				date = TaskStub.getRandomEndDate();
			}
			task.setEndTime(date);
			Task updatedTask;
			updatedTask = logic.executeCommand("update " + taskID + ", end, "+ date.toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT)).getTaskToDisplay();
			OverallTestSuite.compareTasksAttributes(task, updatedTask);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//TODO:
	public void completeTask(){
		
	}
//	@Test
	public void completedTaskUpdate(){
		try{
			Result completeTasksResult= logic.executeCommand(CommandType.DISPLAY + " complete");
			Vector<Task> tasks = completeTasksResult.getTaskList();
			int taskID = gen.nextInt(tasks.size()) + 1;
			Task task = logic.executeDirectIDCommand(CommandType.DISPLAY, taskID).getTaskToDisplay();
			task.setPriority(TaskPriority.LOW);
			Task updatedTask = logic.executeCommand("update " + taskID+",priority, "+TaskPriority.LOW.toString()).getTaskToDisplay();
			OverallTestSuite.compareTasksAttributes(task, updatedTask);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	private Task displayAllAndGetTaskToTest(int taskID){
		try {
			logic.executeCommand(CommandType.DISPLAY + " all");
			Task task = logic.executeDirectIDCommand(CommandType.DISPLAY, taskID).getTaskToDisplay();
			return task;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
