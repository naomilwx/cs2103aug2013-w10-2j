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
		testManager(CommandType.ADD,"Add task");
		testManager(CommandType.ADDREMINDER,"Addreminder 12, today");
		testManager(CommandType.COMPLETE,"Complete 12");
		testManager(CommandType.DELETE,"Delete 12");
		testManager(CommandType.DELETEREMINDER,"Deletereminder 12");
		testManager(CommandType.DISPLAY,"Display 12");
		testManager(CommandType.EXIT,"Exit");
		testManager(CommandType.REDO,"REDO");
		testManager(CommandType.SEARCH,"SEARCH TODAY");
		testManager(CommandType.UNCOMPLETE,"Uncomplete 12");
		testManager(CommandType.UNDO,"UNDO");
		testManager(CommandType.UPDATE,"UPDATE 1, name, taskname");
	}
	
	private void testManager (CommandType expected, String command) throws InvalidCommandFormatException, InvalidCommandTypeException{
		ParserManager testParserManager = new ParserManager();
		testParserManager.passCommand(command);
		assertEquals(expected,testParserManager.execute().getCommand());
	}

}
