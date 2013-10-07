package test.logic.parser;

import org.joda.time.DateTime;
import org.junit.Test;
import static org.junit.Assert.*;

import nailit.common.NIConstants;
import nailit.common.TaskPriority;
import nailit.logic.parser.DeleteParser;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class DeleteParserTest {
	@Test
	public void test(){
		ParserResult expectedDelete = new ParserResult();
		
		expectedDelete.setTaskID(12);
		
		testExecute(expectedDelete.getTaskID(),"12");
		
	}
	
	private void testExecute (int expected, String command){
		DeleteParser testAdd = new DeleteParser(command);
		assertEquals(expected,testAdd.execute().getTaskID());
	}

}
