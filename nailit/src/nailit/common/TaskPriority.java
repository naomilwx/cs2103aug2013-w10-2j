package nailit.common;

public enum TaskPriority {
	LOW(0), MEDIUM(1), HIGH(2);
	private int priorityCode;
	private TaskPriority(int code){
		priorityCode = code;
	}
	public int getPriorityCode(){
		return priorityCode;
	}
}
