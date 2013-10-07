package test.logic.parser;

import org.joda.time.DateTime;
import org.junit.Test;
import static org.junit.Assert.*;

import nailit.common.NIConstants;
import nailit.common.TaskPriority;
import nailit.logic.parser.DisplayParser;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class DisplayParserTest {
	@Test
	public void test(){
		
		
		testExecuteName("CS Assignment 2","ALL");
		testExecuteTag("#Work#", "Tag #Work");
		
	}
	
	private void testExecuteName (String expected, String command){
		DisplayParser testDisplay = new DisplayParser(command);
		assertEquals(expected,testDisplay.execute().getName());
	}
	
	private void testExecuteTag (String expected, String command){
		DisplayParser testDisplay = new DisplayParser(command);
		assertEquals(expected,testDisplay.execute().getTag());
	}
	
	private void testExecuteisDisplayAll (boolean expected, String command){
		DisplayParser testDisplay = new DisplayParser(command);
		assertEquals(expected,testDisplay.execute());
	}
	
	

}