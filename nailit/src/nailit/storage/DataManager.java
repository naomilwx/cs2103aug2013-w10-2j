package nailit.storage;
import org.joda.time.DateTime;

import java.util.Vector;

import nailit.common.NIConstants;
import nailit.common.TaskPriority;
import nailit.common.Task;

public class DataManager {
	
	/**
	 * Private Fields
	 * */
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
			dataList.add(ID + NIConstants.NORMAL_FIELD_SPLITTER + taskString);
		}
		else{
			assert(ID <= dataList.size()-1);
			dataList.set(ID, ID + NIConstants.NORMAL_FIELD_SPLITTER + taskString);
		}
		return ID;
	}
	
	public String retrieve(int ID) throws NoTaskFoundException{
		String line;
		if(ID>=dataList.size()){
			throw new NoTaskFoundException("The ID you typed in exceeds the maximum Index!");
		}
		else if((line = dataList.get(ID))==null){
			throw new NoTaskFoundException("The task has been deleted before");
		}
		else{
			return line;
		}
	}
	
	public void remove(int ID){
		dataList.set(ID, null);
	}
	
	public Vector<String> getDataList(){
		return dataList;
	}
	
	public void setDataList(Vector<String> d){
		dataList = d;
	}
	
	public DataManager clone(){
		return null;
	}
	
	/**
	 * Private Methods
	 * */
	private int generateNewID(){
		return dataList.size();
	}
	
	private boolean addedBefore(int ID){
		return ID != Task.TASKID_NULL;
	}
	
}
