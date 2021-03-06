//@author A0091372H
package test.common;

import static org.junit.Assert.*;
import org.junit.Test;

import nailit.common.Task;
import nailit.common.TaskPriority;
import org.joda.time.DateTime;
public class TaskTest {
	@Test
	public void isOverDueTaskTest(){
		Task pastTask = new Task();
		//At this point task is a floating task. should be false for overdue
		assertFalse(pastTask.isOverDueTask());
		DateTime now = new DateTime();
		System.out.println(now);
		DateTime start = now.minusDays(10);
		DateTime end = now.minusDays(5);
		pastTask.setStartTime(start);
		pastTask.setEndTime(end);
		assertFalse(pastTask.isOverDueTask());
		pastTask.setStartTime(null);
		assertTrue(pastTask.isOverDueTask());
	}
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
		assertFalse(event.isAfterDateTime(testDate));
		
		//test case 2: given time is between start and end time
		testDate = new DateTime(2013, 5, 15, 10, 30);
		assertFalse(event.isAfterDateTime(testDate));
		
		//test case 3: given time is after both start and end time
		testDate = new DateTime(2013, 6, 15, 10, 30);
		assertFalse(event.isAfterDateTime(testDate));
		//test boundary case
		testDate = new DateTime(2013, 6, 2, 10, 30);
		assertFalse(event.isAfterDateTime(testDate));
		
		//testTask with only one time
		//start time set.
		Task task = new Task();
		task.setStartTime(start);
		assertFalse(task.isAfterDateTime(testDate)); //testdate at 2/6/13 10:30
		testDate = new DateTime(2013, 5, 1, 10, 30);
		assertTrue(task.isAfterDateTime(testDate));
		//end time set.
		task.setStartTime(null);
		task.setEndTime(start);
		assertTrue(task.isAfterDateTime(testDate)); //testdate at 1/5/13 10:30
		testDate = new DateTime(2013, 6, 2, 10, 30);
		assertFalse(task.isAfterDateTime(testDate));
		//floating task
		task.setEndTime(null);
		assertFalse(task.isAfterDateTime(testDate));
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
		assertFalse(event.isBeforeDateTime(testDate));
		
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
		assertTrue(task.isBeforeDateTime(testDate)); //testdate at 2/6/13 10:30
		testDate = new DateTime(2013, 5, 1, 10, 30);
		assertFalse(task.isBeforeDateTime(testDate));
		//end time set.
		task.setStartTime(null);
		task.setEndTime(start);
		assertFalse(task.isBeforeDateTime(testDate)); //testdate at 1/5/13 10:30
		testDate = new DateTime(2013, 6, 2, 10, 30);
		assertTrue(task.isBeforeDateTime(testDate));
		//floating task
		task.setEndTime(null);
		assertFalse(task.isBeforeDateTime(testDate));
	}
	@Test
	public void isInDateRangeSingleDateNullStartRangeTest(){
		//Tasks with single
		DateTime time = new DateTime(2013, 5, 2, 10, 30);
		Task task1 = new Task();
		task1.setStartTime(time);
		
		Task task2 = new Task();
		task2.setEndTime(time);
		//Test case 1: end time of range before task time
		//should return false
		DateTime testStart = null;
		DateTime testEnd = new DateTime(2013, 4, 1, 0, 0);
		assertFalse(task1.isInDateRange(testStart, testEnd));
		assertFalse(task2.isInDateRange(testStart, testEnd));
		//test boundary case
		testEnd = new DateTime(2013, 5, 2, 10, 30);
		assertTrue(task1.isInDateRange(testStart, testEnd));
		assertTrue(task2.isInDateRange(testStart, testEnd));
		//Test case 2: end time after task time
		//should return true
		testStart = null;
		testEnd = new DateTime(2013, 5, 15 , 0, 0);
		assertTrue(task1.isInDateRange(testStart, testEnd));
		assertTrue(task2.isInDateRange(testStart, testEnd));
	}
	@Test
	public void isInDateRangeSingleDateNullEndRangeTest(){
		//Tasks with single
		DateTime time = new DateTime(2013, 5, 2, 10, 30);
		Task task1 = new Task();
		task1.setStartTime(time);
		
		Task task2 = new Task();
		task2.setEndTime(time);
		//Test case 1: start of range before task time
		//should return true
		DateTime testStart = new DateTime(2013, 1, 1, 0, 0);
		DateTime testEnd = null;
		assertTrue(task1.isInDateRange(testStart, testEnd));
		assertTrue(task2.isInDateRange(testStart, testEnd));
		//test boundary case
		testStart = new DateTime(2013, 5, 2, 10, 30);
		assertTrue(task1.isInDateRange(testStart, testEnd));
		assertTrue(task2.isInDateRange(testStart, testEnd));
		//test case 2: start time after task time
		//should return false
		testStart = new DateTime(2013, 5, 3, 0 , 0);
		testEnd = null;
		assertTrue(task1.isInDateRange(testStart, testEnd)); //task1 goes on indefinitely from 2/5/13, 10:30
		assertFalse(task2.isInDateRange(testStart, testEnd));
	}
	@Test
	public void isInDateRangeSingleDateTest(){
		//Tasks with single
		DateTime time = new DateTime(2013, 5, 2, 10, 30);
		Task task1 = new Task();
		task1.setStartTime(time);
		
		Task task2 = new Task();
		task2.setEndTime(time);
		//Test case 1: start and end time of range before task time
		//should return false
		DateTime testStart = new DateTime(2013, 1, 1, 0, 0);
		DateTime testEnd = new DateTime(2013, 4, 1, 0, 0);
		assertFalse(task1.isInDateRange(testStart, testEnd));
		assertFalse(task2.isInDateRange(testStart, testEnd));
		//test boundary case
		testEnd = new DateTime(2013, 5, 2, 10, 30);
		assertTrue(task1.isInDateRange(testStart, testEnd));
		assertTrue(task2.isInDateRange(testStart, testEnd));
		//Test case 2: start time before task startTime, end time after task time
		//should return true
		testStart = new DateTime(2013, 4, 1, 0 , 0);
		testEnd = new DateTime(2013, 5, 15 , 0, 0);
		assertTrue(task1.isInDateRange(testStart, testEnd));
		assertTrue(task2.isInDateRange(testStart, testEnd));
		//test case 3: start and end time after task time
		testStart = new DateTime(2013, 5, 3, 0 , 0);
		testEnd = new DateTime(2013, 6, 1 , 0, 0);
		assertTrue(task1.isInDateRange(testStart, testEnd)); //true because end time null means go on indefinitely
		assertFalse(task2.isInDateRange(testStart, testEnd));
	}
	@Test
	public void isInDateRangeNullStartTest(){
		//Tasks with start and end time
		DateTime startTime = new DateTime(2013, 5, 2, 10, 30);
		DateTime endTime = new DateTime(2013, 6, 2, 10, 30);
		Task event = new Task();
		event.setStartTime(startTime);
		event.setEndTime(endTime);
		//Test case 1: end time of range before task startTime
		//should return false
		DateTime testStart = null;
		DateTime testEnd = new DateTime(2013, 4, 1, 0, 0);
		assertFalse(event.isInDateRange(testStart, testEnd));
		//test boundary case
		testEnd = new DateTime(2013, 5, 2, 10, 30);
		assertTrue(event.isInDateRange(testStart, testEnd));
		//Test case 2:end time of range after task endTime
		//should return true
		testStart = null;
		testEnd = new DateTime(2013, 8, 1, 0, 0);
		assertTrue(event.isInDateRange(testStart, testEnd));
		//test boundary case
		testEnd = new DateTime(2013, 6, 2, 10, 30);
		assertTrue(event.isInDateRange(testStart, testEnd));
		//Test case 3: end time between task startTime and endTime
		//should return true
		testStart = null;
		testEnd = new DateTime(2013, 5, 15 , 0, 0);
		assertTrue(event.isInDateRange(testStart, testEnd));
	}
	@Test
	public void isInDateRangeNullEndTest(){
		//Tasks with start and end time
		DateTime startTime = new DateTime(2013, 5, 2, 10, 30);
		DateTime endTime = new DateTime(2013, 6, 2, 10, 30);
		Task event = new Task();
		event.setStartTime(startTime);
		event.setEndTime(endTime);
		//Test case 1: start of range before task startTime
		//should return true
		DateTime testStart = new DateTime(2013, 1, 1, 0, 0);
		DateTime testEnd = null;
		assertTrue(event.isInDateRange(testStart, testEnd));
		//test boundary case
		testStart = new DateTime(2013, 5, 2, 10, 30);
		assertTrue(event.isInDateRange(testStart, testEnd));
		//Test case 2: start of range after task endTime
		//should return false
		testStart = new DateTime(2013, 7, 1, 0, 0);
		testEnd = null;
		assertFalse(event.isInDateRange(testStart, testEnd));
		//test boundary case
		testStart = new DateTime(2013, 6, 2, 10, 30);
		assertTrue(event.isInDateRange(testStart, testEnd));
		//test case 3: start between task startTime and taskEnd time
		//should return true
		testStart = new DateTime(2013, 5, 3, 0 , 0);
		assertTrue(event.isInDateRange(testStart, testEnd));
	}
	@Test
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
		assertTrue(event.isInDateRange(testStart, testEnd));
		//Test case 2: start and end time of range after task endTime
		//should return false
		testStart = new DateTime(2013, 7, 1, 0, 0);
		testEnd = new DateTime(2013, 8, 1, 0, 0);
		assertFalse(event.isInDateRange(testStart, testEnd));
		//test boundary case
		testEnd = new DateTime(2013, 6, 2, 10, 30);
		assertTrue(event.isInDateRange(testStart, testEnd));
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
		Task event = new Task("Test!",start,end,"#School#",TaskPriority.LOW);
		event.setDescription("y");
		event.setReminder(new DateTime(2013, 9, 30, 9, 20));
		event.setID(1);
		assertEquals(event.getID(),1);
		Task eventCopy = event.copy();
		assertEquals(event.getDescription(),eventCopy.getDescription());
		assertEquals(event.getTag(),eventCopy.getTag());
		assertEquals(event.getReminder(), eventCopy.getReminder());
		assertTrue(event.isAtSameEndTime(eventCopy));
		assertTrue(event.equals(eventCopy));
		assertEquals(event.toString(),eventCopy.toString());
		
		Task task = new Task("task");
		task.setID(2);
		task.setCompleted(true);
		Task taskCopy = task.copy();
		assertEquals(task, taskCopy);
		assertEquals(task.getDescription(),taskCopy.getDescription());
		assertEquals(task.getTag(),taskCopy.getTag());
		assertEquals(task.getReminder(), taskCopy.getReminder());
		assertTrue(task.isAtSameEndTime(taskCopy));
		assertTrue(task.equals(taskCopy));
		assertEquals(task.toString(),taskCopy.toString());
	}
}
