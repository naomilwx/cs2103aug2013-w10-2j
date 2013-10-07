package test.logic.parser;

import org.joda.time.DateTime;
import org.junit.Test;
import static org.junit.Assert.*;

import nailit.common.NIConstants;
import nailit.common.TaskPriority;
import nailit.logic.parser.UpdateParser;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class UpdateParserTest {
	@Test
	public void test(){
		ParserResult expectedUpdate1 = new ParserResult();
		
		expectedUpdate1.setTaskID(12);
		expectedUpdate1.setCommand(CommandType.UPDATE);
		
		testExecute(expectedUpdate1.getTaskID(),"12");
		testExecuteCommandType(expectedUpdate1.getCommand().toString(),"12");
		
	}
	
	private void testExecute (int expected, String command){
		UpdateParser testUpdate = new UpdateParser(command);
		assertEquals(expected,testUpdate.execute().getTaskID());
	}
	
	private void testExecuteCommandType (String expected, String command){
		UpdateParser testUpdate = new UpdateParser(command);
		assertEquals(expected,testUpdate.execute().getTaskID());
	}

}