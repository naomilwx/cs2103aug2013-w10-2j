package nailit.storage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import nailit.common.NIConstants;
import nailit.common.TaskPriority;
import nailit.common.Task;

import org.joda.time.DateTime;

public class FileManager {
	private Vector<String> dataListForReading;
	private Vector<String> dataListForWriting;
	private BufferedReader reader;
	private BufferedWriter writer;
	private String path;
	
	/**
	 * Constructor
	 * @throws FileCorruptionException 
	 * */
	public FileManager(String path) throws FileCorruptionException{
		setPath(path);
		readFile();
	}
	
	/**
	 * Public Methods
	 * */
	public void add(String stringToBeStored){
		dataListForWriting.add(stringToBeStored);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
	public Vector<String> getDataList(){
		return dataListForReading; 
	}
	
	public Vector<String> getDataListForWriting(){
		return dataListForWriting;
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
			int lastIndex = -1;
			while((line = reader.readLine()) != null){
				String[] s = line.split("\\" + NIConstants.NORMAL_FIELD_SPLITTER);
				int ID = Integer.parseInt(s[0]);
				
				for(int i=lastIndex+1;i<=ID-1;i++){
					dataListForReading.add(null);
				}
				lastIndex = ID;
				dataListForReading.add(line);
			}
		} catch (Exception e) {
			throw new FileCorruptionException("The database is corrupted");
		}
	}

	private void setPath(String path){
		this.path = path;
	}
	
	/**
	 * Close Reader
	 * */
	
	private void closeReader(){
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
