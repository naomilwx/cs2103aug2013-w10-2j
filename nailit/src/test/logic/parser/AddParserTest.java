package test.logic.parser;

import org.joda.time.DateTime;
import org.junit.Test;
import static org.junit.Assert.*;

import nailit.common.TaskPriority;
import nailit.logic.parser.AddParser;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;


public class AddParserTest {

		@Test
		public void test(){
			ParserResult expectedAdd = new ParserResult();
			DateTime expectedDate;
			
			expectedAdd.setName("CSAssignment");
			expectedAdd.setCommand(CommandType.ADD);
			expectedAdd.setPriority(TaskPriority.LOW);
			expectedAdd.setStartTime(expectedDate = new DateTime(2013,9,11,00,00));
			testExecuteName(expectedAdd.getName());
			//testExecuteCommandType(expectedAdd.getCommand());
			//testExecuteTaskPriority(expectedAdd.getPriority());
			testExecuteTag(expectedAdd.getTag());
		}
		
		private void testExecuteName (String expected){
			AddParser testAdd = new AddParser("CSAssignment,#study#, at 11 Sep 2013, LOW");
			System.out.println(testAdd.execute().getName());
			assertEquals(expected,testAdd.execute().getName());
		}
		
		private void testExecuteCommandType (CommandType expected){
			AddParser testAdd = new AddParser("CSAssignment,#study#, at 11 Sep 2013, LOW");
			assertEquals(expected,testAdd.execute().getCommand());
		}
		
		private void testExecuteTaskPriority (TaskPriority expected){
			AddParser testAdd = new AddParser("CSAssignment,#study#, at 11 Sep 2013, LOW");
			assertEquals(expected,testAdd.execute().getPriority());
		}
		private void testExecuteTag (String expected){
			AddParser testAdd = new AddParser("CSAssignment,#study#, at 11 Sep 2013, LOW");
			assertEquals(expected,testAdd.execute().getTag());
		}
		
		

}
