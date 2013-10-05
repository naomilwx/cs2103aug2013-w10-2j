package nailit.storage;
import org.joda.time.DateTime;

import java.util.Vector;

import nailit.common.TASK_PRIORITY;
import nailit.common.Task;

public class DataManager {
	
	/**
	 * Private Fields
	 * */
	private final int TASKID_NULL = -1;
	private Vector<String> dataList = new Vector<String>();
	
	/**
	 * Constructor
	 * */
	public DataManager(){
	
	}

	/**
	 * Public Methods
	 * */
	public int add(int ID, String taskString){
		
		if(!addedBefore(ID)){
			ID = generateNewID();
			dataList.add(taskString);
		}
		else{
			dataList.set(ID, taskString);
		}
		return ID;
	}
	
	public String retrieve(int lineNum){
		return null;
	}
	
	public String remove(int lineNum){
		return null;
	}
	
	public Vector<String> getDataList(){
		return null;
	}
	
	public void setDataList(Vector<String> d){
		
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
