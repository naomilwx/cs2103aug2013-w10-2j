package test.logic.command;

//@author A0105789R

import static org.junit.Assert.assertEquals;
import java.util.Vector;
import org.joda.time.DateTime;
import org.junit.Test;
import nailit.common.Result;
import nailit.common.Task;
import nailit.common.TaskPriority;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class CommandSearchTest {
	private static ParserResult parserResultAdd1 = createPR(CommandType.ADD, "task1", 
			"stuty", TaskPriority.LOW, createDateTime(2013, 1, 1, 1, 0), 
			createDateTime(2013, 1, 2, 1, 0), false, 0);
	
	private static ParserResult parserResultAdd2 = createPR(CommandType.ADD, "task2", 
			"stuty", TaskPriority.MEDIUM, createDateTime(2013, 3, 3, 1, 0), 
			createDateTime(2013, 4, 2, 1, 0), false, 0);
	
	
	// display all parserResult
	private static ParserResult parserResultSearchTask1 = createPR(CommandType.SEARCH, "task1", 
			"stuty", TaskPriority.LOW, null, null, false, 0);
	
	// create the expected result
	// create task objects
	private static Task task1 = createTask("task1", 
					"study", TaskPriority.LOW, createDateTime(2013, 1, 1, 1, 0), 
					createDateTime(2013, 1, 2, 1, 0), 1);
			
	@Test
	public void testSearch() throws Exception {
		CommandManagerStub cm = new CommandManagerStub();
		cm.executeCommand(parserResultAdd1);
		cm.executeCommand(parserResultAdd2);
		Result resultOfSearch = cm.executeCommand(parserResultSearchTask1);
		Vector<Task> currentTaskList = new Vector<Task>();
		currentTaskList.add(task1);
		
		Result expectedResult = new Result(false, true, Result.LIST_DISPLAY, 
				"", null, currentTaskList, null);
		
		testTwoResultObj(resultOfSearch, expectedResult);
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
	}
}