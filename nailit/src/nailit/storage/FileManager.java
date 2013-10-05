package nailit.storage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import nailit.common.TaskPriority;
import nailit.common.Task;

import org.joda.time.DateTime;

public class FileManager {
	private Vector<String> dataList;
	private BufferedReader reader;
	private BufferedWriter writer;
	
	/**
	 * Constructor
	 * */
	public FileManager(String path){
		dataList = new Vector<String>();
		try {
			reader = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Public Methods
	 * */
	public void add(String stringToBeStored){
		dataList.add(stringToBeStored);
	}
	
	
	public void save(){
		
	}
	
	public Vector<String> getDataList(){
		return null; 
	}
	
	public void setDataList(Vector<String> d){
		
	}
	
	/**
	 * Privite Methods
	 * */
	private void read(){
		try {
			String line = null;
			int lastIndex = -1;
			while((line = reader.readLine()) != null){
				String[] s = line.split("\\" + Task.FIELD_SPLITTER);
				int ID = Integer.parseInt(s[0]);
				String taskString = line.substring(s[0].length()+Task.FIELD_SPLITTER.length());
				
				for(int i=lastIndex+1;i<=ID-1;i++){
					dataList.add(null);
				}
				lastIndex = ID;
				dataList.add(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
