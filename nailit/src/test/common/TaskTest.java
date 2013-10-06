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
		DateTime end = new DateTime(2013,3,30,11,20);
		Task task1 = new Task();
		Task task2 = new Task();
		task1.setStartTime(start);
		task2.setStartTime(start);
		task2.setEndTime(end);
		System.out.println(task1);
		assertTrue(task1.isAtSameStartTime(task2));
		assertFalse(task1.isAtSameTime(task2));
		assertFalse(task1.isOneDayEvent());
		assertTrue(task2.isOneDayEvent());
		task1.setEndTime(end);
		assertTrue(task1.isAtSameEndTime(task2));
		assertTrue(task1.isAtSameTime(task2));
		task2.setEndTime(new DateTime());
		assertFalse(task2.isAtSameEndTime(task1));
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
