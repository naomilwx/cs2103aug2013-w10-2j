package nailit.common;

import java.util.Comparator;
import java.util.Vector;

import org.joda.time.DateTime;

import edu.emory.mathcs.backport.java.util.Collections;

public class Utilities {
	public static Comparator<Task> taskDateTimeComparator = new Comparator<Task>(){

		@Override
		public int compare(Task task1, Task task2) {
			if(task1.getEndTime() == null){
				return 1;
			}else if(task2.getEndTime() == null){
				return -1;
			}else{
				return task1.getEndTime().compareTo(task2.getEndTime());
			}
		}
		
	};
	public static String formatTaskTag(String tag){
		if(tag.equals("")){
			return tag;
		}else{
			if(getFirstChar(tag) != '#'){
				tag = "#" + tag;
			}
			if(getLastChar(tag) != '#'){
				tag = tag + "#";
			}
			return tag;
		}
	}
	public static DateTime getStartOfDay(DateTime date){
		return new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), 0, 0);
	}
	public static DateTime getEndOfDay(DateTime date){
		return new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), 23, 59);
	}
	public static char getFirstChar(String s){
		if(s.length() > 0){
			return s.charAt(0);
		}else{
			return ' ';
		}
	}
	public static char getLastChar(String s){
		return s.charAt(s.length() - 1);
	}
	public static void sortTasksByEndDate(Vector<Task> tasks){
		Collections.sort(tasks, taskDateTimeComparator);
	}
	public static Vector<Task> filterAndSortTasks(Vector<Task> tasks){
		Vector<Task> filteredTasks = new Vector<Task>();
		for(Task task: tasks){
			if(!task.isEvent()){
				filteredTasks.add(task);
			}
		}
		sortTasksByEndDate(filteredTasks);
		return filteredTasks;
	}
	public static Vector<Task> filterAndSortEvents(Vector<Task> tasks){
		Vector<Task> filteredEvents = new Vector<Task>();
		for(Task task: tasks){
			if(task.isEvent()){
				filteredEvents.add(task);
			}
		}
		sortTasksByEndDate(filteredEvents);
		return filteredEvents;
	}
}
