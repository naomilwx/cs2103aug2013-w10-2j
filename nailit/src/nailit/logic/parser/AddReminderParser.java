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
			if (Parser.isNumber(userCommand)){
				resultExecution.setTaskID(Integer.parseInt(userCommand));
			}else if (Parser.isDateTime(listOfCommand[i])) {
				if (resultExecution.getEndTime() == null) {
					if (Parser.numberOfTime(listOfCommand[i]) == 2) {
						resultExecution.setStartTime(Parser.retrieveDateTimeFirst(listOfCommand[i]));
						resultExecution.setEndTime(Parser.retrieveDateTimeSecond(listOfCommand[i]));
					} else {
						resultExecution.setEndTime(Parser.retrieveDateTime(listOfCommand[i]));
					}
				} else { 
					resultExecution.setEndTime(Parser.retrieveDateTime(listOfCommand[i]));
				}
			} 
		}
		
		if (!resultExecution.isNullStartTime() && resultExecution.isNullEndTime()){
			resultExecution.setEndTime(resultExecution.getStartTime());
			resultExecution.setStartTime(null);
		}
		return resultExecution;
	}

}
