package test.logic.parser;

import static org.junit.Assert.assertEquals;
import nailit.common.NIConstants;
import nailit.logic.CommandType;
import nailit.logic.parser.Parser;
import nailit.logic.parser.ParserManager;

import org.joda.time.DateTime;
import org.junit.Test;

public class ParserManagerTest {
	@Test
	public void test(){
		DateTime expectedDate;
		
		expectedDate = new DateTime(2013,9,11,00,00);
		
		testManager(CommandType.DELETE,"Delete 12");
	}
	
	private void testManager (CommandType expected, String command){
		ParserManager testParserManager = new ParserManager();
		testParserManager.passCommand(command);
		assertEquals(expected,testParserManager.execute().getCommand());
	}

}
