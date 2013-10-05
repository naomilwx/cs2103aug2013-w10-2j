package nailit.common;
import java.io.Serializable;


public enum TaskPriority {
	LOW(0), MEDIUM(1), HIGH(2);
	
	private int priorityCode;
	public static final TaskPriority DEFAULT_TASK_PRIORITY  = MEDIUM;
	
	private TaskPriority(int code){
		priorityCode = code;
	}
	
	public static boolean isTaskPriority(String p){
		for(TaskPriority priority: TaskPriority.values()){
			if(p.equalsIgnoreCase(priority.toString())){
				return true;
			}
		}
		return false;
	}
	
	public int getPriorityCode(){
		return priorityCode;
	}
	
	public static TaskPriority getPriority(int priorityCode) throws Exception{
		switch(priorityCode){
			case 0: return LOW;
			case 1: return MEDIUM;
			case 2: return HIGH;
			default: throw new Exception("No such Priority!");
		}
	}
}

