package test.storage;

import static org.junit.Assert.*;

import java.util.Vector;

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
	public void test() throws FileCorruptionException, NoTaskFoundException {
		sto = new StorageManager();
		sto.clear();
		Task task1 = new Task("frist task");
		Task task2 = new Task("second task");
		testAddCommand("1,frist task,null,null,1,,,0\n",task1);
		testAddCommand("1,frist task,null,null,1,,,0\n2,second task,null,null,1,,,0\n",task2);
		Task task3 = new Task(2,"third task",new DateTime("2013-02-03"),new DateTime("2013-03-03"),TaskPriority.MEDIUM,"school stuff","desc",true);
		testAddCommand("1,frist task,null,null,1,,,0\n2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1\n",task3);
//		Task task4 = new Task(-2,"nonsense task",null,null,TaskPriority.MEDIUM,"school stuff","desc",true);
//		testAddCommand("",task4);
		testRemoveCommand("2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1\n",1);
		
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
	
	public void testRemoveCommand(String expected,int ID) throws NoTaskFoundException, FileCorruptionException{
		sto.remove(ID);
		Vector<Task> v = sto.retrieveAll();
		String out = printVector(v);
		assertEquals(expected,out);
	}

}
