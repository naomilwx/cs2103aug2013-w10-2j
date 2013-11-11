package test.logic.parser;
//@author A0105559B

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import test.logic.command.CommandTest;
import static org.junit.Assert.*;
import nailit.common.NIConstants;
import nailit.logic.exception.InvalidCommandFormatException;
import nailit.logic.parser.DisplayParser;
import nailit.logic.ParserResult;

@Category(CommandTest.class)
public class DisplayParserTest {
	@Test
	public void test() throws InvalidCommandFormatException{
		
		ParserResult expectedDisplay = new ParserResult();
		expectedDisplay.setEndTime(new DateTime(2013,9,11,00,00));
		
		testExecuteIsDisplayAll(true,"ALL");
		testExecuteTaskID(12, "12");
		testExecuteEndTime(expectedDisplay.getEndTime().toString(NIConstants.DISPLAY_DATE_FORMAT), "sep 11 2013");
		testExecuteIsDisplayHistory(true, "HISTORY");
		testExecuteIsDisplayComplete(true, "COMPLETE");
		testExecuteIsDisplayUncomplete(true, "UNCOMPLETE");
	}
	
	private void testExecuteIsDisplayAll (boolean expected, String command) throws InvalidCommandFormatException{
		DisplayParser testDisplay = new DisplayParser(command);
		assertEquals(expected,testDisplay.execute().isDisplayAll());
	}
	
	private void testExecuteIsDisplayHistory (boolean expected, String command) throws InvalidCommandFormatException{
		DisplayParser testDisplay = new DisplayParser(command);
		assertEquals(expected,testDisplay.execute().isDisplayHistory());
	}
	
	private void testExecuteIsDisplayComplete (boolean expected, String command) throws InvalidCommandFormatException{
		DisplayParser testDisplay = new DisplayParser(command);
		assertEquals(expected,testDisplay.execute().isDisplayComplete());
	}
	
	private void testExecuteIsDisplayUncomplete (boolean expected, String command) throws InvalidCommandFormatException{
		DisplayParser testDisplay = new DisplayParser(command);
		assertEquals(expected,testDisplay.execute().isDisplayUncomplete());
	}
	
	private void testExecuteTaskID (int expected, String command) throws InvalidCommandFormatException{
		DisplayParser testDisplay = new DisplayParser(command);
		assertEquals(expected,testDisplay.execute().getTaskID());
	}
	
	private void testExecuteEndTime (String expected, String command) throws InvalidCommandFormatException{
		DisplayParser testDisplay = new DisplayParser(command);
		assertEquals(expected,testDisplay.execute().getEndTime().toString(NIConstants.DISPLAY_DATE_FORMAT));
	}

}