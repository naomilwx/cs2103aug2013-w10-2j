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
			expectedAdd.setTag("#study#");
			testExecuteName(expectedAdd.getName(),"CSAssignment,#study#, at 11 Sep 2013, LOW");
			testExecuteCommandType(expectedAdd.getCommand().toString(),"CSAssignment,#study#, at 11 Sep 2013, LOW");
			testExecuteTaskPriority(expectedAdd.getPriority().toString(),"CSAssignment,#study#, at 11 Sep 2013, LOW");
			testExecuteTag(expectedAdd.getTag(),"CSAssignment,#study#, at 11 Sep 2013, LOW");
		}
		
		private void testExecuteName (String expected, String command){
			AddParser testAdd = new AddParser(command);
			System.out.println(testAdd.execute().getName());
			assertEquals(expected,testAdd.execute().getName());
		}
		
		private void testExecuteCommandType (String expected, String command){
			AddParser testAdd = new AddParser(command);
			assertEquals(expected,testAdd.execute().getCommand().toString());
		}
		
		private void testExecuteTaskPriority (String expected, String command){
			AddParser testAdd = new AddParser(command);
			assertEquals(expected,testAdd.execute().getPriority().toString());
		}
		private void testExecuteTag (String expected, String command){
			AddParser testAdd = new AddParser(command);
			assertEquals(expected,testAdd.execute().getTag());
		}
		
		

}
