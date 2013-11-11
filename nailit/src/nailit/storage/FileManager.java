package nailit.storage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import nailit.common.NIConstants;
import nailit.common.TaskPriority;
import nailit.common.Task;
import nailit.storage.FileCorruptionException;

import org.joda.time.DateTime;
/**
 * FileManager class is used to read and write data directly from and to the file 
 * @author a0105683e
 * */
public class FileManager {
	private Vector<String> dataListForReading;
	private Vector<String> dataListForWriting;
	private BufferedReader reader;
	private BufferedWriter writer;
	private String path;
	
	/**
	 * Constructor 
	 * @param path 
	 * @throws FileCorruptionException 
	 * */
	public FileManager(String path) throws FileCorruptionException{
		setEnvironment(path);
		readFile();
	}
	
	
	/***************************
	 * Public Methods
	 ***************************/
	
	
	public void add(String taskStirng){
		dataListForWriting.add(taskStirng);
	}
	
	public void writingProcessInit(){
		dataListForWriting = new Vector<String>();
	}
	
	public void save(){
		try {
			writer = new BufferedWriter(new FileWriter(path));
			for(int i=0;i<dataListForWriting.size();i++){
				String line = dataListForWriting.get(i);
				if(line != null){
					writer.write(line+"\n");	
				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
		
	public Vector<String> getFileContents(){
		return dataListForReading; 
	}
	
	public Vector<String> getDataListForWriting(){
		return dataListForWriting;
	}
	
	public void setDataListForWriting(Vector<String> dataList){
		dataListForWriting = dataList;
	}
	
	
	/***************************
	 * Private Methods
	 ***************************/
	
	
	
	/**
	 * readFile
	 * @throws FileCorruptionException
	 * */
	private void readFile() throws FileCorruptionException{
		dataListForReading = new Vector<String>();
		initializeReader();
		read();
		closeReader();
	}
		
	/**
	 * Initialize reader
	 * */
	private void initializeReader(){
		try {
		    File file = new File(path);
		    		    
		    if(!file.exists()){
		    	file.createNewFile();
		    }
		    
		    reader = new BufferedReader(new FileReader(file));
		} catch (IOException e) {
	        e.printStackTrace();
		}

	}
	/**
	 * read into vector
	 * @throws FileCorruptionException 
	 * */
	private void read() throws FileCorruptionException{
		try {
			String line = null;
			
			while((line = reader.readLine()) != null){
				dataListForReading.add(line);
			}
			
		} catch (Exception e) {
			throw new FileCorruptionException("The database is corrupted");
		}
	}

	private void setEnvironment(String path){
		this.path = path;
	}
	
	/**
	 * Close Reader
	 * */
	
	private void closeReader(){
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
