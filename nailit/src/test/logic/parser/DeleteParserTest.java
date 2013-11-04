package test.logic.parser;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import test.logic.command.CommandTest;
import static org.junit.Assert.*;
import nailit.common.NIConstants;
import nailit.common.TaskPriority;
import nailit.logic.parser.AddParser;
import nailit.logic.parser.DeleteParser;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
@Category(CommandTest.class)
public class DeleteParserTest {
	@Test
	public void test(){
		ParserResult expectedDelete = new ParserResult();
		
		expectedDelete.setTaskID(12);
		expectedDelete.setCommand(CommandType.DELETE);
		
		testExecute(expectedDelete.getTaskID(),"12");
		testExecuteCommandType(expectedDelete.getCommand().toString(),"12");
		
	}
	
	private void testExecute (int expected, String command){
		DeleteParser testDelete = new DeleteParser(command);
		assertEquals(expected,testDelete.execute().getTaskID());
	}
	
	private void testExecuteCommandType (String expected, String command){
		DeleteParser testDelete = new DeleteParser(command);
		assertEquals(expected,testDelete.execute().getCommand().toString());
	}

}
