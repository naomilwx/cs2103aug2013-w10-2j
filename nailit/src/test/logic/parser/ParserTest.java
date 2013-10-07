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
		
		testExecute("testOne", expectedDate.toString(NIConstants.DISPLAY_DATE_FORMAT),"11 Sep 2013");
	}
	
	private void testExecute (String description, String expected, String command){
		assertEquals(description,expected,Parser.retrieveDateTime(command).toString(NIConstants.DISPLAY_DATE_FORMAT));
	}
}
