package test.logic.parser;
//@author A0105559B

import static org.junit.Assert.assertEquals;
import nailit.common.CommandType;
import nailit.logic.exception.InvalidCommandFormatException;
import nailit.logic.exception.InvalidCommandTypeException;
import nailit.logic.parser.ParserManager;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import test.logic.command.CommandTest;

@Category(CommandTest.class)
public class ParserManagerTest {
	@Test
	public void test() throws InvalidCommandFormatException, InvalidCommandTypeException{
		//DateTime expectedDate = new DateTime(2013,9,11,00,00);
		testManager(CommandType.DELETE,"Delete 12");
	}
	
	private void testManager (CommandType expected, String command) throws InvalidCommandFormatException, InvalidCommandTypeException{
		ParserManager testParserManager = new ParserManager();
		testParserManager.passCommand(command);
		assertEquals(expected,testParserManager.execute().getCommand());
	}

}
