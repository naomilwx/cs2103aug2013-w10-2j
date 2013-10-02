package nailit.storage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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
		reader = new BufferedReader(new FileReader(File(path)));
	}
	
	/**
	 * Public Methods
	 * */
	public int add(int ID, String name,DateTime startDate,DateTime endDate,TASK_PRIORITY priority,String tag,String desc){

		return -2;
	}
	
	public ArrayList<String> read(){
		return null;
	}
	
	public void save(){
		
	}
	
	public ArrayList<String> getDataList(){
		return null;
	}
	
	public void setDataList(ArrayList<String> d){
		
	}

}
