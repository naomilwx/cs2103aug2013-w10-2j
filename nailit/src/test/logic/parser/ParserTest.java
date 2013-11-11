package test.logic.parser;

// @author A0105559B
import static org.junit.Assert.*;
import nailit.logic.parser.Parser;
import nailit.common.NIConstants;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import test.logic.command.CommandTest;
@Category(CommandTest.class)
public class ParserTest {
	@Test
	public void test(){
		DateTime expectedDate;
		
		expectedDate = new DateTime(2013,9,11,00,00);
		
		testRetrieveDateTime(expectedDate.toString(NIConstants.DISPLAY_DATE_FORMAT),"11 Sep 2013");
		testIsTaskID(true, "12");
		testIsTaskID(false, "sep 11 2012");
		testIsDateTime(true, "sep 11 2012");
		testIsDateTime(false, "Assignment 2");
		testIsDateTime(true, "from yesterday to tomorrow");
		testNumberOfTime(2,"from yesterday to tomorrow");
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
	
	private void testNumberOfTime( int expected, String command)
	{
		assertEquals(expected, Parser.numberOfTime(command));
	}
	@Test
	public void testIsValidString(){
		assertTrue(Parser.isValidString(""));
		assertTrue(Parser.isValidString("$hello%"));
		assertTrue(Parser.isValidString("$ %"));
		assertTrue(Parser.isValidString("%$"));
		assertFalse(Parser.isValidString(NIConstants.HARDDISK_FIELD_SPLITTER));
		assertFalse(Parser.isValidString("hello, "+NIConstants.HARDDISK_FIELD_SPLITTER + "world"));
		assertFalse(Parser.isValidString("hello, "+NIConstants.HARDDISK_FIELD_SPLITTER + "world" + NIConstants.HARDDISK_FIELD_SPLITTER + "next string"));
	}
}
