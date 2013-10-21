package test.common;

import static org.junit.Assert.*;
import org.junit.Test;

import nailit.common.Task;
import nailit.common.TaskPriority;
import org.joda.time.DateTime;
public class TaskTest {
	@Test
	public void emptyTaskTest() {
		Task emptyTask = new Task();
		assertTrue(emptyTask.isFloatingTask());
		assertFalse(emptyTask.isEvent());
		Task secondEmptyTask = new Task();
		assertFalse(emptyTask.equals(secondEmptyTask));
		assertTrue(emptyTask.isAtSameTime(secondEmptyTask));
	}
	@Test
	public void TaskIDTest(){
		Task task1 = new Task("Test Task!");
		assertEquals(task1.getID(),Task.TASKID_NULL);
		boolean IDAddSuccess = task1.setID(1);
		assertTrue(IDAddSuccess);
		assertEquals(task1.getID(),1);
		IDAddSuccess = task1.setID(2);
		assertFalse(IDAddSuccess);
		assertEquals(task1.getID(),1);
	}
	@Test
	public void timeCompareFunctionsTest(){
		DateTime start = new DateTime(2013,3,30,10,20);
		DateTime end = new DateTime(2013,4,30,11,20);
		Task task1 = new Task();
		Task task2 = new Task();
		task1.setStartTime(start);
		task2.setStartTime(start);
		task2.setEndTime(end);
		
		System.out.println(task1);
		assertTrue(task1.isAtSameStartTime(task2));
		assertFalse(task1.isAtSameTime(task2));
		
		System.out.println(task2);
		task1.setEndTime(end);
		assertTrue(task1.isAtSameEndTime(task2));
		assertTrue(task1.isAtSameTime(task2));
		task2.setEndTime(new DateTime());
		assertFalse(task2.isAtSameEndTime(task1));
	}
	@Test
	public void oneDayEventTest(){
		DateTime start = new DateTime(2013,3,30,10,20);
		DateTime end = new DateTime(2013,4,30,11,20);
		Task task1 = new Task();
		Task task2 = new Task();
		task1.setStartTime(start);
		task2.setStartTime(start);
		task2.setEndTime(end);
		
		assertFalse(task1.isOneDayEvent());
		assertFalse(task2.isOneDayEvent());
		
		end = new DateTime(2013,3,30,11,20);
		task2.setEndTime(end);
		assertTrue(task2.isOneDayEvent());
	}
	@Test
	public void isAfterDateTimeTest(){
		DateTime start = new DateTime(2013, 5, 2, 10, 30);
		DateTime end = new DateTime(2013, 6, 2, 10, 30);
		DateTime testDate;
		//Test tasks with start and end times
		Task event = new Task();
		event.setStartTime(start);
		event.setEndTime(end);
		//test case 1: given time is before both start and end time
		testDate = new DateTime(2013, 5, 1, 10, 30);
		assertTrue(event.isAfterDateTime(testDate));
		//test boundary case
		testDate = new DateTime(2013, 5, 2, 10, 30);
		assertTrue(event.isAfterDateTime(testDate));
		
		//test case 2: given time is between start and end time
		testDate = new DateTime(2013, 5, 15, 10, 30);
		assertFalse(event.isAfterDateTime(testDate));
		
		//test case 3: givent time is after both start and end time
		testDate = new DateTime(2013, 6, 15, 10, 30);
		assertFalse(event.isAfterDateTime(testDate));
		//test boundary case
		testDate = new DateTime(2013, 6, 2, 10, 30);
		assertFalse(event.isAfterDateTime(testDate));
		
		//testTask with only one time
		//start time set.
		Task task = new Task();
		task.setStartTime(start);
		assertFalse(event.isAfterDateTime(testDate)); //testdate at 2/6/13 10:30
		testDate = new DateTime(2013, 5, 1, 10, 30);
		assertTrue(event.isAfterDateTime(testDate));
		//end time set.
		task.setStartTime(null);
		task.setEndTime(start);
		assertTrue(event.isAfterDateTime(testDate)); //testdate at 1/5/13 10:30
		testDate = new DateTime(2013, 6, 2, 10, 30);
		assertFalse(event.isAfterDateTime(testDate));
		//floating task
		task.setEndTime(null);
		assertFalse(event.isAfterDateTime(testDate));
	}
	@Test
	public void isBeforeDateTimeTest(){
		DateTime start = new DateTime(2013, 5, 2, 10, 30);
		DateTime end = new DateTime(2013, 6, 2, 10, 30);
		DateTime testDate;
		//Test tasks with start and end times
		Task event = new Task();
		event.setStartTime(start);
		event.setEndTime(end);
		//test case 1: given time is before both start and end time
		testDate = new DateTime(2013, 5, 1, 10, 30);
		assertFalse(event.isBeforeDateTime(testDate));
		//test boundary case
		testDate = new DateTime(2013, 5, 2, 10, 30);
		assertTrue(event.isAfterDateTime(testDate));
		
		//test case 2: given time is between start and end time
		testDate = new DateTime(2013, 5, 15, 10, 30);
		assertTrue(event.isBeforeDateTime(testDate));
		
		//test case 3: given time is after both start and end time
		testDate = new DateTime(2013, 6, 15, 10, 30);
		assertTrue(event.isBeforeDateTime(testDate));
		//test boundary case
		testDate = new DateTime(2013, 6, 2, 10, 30);
		assertTrue(event.isBeforeDateTime(testDate));
		
		//testTask with only one time
		//start time set.
		Task task = new Task();
		task.setStartTime(start);
		assertTrue(event.isBeforeDateTime(testDate)); //testdate at 2/6/13 10:30
		testDate = new DateTime(2013, 5, 1, 10, 30);
		assertFalse(event.isBeforeDateTime(testDate));
		//end time set.
		task.setStartTime(null);
		task.setEndTime(start);
		assertFalse(event.isBeforeDateTime(testDate)); //testdate at 1/5/13 10:30
		testDate = new DateTime(2013, 6, 2, 10, 30);
		assertTrue(event.isBeforeDateTime(testDate));
		//floating task
		task.setEndTime(null);
		assertTrue(event.isBeforeDateTime(testDate));
	}
	@Test
	public void isInDateRangeSingleDateTest(){
		//Tasks with single
		DateTime time = new DateTime(2013, 5, 2, 10, 30);
		Task task1 = new Task();
		task1.setStartTime(time);
		
		Task task2 = new Task();
		task2.setEndTime(time);
		//Test case 1: start and end time of range before task startTime
		//should return false
		DateTime testStart = new DateTime(2013, 1, 1, 0, 0);
		DateTime testEnd = new DateTime(2013, 4, 1, 0, 0);
		assertFalse(task1.isInDateRange(testStart, testEnd));
		assertFalse(task2.isInDateRange(testStart, testEnd));
		//test boundary case
		testEnd = new DateTime(2013, 5, 2, 10, 30);
		assertFalse(task1.isInDateRange(testStart, testEnd));
		assertFalse(task2.isInDateRange(testStart, testEnd));
		//Test case 2: start time before task startTime, end time after task startTime
		//should return true
		testStart = new DateTime(2013, 4, 1, 0 , 0);
		testEnd = new DateTime(2013, 5, 15 , 0, 0);
		assertTrue(task1.isInDateRange(testStart, testEnd));
		assertTrue(task2.isInDateRange(testStart, testEnd));
		//test case 3: start and end time after task startTime
		//should return false
		testStart = new DateTime(2013, 5, 3, 0 , 0);
		testEnd = new DateTime(2013, 6, 1 , 0, 0);
		assertFalse(task1.isInDateRange(testStart, testEnd));
		assertFalse(task2.isInDateRange(testStart, testEnd));
	}
	
	public void isInDateRangeTest(){
		//Tasks with start and end time
		DateTime startTime = new DateTime(2013, 5, 2, 10, 30);
		DateTime endTime = new DateTime(2013, 6, 2, 10, 30);
		Task event = new Task();
		event.setStartTime(startTime);
		event.setEndTime(endTime);
		//Test case 1: start and end time of range before task startTime
		//should return false
		DateTime testStart = new DateTime(2013, 1, 1, 0, 0);
		DateTime testEnd = new DateTime(2013, 4, 1, 0, 0);
		assertFalse(event.isInDateRange(testStart, testEnd));
		//test boundary case
		testEnd = new DateTime(2013, 5, 2, 10, 30);
		assertFalse(event.isInDateRange(testStart, testEnd));
		//Test case 2: start and end time of range after task endTime
		//should return false
		testStart = new DateTime(2013, 7, 1, 0, 0);
		testEnd = new DateTime(2013, 8, 1, 0, 0);
		assertFalse(event.isInDateRange(testStart, testEnd));
		//test boundary case
		testEnd = new DateTime(2013, 6, 2, 10, 30);
		assertFalse(event.isInDateRange(testStart, testEnd));
		//Test case 3: start time before task startTime, end time between task startTime and endTime
		//should return true
		testStart = new DateTime(2013, 4, 1, 0 , 0);
		testEnd = new DateTime(2013, 5, 15 , 0, 0);
		assertTrue(event.isInDateRange(testStart, testEnd));
		//Test case 4: start time before task startTime, end time is after task endTime
		//should return true
		testEnd = new DateTime(2013, 7, 15 , 0, 0);
		assertTrue(event.isInDateRange(testStart, testEnd));
		//test case 5: start and end time between task startTime and taskEnd time
		//should return true
		testStart = new DateTime(2013, 5, 3, 0 , 0);
		testEnd = new DateTime(2013, 6, 1 , 0, 0);
		assertTrue(event.isInDateRange(testStart, testEnd));
		//test case 6: start between task startTime and taskEnd time, end after taskEnd time
		//should return true
		testEnd = new DateTime(2013, 7, 1, 0, 0);
		assertTrue(event.isInDateRange(testStart, testEnd));
	}
	@Test
	public void TaskCopyTest(){
		DateTime start = new DateTime(2013, 9, 30, 10, 20);
		DateTime end = new DateTime();
		Task task = new Task("Test!",start,end,"School",TaskPriority.LOW);
		task.setDescription("y");
		task.setID(1);
		task.setCompleted(true);
		assertEquals(task.getID(),1);
		assertTrue(task.checkCompleted());
		Task taskCopy = task.copy();
		assertEquals(task.getDescription(),taskCopy.getDescription());
		assertEquals(task.getTag(),taskCopy.getTag());
		assertTrue(task.isAtSameEndTime(taskCopy));
		assertTrue(task.equals(taskCopy));
		assertEquals(task.toString(),taskCopy.toString());
		assertTrue(task.isClone(taskCopy));
	}
}
