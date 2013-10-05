package nailit.storage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import nailit.common.TASK_PRIORITY;
import nailit.common.Task;

import org.joda.time.DateTime;

public class FileManager {
	private ArrayList<String> dataList;
	private BufferedReader reader;
	private BufferedWriter writer;
	
	/**
	 * Constructor
	 * */
	public FileManager(String path){
		dataList = new ArrayList<String>();
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
	
	public ArrayList<String> getDataList(){
		return null; 
	}
	
	public void setDataList(ArrayList<String> d){
		
	}
	
	/**
	 * Privite Methods
	 * */
	private ArrayList<String> read(){
		try {
			String line = null;
			while((line = reader.readLine()) != null){
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
