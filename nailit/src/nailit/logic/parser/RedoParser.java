package nailit.logic.parser;

//@author A0105559B
import nailit.common.CommandType;
import nailit.logic.ParserResult;

public class RedoParser extends Parser {
	
	private String userCommand;
	
	public RedoParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
		// Set the commandType
		resultExecution.setCommand(CommandType.REDO);
		if (userCommand.equalsIgnoreCase("")){
			resultExecution.setTaskId(-1);
			return resultExecution;
		}
		return resultExecution;
	}
}