//@author A0091372H
package test.common;

import java.util.Random;

import org.joda.time.DateTime;

import nailit.common.Task;
import nailit.common.TaskPriority;

import org.apache.commons.lang.RandomStringUtils;

public class TaskStub extends Task{
	public static final int GENERATE_FLOATING = 0;
	public static final int GENERATE_TASK = 1;
	public static final int GENERATE_EVENT = 2;
	static Random generator = new Random();
	public static DateTime baseDate = new DateTime(2013, 4, 5, 10 , 10);
	public static DateTime getRandomEndDate(){
		long base = baseDate.getMillis();
		long newDate = (long) (base + Math.round(generator.nextDouble() * 1000 * 60 * 60 * 24 * 365)); 
		return new DateTime(newDate);
	} 
	public static String getRandomDescription(){
		return RandomStringUtils.randomAlphanumeric(20) + ", " + RandomStringUtils.randomAlphanumeric(10);
	}
	public static String getRandomString(){
		return RandomStringUtils.randomAlphanumeric(generator.nextInt(20)+1) + " "+ RandomStringUtils.randomAlphanumeric(generator.nextInt(10)+1);
	}
	public TaskStub(int n) throws Exception{
		super(getRandomString(), null, null, "#" + RandomStringUtils.randomAlphanumeric(5) + "#", 
				TaskPriority.getPriority(generator.nextInt(3)));
		switch(n){
			case GENERATE_FLOATING:
				break;
			case GENERATE_TASK:
				setEndTime(getRandomEndDate());
				break;
			case GENERATE_EVENT:
				setStartTime(baseDate);
				setEndTime(getRandomEndDate());
				break;
		}
	}
}
 