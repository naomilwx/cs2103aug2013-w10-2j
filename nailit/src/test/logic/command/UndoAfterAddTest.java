package test.logic.command;

//@author A0105789R

import static org.junit.Assert.assertEquals;

import java.util.Vector;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import test.overall.OverallTestSuite;

import nailit.common.CommandType;
import nailit.common.Result;
import nailit.common.Task;
import nailit.common.TaskPriority;

import nailit.logic.ParserResult;

@Category(CommandTest.class)
public class UndoAfterAddTest {

	private static ParserResult parserResultAdd1 = createPR(CommandType.ADD, "task1", 
			"study", TaskPriority.LOW, createDateTime(2013, 1, 1, 1, 0), 
			createDateTime(2013, 1, 2, 1, 0), false, 0);
	
	private static ParserResult parserResultAdd2 = createPR(CommandType.ADD, "task2", 
			"study", TaskPriority.MEDIUM, createDateTime(2013, 3, 3, 1, 0), 
			createDateTime(2013, 4, 2, 1, 0), false, 0);
	
	private static ParserResult parserResultAdd3 = createPR(CommandType.ADD, "task3", 
			"study", TaskPriority.HIGH, createDateTime(2013, 1, 8, 5, 0), 
			createDateTime(2013, 1, 9, 1, 0), false, 0);
	
	
	// display all parserResult
	private static ParserResult parserResultDisplayAll = createPR(CommandType.DISPLAY, "task3", 
			"study", TaskPriority.HIGH, createDateTime(2013, 1, 8, 5, 0), 
			createDateTime(2013, 1, 9, 1, 0), true, 0);

	// undo parserResult
	private static ParserResult parserResultUndo = createPR(CommandType.UNDO, "task3", 
				"study", TaskPriority.HIGH, createDateTime(2013, 1, 8, 5, 0), 
				createDateTime(2013, 1, 9, 1, 0), false, 0); // only the command type matter
	
	// create the expected result
	// create task objects
	private static Task task1 = createTask("task1", 
					"study", TaskPriority.LOW, createDateTime(2013, 1, 1, 1, 0), 
					createDateTime(2013, 1, 2, 1, 0), 1);
	
	/*
	 * This test tests using use cases: add->add->add->displayAll->undo->undo
	 * */
	@Test
	public void testUndoAfterAdd() throws Exception {
		
		// execute
		// undo twice
		CommandManagerStub cm = new CommandManagerStub();
		cm.executeCommand(parserResultAdd1);
		cm.executeCommand(parserResultAdd2);
		cm.executeCommand(parserResultAdd3);
		cm.executeCommand(parserResultDisplayAll);
		cm.executeCommand(parserResultUndo);
		Result resultOfUndo = cm.executeCommand(parserResultUndo);
		Vector<Task> currentTaskList = new Vector<Task>();
		currentTaskList.add(task1);
		
		Result expectedResult = new Result(false, true, Result.EXECUTION_RESULT_DISPLAY, 
				"Undo successfully.", null, currentTaskList, null);
		
		testTwoResultObj(resultOfUndo, expectedResult);
		
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
		OverallTestSuite.compareTasksAttributes(expected.getTaskList().get(0), result.getTaskList().get(0));
	}

}
