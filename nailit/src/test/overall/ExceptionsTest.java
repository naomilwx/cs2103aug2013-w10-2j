package test.overall;
import static org.junit.Assert.fail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import nailit.common.CommandType;
import nailit.common.NIConstants;
import nailit.common.Result;
import nailit.logic.LogicManager;
import nailit.logic.exception.InvalidCommandFormatException;
import nailit.logic.parser.ParserExceptionConstants;
import nailit.storage.FileCorruptionException;

import org.junit.BeforeClass;
import org.junit.Test;

public class ExceptionsTest{
	public static LogicManager logic;
	public static String INVALID_COMMAND_TYPE_STRING = "hello";
	@BeforeClass
	public static void initialise(){
		try {
			logic = new LogicManager();
		} catch (FileCorruptionException e) {
			fail("storage file corrupted");
			e.printStackTrace();
		}
	}
	@Test
	public void emptyCommandTest(){
		Result result =null;
		try{
			result = logic.executeCommand("");
		}catch(Exception e){
			assertEquals(ParserExceptionConstants.EMPTY_COMMAND_TYPE, e.getMessage());
		}finally{
			assertTrue(result == null);
		}
	}
	@Test
	public void invalidCommandTest(){
		Result result = null;
		try{
			result = logic.executeCommand(INVALID_COMMAND_TYPE_STRING);
		}catch(Exception e){
			assertEquals(ParserExceptionConstants.INVLID_COMMAND, e.getMessage());
		}finally{
			assertTrue(result == null);
		}
	}
	@Test
	public void emptyAddCommandParameterTest(){
		Result result = null;
		try{
			result = logic.executeCommand(CommandType.ADD + "  ");
		}catch(Exception e){
			assertEquals(ParserExceptionConstants.EMPTY_INPUT_STRING_ADD, e.getMessage());
			assertEquals(CommandType.ADD.toString(), ((InvalidCommandFormatException) e).getCommandType().toString());
		}finally{
			assertTrue(result == null);
		}
	}
	@Test
	public void emptyUpdateCommandParameterTest(){
		Result result = null;
		try{
			result = logic.executeCommand(CommandType.UPDATE + "  ");
		}catch(Exception e){
			assertEquals(ParserExceptionConstants.EMPTY_INPUT_STRING_UPDATE, e.getMessage());
			assertEquals(CommandType.UPDATE.toString(), ((InvalidCommandFormatException) e).getCommandType().toString());
		}finally{
			assertTrue(result == null);
		}
	}
	@Test
	public void emptySearchCommandParameterTest(){
		Result result = null;
		try{
			result = logic.executeCommand(CommandType.SEARCH + "  ");
		}catch(Exception e){
			assertEquals(ParserExceptionConstants.EMPTY_INPUT_STRING_SEARCH, e.getMessage());
			assertEquals(CommandType.SEARCH.toString(), ((InvalidCommandFormatException) e).getCommandType().toString());
		}finally{
			assertTrue(result == null);
		}
	}
	@Test
	public void emptyAddReminderCommandParameterTest(){
		Result result = null;
		try{
			result = logic.executeCommand(CommandType.ADDREMINDER + "  ");
		}catch(Exception e){
			assertEquals(ParserExceptionConstants.EMPTY_INPUT_STRING_ADDREMINDER, e.getMessage());
			assertEquals(CommandType.ADDREMINDER.toString(), ((InvalidCommandFormatException) e).getCommandType().toString());
		}finally{
			assertTrue(result == null);
		}
	}
	@Test
	public void emptyDelReminderCommandParameterTest(){
		Result result = null;
		try{
			result = logic.executeCommand(CommandType.DELETEREMINDER + "  ");
		}catch(Exception e){
			assertEquals(ParserExceptionConstants.EMPTY_INPUT_STRING_DELETEREMINDER, e.getMessage());
			assertEquals(CommandType.DELETEREMINDER.toString(), ((InvalidCommandFormatException) e).getCommandType().toString());
		}finally{
			assertTrue(result == null);
		}
	}
	@Test
	public void emptyCompleteCommandParameterTest(){
		Result result = null;
		try{
			result = logic.executeCommand(CommandType.COMPLETE + "  ");
		}catch(Exception e){
			assertEquals(ParserExceptionConstants.EMPTY_INPUT_STRING_COMPLETE, e.getMessage());
			assertEquals(CommandType.COMPLETE.toString(), ((InvalidCommandFormatException) e).getCommandType().toString());
		}finally{
			assertTrue(result == null);
		}
	}
	@Test
	public void emptyUncompleteCommandParameterTest(){
		Result result = null;
		try{
			result = logic.executeCommand(CommandType.UNCOMPLETE + "  ");
		}catch(Exception e){
			assertEquals(ParserExceptionConstants.EMPTY_INPUT_STRING_UNCOMPLETE, e.getMessage());
			assertEquals(CommandType.UNCOMPLETE.toString(), ((InvalidCommandFormatException) e).getCommandType().toString());
		}finally{
			assertTrue(result == null);
		}
	}
	@Test
	public void emptyDisplayCommandParameterTest(){
		Result result = null;
		try{
			result = logic.executeCommand(CommandType.DISPLAY + "  ");
		}catch(Exception e){
			assertEquals(ParserExceptionConstants.EMPTY_INPUT_STRING_DISPLAY, e.getMessage());
			assertEquals(CommandType.DISPLAY.toString(), ((InvalidCommandFormatException) e).getCommandType().toString());
		}finally{
			assertTrue(result == null);
		}
	}
	@Test
	public void emptyDeleteCommandParameterTest(){
		Result result = null;
		try{
			result = logic.executeCommand(CommandType.DELETE + "  ");
		}catch(Exception e){
			assertEquals(ParserExceptionConstants.EMPTY_INPUT_STRING_DELETE, e.getMessage());
			assertEquals(CommandType.DELETE.toString(), ((InvalidCommandFormatException) e).getCommandType().toString());
		}finally{
			assertTrue(result == null);
		}
	}
	@Test
	public void addWithReservedStringTest(){
		Result result = null;
		try{
			result = logic.executeCommand(CommandType.ADD + " new task 123 " + NIConstants.HARDDISK_FIELD_SPLITTER + "stuff");
		}catch(Exception e){
			assertEquals(ParserExceptionConstants.RESERVE_WORD_CLASH, ((InvalidCommandFormatException) e).getMessage());
		}finally{
			assertTrue(result == null);
		}
	}
	
	@Test
	public void addWithWrongDescriptionFormatTest(){
		Result result = null;
		try{
			result = logic.executeCommand(CommandType.ADD + " new task 123 " + "(description,");
		}catch(Exception e){
			assertEquals(ParserExceptionConstants.BRACKET_UNMATCHED, e.getMessage());
			assertEquals(CommandType.ADD.toString(), ((InvalidCommandFormatException) e).getCommandType().toString());
		}finally{
			assertTrue(result == null);
		}
	}
	@Test
	public void updateWithoutFieldTest(){
		Result result = null;
		try{
			result = logic.executeCommand(CommandType.UPDATE.toString() + " 1");
		}catch(Exception e){
			assertEquals(ParserExceptionConstants.INVALID_STRING, e.getMessage());
			assertEquals(CommandType.UPDATE.toString(), ((InvalidCommandFormatException) e).getCommandType().toString());
		}finally{
			assertTrue(result == null);
		}
	}
	@Test
	public void updateWithInvalidID(){
		Result result = null;
		try{
			result = logic.executeCommand(CommandType.UPDATE.toString() + " aaa");
		}catch(Exception e){
			assertEquals(ParserExceptionConstants.NO_TASK_ID, e.getMessage());
			assertEquals(CommandType.UPDATE.toString(), ((InvalidCommandFormatException) e).getCommandType().toString());
		}finally{
			assertTrue(result == null);
		}
	}
	@Test
	public void updateWithInvalidField(){
		Result result = null;
		try{
			result = logic.executeCommand(CommandType.UPDATE.toString() + " 1, haha");
		}catch(Exception e){
			assertEquals(ParserExceptionConstants.INVALID_STRING, e.getMessage());
//			assertEquals(CommandType.UPDATE.toString(), ((InvalidCommandFormatException) e).getCommandType().toString());
		}finally{
			assertTrue(result == null);
		}
	}
	@Test
	public void updateWithInvalidTime(){
		Result result = null;
		try{
			result = logic.executeCommand(CommandType.UPDATE.toString() + " 1, end, blah");
		}catch(Exception e){
			assertEquals(ParserExceptionConstants.WRONG_TIME_FORMAT, e.getMessage());
			assertEquals(CommandType.UPDATE.toString(), ((InvalidCommandFormatException) e).getCommandType().toString());
		}finally{
			assertTrue(result == null);
		}
	}
	@Test
	public void updateWithStartTime(){
		Result result = null;
		try{
			result = logic.executeCommand(CommandType.UPDATE.toString() + " 1, start, lala");
		}catch(Exception e){
			assertEquals(ParserExceptionConstants.WRONG_TIME_FORMAT, e.getMessage());
			assertEquals(CommandType.UPDATE.toString(), ((InvalidCommandFormatException) e).getCommandType().toString());
		}finally{
			assertTrue(result == null);
		}
	}
	@Test
	public void updatedateWithInvalidTime(){
		Result result = null;
		try{
			result = logic.executeCommand(CommandType.UPDATE.toString() + " 1, date, monday to blah");
		}catch(Exception e){
			assertEquals(ParserExceptionConstants.WRONG_TIME_FORMAT, e.getMessage());
		}finally{
			assertTrue(result == null);
		}
	}
	@Test
	public void updateWithInvalidPriority(){
		Result result = null;
		try{
			result = logic.executeCommand(CommandType.UPDATE.toString() + " 1, priority, monday");
		}catch(Exception e){
			assertEquals(ParserExceptionConstants.WRONG_PRIORITY_FORMAT, e.getMessage());
		}finally{
			assertTrue(result == null);
		}
	}
}
