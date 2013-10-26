package nailit.logic.parser;

import nailit.common.NIConstants;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class AddReminderParser extends Parser{

	private String userCommand;
	private String[] listOfCommand;
	
	public AddReminderParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
		resultExecution.setCommand(CommandType.ADDREMINDER);
		listOfCommand = userCommand.split(NIConstants.NORMAL_FIELD_SPLITTER);
		if (Parser.isNumber(userCommand))
			resultExecution.setTaskID(Integer.parseInt(userCommand));
		else 
			throw new Error("Wrong Format");
		return resultExecution;
	}

}
