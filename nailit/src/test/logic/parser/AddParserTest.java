package test.logic.parser;
//@author A0105559B

import org.joda.time.DateTime;
import org.junit.Test;
import static org.junit.Assert.*;
import nailit.common.CommandType;
import nailit.common.NIConstants;
import nailit.common.TaskPriority;
import nailit.logic.exception.InvalidCommandFormatException;
import nailit.logic.parser.AddParser;
import nailit.logic.ParserResult;

public class AddParserTest {

		@Test
		public void test() throws InvalidCommandFormatException{
			ParserResult expectedAdd = new ParserResult();
			ParserResult expectedAdd2 = new ParserResult();
			
			
			expectedAdd.setName("CSAssignment");
			expectedAdd.setCommand(CommandType.ADD);
			expectedAdd.setPriority(TaskPriority.LOW);
			expectedAdd.setEndTime(new DateTime(2013,9,11,00,00));
			expectedAdd.setTag("#study#");
			expectedAdd.setDescription("this is description");
			
			testExecuteName(expectedAdd.getName(),"CSAssignment,#study#, 11 Sep 2013, LOW, (this is description)");
			testExecuteCommandType(expectedAdd.getCommand().toString(),"CSAssignment,#study#, 11 Sep 2013, LOW, (this is description)");
			testExecuteTaskPriority(expectedAdd.getPriority().toString(),"CSAssignment,#study#, 11 Sep 2013, LOW, (this is description)");
			testExecuteTag(expectedAdd.getTag(),"CSAssignment,#study#, at 11 Sep 2013, LOW, (this is description)");
			testExecuteEndTime(expectedAdd.getEndTime().toString(NIConstants.DISPLAY_DATE_FORMAT),"CSAssignment,#study#, 11 Sep 2013, LOW, (this is description)");
			testExecuteDescription(expectedAdd.getDescription(),"CSAssignment,#study#, 11 Sep 2013, LOW, (this is description)");
		
			// this is the test for special cases(boundary case) for "from .. to .."
			expectedAdd2.setName("CSAssignment");
			expectedAdd2.setCommand(CommandType.ADD);
			expectedAdd2.setPriority(TaskPriority.LOW);
			expectedAdd2.setStartTime(new DateTime(2013,9,11,00,00));
			expectedAdd2.setEndTime(new DateTime(2013,9,12,00,00));
			expectedAdd2.setTag("#study#");
			
			testExecuteName(expectedAdd2.getName(),"CSAssignment,#study#, from 11 Sep 2013 to 12 Sep 2013, LOW");
			testExecuteCommandType(expectedAdd2.getCommand().toString(),"CSAssignment,#study#, from 11 Sep 2013 to 12 Sep 2013, LOW");
			testExecuteTaskPriority(expectedAdd2.getPriority().toString(),"CSAssignment,#study#, from 11 Sep 2013 to 12 Sep 2013, LOW");
			testExecuteTag(expectedAdd2.getTag(),"CSAssignment,#study#, from 11 Sep 2013 to 12 Sep 20133, LOW");
			testExecuteStartTime(expectedAdd2.getStartTime().toString(NIConstants.DISPLAY_DATE_FORMAT),"CSAssignment,#study#, from 11 Sep 2013 to 12 Sep 2013, LOW");
			testExecuteEndTime(expectedAdd2.getEndTime().toString(NIConstants.DISPLAY_DATE_FORMAT),"CSAssignment,#study#, from 11 Sep 2013 to 12 Sep 2013, LOW");
			
		}
		
		private void testExecuteName (String expected, String command) throws InvalidCommandFormatException{
			AddParser testAdd = new AddParser(command);
			assertEquals(expected,testAdd.execute().getName());
		}
		
		private void testExecuteCommandType (String expected, String command) throws InvalidCommandFormatException{
			AddParser testAdd = new AddParser(command);
			assertEquals(expected,testAdd.execute().getCommand().toString());
		}
		
		private void testExecuteTaskPriority (String expected, String command) throws InvalidCommandFormatException{
			AddParser testAdd = new AddParser(command);
			assertEquals(expected,testAdd.execute().getPriority().toString());
		}
		
		private void testExecuteTag (String expected, String command) throws InvalidCommandFormatException{
			AddParser testAdd = new AddParser(command);
			assertEquals(expected,testAdd.execute().getTag());
		}
		
		private void testExecuteStartTime (String expected, String command) throws InvalidCommandFormatException{
			AddParser testAdd = new AddParser(command);
			assertEquals(expected,testAdd.execute().getStartTime().toString(NIConstants.DISPLAY_DATE_FORMAT));
		}
		
		private void testExecuteEndTime (String expected, String command) throws InvalidCommandFormatException{
			AddParser testAdd = new AddParser(command);
			assertEquals(expected,testAdd.execute().getEndTime().toString(NIConstants.DISPLAY_DATE_FORMAT));
		}

		private void testExecuteDescription (String expected, String command) throws InvalidCommandFormatException{
			AddParser testAdd = new AddParser(command);
			assertEquals(expected,testAdd.execute().getDescription());
		}
}
