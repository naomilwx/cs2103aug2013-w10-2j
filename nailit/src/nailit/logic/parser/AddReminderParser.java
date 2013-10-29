package nailit.logic.parser;

import nailit.common.NIConstants;
import nailit.common.TaskPriority;
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
		for (int i=0; i<listOfCommand.length; i++) {
			listOfCommand[i] = listOfCommand[i].trim();
			if (Parser.isNumber(listOfCommand[i])){
				resultExecution.setTaskID(Integer.parseInt(listOfCommand[i]));
			}else if (Parser.isDateTime(listOfCommand[i])) {
				resultExecution.setReminderTime(Parser.retrieveDateTime(listOfCommand[i]));
			} 
		}
		return resultExecution;
	}

}
