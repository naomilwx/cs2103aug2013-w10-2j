package test.logic.command;

import static org.junit.Assert.assertEquals;

import java.util.Vector;

import nailit.common.Result;
import nailit.common.Task;
import nailit.common.TaskPriority;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.logic.command.CommandManager;

import org.joda.time.DateTime;
import org.junit.Test;

public class UndoAfterUpdateTest {
	private static ParserResult parserResultAdd1 = createPR(CommandType.ADD, "task1", 
			"stuty", TaskPriority.LOW, createDateTime(2013, 1, 1, 1, 0), 
			createDateTime(2013, 1, 2, 1, 0), false, 0);
	
	private static ParserResult parserResultAdd2 = createPR(CommandType.ADD, "task2", 
			"stuty", TaskPriority.MEDIUM, createDateTime(2013, 3, 3, 1, 0), 
			createDateTime(2013, 4, 2, 1, 0), false, 0);
	
	private static ParserResult parserResultUpdate = createPR(CommandType.UPDATE, "task2", 
			"stuty", TaskPriority.MEDIUM, createDateTime(2013, 3, 3, 1, 0), 
			createDateTime(2013, 4, 2, 1, 0), false, 1); // since the task ID should be 1
	

	
	// display all parserResult
	private static ParserResult parserResultDisplayAll = createPR(CommandType.DISPLAY, "task3", 
			"stuty", TaskPriority.HIGH, createDateTime(2013, 1, 8, 5, 0), 
			createDateTime(2013, 1, 9, 1, 0), true, 0);
	
	// undo parserResult
	private static ParserResult parserResultUndo = createPR(CommandType.UNDO, "task3", 
				"stuty", TaskPriority.HIGH, createDateTime(2013, 1, 8, 5, 0), 
				createDateTime(2013, 1, 9, 1, 0), false, 0); // only the command type matter
	
	// create the expected result
			// create task objs
	private static Task task1 = createTask("task1", 
					"study", TaskPriority.LOW, createDateTime(2013, 1, 1, 1, 0), 
					createDateTime(2013, 1, 2, 1, 0), 1);
			
			
	private static Task task2 = createTask("task2", 
					"stuty", TaskPriority.MEDIUM, createDateTime(2013, 3, 3, 1, 0), 
					createDateTime(2013, 4, 2, 1, 0), 2);
	
	
	
	/*
	 * This test tests using use cases: add->add->displayAll->update->undo->undo
	 * */
	@Test
	public void testUndoAfterUpdate() throws Exception {
		
		// execute
		CommandManagerStub cm = new CommandManagerStub();
		cm.executeCommand(parserResultAdd1);
		cm.executeCommand(parserResultAdd2);
		cm.executeCommand(parserResultDisplayAll);
		cm.executeCommand(parserResultUpdate);
		
		cm.executeCommand(parserResultUndo); // undo an update and an add
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
			pr.setTaskID(id);
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
//		assertEquals(expected.getPrintOut(), result.getPrintOut());
		assertEquals(expected.getTaskList(), result.getTaskList());
	}

}
