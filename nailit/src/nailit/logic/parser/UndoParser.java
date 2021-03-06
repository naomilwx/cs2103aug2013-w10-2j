package nailit.logic.parser;

//@author A0105559B
import nailit.common.CommandType;
import nailit.logic.ParserResult;

public class UndoParser extends Parser {
	
	private String userCommand;
	
	public UndoParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
		// Set the commandType
		resultExecution.setCommand(CommandType.UNDO);
		if (userCommand.equalsIgnoreCase("")){
			resultExecution.setTaskId(-1);
			return resultExecution;
		}
		return resultExecution;
	}
}