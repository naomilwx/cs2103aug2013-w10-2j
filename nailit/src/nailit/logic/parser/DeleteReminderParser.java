package nailit.logic.parser;

//@author A0105559B
import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class DeleteReminderParser extends Parser {
	
	private String userCommand;
	
	public DeleteReminderParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
	
		resultExecution.setCommand(CommandType.DELETEREMINDER);
		if (Parser.isNumber(userCommand))
			resultExecution.setTaskId(Integer.parseInt(userCommand));
		else 
			throw new Error("Wrong Format");
		
		return resultExecution;
	}
}
