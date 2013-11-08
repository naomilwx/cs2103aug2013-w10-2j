package test.storage;
/**
 * @author a0105683e
 * */
import static org.junit.Assert.*;

import java.util.Vector;

import nailit.common.FilterObject;
import nailit.common.NIConstants;
import nailit.common.Task;
import nailit.common.TaskPriority;
import nailit.storage.FileCorruptionException;
import nailit.storage.NoTaskFoundException;
import nailit.storage.StorageManager;

import org.joda.time.DateTime;
import org.junit.Test;

public class StorageManagerTest {
	public StorageManager sto;
	
	@Test
	public void addTest() throws FileCorruptionException{
		sto = new StorageManager();
		sto.clear();
		
		/**
		 * Test for add null task should return null task ID
		 */
		Task task1 = null;
		testAddCommand("",Task.TASKID_NULL,task1);
		
		/**
		 * Tests for add a task with boundary ID value: -2, 0, 1
		 */
		Task task2 = new Task("second task");
		task2.setID(1);
		testAddCommand("The ID 1 is out of the range",1,task2);
		Task task3 = new Task("third task");
		task3.setID(0);
		testAddCommand("The ID 0 is out of the range",0,task3);
		Task task4 = new Task("fourth task");
		task4.setID(-2);
		testAddCommand("The ID -2 is out of the range",-2,task4);
		
		//TODO: test for MAXIMUN
		
		/**
		 * Test with valid ID
		 */
		Task task5 = new Task("fifth task");
		testAddCommand("1,fifth task,null,null,1,,,0,null\n",1,task5);
		Task task6 = new Task(Task.TASKID_NULL,"sixth task",new DateTime("2013-02-03"),new DateTime("2013-03-03"),TaskPriority.MEDIUM,"school stuff","desc",true);
		testAddCommand("" +
				"1,fifth task,null,null,1,,,0,null\n" +
				"2,sixth task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
				"",2,task6);
		Task task7 = new Task(2,"seventh task",new DateTime("2013-02-03"),new DateTime("2013-03-03"),TaskPriority.MEDIUM,"school stuff","desc",true);
		testAddCommand("" +
				"1,fifth task,null,null,1,,,0,null\n" +
				"2,seventh task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
				"",2,task7);

	}
	
	@Test
	public void removeTest() throws FileCorruptionException{
		sto = new StorageManager();
		sto.clear();
		
		/**
		 * Test for operations on empty storage
		 * */
		testRemoveCommand("Assertion Error!",Task.TASKID_NULL);
		testRemoveCommand("Assertion Error!",0);
		testRemoveCommand("Assertion Error!",1);
		testRemoveCommand("Assertion Error!",2);
		
		/**
		 * Test for operations on non-empty storage
		 * */
		Task task5 = new Task("fifth task");
		testAddCommand("1,fifth task,null,null,1,,,0,null\n",1,task5);
		Task task6 = new Task(Task.TASKID_NULL,"sixth task",new DateTime("2013-02-03"),new DateTime("2013-03-03"),TaskPriority.MEDIUM,"school stuff","desc",true);
		testAddCommand("" +
				"1,fifth task,null,null,1,,,0,null\n" +
				"2,sixth task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
				"",2,task6);
		
		
		/**
		 * Test for undo remove
		 * */
		sto.clear();
		Task task1 = new Task("first task");
		testAddCommand("" +
				"1,first task,null,null,1,,,0,null\n" +
				"",1,task1);
		testRemoveCommand("",1);
		Task taskRemoveUndo = task1.copy();
		taskRemoveUndo.setID(1);
		testUndoRemoveCommand("" +
					   "1,first task,null,null,1,,,0,null\n",1,taskRemoveUndo);
		
	}
	
//	@Test
//		public void integrateTest() throws FileCorruptionException, NoTaskFoundException {
//			sto = new StorageManager();
//			sto.clear();
//			Task task1 = new Task("first task");
//			Task task2 = new Task("second task");
//			testAddCommand("" +
//					"1,first task,null,null,1,,,0,null\n" +
//					"",1,task1);
//			testAddCommand("" +
//					"1,first task,null,null,1,,,0,null\n" +
//					"2,second task,null,null,1,,,0,null\n" +
//					"",2,task2);
//			Task task3 = new Task(2,"third task",new DateTime("2013-02-03"),new DateTime("2013-03-03"),TaskPriority.MEDIUM,"school stuff","desc",true);
//			testAddCommand("" +
//					"1,first task,null,null,1,,,0,null\n" +
//					"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
//					"",2,task3);
//	//		Task task4 = new Task(-2,"nonsense task",null,null,TaskPriority.MEDIUM,"school stuff","desc",true);
//	//		testAddCommand("",task4);
//			FilterObject fo = new FilterObject("first",null,null,null,null,null);
//			testFilterCommand("" +
//					"1,first task,null,null,1,,,0,null\n" +
//					"",fo);
//			FilterObject fo1 = new FilterObject(null,new DateTime("2013-02-02"),null,null,null,null);
//			testFilterCommand("" +
//					"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
//					"",fo1);
//			FilterObject fo2 = new FilterObject(null,new DateTime("2013-02-02"),null,"school",null,null);
//			testFilterCommand("",fo2);
//			FilterObject fo3 = new FilterObject(null,new DateTime("2013-02-02"),null,"school stuff",null,null);
//			testFilterCommand("" +
//					"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
//					"",fo3);
//			FilterObject fo4 = new FilterObject("task",null,null,null,null,null);
//			testFilterCommand("" +
//					"1,first task,null,null,1,,,0,null\n" +
//					"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
//					"",fo4);
//			testRemoveCommand("" +
//					"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
//					"",1);
//			Task task6 = new Task(Task.TASKID_NULL,"testForUndo",new DateTime("1992-02-10"),null,TaskPriority.HIGH,"social","something should have assertions here!",false);
//			testAddCommand("" +
//					"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
//					"3,testForUndo,1992-02-10T00:00:00.000+08:00,null,2,social,something should have assertions here!,0,null\n" +
//					"",3,task6);
//			testUndoAddCommand("" +
//					"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
//					"",3);
//			Task task7 = new Task(Task.TASKID_NULL,"Add New Task After Undo",new DateTime("1992-02-10"),null,TaskPriority.HIGH,"social","something should have assertions here!",false);
//			testAddCommand("" +
//					"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
//					"3,Add New Task After Undo,1992-02-10T00:00:00.000+08:00,null,2,social,something should have assertions here!,0,null\n" +
//					"",3,task7);
//			Task task8 = new Task(Task.TASKID_NULL,"test for add new task",new DateTime("2013-01-02"),null,TaskPriority.LOW,"school","no more description",false);
//			testAddCommand("" +
//					"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
//					"3,Add New Task After Undo,1992-02-10T00:00:00.000+08:00,null,2,social,something should have assertions here!,0,null\n" +
//					"4,test for add new task,2013-01-02T00:00:00.000+08:00,null,0,school,no more description,0,null\n" +
//					"",4,task8);
//	
//		}

	@Test//TODO: 
	public void filterTest() throws FileCorruptionException{
		sto = new StorageManager();
		sto.clear();
		
		/***************************************************************
		 * Testing on the empty storage
		 **************************************************************** */
		
		/**
		 * Filter on the empty storage
		 * */
		FilterObject foOnEmptySto = new FilterObject("first",null,null,null,null,null);
		testFilterCommand("",foOnEmptySto);
		
		/**
		 * null filter test on the empty storage
		 * */
		testFilterCommand("",null);
		testFilterCommand("",new FilterObject());//because storage is null
		
		/***************************************************************
		 * Testing on the non-empty storage
		 **************************************************************** */
		Task task1 = new Task("first task");
		Task task2 = new Task(Task.TASKID_NULL,"third task",new DateTime("2013-02-03"),new DateTime("2013-03-03"),TaskPriority.MEDIUM,"school stuff","desc",true);
		testAddCommand("" +
				"1,first task,null,null,1,,,0,null\n" +
				"",1,task1);
		testAddCommand("" +
				"1,first task,null,null,1,,,0,null\n" +
				"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
				"",2,task2);

		/**
		 * Testing when filter object is null
		 * */
		testFilterCommand("",null);
		testFilterCommand("" +
				"1,first task,null,null,1,,,0,null\n" +
				"2,third task,2013-02-03T00:00:00.000+08:00,2013-03-03T00:00:00.000+08:00,1,school stuff,desc,1,null\n" +
				"",new FilterObject());
		
		/**
		 * Testing when filter object is not null
		 * */
		sto.clear();
		addRealisticTasks();
		FilterObject fo1 = new FilterObject(null,null,null,null,null,null);
		testFilterCommand("1,CS2103 REVIEW,2013-02-13T00:00:00.000+08:00,2013-02-15T00:00:00.000+08:00,2,Exam preparation,Be well prepared,1,2013-02-10T00:00:00.000+08:00\n" +
				"2,CS2103 Project Demo,null,2013-11-07T00:00:00.000+08:00,2,Project,Be confident,0,2013-11-02T00:00:00.000+08:00\n" +
				"3,CS2105 REVIEW,2013-11-12T00:00:00.000+08:00,2013-11-26T00:00:00.000+08:00,1,Exam preparation,Be brave,0,2013-11-10T00:00:00.000+08:00\n" +
				"4,CS3230 ONLINE ASSIGNMENT,null,2103-11-06T00:00:00.000+08:00,2,Assignment,BE Smart,0,2013-10-31T00:00:00.000+08:00\n" +
				"5,CS3230 FINAL REVIEW,2013-11-01T00:00:00.000+08:00,2013-11-27T00:00:00.000+08:00,1,Exam preparation,Be something,0,2013-12-02T00:00:00.000+08:00\n" +
				"6,GO HOME,2013-12-03T00:00:00.000+08:00,2014-01-09T00:00:00.000+08:00,1,,,0,null\n" +
				"7,WU HAN JOURNEY,2013-12-10T00:00:00.000+08:00,2013-12-15T00:00:00.000+08:00,0,TRAVEL,BE CAREFUL,0,2013-12-08T00:00:00.000+08:00\n" +
				"8,SHANGHAI JOURNEY,2013-12-16T00:00:00.000+08:00,2013-12-18T00:00:00.000+08:00,0,TRAVEL,BE OPEN-MINDED,0,2013-12-02T00:00:00.000+08:00\n" +
				"9,ALICE'S magic journey,2013-12-20T00:00:00.000+08:00,2013-12-25T00:00:00.000+08:00,0,,,0,null\n" +
				"10,2103 V0.3,null,2013-10-30T00:00:00.000+08:00,2,Project,Be well-prepared,0,2013-02-13T00:00:00.000+08:00\n" +
				"11,PC1431,2013-11-08T00:00:00.000+08:00,2013-10-30T00:00:00.000+08:00,2,Project,Be well-prepared,0,2013-02-14T00:00:00.000+08:00\n" +
				"12,NON-SENSE,2013-09-08T00:00:00.000+08:00,2013-10-30T00:00:00.000+08:00,2,Project,Be well-prepared,1,2013-02-14T00:00:00.000+08:00\n" +
				"13,My birthday,2014-02-10T00:00:00.000+08:00,2014-02-10T00:00:00.000+08:00,2,Birthday,Be Happy,0,2014-02-09T00:00:00.000+08:00\n" +
				"14,lalala,2013-04-05T00:00:00.000+08:00,2013-10-30T00:00:00.000+08:00,2,Non-sense,Be well-prepared,1,2013-02-14T00:00:00.000+08:00\n" +
				"15,CS2102,2013-10-02T00:00:00.000+08:00,2013-11-30T00:00:00.000+08:00,2,Project,Be well-prepared,1,2013-02-14T00:00:00.000+08:00\n" +
				"17,CS2101 EXAM,null,2013-11-04T00:00:00.000+08:00,2,Project,Be well-prepared,0,2013-02-14T00:00:00.000+08:00\n" +
				"16,CS2101 ORAL PRESENTATION,null,2013-11-08T00:00:00.000+08:00,2,ORAL PRESENTATION,Be well-prepared,0,2013-02-14T00:00:00.000+08:00\n" +
				"19,You tiao,null,2013-12-04T00:00:00.000+08:00,2,Meal,Be Hungry,0,2013-02-14T00:00:00.000+08:00\n" +
				"18,CS3230R,null,2013-11-07T00:00:00.000+08:00,2,Research,Be well-prepared,0,2013-02-14T00:00:00.000+08:00\n" +
				"20,Zongzi,null,2013-12-04T00:00:00.000+08:00,2,fake Cs,fake CS task,0,2013-02-14T00:00:00.000+08:00\n"
				,fo1);
		FilterObject fo2 = new FilterObject("cs",null,null,null,null,null);
		testFilterCommand("1,CS2103 REVIEW,2013-02-13T00:00:00.000+08:00,2013-02-15T00:00:00.000+08:00,2,Exam preparation,Be well prepared,1,2013-02-10T00:00:00.000+08:00\n" +
				"2,CS2103 Project Demo,null,2013-11-07T00:00:00.000+08:00,2,Project,Be confident,0,2013-11-02T00:00:00.000+08:00\n" +
				"3,CS2105 REVIEW,2013-11-12T00:00:00.000+08:00,2013-11-26T00:00:00.000+08:00,1,Exam preparation,Be brave,0,2013-11-10T00:00:00.000+08:00\n" +
				"4,CS3230 ONLINE ASSIGNMENT,null,2103-11-06T00:00:00.000+08:00,2,Assignment,BE Smart,0,2013-10-31T00:00:00.000+08:00\n" +
				"5,CS3230 FINAL REVIEW,2013-11-01T00:00:00.000+08:00,2013-11-27T00:00:00.000+08:00,1,Exam preparation,Be something,1,2013-12-02T00:00:00.000+08:00\n" +
				"15,CS2102,2013-10-02T00:00:00.000+08:00,2013-11-30T00:00:00.000+08:00,2,Project,Be well-prepared,1,2013-02-14T00:00:00.000+08:00\n" +
				"17,CS2101 EXAM,null,2013-11-04T00:00:00.000+08:00,2,Project,Be well-prepared,0,2013-02-14T00:00:00.000+08:00\n" +
				"16,CS2101 ORAL PRESENTATION,null,2013-11-08T00:00:00.000+08:00,2,ORAL PRESENTATION,Be well-prepared,0,2013-02-14T00:00:00.000+08:00\n" +
				"18,CS3230R,null,2013-11-07T00:00:00.000+08:00,2,Research,Be well-prepared,0,2013-02-14T00:00:00.000+08:00\n" +
				"20,Zongzi,null,2013-12-04T00:00:00.000+08:00,2,fake Cs,fake CS task,0,2013-02-14T00:00:00.000+08:00\n"
				,fo2);

	}
	public void addRealisticTasks() throws FileCorruptionException{
		sto.clear();
		Task task1 = new Task(Task.TASKID_NULL,"CS2103 REVIEW",new DateTime("2013-02-13"),new DateTime("2013-02-15"),TaskPriority.HIGH,"Exam preparation","Be well prepared",false,new DateTime("2013-02-10"));
		Task task2 = new Task(Task.TASKID_NULL,"CS2103 Project Demo",null,new DateTime("2013-11-07"),TaskPriority.HIGH,"Project","Be confident",false,new DateTime("2013-11-02"));
		Task task3 = new Task(Task.TASKID_NULL,"CS2105 REVIEW",new DateTime("2013-11-12"),new DateTime("2013-11-26"),TaskPriority.MEDIUM,"Exam preparation","Be brave",false,new DateTime("2013-11-10"));
		Task task4 = new Task(Task.TASKID_NULL,"CS3230 ONLINE ASSIGNMENT",null,new DateTime("2103-11-06"),TaskPriority.HIGH,"Assignment","BE Smart",false,new DateTime("2013-10-31"));
		Task task5 = new Task(Task.TASKID_NULL,"CS3230 FINAL REVIEW",new DateTime("2013-11-01"),new DateTime("2013-11-27"),TaskPriority.MEDIUM,"Exam preparation","Be something",false,new DateTime("2013-12-02"));
		Task task6 = new Task(Task.TASKID_NULL,"GO HOME",new DateTime("2013-12-03"),new DateTime("2014-01-09"),TaskPriority.MEDIUM,"","",false,null);
		Task task7 = new Task(Task.TASKID_NULL,"WU HAN JOURNEY",new DateTime("2013-12-10"),new DateTime("2013-12-15"),TaskPriority.LOW,"TRAVEL","BE CAREFUL",false,new DateTime("2013-12-08"));
		Task task8 = new Task(Task.TASKID_NULL,"SHANGHAI JOURNEY",new DateTime("2013-12-16"),new DateTime("2013-12-18"),TaskPriority.LOW,"TRAVEL","BE OPEN-MINDED",false,new DateTime("2013-12-02"));
		Task task9 = new Task(Task.TASKID_NULL,"ALICE'S magic journey",new DateTime("2013-12-20"),new DateTime("2013-12-25"),TaskPriority.LOW,"","",false,null);
		Task task10 = new Task(Task.TASKID_NULL,"2103 V0.3",null,new DateTime("2013-10-30"),TaskPriority.HIGH,"Project","Be well-prepared",false,new DateTime("2013-02-13"));
		Task task11 = new Task(Task.TASKID_NULL,"PC1431",new DateTime("2013-11-08"),new DateTime("2013-10-30"),TaskPriority.HIGH,"Project","Be well-prepared",false,new DateTime("2013-02-14"));
		Task task12 = new Task(Task.TASKID_NULL,"NON-SENSE",new DateTime("2013-09-08"),new DateTime("2013-10-30"),TaskPriority.HIGH,"Project","Be well-prepared",false,new DateTime("2013-02-14"));
		Task task13 = new Task(Task.TASKID_NULL,"My birthday",new DateTime("2014-02-10"),new DateTime("2014-02-10"),TaskPriority.HIGH,"Birthday","Be Happy",false,new DateTime("2014-02-09"));
		Task task14 = new Task(Task.TASKID_NULL,"lalala",new DateTime("2013-04-05"),new DateTime("2013-10-30"),TaskPriority.HIGH,"Non-sense","Be well-prepared",false,new DateTime("2013-02-14"));
		Task task15 = new Task(Task.TASKID_NULL,"CS2102",new DateTime("2013-10-02"),new DateTime("2013-11-30"),TaskPriority.HIGH,"Project","Be well-prepared",false,new DateTime("2013-02-14"));
		Task task16 = new Task(Task.TASKID_NULL,"CS2101 ORAL PRESENTATION",null,new DateTime("2013-11-08"),TaskPriority.HIGH,"ORAL PRESENTATION","Be well-prepared",false,new DateTime("2013-02-14"));
		Task task17 = new Task(Task.TASKID_NULL,"CS2101 EXAM",null,new DateTime("2013-11-04"),TaskPriority.HIGH,"Project","Be well-prepared",false,new DateTime("2013-02-14"));
		Task task18 = new Task(Task.TASKID_NULL,"CS3230R",null,new DateTime("2013-11-07"),TaskPriority.HIGH,"Research","Be well-prepared",false,new DateTime("2013-02-14"));
		Task task19 = new Task(Task.TASKID_NULL,"You tiao",null,new DateTime("2013-12-04"),TaskPriority.HIGH,"Meal","Be Hungry",false,new DateTime("2013-02-14"));
		Task task20 = new Task(Task.TASKID_NULL,"Zongzi",null,new DateTime("2013-12-04"),TaskPriority.HIGH,"fake Cs","fake CS task",false,new DateTime("2013-02-14"));
//		
//		Task task1 = new Task(Task.TASKID_NULL,"CS2103 REVIEW",new DateTime("201"),new DateTime("2013-02-15"),TaskPriority.HIGH,"Exam preparation","Be well prepared",false,new DateTime("2013-02-10"));
//		Task task2 = new Task(Task.TASKID_NULL,"CS2101 Presentation",null,new DateTime("2013-11-07"),TaskPriority.HIGH,"Project","Be confident",false,new DateTime("2013-11-02"));
//		Task task3 = new Task(Task.TASKID_NULL,"CS2105 Project 3",null,new DateTime("2013-11-08"),TaskPriority.MEDIUM,"School","Be brave",false,new DateTime("2013-11-10"));
//		Task task4 = new Task(Task.TASKID_NULL,"CS3230 Project 2",null,new DateTime("2103-11-08"),TaskPriority.HIGH,"Assignment","BE Smart",false,new DateTime("2013-10-31"));
//		Task task5 = new Task(Task.TASKID_NULL,"CS3230 Report Submission",new DateTime("2013-11-08"),new DateTime("2013-11-27"),TaskPriority.MEDIUM,"Exam preparation","Be something",false,new DateTime("2013-12-02"));
//		Task task6 = new Task(Task.TASKID_NULL,"CS2309 Presentation",null,new DateTime("2013-11-11"),TaskPriority.HIGH,"Project","Be confident",false,new DateTime("2013-11-02"));

		sto.add(task1);
		sto.add(task2);
		sto.add(task3);
		sto.add(task4);
		sto.add(task5);
		sto.add(task6);
		sto.add(task7);
		sto.add(task8);
		sto.add(task9);
		sto.add(task10);
		sto.add(task11);
		sto.add(task12);
		sto.add(task13);
		sto.add(task14);
		sto.add(task15);
		sto.add(task16);
		sto.add(task17);
		sto.add(task18);
		sto.add(task19);
		sto.add(task20);

	}
	
	public String printVector(Vector<Task> v){
		String out = "";
		for(int i=0;i<v.size();i++){
			out += v.get(i).getID()+","+changeToDiskFormat(v.get(i))+"\n";
		}
		return out;
	}
	public String changeToDiskFormat(Task task){
		int priority = task.getPriority().getPriorityCode();
		
		String name = task.getName();
		String startDate;
		if(task.getStartTime() == null){
			startDate = null;
		}
		else{
			startDate = task.getStartTime().toString();
		}
		
		String endDate;
		if(task.getEndTime() == null){
			endDate = null;
		}
		else{
			endDate = task.getEndTime().toString();
		}
		String desc = task.getDescription();
		String tag = task.getTag();
		
		int completeStatus = (task.checkCompletedOrOver())? 1:0;
		
		String reminderDate;
		if(task.getReminder()==null){
			reminderDate = null;
		}else{
			reminderDate = task.getReminder().toString();
		}
		
		String taskString = name + "," + startDate + "," + endDate + ","+ priority + "," + tag + "," +desc +"," + completeStatus + "," + reminderDate;
		
		return taskString;
	}

	public void testAddCommand(String expected,int expectedID,Task task) throws FileCorruptionException{
		String out;
		try{
			int ID = sto.add(task);
			Vector<Task> v = sto.retrieveAll();
			try{
				assertEquals(expectedID,ID);
				out = printVector(v);
			}catch(AssertionError a1){
				out = "The number is not match!"; 
			}
		}catch(AssertionError a2){
			out = "The ID "+task.getID()+" is out of the range";
		}
		
		assertEquals(expected,out);
	}
	
	public void testUndoRemoveCommand(String expected,int expectedID,Task task){
		String out;
		try{
			int ID = sto.add(task);
			Vector<Task> v = sto.retrieveAll();
			try{
				assertEquals(expectedID,ID);
				out = printVector(v);
			}catch(AssertionError a1){
				out = "The number is not match!"; 
			}
		}catch(AssertionError a2){
			out = "The ID "+task.getID()+" is out of the range";
		}
		
		assertEquals(expected,out);
	}
	public void testRemoveCommand(String expected,int ID) {
		String out;
		try {
			sto.remove(ID,false);
			Vector<Task> v = sto.retrieveAll();
			out = printVector(v);
		} catch (NoTaskFoundException e) {
			out = "No task can be found!";
		}catch(AssertionError a){
			out = "Assertion Error!";
		}

		
		assertEquals(expected,out);
	}

	public void testFilterCommand(String expected,FilterObject fo){
		Vector<Task> v=sto.filter(fo);
		String out = printVector(v);
		assertEquals(expected,out);
	}
	public void testUndoAddCommand(String expected,int ID) throws NoTaskFoundException{
		sto.remove(ID,true);
		Vector<Task> v = sto.retrieveAll();
		String out = printVector(v);
		assertEquals(expected,out);
	}
}
