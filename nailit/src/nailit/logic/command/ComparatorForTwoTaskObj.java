package nailit.logic.command;

import java.util.Comparator;

import org.joda.time.DateTime;

import nailit.common.Task;
import nailit.common.TaskPriority;

public class ComparatorForTwoTaskObj implements Comparator<Task>{
	@Override
	public int compare(Task hostTask, Task taskToCompare) {
		boolean hostTaskIsCompleted = hostTask.checkCompleted();
		boolean taskToCompareIsCompleted = taskToCompare.checkCompleted();
		
		int hostTaskPriorityCode = hostTask.getPriority().getPriorityCode();
		int taskToComparePriorityCode = taskToCompare.getPriority().getPriorityCode();
		
		DateTime hostTaskDate = hostTask.getStartTime();
		DateTime taskToCompareDate = taskToCompare.getStartTime();
		
		if(compareCompleteLevel(hostTaskIsCompleted, taskToCompareIsCompleted) == 0) {
			if(compareDate(hostTaskDate, taskToCompareDate) == 0) {
				return comparePriorityCode(hostTaskPriorityCode, taskToComparePriorityCode);
			} else { // compare Date
				return compareDate(hostTaskDate, taskToCompareDate);
			}
		} else { // not same completeLevel
			return compareCompleteLevel(hostTaskIsCompleted, taskToCompareIsCompleted);
		}
	}

	private int compareDate(DateTime hostTaskDate, DateTime taskToCompareDate) {
		// has start date is better than null
		if(hostTaskDate != null && taskToCompareDate == null) {
			return -1;
		} else if(hostTaskDate == null && taskToCompareDate != null) { 
			return 1;
		} else if(hostTaskDate == null && taskToCompareDate == null) {
			return 0;
		} else {
			return hostTaskDate.compareTo(taskToCompareDate); 
		}
	}

	private int comparePriorityCode(int hostTaskPriorityCode,
			int taskToComparePriorityCode) {
		if(hostTaskPriorityCode < taskToComparePriorityCode) {
			return -1;
		} else if(hostTaskPriorityCode > taskToComparePriorityCode) {
			return 1;
		} else {
			return 0;
		}
	}

	private int compareCompleteLevel(boolean hostTaskIsCompleted,
			boolean taskToCompareIsCompleted) {
		if(hostTaskIsCompleted == taskToCompareIsCompleted) {
			return 0;
		} else if(hostTaskIsCompleted == false && taskToCompareIsCompleted == true) {
			return 1;
		} else {
			return -1;
		}
	}


}
