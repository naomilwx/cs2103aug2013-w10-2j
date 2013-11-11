//@author A0091372H
package test.overall;

import test.common.TaskTest;
import test.logic.command.CommandTest;
import test.logic.parser.ParserTest;
import test.storage.StorageManagerTest;
import static org.junit.Assert.*;
import nailit.common.NIConstants;
import nailit.common.Task;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	StorageManagerTest.class,
	TaskTest.class,
	TaskTest.class,
//	ParserTest.class,
	CommandTest.class,
	OverallTestAdd.class,
	OverAllTestUpdate.class,
	OverAllTestDelete.class,
})

public class OverallTestSuite {
	public static void compareTasksAttributes(Task expectedTask, Task resultingTask){
		assertEquals(expectedTask, resultingTask);
		assertEquals(expectedTask.getName(), resultingTask.getName());
		assertTrue(checkDateTimeEquals(expectedTask.getStartTime(), resultingTask.getStartTime()));
		assertTrue(checkDateTimeEquals(expectedTask.getEndTime(), resultingTask.getEndTime()));
		assertEquals(expectedTask.getPriority(), resultingTask.getPriority());
		assertEquals(expectedTask.getTag(), resultingTask.getTag());
		assertEquals(expectedTask.getDescription(), resultingTask.getDescription());
		assertTrue(checkDateTimeEquals(expectedTask.getReminder(), resultingTask.getReminder()));
	}
	public static boolean checkDateTimeEquals(DateTime date1, DateTime date2){
		if(date1 == null && date2 == null){
			return true;
		}
		if(date1 == null && date2 != null){
			return false;
		}
		if(date1 != null && date2 == null){
			return false;
		}
		return date1.toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT).equals(date2.toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT));
	}
}

