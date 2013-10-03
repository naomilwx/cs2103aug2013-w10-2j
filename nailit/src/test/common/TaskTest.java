package test.common;

import static org.junit.Assert.*;
import org.junit.Test;

import nailit.common.Task;
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
	}
}
