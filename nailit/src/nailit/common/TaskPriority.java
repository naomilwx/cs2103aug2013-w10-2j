package nailit.common;

public enum TaskPriority {
	LOW(0), MEDIUM(1), HIGH(2);
	private int priorityCode;
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
}
