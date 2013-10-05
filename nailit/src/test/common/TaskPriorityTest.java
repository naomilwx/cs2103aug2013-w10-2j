package test.common;

import static org.junit.Assert.*;
import nailit.common.TaskPriority;

import org.junit.Test;

public class TaskPriorityTest {

	@Test
	public void isTaskPriorityTest() {
		assertTrue(TaskPriority.isTaskPriority("MEDIUM"));
		assertTrue(TaskPriority.isTaskPriority("high"));
		assertFalse(TaskPriority.isTaskPriority("ha"));
	}

}
