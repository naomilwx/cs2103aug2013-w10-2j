package test.logic.command;

import static org.junit.Assert.*;

import nailit.common.Result;
import nailit.common.TaskPriority;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.logic.command.CommandManager;
import nailit.storage.FileCorruptionException;

import org.joda.time.DateTime;
import org.junit.Test;

public class CommandTest {
	
	private final String SuccessMsg = "The new task is added successfully, the Task ID for it is: ";
	private final String SuccessDeleteMsg = "The task is deleted successfully, the Task ID for it is: ";;


	@Test
	public void testCommandAdd() throws FileCorruptionException {
		CommandManager cm = new CommandManager();
		DateTime startTime = new DateTime(2013, 10, 9, 10, 0);
		DateTime endTime = new DateTime(2013, 10, 9, 11, 0);
		ParserResult prForCommandAdd = createParserResult(CommandType.ADD, 
				"CS2103 project demo", startTime, endTime, TaskPriority.HIGH, 
				"school work");
		Result resultObjOfCommandAdd = cm.executeCommand(prForCommandAdd);
		int taskID = cm.getOperationsHistory().firstElement().getTaskID();
		Result expectedResultObj = createSimpleResultObj(false, true, Result.NOTIFICATION_DISPLAY, SuccessMsg + taskID);
		testTwoResultObj(resultObjOfCommandAdd, expectedResultObj);
	}
	
	private void testTwoResultObj(Result resultObjOfCommandAdd,
			Result expectedResultObj) {
		assertEquals(resultObjOfCommandAdd.getExitStatus(), expectedResultObj.getExitStatus());
		assertEquals(resultObjOfCommandAdd.getExecutionSuccess(), expectedResultObj.getExecutionSuccess());
		assertEquals(resultObjOfCommandAdd.getDisplayType(), expectedResultObj.getDisplayType());
		assertEquals(resultObjOfCommandAdd.getPrintOut(), expectedResultObj.getPrintOut());
	}

	private Result createSimpleResultObj(boolean isExitCommand, boolean isSuccess, int displayType, String printOut) {
		return new Result(isExitCommand, isSuccess, displayType, printOut);
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

	@Test
	public void testCommandDelete() throws FileCorruptionException {
		CommandManager cm = new CommandManager();
		DateTime startTime = new DateTime(2013, 10, 9, 10, 0);
		DateTime endTime = new DateTime(2013, 10, 9, 11, 0);
		ParserResult prForCommandAdd = createParserResult(CommandType.DELETE, 
				"CS2103 project demo", startTime, endTime, TaskPriority.HIGH, 
				"school work");
		// function delete, taskToDeleteID needed
		prForCommandAdd.setTaskID(123);
		Result resultObjOfCommandAdd = cm.executeCommand(prForCommandAdd);
		int taskID = cm.getOperationsHistory().firstElement().getTaskID();
		Result expectedResultObj = createSimpleResultObj(false, true, Result.NOTIFICATION_DISPLAY, SuccessDeleteMsg + taskID);
		testTwoResultObj(resultObjOfCommandAdd, expectedResultObj);
	}
	
	@Test
	public void testCommandUpdate() {
		
	}
	
	@Test
	public void testCommandDisplay() {
		
	}
}
