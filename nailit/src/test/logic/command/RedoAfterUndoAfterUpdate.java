package test.logic.command;

//@author A0105789R

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Vector;

import nailit.common.CommandType;
import nailit.common.Result;
import nailit.common.Task;
import nailit.common.TaskPriority;
import nailit.logic.ParserResult;

import org.joda.time.DateTime;

import test.overall.OverallTestSuite;

public class RedoAfterUndoAfterUpdate {
	private static ParserResult parserResultAdd1 = createPR(CommandType.ADD, "task1", 
			"study", TaskPriority.LOW, createDateTime(2013, 1, 1, 1, 0), 
			createDateTime(2013, 1, 2, 1, 0), false, 0);
	
	private static ParserResult parserResultUpdate = createPR(CommandType.UPDATE, "task2", 
			"study", TaskPriority.MEDIUM, createDateTime(2013, 3, 3, 1, 0), 
			createDateTime(2013, 4, 2, 1, 0), false, 1); // since the task ID should be 1
	
	// display all parserResult
	private static ParserResult parserResultDisplayAll = createPR(CommandType.DISPLAY, "task3", 
			"study", TaskPriority.HIGH, createDateTime(2013, 1, 8, 5, 0), 
			createDateTime(2013, 1, 9, 1, 0), true, 0);
	
	// undo parserResult
	private static ParserResult parserResultUndo = createPR(CommandType.UNDO, "task3", 
				"study", TaskPriority.HIGH, createDateTime(2013, 1, 8, 5, 0), 
				createDateTime(2013, 1, 9, 1, 0), false, 0); // only the command type matter
	
	// redo the undo
	private static ParserResult parserResultRedo = createPR(CommandType.REDO, "task3", 
				"study", TaskPriority.HIGH, createDateTime(2013, 1, 8, 5, 0), 
				createDateTime(2013, 1, 9, 1, 0), false, 0); // only the command type matter
	
	private static Task task2 = createTask("task2", 
					"study", TaskPriority.MEDIUM, createDateTime(2013, 3, 3, 1, 0), 
					createDateTime(2013, 4, 2, 1, 0), 1);
	
	@Test
	public void testUndoAfterUpdate() throws Exception {
		// add, update, undo, undo, redo, redo
		// execute
		CommandManagerStub cm = new CommandManagerStub();
		cm.executeCommand(parserResultAdd1);
		cm.executeCommand(parserResultDisplayAll);
		cm.executeCommand(parserResultUpdate);
		cm.executeCommand(parserResultUndo);
		cm.executeCommand(parserResultUndo);
		cm.executeCommand(parserResultRedo);
		Result resultOfRedo = cm.executeCommand(parserResultRedo);
		Vector<Task> currentTaskList = new Vector<Task>();
		currentTaskList.add(task2);
		
		Result expectedResult = new Result(false, true, Result.EXECUTION_RESULT_DISPLAY, 
				"Redo successfully.", null, currentTaskList, null);
		
		testTwoResultObj(resultOfRedo, expectedResult);
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
