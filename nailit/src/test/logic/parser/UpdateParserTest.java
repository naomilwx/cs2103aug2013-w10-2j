package test.logic.parser;

import org.joda.time.DateTime;
import org.junit.Test;
import static org.junit.Assert.*;

import nailit.common.NIConstants;
import nailit.common.TaskPriority;
import nailit.logic.parser.AddParser;
import nailit.logic.parser.UpdateParser;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class UpdateParserTest {
	@Test
	public void test(){
		ParserResult expectedUpdate1 = new ParserResult();
		
		expectedUpdate1.setTaskID(12);
		expectedUpdate1.setCommand(CommandType.UPDATE);
		
		testExecuteName("CS Assignment 2","Name CS Assignment 2");
		testExecuteTag("#Work#", "Tag #Work#");
		testExecuteTaskPriority("LOW", "Priority LOW");
		testExecuteStartTime("11 Feb 2013", "StartTime feb 11 2013");
		testExecuteEndTime("11 Feb 2013", "endtime feb 11 2013");
		
	}
	
	private void testExecuteName (String expected, String command){
		UpdateParser testUpdate = new UpdateParser(command);
		assertEquals(expected,testUpdate.execute().getName());
	}
	
	private void testExecuteTag (String expected, String command){
		UpdateParser testUpdate = new UpdateParser(command);
		assertEquals(expected,testUpdate.execute().getTag());
	}
	
	private void testExecuteTaskPriority (String expected, String command){
		UpdateParser testUpdate = new UpdateParser(command);
		assertEquals(expected,testUpdate.execute().getPriority().toString());
	}
	
	private void testExecuteStartTime (String expected, String command){
		UpdateParser testUpdate = new UpdateParser(command);
		assertEquals(expected,testUpdate.execute().getStartTime().toString(NIConstants.DISPLAY_DATE_FORMAT));
	}
	
	private void testExecuteEndTime (String expected, String command){
		UpdateParser testUpdate = new UpdateParser(command);
		assertEquals(expected,testUpdate.execute().getEndTime().toString(NIConstants.DISPLAY_DATE_FORMAT));
	}
	
	

}