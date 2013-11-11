package test.common;

import static org.junit.Assert.*;
import nailit.common.TaskPriority;

import org.junit.Test;

public class TaskPriorityTest {

	@Test
	
	public void test(){
		
		isTaskPriorityTest(true, "LOW");
		isTaskPriorityTest(true, "low");
		isTaskPriorityTest(true, "LOW");
		isTaskPriorityTest(true, "higH");
		isTaskPriorityTest(false, "highl");
	}
	
	public void isTaskPriorityTest(boolean expected, String command) {
		
		assertEquals(expected, TaskPriority.isTaskPriority(command));
	}

}
