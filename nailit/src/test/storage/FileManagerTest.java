package test.storage;

import static org.junit.Assert.*;

import java.util.Vector;

import nailit.storage.FileCorruptionException;
import nailit.storage.FileManager;

import org.junit.Test;
/**
 * @author a0105683e
 * */
public class FileManagerTest {
	public FileManager hardDisk = null;

	@Test
	public void test() throws FileCorruptionException {
		initializeHardDisk();
		hardDisk.writingProcessInit();
		testAddCommand("1,testfor1st,2013-01-02,2013-02-01,2,social,no more description,1\n","1,testfor1st,2013-01-02,2013-02-01,2,social,no more description,1");
		hardDisk.save();
		testAddCommand("1,testfor1st,2013-01-02,2013-02-01,2,social,no more description,1\n3,testFor2nd,2013-03-03,2013-04-05,1,scholl,some description,1\n","3,testFor2nd,2013-03-03,2013-04-05,1,scholl,some description,1");
		hardDisk.save();
	}
	
	public void testAddCommand(String expected,String task){
		hardDisk.add(task);
		Vector<String> v = hardDisk.getDataListForWriting();
		String out="";
		for(int i=0;i<v.size();i++){
			out+=v.get(i)+"\n";
		}
		assertEquals(expected,out);
	}
	public void initializeHardDisk() throws FileCorruptionException{
		hardDisk = new FileManager("my.txt");
	}

}
