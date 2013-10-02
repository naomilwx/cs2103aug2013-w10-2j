package nailit.storage;
import org.joda.time.DateTime;

import java.util.ArrayList;

import nailit.common.TASK_PRIORITY;
import nailit.common.Task;

public class DataManager {
	
	/**
	 * Private Fields
	 * */
	private final int TASKID_NULL = -1;
	private ArrayList<String> dataList = new ArrayList<String>();
	
	/**
	 * Constructor
	 * */
	public DataManager(){
	
	}

	/**
	 * Public Methods
	 * */
	public int add(int ID, String name,DateTime startDate,DateTime endDate,int priority,String tag,String desc){
	
		if(!addedBefore(ID)){
			ID = generateNewID();
		}
		return -2;
	}
	
	public String retrieve(int lineNum){
		return null;
	}
	
	public String remove(int lineNum){
		return null;
	}
	
	public ArrayList<String> getDataList(){
		return null;
	}
	
	public void setDataList(ArrayList<String> d){
		
	}
	
	public DataManager clone(){
		return null;
	}
	
	/**
	 * Private Methods
	 * */
	private int generateNewID(){
		return -1;
	}
	
	private boolean addedBefore(int ID){
		return ID != TASKID_NULL;
	}
}
