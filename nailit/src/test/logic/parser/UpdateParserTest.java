package test.logic.parser;
//@author A0105559B

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import test.logic.command.AllCommandTest;
import static org.junit.Assert.*;
import nailit.common.CommandType;
import nailit.common.NIConstants;
import nailit.common.TaskPriority;
import nailit.logic.exception.InvalidCommandFormatException;
import nailit.logic.parser.UpdateParser;
import nailit.logic.ParserResult;

@Category(AllCommandTest.class)
public class UpdateParserTest {
	@Test
	public void test() throws InvalidCommandFormatException{
		ParserResult expectedUpdate1 = new ParserResult();
		
		// Update name field
		expectedUpdate1.setTaskId(12);
		expectedUpdate1.setCommand(CommandType.UPDATE);
		expectedUpdate1.setName("CS Assignment 2");
		
		testExecuteName(expectedUpdate1.getName(),"12, Name,CS Assignment 2");
		testExecuteCommandType(expectedUpdate1.getCommand().toString(),"12, Name,CS Assignment 2");
		testExecute(expectedUpdate1.getTaskID(), "12, Name, CS Assignment 2");
		
		// Update tag field
		ParserResult expectedUpdate2 = new ParserResult();
		
		expectedUpdate2.setTaskId(12);
		expectedUpdate2.setCommand(CommandType.UPDATE);
		expectedUpdate2.setTag("#Work#");
		
		testExecuteTag(expectedUpdate2.getTag(),"12,Tag,#Work#");
		testExecuteCommandType(expectedUpdate2.getCommand().toString(),"12,Tag,#Work#");
		testExecute(expectedUpdate2.getTaskID(), "12,Tag,#Work#");
		
		// Update priority field
		ParserResult expectedUpdate3 = new ParserResult();
				
		expectedUpdate3.setTaskId(12);
		expectedUpdate3.setCommand(CommandType.UPDATE);
		expectedUpdate3.setPriority(TaskPriority.LOW);
				
		testExecuteTaskPriority(expectedUpdate3.getPriority().toString(),"12,Priority,LOW");
		testExecuteCommandType(expectedUpdate3.getCommand().toString(),"12,Priority,LOW");
		testExecute(expectedUpdate3.getTaskID(), "12,Priority,LOW");
		
		// Update start time field
		ParserResult expectedUpdate4 = new ParserResult();
						
		expectedUpdate4.setTaskId(12);
		expectedUpdate4.setCommand(CommandType.UPDATE);
		expectedUpdate4.setStartTime(new DateTime(2013,2,11,00,00));
						
		testExecuteStartTime(expectedUpdate4.getStartTime().toString(NIConstants.DISPLAY_DATE_FORMAT),"12,Start,feb 11 2013");
		testExecuteCommandType(expectedUpdate4.getCommand().toString(),"12,Start,feb 11 2013");
		testExecute(expectedUpdate4.getTaskID(), "12,Start,feb 11 2013");
					
		// Update start time field
		ParserResult expectedUpdate5 = new ParserResult();
								
		expectedUpdate5.setTaskId(12);
		expectedUpdate5.setCommand(CommandType.UPDATE);
		expectedUpdate5.setEndTime(new DateTime(2013,2,11,00,00));
								
		testExecuteEndTime(expectedUpdate5.getEndTime().toString(NIConstants.DISPLAY_DATE_FORMAT),"12,end,feb 11 2013");
		testExecuteCommandType(expectedUpdate5.getCommand().toString(),"12,end,feb 11 2013");
		testExecute(expectedUpdate5.getTaskID(), "12,end,feb 11 2013");
	
		
	}
	
	private void testExecuteName (String expected, String command) throws InvalidCommandFormatException{
		UpdateParser testUpdate = new UpdateParser(command);
		assertEquals(expected,testUpdate.execute().getName());
	}
	
	private void testExecuteTag (String expected, String command) throws InvalidCommandFormatException{
		UpdateParser testUpdate = new UpdateParser(command);
		assertEquals(expected,testUpdate.execute().getTag());
	}
	
	private void testExecuteTaskPriority (String expected, String command) throws InvalidCommandFormatException{
		UpdateParser testUpdate = new UpdateParser(command);
		assertEquals(expected,testUpdate.execute().getPriority().toString());
	}
	
	private void testExecuteStartTime (String expected, String command) throws InvalidCommandFormatException{
		UpdateParser testUpdate = new UpdateParser(command);
		assertEquals(expected,testUpdate.execute().getStartTime().toString(NIConstants.DISPLAY_DATE_FORMAT));
	}
	
	private void testExecuteEndTime (String expected, String command) throws InvalidCommandFormatException{
		UpdateParser testUpdate = new UpdateParser(command);
		assertEquals(expected,testUpdate.execute().getEndTime().toString(NIConstants.DISPLAY_DATE_FORMAT));
	}
	
	private void testExecuteCommandType (String expected, String command) throws InvalidCommandFormatException{
		UpdateParser testUpdate = new UpdateParser(command);
		assertEquals(expected,testUpdate.execute().getCommand().toString());
	}
	
	private void testExecute (int expected, String command) throws InvalidCommandFormatException{
		UpdateParser testUpdate = new UpdateParser(command);
		assertEquals(expected,testUpdate.execute().getTaskID());
	}

}