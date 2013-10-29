package test.storage;

import static org.junit.Assert.*;

import java.util.Vector;

import nailit.common.FilterObject;
import nailit.common.NIConstants;
import nailit.common.Task;
import nailit.common.TaskPriority;
import nailit.storage.FileCorruptionException;
import nailit.storage.NoTaskFoundException;
import nailit.storage.StorageManager;

import org.joda.time.DateTime;
import org.junit.Test;

public class StorageManagerTest {
	public StorageManager sto;
	
	@Test
	public void addTest() throws FileCorruptionException{
		sto = new StorageManager();
		sto.clear();
		
		/**
		 * Test for add null task should return null task ID
		 */
		Task task1 = null;
		testAddCommand("",Task.TASKID_NULL,task1);
		
		/**
		 * Tests for add a task with boundary ID value: -2, 0, 1
		 */
		Task task2 = new Task("second task");
		task2.setID(1);
		testAddCommand("The ID 1 is out of the range",1,task2);
		Task task3 = new Task("third task");
		task3.setID(0);
		testAddCommand("The ID 0 is out of the range",0,task3);
		Task task4 = new Task("fourth task");
		task4.setID(-2);
		testAddCommand("The ID -2 is out of the range",-2,task4);
		
		//TODO: test for MAXIMUN
		
		/**
		 * Test with valid ID
		 */
		Task task5 = new Task("fifth task");
		testAddCommand("1,fifth task,null,null,1,,,0,null\n",1,task5);
		Task task6 = new Task(Task.TASKID_NULL,"sixth task",new DateTime("2013-02-03"),new DateTime("2013-03-03"),TaskPriority.MEDIUM,"school stuff","desc",true);
		testAddCommand("" +
				"1,fifth task,null,null,1,,,0,null\n" +
				"2,sixth task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
				"",2,task6);
		Task task7 = new Task(2,"seventh task",new DateTime("2013-02-03"),new DateTime("2013-03-03"),TaskPriority.MEDIUM,"school stuff","desc",true);
		testAddCommand("" +
				"1,fifth task,null,null,1,,,0,null\n" +
				"2,seventh task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
				"",2,task7);

	}
	
	@Test
	public void removeTest() throws FileCorruptionException{
		sto = new StorageManager();
		sto.clear();
		
		/**
		 * Test for operations on empty storage
		 * */
		testRemoveCommand("Assertion Error!",Task.TASKID_NULL);
		testRemoveCommand("Assertion Error!",0);
		testRemoveCommand("Assertion Error!",1);
		testRemoveCommand("Assertion Error!",2);
		
		/**
		 * Test for operations on non-empty storage
		 * */
		Task task5 = new Task("fifth task");
		testAddCommand("1,fifth task,null,null,1,,,0,null\n",1,task5);
		Task task6 = new Task(Task.TASKID_NULL,"sixth task",new DateTime("2013-02-03"),new DateTime("2013-03-03"),TaskPriority.MEDIUM,"school stuff","desc",true);
		testAddCommand("" +
				"1,fifth task,null,null,1,,,0,null\n" +
				"2,sixth task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
				"",2,task6);
		
		
		/**
		 * Test for undo remove
		 * */
		sto.clear();
		Task task1 = new Task("first task");
		testAddCommand("" +
				"1,first task,null,null,1,,,0,null\n" +
				"",1,task1);
		testRemoveCommand("",1);
		Task taskRemoveUndo = task1.copy();
		taskRemoveUndo.setID(1);
		testUndoRemoveCommand("" +
					   "1,first task,null,null,1,,,0,null\n",1,taskRemoveUndo);
		
	}
	
	@Test 
	public void filterTest() throws FileCorruptionException{
		sto = new StorageManager();
		sto.clear();
		
		/***************************************************************
		 * Testing on the empty storage
		 **************************************************************** */
		
		/**
		 * Filter on the empty storage
		 * */
		FilterObject foOnEmptySto = new FilterObject("first",null,null,null,null,null);
		testFilterCommand("",foOnEmptySto);
		
		/**
		 * null filter test on the empty storage
		 * */
		testFilterCommand("",null);
		testFilterCommand("",new FilterObject());//because storage is null
		
		/***************************************************************
		 * Testing on the non-empty storage
		 **************************************************************** */
		Task task1 = new Task("first task");
		Task task2 = new Task(Task.TASKID_NULL,"third task",new DateTime("2013-02-03"),new DateTime("2013-03-03"),TaskPriority.MEDIUM,"school stuff","desc",true);
		testAddCommand("" +
				"1,first task,null,null,1,,,0,null\n" +
				"",1,task1);
		testAddCommand("" +
				"1,first task,null,null,1,,,0,null\n" +
				"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
				"",2,task2);

		/**
		 * Testing when filter object is null
		 * */
		testFilterCommand("",null);
		testFilterCommand("" +
				"1,first task,null,null,1,,,0,null\n" +
				"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
				"",new FilterObject());
		
		/**
		 * Testing when filter object is not null
		 * */
		FilterObject fo = new FilterObject("first",null,null,null,null,null);
		testFilterCommand("" +
				"1,first task,null,null,1,,,0,null\n" +
				"",fo);
		
		FilterObject fo1 = new FilterObject(null,new DateTime("2013-02-02"),null,null,null,null);
		testFilterCommand("" +
				"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
				"",fo1);
		
		FilterObject fo2 = new FilterObject(null,new DateTime("2013-02-02"),null,"school",null,null);
		testFilterCommand("",fo2);
		
		FilterObject fo3 = new FilterObject(null,new DateTime("2013-02-02"),null,"school stuff",null,null);
		testFilterCommand("" +
				"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
				"",fo3);
		
		FilterObject fo4 = new FilterObject("task",null,null,null,null,null);
		testFilterCommand("" +
				"1,first task,null,null,1,,,0,null\n" +
				"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
				"",fo4);		
	}
	@Test
	public void integrateTest() throws FileCorruptionException, NoTaskFoundException {
		sto = new StorageManager();
		sto.clear();
		Task task1 = new Task("first task");
		Task task2 = new Task("second task");
		testAddCommand("" +
				"1,first task,null,null,1,,,0,null\n" +
				"",1,task1);
		testAddCommand("" +
				"1,first task,null,null,1,,,0,null\n" +
				"2,second task,null,null,1,,,0,null\n" +
				"",2,task2);
		Task task3 = new Task(2,"third task",new DateTime("2013-02-03"),new DateTime("2013-03-03"),TaskPriority.MEDIUM,"school stuff","desc",true);
		testAddCommand("" +
				"1,first task,null,null,1,,,0,null\n" +
				"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
				"",2,task3);
//		Task task4 = new Task(-2,"nonsense task",null,null,TaskPriority.MEDIUM,"school stuff","desc",true);
//		testAddCommand("",task4);
		FilterObject fo = new FilterObject("first",null,null,null,null,null);
		testFilterCommand("" +
				"1,first task,null,null,1,,,0,null\n" +
				"",fo);
		FilterObject fo1 = new FilterObject(null,new DateTime("2013-02-02"),null,null,null,null);
		testFilterCommand("" +
				"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
				"",fo1);
		FilterObject fo2 = new FilterObject(null,new DateTime("2013-02-02"),null,"school",null,null);
		testFilterCommand("",fo2);
		FilterObject fo3 = new FilterObject(null,new DateTime("2013-02-02"),null,"school stuff",null,null);
		testFilterCommand("" +
				"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
				"",fo3);
		FilterObject fo4 = new FilterObject("task",null,null,null,null,null);
		testFilterCommand("" +
				"1,first task,null,null,1,,,0,null\n" +
				"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
				"",fo4);
		testRemoveCommand("" +
				"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
				"",1);
		Task task6 = new Task(Task.TASKID_NULL,"testForUndo",new DateTime("1992-02-10"),null,TaskPriority.HIGH,"social","something should have assertions here!",false);
		testAddCommand("" +
				"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
				"3,testForUndo,1992-02-10T00:00:00.000+08:00,null,2,social,something should have assertions here!,0,null\n" +
				"",3,task6);
		testUndoAddCommand("" +
				"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
				"",3);
		Task task7 = new Task(Task.TASKID_NULL,"Add New Task After Undo",new DateTime("1992-02-10"),null,TaskPriority.HIGH,"social","something should have assertions here!",false);
		testAddCommand("" +
				"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
				"3,Add New Task After Undo,1992-02-10T00:00:00.000+08:00,null,2,social,something should have assertions here!,0,null\n" +
				"",3,task7);
		Task task8 = new Task(Task.TASKID_NULL,"test for add new task",new DateTime("2013-01-02"),null,TaskPriority.LOW,"school","no more description",false);
		testAddCommand("" +
				"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
				"3,Add New Task After Undo,1992-02-10T00:00:00.000+08:00,null,2,social,something should have assertions here!,0,null\n" +
				"4,test for add new task,2013-01-02T00:00:00.000+08:00,null,0,school,no more description,0,null\n" +
				"",4,task8);

	} 
	
	public String printVector(Vector<Task> v){
		String out = "";
		for(int i=0;i<v.size();i++){
			out += v.get(i).getID()+","+changeToDiskFormat(v.get(i))+"\n";
		}
		return out;
	}
	public String changeToDiskFormat(Task task){
		int priority = task.getPriority().getPriorityCode();
		
		String name = task.getName();
		String startDate;
		if(task.getStartTime() == null){
			startDate = null;
		}
		else{
			startDate = task.getStartTime().toString();
		}
		
		String endDate;
		if(task.getEndTime() == null){
			endDate = null;
		}
		else{
			endDate = task.getEndTime().toString();
		}
		String desc = task.getDescription();
		String tag = task.getTag();
		
		int completeStatus = (task.checkCompleted())? 1:0;
		
		String reminderDate;
		if(task.getReminder()==null){
			reminderDate = null;
		}else{
			reminderDate = task.getReminder().toString();
		}
		
		String taskString = name + "," + startDate + "," + endDate + ","+ priority + "," + tag + "," +desc +"," + completeStatus + "," + reminderDate;
		
		return taskString;
	}

	public void testAddCommand(String expected,int expectedID,Task task) throws FileCorruptionException{
		String out;
		try{
			int ID = sto.add(task);
			Vector<Task> v = sto.retrieveAll();
			try{
				assertEquals(expectedID,ID);
				out = printVector(v);
			}catch(AssertionError a1){
				out = "The number is not match!"; 
			}
		}catch(AssertionError a2){
			out = "The ID "+task.getID()+" is out of the range";
		}
		
		assertEquals(expected,out);
	}
	
	public void testUndoRemoveCommand(String expected,int expectedID,Task task){
		String out;
		try{
			int ID = sto.add(task);
			Vector<Task> v = sto.retrieveAll();
			try{
				assertEquals(expectedID,ID);
				out = printVector(v);
			}catch(AssertionError a1){
				out = "The number is not match!"; 
			}
		}catch(AssertionError a2){
			out = "The ID "+task.getID()+" is out of the range";
		}
		
		assertEquals(expected,out);
	}
	public void testRemoveCommand(String expected,int ID) {
		String out;
		try {
			sto.remove(ID,false);
			Vector<Task> v = sto.retrieveAll();
			out = printVector(v);
		} catch (NoTaskFoundException e) {
			out = "No task can be found!";
		}catch(AssertionError a){
			out = "Assertion Error!";
		}

		
		assertEquals(expected,out);
	}

	public void testFilterCommand(String expected,FilterObject fo){
		Vector<Task> v=sto.filter(fo);
		String out = printVector(v);
		assertEquals(expected,out);
	}
	public void testUndoAddCommand(String expected,int ID) throws NoTaskFoundException{
		sto.remove(ID,true);
		Vector<Task> v = sto.retrieveAll();
		String out = printVector(v);
		assertEquals(expected,out);
	}
}
