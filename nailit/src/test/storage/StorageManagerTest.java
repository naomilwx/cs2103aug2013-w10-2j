package test.storage;

import static org.junit.Assert.*;

import java.util.Vector;

import nailit.common.Task;
import nailit.storage.FileCorruptionException;
import nailit.storage.StorageManager;

import org.junit.Test;

public class StorageManagerTest {
	public StorageManager sto;
	@Test
	public void test() throws FileCorruptionException {
		sto = new StorageManager();
		Task task1 = new Task("frist task");
		testAddCommand("",task1);
		
	}
	
	public String printVector(Vector<Task> v){
		String out = "";
		for(int i=0;i<v.size();i++){
			out += v.get(i).changeToDiskFormat()+"\n";
		}
		return out;
	}
	public void testAddCommand(String expected,Task task) throws FileCorruptionException{
		sto.add(task);
		Vector<Task> v = sto.retrieveAll();
		String out = printVector(v)+"\n";
		assertEquals(expected,out);
	}

}
