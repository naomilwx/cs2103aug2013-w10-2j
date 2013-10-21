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
	public void removeTest() throws FileCorruptionException{
		sto = new StorageManager();
		sto.clear();
		Task task1 = new Task("first task");
		Task task2 = new Task("second task");
		testRemoveCommand("Assertion Error!",1);
		testRemoveCommand("Assertion Error!",0);
		testRemoveCommand("No task can be found!",Task.TASKID_NULL);
		
		testRemoveCommand("Assertion Error!",2);
		testAddCommand("" +
				"1,first task,null,null,1,,,0\n",task1);
		testRemoveCommand("",1);
		
		//undoRemove test
				Task taskRemoveUndo = task1.copy();
				taskRemoveUndo.setID(1);
				testAddCommand("" +
						"1,first task,null,null,1,,,0\n",taskRemoveUndo);

	}
	
	@Test
	public void integrateTest() throws FileCorruptionException, NoTaskFoundException {
		sto = new StorageManager();
		sto.clear();
		Task task1 = new Task("first task");
		Task task2 = new Task("second task");
		testAddCommand("" +
				"1,first task,null,null,1,,,0\n",task1);
		testAddCommand("" +
				"1,first task,null,null,1,,,0\n2,second task,null,null,1,,,0\n",task2);
		Task task3 = new Task(2,"third task",new DateTime("2013-02-03"),new DateTime("2013-03-03"),TaskPriority.MEDIUM,"school stuff","desc",true);
		testAddCommand("" +
				"1,first task,null,null,1,,,0\n2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1\n",task3);
//		Task task4 = new Task(-2,"nonsense task",null,null,TaskPriority.MEDIUM,"school stuff","desc",true);
//		testAddCommand("",task4);
		FilterObject fo = new FilterObject("first",null,null,null,null,null);
		testFilterCommand("" +
				"1,first task,null,null,1,,,0\n",fo);
		FilterObject fo1 = new FilterObject(null,new DateTime("2013-02-02"),null,null,null,null);
		testFilterCommand("" +
				"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1\n",fo1);
		FilterObject fo2 = new FilterObject(null,new DateTime("2013-02-02"),null,"school",null,null);
		testFilterCommand("",fo2);
		FilterObject fo3 = new FilterObject(null,new DateTime("2013-02-02"),null,"school stuff",null,null);
		testFilterCommand("" +
				"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1\n",fo3);
		FilterObject fo4 = new FilterObject("task",null,null,null,null,null);
		testFilterCommand("" +
				"1,first task,null,null,1,,,0\n2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1\n",fo4);
		testRemoveCommand("" +
				"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1\n",1);
		Task task6 = new Task(Task.TASKID_NULL,"testForUndo",new DateTime("1992-02-10"),null,TaskPriority.HIGH,"social","something should have assertions here!",false);
		testAddCommand("" +
				"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1\n" +
				"3,testForUndo,1992-02-10T00:00:00.000+08:00,null,2,social,something should have assertions here!,0\n",task6);
		testUndoAddCommand("" +
				"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1\n",3);
		Task task7 = new Task(Task.TASKID_NULL,"Add New Task After Undo",new DateTime("1992-02-10"),null,TaskPriority.HIGH,"social","something should have assertions here!",false);
		testAddCommand("" +
				"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1\n" +
				"3,Add New Task After Undo,1992-02-10T00:00:00.000+08:00,null,2,social,something should have assertions here!,0\n",task7);
		Task task8 = new Task(Task.TASKID_NULL,"test for add new task",new DateTime("2013-01-02"),null,TaskPriority.LOW,"school","no more description",false);
		testAddCommand("" +
				"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1\n" +
				"3,Add New Task After Undo,1992-02-10T00:00:00.000+08:00,null,2,social,something should have assertions here!,0\n" +
				"4,test for add new task,2013-01-02T00:00:00.000+08:00,null,0,school,no more description,0\n",task8);

	} 
	
	public String printVector(Vector<Task> v){
		String out = "";
		for(int i=0;i<v.size();i++){
			out += v.get(i).getID()+NIConstants.NORMAL_FIELD_SPLITTER+v.get(i).changeToDiskFormat()+"\n";
		}
		return out;
	}
	public void testAddCommand(String expected,Task task) throws FileCorruptionException{
		sto.add(task);
		Vector<Task> v = sto.retrieveAll();
		String out = printVector(v);
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
