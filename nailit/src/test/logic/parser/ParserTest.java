package test.logic.parser;

import static org.junit.Assert.assertEquals;

import nailit.logic.parser.Parser;
import nailit.common.NIConstants;

import org.joda.time.DateTime;
import org.junit.Test;

public class ParserTest {
	@Test
	public void test(){
		DateTime expectedDate;
		
		expectedDate = new DateTime(2013,9,11,00,00);
		
		testRetrieveDateTime(expectedDate.toString(NIConstants.DISPLAY_DATE_FORMAT),"11 Sep 2013");
		//testIsTaskID(true, "12");
		//testIsTaskID(false, "sep 11 2012");
		//testIsDateTime(true, "sep 11 2012");
		testIsDateTime(false, "Assignment 2");
		testIsDateTime(true, "from yesterday to tomorrow");
		//testHasDateTime(false, "from 20");
		//testHasDateTime(true, "from 9am to 10 pm");
	}
	
	private void testRetrieveDateTime (String expected, String command){
		assertEquals(expected,Parser.retrieveDateTime(command).toString(NIConstants.DISPLAY_DATE_FORMAT));
	}
	
	private void testIsTaskID( boolean expected, String command){
		assertEquals(expected, Parser.isTaskID(command));
	}
	
	private void testIsDateTime( boolean expected, String command)
	{
		assertEquals(expected, Parser.isDateTime(command));
	}
	
	
}
