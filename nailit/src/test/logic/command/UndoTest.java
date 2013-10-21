package test.logic.command;

import static org.junit.Assert.assertEquals;

import java.util.Vector;

import org.joda.time.DateTime;
import org.junit.Test;

import nailit.common.Result;
import nailit.common.Task;
import nailit.common.TaskPriority;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.logic.command.CommandManager;
import nailit.storage.FileCorruptionException;

public class UndoTest {

	@Test
	public void testUndoAfterAdd() throws Exception {
		
		ParserResult parserResultAdd1 = new ParserResult();
		createPR(parserResultAdd1,CommandType.ADD, "task1", 
				"stuty", TaskPriority.LOW, createDateTime(2013, 1, 1, 1, 0), 
				createDateTime(2013, 1, 2, 1, 0));
		
		ParserResult parserResultAdd2 = new ParserResult();
		createPR(parserResultAdd2, CommandType.ADD, "task2", 
				"stuty", TaskPriority.MEDIUM, createDateTime(2013, 3, 3, 1, 0), 
				createDateTime(2013, 4, 2, 1, 0));
		
		ParserResult parserResultAdd3 = new ParserResult();
		createPR(parserResultAdd3, CommandType.ADD, "task3", 
				"stuty", TaskPriority.HIGH, createDateTime(2013, 1, 8, 5, 0), 
				createDateTime(2013, 1, 9, 1, 0));
		
		// display all parserResult
		ParserResult parserResultDisplayAll = new ParserResult();
		createPR(parserResultDisplayAll, CommandType.DISPLAY, "task3", 
				"stuty", TaskPriority.HIGH, createDateTime(2013, 1, 8, 5, 0), 
				createDateTime(2013, 1, 9, 1, 0));
		parserResultDisplayAll.setDisplayAll(true);
		
		// undo parserResult
		ParserResult parserResultUndo = new ParserResult();
		createPR(parserResultUndo, CommandType.UNDO, "task3", 
				"stuty", TaskPriority.HIGH, createDateTime(2013, 1, 8, 5, 0), 
				createDateTime(2013, 1, 9, 1, 0)); // only the command type matter
		
		// execute
		
		CommandManager cm = new CommandManager();
		cm.executeCommand(parserResultAdd1);
		cm.executeCommand(parserResultAdd2);
		cm.executeCommand(parserResultAdd3);
		cm.executeCommand(parserResultDisplayAll);
		Result resultOfUndo = cm.executeCommand(parserResultUndo);
		
		// create the expected result
		// create task objs
		Task task1 = createTask("task1", 
				"study", TaskPriority.LOW, createDateTime(2013, 1, 1, 1, 0), 
				createDateTime(2013, 1, 2, 1, 0));
		task1.setID(1);
		
		Task task2 = createTask("task2", 
				"stuty", TaskPriority.MEDIUM, createDateTime(2013, 3, 3, 1, 0), 
				createDateTime(2013, 4, 2, 1, 0));
		task2.setID(2);
		
		
		Vector<Task> currentTaskList = new Vector<Task>();
		currentTaskList.add(task1);
		currentTaskList.add(task2);
		
		Result expectedResult = new Result(false, true, Result.EXECUTION_RESULT_DISPLAY, 
				"Undo successfully.", null, currentTaskList, null);
		
		testTwoResultObj(resultOfUndo, expectedResult);
		
	}
	
	private void createPR(ParserResult pr, CommandType commandType, String name, 
			String tag, TaskPriority priority, DateTime st, DateTime et) {
		
		pr.setCommand(commandType);
		pr.setName(name);
		pr.setPriority(priority);
		pr.setStartTime(st);
		pr.setEndTime(et);
		pr.setTag(tag);
	}
	
	private Task createTask(String name, 
			String tag, TaskPriority priority, DateTime st, DateTime et){
		return new Task(name, st, et, tag, priority);
	}
	
	private DateTime createDateTime(int year, int month, int day, int hour, int minute) {
		return new DateTime(year, month, day, hour, minute);
	}
	
	private void testTwoResultObj(Result result,
			Result expected) {
		assertEquals(expected.getExitStatus(), result.getExitStatus());
		assertEquals(expected.getExecutionSuccess(), result.getExecutionSuccess());
		assertEquals(expected.getDisplayType(), result.getDisplayType());
		assertEquals(expected.getPrintOut(), result.getPrintOut());
		assertEquals(expected.getTaskList(), result.getTaskList());
	}

}
