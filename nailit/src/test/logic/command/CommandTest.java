package test.logic.command;

import static org.junit.Assert.*;

import java.util.Vector;

import nailit.common.Result;
import nailit.common.Task;
import nailit.common.TaskPriority;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.logic.command.CommandManager;
import nailit.storage.FileCorruptionException;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.experimental.categories.Category;
@Category(CommandTest.class)
public class CommandTest {
	
	private final String SuccessMsg = "The new task is added successfully, the Task ID for it is: ";
	private final String SuccessDeleteMsg = "The task is deleted successfully, the Task ID for it is: ";
	private final String SuccessMsgFirstPart = "The task with Task ID: "; 
	private final String SuccessMsgSecondPart	= "is updated successfully";
	private final String UnsuccessfulFeedback = "Sorry, there is no such task record in the storage, please check and try again.";
	private final String FeedbackForNotExistingTask = "The to-delete task does not exist in the storage."; 

	
	private final DateTime startTime = new DateTime(2013, 10, 9, 10, 0);
	private final DateTime endTime = new DateTime(2013, 10, 9, 11, 0);

	@Test
	public void testCommandAdd() throws Exception {
		CommandManager cm = new CommandManagerStub();
		ParserResult prForCommandAdd = createParserResult(CommandType.ADD);
		Result resultObjOfCommandAdd = cm.executeCommand(prForCommandAdd);
//		int taskID = cm.getOperationsHistory().firstElement().getTaskID();
		
		Task expectedAddedTask = new Task("CS2103 project", startTime, endTime, "school work", TaskPriority.HIGH);
		expectedAddedTask.setID(1);
		Result expectedResultObj = new Result(false, true, Result.EXECUTION_RESULT_DISPLAY, null, expectedAddedTask, null, null);
		testTwoResultObj(resultObjOfCommandAdd, expectedResultObj);
	}
	
//	@Test
	public void testCommandDelete() throws Exception { //TODO:Implement working test
		CommandManager cm = new CommandManagerStub();
		ParserResult prForCommandDelete = createParserResult(CommandType.DELETE);
		prForCommandDelete.setTaskId(123);
		Result resultObjOfCommandDelete = cm.executeCommand(prForCommandDelete);
//		int taskID = cm.getOperationsHistory().firstElement().getTaskID();

		Result expectedResultObj = new Result(false, true, Result.NOTIFICATION_DISPLAY, FeedbackForNotExistingTask);
		testTwoResultObj(resultObjOfCommandDelete, expectedResultObj);
	}
	
	@Test
	public void testCommandUpdateNotExistingTask() throws Exception {
		CommandManager cm = new CommandManagerStub();
		ParserResult prForCommandUpdate = createParserResult(CommandType.UPDATE); 
		// function update, taskToDeleteID needed
		prForCommandUpdate.setTaskId(123);
		Result resultObjOfCommandUpdate = cm.executeCommand(prForCommandUpdate);
		int taskID = cm.getOperationsHistory().firstElement().getTaskId();
		Result expectedResultObj = new Result(false, false, Result.NOTIFICATION_DISPLAY, UnsuccessfulFeedback);
		testTwoResultObj(resultObjOfCommandUpdate, expectedResultObj);
	}
	
	@Test
	public void testCommandUpdate() throws Exception { //TODO:
		CommandManager cm = new CommandManagerStub();
		ParserResult prForCommandUpdate = createParserResult(CommandType.UPDATE);
		// function update, taskToDeleteID needed
		prForCommandUpdate.setTaskId(123);
		Result resultObjOfCommandUpdate = cm.executeCommand(prForCommandUpdate);
		int taskID = cm.getOperationsHistory().firstElement().getTaskId();
		Result expectedResultObj = new Result(false, false, Result.NOTIFICATION_DISPLAY, UnsuccessfulFeedback);
		testTwoResultObj(resultObjOfCommandUpdate, expectedResultObj);
	}
	
//	@Test TODO:
	public void testCommandDisplayNotExistingTask() throws Exception {
		CommandManager cm = new CommandManagerStub();
		ParserResult prForCommandDisplay = createParserResult(CommandType.DISPLAY);
		prForCommandDisplay.setTaskId(123);
		Result resultObjOfCommandUpdate = cm.executeCommand(prForCommandDisplay);
		Result expectedResultObj = new Result(false, false, Result.NOTIFICATION_DISPLAY, UnsuccessfulFeedback, null, null);
		testTwoResultObj(resultObjOfCommandUpdate, expectedResultObj);
	}
	
//	@Test
	public void testCommandAddAgain() throws Exception {
		CommandManager cm = new CommandManagerStub();
		ParserResult prForCommandAdd = createParserResult(CommandType.ADD);
		Result resultObjOfCommandAdd = cm.executeCommand(prForCommandAdd);
		int taskID = cm.getOperationsHistory().firstElement().getTaskId();
		Result expectedResultObj = new Result(false, true, Result.EXECUTION_RESULT_DISPLAY, SuccessMsg + taskID);
		testTwoResultObj(resultObjOfCommandAdd, expectedResultObj);
	}
	
//	@Test TODO:
	public void testCommandDisplayExistingTask() throws Exception { //TODO:
		CommandManager cm = new CommandManagerStub();
		ParserResult prForCommandDisplay = createParserResult(CommandType.DISPLAY);
		prForCommandDisplay.setTaskId(123);
		Result resultObjOfCommandUpdate = cm.executeCommand(prForCommandDisplay);
		Vector<Task> vectorOfTask = new Vector<Task>();
		Task expectedDisplayTask = createTask();
		vectorOfTask.add(expectedDisplayTask);
		Result expectedResultObj = new Result(false, false, Result.NOTIFICATION_DISPLAY, UnsuccessfulFeedback, vectorOfTask, null);
		testTwoResultObjFromCommandDisplay(resultObjOfCommandUpdate, expectedResultObj);
	}
	
	private void testTwoResultObjFromCommandDisplay(
			Result resultObjOfCommandUpdate, Result expectedResultObj) {
		testTwoResultObj(resultObjOfCommandUpdate, expectedResultObj);
//		ask displayedTask = resultObjOfCommandUpdate.getTaskList().firstElement();
//		Task expectedTask = expectedResultObj.getTaskList().firstElement();
//		assertEquals(displayedTask.getName(), expectedTask.getName());
//		assertEquals(displayedTask.getStartTime(), expectedTask.getStartTime());
//		assertEquals(displayedTask.getEndTime(), expectedTask.getEndTime());
//		assertEquals(displayedTask.getPriority(), expectedTask.getPriority());
//		assertEquals(displayedTask.getTag(), expectedTask.getTag());
	}

	private Task createTask() {
		return new Task("CS2103 project", startTime, endTime, "school work", TaskPriority.HIGH);
	}

	private void testTwoResultObj(Result resultObjOfCommandAdd,
			Result expectedResultObj) {
		assertEquals(resultObjOfCommandAdd.getExitStatus(), expectedResultObj.getExitStatus());
		assertEquals(resultObjOfCommandAdd.getExecutionSuccess(), expectedResultObj.getExecutionSuccess());
		assertEquals(resultObjOfCommandAdd.getDisplayType(), expectedResultObj.getDisplayType());
		assertEquals(resultObjOfCommandAdd.getTaskToDisplay(), expectedResultObj.getTaskToDisplay());
	}
	
	private ParserResult createParserResult(CommandType commandType, String taskName, DateTime startTime, 
			DateTime endTime, TaskPriority taskPriority, String taskTag) {
		ParserResult newPR = new ParserResult();
		newPR.setCommand(commandType);
		newPR.setName(taskName);
		newPR.setStartTime(startTime);
		newPR.setEndTime(endTime);
		newPR.setPriority(taskPriority);
		newPR.setTag(taskTag);
		return newPR;
	}
	

	
	private ParserResult createParserResult(CommandType commandType) {
		return createParserResult(commandType, 
				"CS2103 project", startTime, endTime, TaskPriority.HIGH, 
				"school work");
	}
}
