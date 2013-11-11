package test.logic.command;

//@author A0105789R

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTime;
import org.junit.Test;

import nailit.common.CommandType;
import nailit.common.NIConstants;
import nailit.common.Result;
import nailit.common.TaskPriority;
import nailit.logic.ParserResult;

public class CommandAddAndDeleteReminderTest {
	
	private static ParserResult parserResultAdd1 = createPR(CommandType.ADD, "task1", 
			"stuty", TaskPriority.LOW, createDateTime(2013, 1, 1, 1, 0), 
			createDateTime(2013, 1, 2, 1, 0), false, 0, null);

	
	private static ParserResult parserResultAddReminder = createPR(CommandType.ADDREMINDER, "task2", 
			"stuty", TaskPriority.MEDIUM, createDateTime(2013, 3, 3, 1, 0), 
			createDateTime(2013, 4, 2, 1, 0), false, 1, createDateTime(2013, 2, 2, 1, 0));
	
	private static ParserResult parserResultDeleteReminder = createPR(CommandType.DELETEREMINDER, "task2", 
			"stuty", TaskPriority.MEDIUM, createDateTime(2013, 3, 3, 1, 0), 
			createDateTime(2013, 4, 2, 1, 0), false, 1, null);
	
	
	// display all parserResult
	private static ParserResult parserResultDisplayAll = createPR(CommandType.DISPLAY, "task3", 
			"stuty", TaskPriority.HIGH, createDateTime(2013, 1, 8, 5, 0), 
			createDateTime(2013, 1, 9, 1, 0), true, 0, null);
	
	@Test
	public void testAddReminder() throws Exception {
		CommandManagerStub cm = new CommandManagerStub();
		cm.executeCommand(parserResultAdd1);
		cm.executeCommand(parserResultDisplayAll);
		Result resultOfAdd = cm.executeCommand(parserResultAddReminder);
		
		Result expectedResult = new Result(false, true, Result.NOTIFICATION_DISPLAY, 
				"Added reminder for: task1 on " + createDateTime(2013, 2, 2, 1, 0).toString(NIConstants.DISPLAY_DATE_FORMAT), 
				null, null, null);
		
		testTwoResultObj(resultOfAdd, expectedResult);
	}
	
	@Test
	public void testDeleteReminder() throws Exception {
		CommandManagerStub cm = new CommandManagerStub();
		cm.executeCommand(parserResultAdd1);
		cm.executeCommand(parserResultDisplayAll);
		cm.executeCommand(parserResultAddReminder);
		Result resultOfAdd = cm.executeCommand(parserResultDeleteReminder);
		
		Result expectedResult = new Result(false, true, Result.NOTIFICATION_DISPLAY, 
				"Deleted reminder on " + createDateTime(2013, 2, 2, 1, 0).toString(NIConstants.DISPLAY_DATE_FORMAT) + " for task1", 
				null, null, null);
		
		testTwoResultObj(resultOfAdd, expectedResult);
	}
	
	private static ParserResult createPR(CommandType commandType, String name, 
			String tag, TaskPriority priority, DateTime st, DateTime et, boolean isDisplayAll, int id, DateTime reminder) {
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
		pr.setReminderTime(reminder);
		pr.setDisplayAll(isDisplayAll);
		
		return pr;
	}
	
	private static DateTime createDateTime(int year, int month, int day, int hour, int minute) {
		return new DateTime(year, month, day, hour, minute);
	} 
	
	private void testTwoResultObj(Result result,
			Result expected) {
		assertEquals(expected.getExitStatus(), result.getExitStatus());
		assertEquals(expected.getExecutionSuccess(), result.getExecutionSuccess());
		assertEquals(expected.getDisplayType(), result.getDisplayType());
		assertEquals(expected.getPrintOut(), result.getPrintOut());
	}
}
