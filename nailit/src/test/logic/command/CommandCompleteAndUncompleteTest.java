package test.logic.command;

//@author A0105789R

import static org.junit.Assert.assertEquals;

import java.util.Vector;

import org.joda.time.DateTime;
import org.junit.Test;

import test.overall.OverallTestSuite;
import nailit.common.CommandType;
import nailit.common.Result;
import nailit.common.Task;
import nailit.common.TaskPriority;
import nailit.logic.ParserResult;

public class CommandCompleteAndUncompleteTest {
	
	private static ParserResult parserResultAdd1 = createPR(CommandType.ADD, "task1", 
			"study", TaskPriority.LOW, createDateTime(2013, 1, 1, 1, 0), 
			createDateTime(2013, 1, 2, 1, 0), false, 0);

	private static ParserResult parserResultComplete = createPR(CommandType.COMPLETE, "task2", 
			"study", TaskPriority.MEDIUM, createDateTime(2013, 3, 3, 1, 0), 
			createDateTime(2013, 4, 2, 1, 0), false, 1);
	
	private static ParserResult parserResultUncomplete = createPR(CommandType.UNCOMPLETE, "task2", 
			"study", TaskPriority.MEDIUM, createDateTime(2013, 3, 3, 1, 0), 
			createDateTime(2013, 4, 2, 1, 0), false, 1);
	
	
	// display all parserResult
	private static ParserResult parserResultDisplayAll = createPR(CommandType.DISPLAY, "task3", 
			"study", TaskPriority.HIGH, createDateTime(2013, 1, 8, 5, 0), 
			createDateTime(2013, 1, 9, 1, 0), true, 0);
	
	// create the expected result
	// create task objects
	private static Task task1 = createTask("task1", 
					"study", TaskPriority.LOW, createDateTime(2013, 1, 1, 1, 0), 
					createDateTime(2013, 1, 2, 1, 0), 1);
	
	@Test
	public void testComplete() throws Exception {
		CommandManagerStub cm = new CommandManagerStub();
		cm.executeCommand(parserResultAdd1);
		cm.executeCommand(parserResultDisplayAll);
		Result resultOfAdd = cm.executeCommand(parserResultComplete);
		Vector<Task> currentTaskList = new Vector<Task>();
		task1.setCompleted(true);
		currentTaskList.add(task1);
		
		Result expectedResult = new Result(false, true, Result.EXECUTION_RESULT_DISPLAY, 
				"", task1, currentTaskList, null);
		
		testTwoResultObj(resultOfAdd, expectedResult);
	}
	
	@Test
	public void testUncomplete() throws Exception {
		CommandManagerStub cm = new CommandManagerStub();
		cm.executeCommand(parserResultAdd1);
		cm.executeCommand(parserResultDisplayAll);
		cm.executeCommand(parserResultComplete);
		Result resultOfAdd = cm.executeCommand(parserResultUncomplete);
		Vector<Task> currentTaskList = new Vector<Task>();
		task1.setCompleted(false);
		currentTaskList.add(task1);

		Result expectedResult = new Result(false, true,
				Result.EXECUTION_RESULT_DISPLAY, "", task1, currentTaskList,
				null);

		testTwoResultObj(resultOfAdd, expectedResult);
	}
	
	
	private static ParserResult createPR(CommandType commandType, String name, 
			String tag, TaskPriority priority, DateTime st, DateTime et, boolean isDisplayAll, int id) {
		ParserResult pr = new ParserResult();
		pr.setCommand(commandType);
		pr.setName(name);
		pr.setPriority(priority);
		pr.setStartTime(st);
		pr.setEndTime(et);
		pr.setTag(tag);
		if(id != 0) {
			pr.setTaskId(id);
		}
		
		pr.setDisplayAll(isDisplayAll);
		
		return pr;
	}
	
	private static Task createTask(String name, 
			String tag, TaskPriority priority, DateTime st, DateTime et, int id){
		Task nt = new Task(name, st, et, tag, priority);
		nt.setID(id);
		return nt;
	}
	
	private static DateTime createDateTime(int year, int month, int day, int hour, int minute) {
		return new DateTime(year, month, day, hour, minute);
	}
	
	private void testTwoResultObj(Result result,
			Result expected) {
		assertEquals(expected.getExitStatus(), result.getExitStatus());
		assertEquals(expected.getExecutionSuccess(), result.getExecutionSuccess());
		assertEquals(expected.getDisplayType(), result.getDisplayType());
		assertEquals(expected.getTaskList(), result.getTaskList());
		OverallTestSuite.compareTasksAttributes(expected.getTaskToDisplay(), result.getTaskToDisplay());
	}
	
}
