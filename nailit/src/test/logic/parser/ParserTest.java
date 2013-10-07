package test.logic.parser;

import static org.junit.Assert.assertEquals;

import nailit.logic.parser.Parser;

import org.joda.time.DateTime;
import org.junit.Test;

public class ParserTest {
	@Test
	public void test(){
		DateTime expectedDate;
		
		expectedDate = new DateTime(2013,9,11,00,00);
		
		testExecute("testOne", expectedDate,"11 Sep 2013");
	}
	
	private void testExecute (String description, DateTime expected, String command){
		assertEquals(description,expected,Parser.retrieveDateTime(command));
	}
}
