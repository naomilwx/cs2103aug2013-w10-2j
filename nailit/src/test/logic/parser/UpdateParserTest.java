package test.logic.parser;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import test.logic.command.CommandTest;
import static org.junit.Assert.*;
import nailit.common.CommandType;
import nailit.common.NIConstants;
import nailit.logic.exception.InvalidCommandFormatException;
import nailit.logic.parser.UpdateParser;
import nailit.logic.ParserResult;

@Category(CommandTest.class)
public class UpdateParserTest {
	@Test
	public void test() throws InvalidCommandFormatException{
		ParserResult expectedUpdate1 = new ParserResult();
		
//		expectedUpdate1.setTaskID(12);
		expectedUpdate1.setCommand(CommandType.UPDATE);
		
		testExecuteName("CS Assignment 2","12 Name CS Assignment 2");
		testExecuteTag("#Work#", "12 Tag #Work#");
		testExecuteTaskPriority("LOW", "12 Priority LOW");
		testExecuteStartTime("11 Feb 2013", "12 Start feb 11 2013");
		testExecuteEndTime("11 Feb 2013", "12 end feb 11 2013");
		
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
	
	

}