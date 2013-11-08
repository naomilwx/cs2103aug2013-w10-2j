package test.logic;

import static org.junit.Assert.*;
import nailit.logic.parser.AddParser;

import org.junit.Test;

public class ParserResultTest {

		@Test
		public void test(){
		}
		
		private void testisDateTime (String description, String expected, String command)
		{
			assertEquals(description,expected,AddParser.isDateTime(command));
		}
		
		

}