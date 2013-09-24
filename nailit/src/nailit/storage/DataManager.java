package nailit.storage;
import org.joda.time.DateTime;

import java.util.ArrayList;

import nailit.Task;

public class DataManager {
	private final int TASKID_NULL = -1;
	private FileManager hardDisk;
	private ArrayList<Task> tasks = new ArrayList<Task>();
	private String hardDiskPath = "";
	
	public DataManager(){
		setUpHardDisk();
		initialiseInMeomoryTasks();
	}
	public void setUpHardDisk(){
		hardDisk = new FileManager(hardDiskPath);
	}
	public void initialiseInMeomoryTasks(){
		hardDisk.read(tasks);
	}
	
	public int add(Task task){
		
		int ID = task.getID();
		int priority = task.getPriority();
		String name = task.getName();
		DateTime startDate = task.getStartTime();
		DateTime endDate = task.getEndTime();
		String desc = task.getDescription();
		String tag = task.getTag();
		
		if(!addedBefore(ID)){
			ID = generateNewID();
		}
		hardDisk.add(ID,name,startDate,endDate,priority,desc,tag);
		return ID;
	}
	
	public void saveToFile(){
		hardDisk.save(tasks);
	}
	public boolean addedBefore(int ID){
		return ID != TASKID_NULL;
	}
	public int generateNewID(){
		return -1;
	}
	//Constructor

}
