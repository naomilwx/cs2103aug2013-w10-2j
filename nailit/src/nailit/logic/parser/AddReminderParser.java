package nailit.logic.parser;

import nailit.common.NIConstants;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class AddReminderParser extends Parser{

	private String userCommand;
	private String[] listOfCommands;
	
	public AddReminderParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
		resultExecution.setCommand(CommandType.ADDREMINDER);
		listOfCommands = userCommand.split(NIConstants.NORMAL_FIELD_SPLITTER);
		for (int i=0; i<listOfCommands.length; i++) {
			listOfCommands[i] = listOfCommands[i].trim();
			if (Parser.isNumber(listOfCommands[i])){
				resultExecution.setTaskId(Integer.parseInt(listOfCommands[i]));
				System.out.println(listOfCommands[i]);
			}else if (Parser.isDateTime(listOfCommands[i])) {
				resultExecution.setReminderTime(Parser.retrieveDateTime(listOfCommands[i]));
			} 
		}
		return resultExecution;
	}

}
