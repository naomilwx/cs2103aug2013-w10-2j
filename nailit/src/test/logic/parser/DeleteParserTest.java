package test.logic.parser;
//@author A0105559B

import org.junit.Test;
import org.junit.experimental.categories.Category;
import test.logic.command.AllCommandTest;
import static org.junit.Assert.*;
import nailit.common.CommandType;
import nailit.logic.exception.InvalidCommandFormatException;
import nailit.logic.parser.DeleteParser;
import nailit.logic.ParserResult;

@Category(AllCommandTest.class)
public class DeleteParserTest {
	@Test
	public void test() throws InvalidCommandFormatException{
		ParserResult expectedDelete = new ParserResult();
		
		expectedDelete.setTaskId(12);
		expectedDelete.setCommand(CommandType.DELETE);
		
		testExecute(expectedDelete.getTaskID(),"12");
		testExecuteCommandType(expectedDelete.getCommand().toString(),"12");
		
	}
	
	private void testExecute (int expected, String command) throws InvalidCommandFormatException{
		DeleteParser testDelete = new DeleteParser(command);
		assertEquals(expected,testDelete.execute().getTaskID());
	}
	
	private void testExecuteCommandType (String expected, String command) throws InvalidCommandFormatException{
		DeleteParser testDelete = new DeleteParser(command);
		assertEquals(expected,testDelete.execute().getCommand().toString());
	}

}
