package test.logic.parser;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import test.logic.command.CommandTest;
import static org.junit.Assert.*;
import nailit.common.CommandType;
import nailit.common.NIConstants;
import nailit.common.TaskPriority;
import nailit.logic.exception.InvalidCommandFormatException;
import nailit.logic.parser.AddParser;
import nailit.logic.ParserResult;

@Category(ParserTest.class)
public class AddParserTest {

		@Test
		public void test() throws InvalidCommandFormatException{
			ParserResult expectedAdd = new ParserResult();
			ParserResult expectedAdd2 = new ParserResult();
			
			DateTime expectedStartDate;
			DateTime expectedEndDate;
			
			expectedAdd.setName("CSAssignment");
			expectedAdd.setCommand(CommandType.ADD);
			expectedAdd.setPriority(TaskPriority.LOW);
			expectedAdd.setStartTime(expectedStartDate = new DateTime(2013,9,11,00,00));
			expectedAdd.setEndTime(expectedEndDate = new DateTime(2013,9,12,00,00));
			expectedAdd.setTag("#study#");
			
			testExecuteName(expectedAdd.getName(),"CSAssignment,#study#, 11 Sep 2013, LOW");
			testExecuteCommandType(expectedAdd.getCommand().toString(),"CSAssignment,#study#, 11 Sep 2013, LOW");
			testExecuteTaskPriority(expectedAdd.getPriority().toString(),"CSAssignment,#study#, 11 Sep 2013, LOW");
			testExecuteTag(expectedAdd.getTag(),"CSAssignment,#study#, at 11 Sep 2013, LOW");
			testExecuteStartTime(expectedAdd.getStartTime().toString(NIConstants.DISPLAY_DATE_FORMAT),"CSAssignment,#study#, 11 Sep 2013, LOW");
			
			// this is the test for special cases(boundary case) for "from .. to .."
			testExecuteName(expectedAdd.getName(),"CSAssignment,#study#, from 11 Sep 2013 to 12 Sep 2013, LOW");
			testExecuteCommandType(expectedAdd.getCommand().toString(),"CSAssignment,#study#, from 11 Sep 2013 to 12 Sep 2013, LOW");
			testExecuteTaskPriority(expectedAdd.getPriority().toString(),"CSAssignment,#study#, from 11 Sep 2013 to 12 Sep 2013, LOW");
			testExecuteTag(expectedAdd.getTag(),"CSAssignment,#study#, from 11 Sep 2013 to 12 Sep 20133, LOW");
			testExecuteStartTime(expectedAdd.getStartTime().toString(NIConstants.DISPLAY_DATE_FORMAT),"CSAssignment,#study#, from 11 Sep 2013 to 12 Sep 2013, LOW");
			testExecuteEndTime(expectedAdd.getEndTime().toString(NIConstants.DISPLAY_DATE_FORMAT),"CSAssignment,#study#, from 11 Sep 2013 to 12 Sep 2013, LOW");
			
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

}
