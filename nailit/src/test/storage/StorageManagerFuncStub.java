//@author A0091372H
package test.storage;

import java.util.HashMap;
import java.util.Vector;

import org.joda.time.DateTime;

import nailit.storage.FileCorruptionException;
import nailit.storage.StorageManager;
import nailit.common.FilterObject;
import nailit.common.Task;
import nailit.common.TaskPriority;

public class StorageManagerFuncStub extends StorageManager {
	private HashMap<Integer, Task> taskMap = new HashMap<Integer, Task>();
	int nextID = 1;
	public StorageManagerFuncStub() throws FileCorruptionException {
		super();
	}
	
	@Override
	public int add(Task newTask) {
		int IDToReturn  = nextID;
		if(newTask.getID() == Task.TASKID_NULL){
			Task taskToStore = newTask.copy();
			taskToStore.setID(IDToReturn);
			taskMap.put(IDToReturn, taskToStore);
			nextID += 1;
		}else{
			IDToReturn = newTask.getID();
			taskMap.put(IDToReturn, newTask.copy());
		}
		return IDToReturn;
	}
	
	@Override
	public Task retrieve(int taskID) {
		return taskMap.get(taskID);
	}
	@Override
	public Task remove(int taskID, boolean isUndoAdd) {
		if(isUndoAdd){
			nextID -= 1;
		}
		
		Task ret = taskMap.remove(taskID);
		System.out.println(ret);
		return ret;
	}
	
	@Override
	public Vector<Task> retrieveAll(){
		Vector<Task> task = new Vector<Task>();
		for(int key: taskMap.keySet()){
			task.add(taskMap.get(key));
		}
		return task;
	}
	
	@Override
	public void clear(){
		nextID = 1;
		taskMap.clear();
	}
}
