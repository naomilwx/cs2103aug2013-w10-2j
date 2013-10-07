package test.logic.parser;

import org.joda.time.DateTime;
import org.junit.Test;
import static org.junit.Assert.*;

import nailit.common.NIConstants;
import nailit.common.TaskPriority;
import nailit.logic.parser.AddParser;
import nailit.logic.parser.DeleteParser;
import nailit.logic.parser.DisplayParser;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class DisplayParserTest {
	@Test
	public void test(){
		
		
		testExecuteIsDisplayAll(true,"ALL");
		testExecuteTaskID(12, "12");
		testExecuteStartTime("11 Sep 2013", "sep 11 2013");
		
	}
	
	private void testExecuteIsDisplayAll (boolean expected, String command){
		DisplayParser testDisplay = new DisplayParser(command);
		assertEquals(expected,testDisplay.execute().isDisplayAll());
	}
	
	private void testExecuteTaskID (int expected, String command){
		DisplayParser testDisplay = new DisplayParser(command);
		assertEquals(expected,testDisplay.execute().getTaskID());
	}
	
	private void testExecuteStartTime (String expected, String command){
		DisplayParser testDisplay = new DisplayParser(command);
		assertEquals(expected,testDisplay.execute().getStartTime().toString(NIConstants.DISPLAY_DATE_FORMAT));
	}

}