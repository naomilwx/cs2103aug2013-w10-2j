package test.storage;

import static org.junit.Assert.*;

import nailit.common.Task;
import nailit.storage.FileCorruptionException;
import nailit.storage.StorageManager;

import org.junit.Test;

public class StorageManagerTest {
	public StorageManager sto;
	@Test
	public void test() throws FileCorruptionException {
		sto = new StorageManager();
		
	}
	
	public void testAddCommand(String expected,Task task){
		sto.add(task);
	}

}
