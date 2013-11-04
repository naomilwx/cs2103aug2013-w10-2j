package nailit.logic.parser;

// @auther A0105559B

import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.common.NIConstants;
import nailit.common.TaskPriority;

public class AddParser extends Parser {

	private String userCommand;
	private String[] listOfCommands;
	private String name="";
	
	public AddParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
		
		resultExecution.setCommand(CommandType.ADD);
		if (userCommand.equals("")){
			throw new Error("Wrong Format");
		}
		int startIndex = -1, endIndex = 0;
		startIndex = userCommand.indexOf('(');
		if (startIndex!=-1){
			for (int i=startIndex+1; i<userCommand.length(); i++)
				if (userCommand.charAt(i)==')'){
					endIndex = i;
				}
			if (endIndex == 0)
				throw new Error ("Wrong Format: Bracket is not matched");
			resultExecution.setDescription(userCommand.substring(startIndex+1, endIndex));
			userCommand = userCommand+" ";
			
			String temp = userCommand.substring(endIndex+1, userCommand.length());
			int index = -1;
			for (int i=startIndex; i>0; i--){
				if (userCommand.charAt(i) == ','){
					index = i;
					break;
				}
			}
			if (index != -1){
				userCommand = userCommand.substring(0,index);
			}
			
			userCommand += temp;
		}

		listOfCommands = userCommand.split(NIConstants.NORMAL_FIELD_SPLITTER);
		for (int i=0; i<listOfCommands.length; i++) {
			listOfCommands[i] = listOfCommands[i].trim();
			if (TaskPriority.isTaskPriority(listOfCommands[i])) {
				resultExecution.setPriority(TaskPriority.valueOf(listOfCommands[i].toUpperCase()));
			}else if (Parser.isTag(listOfCommands[i])) {
				resultExecution.setTag(listOfCommands[i]);
			}else if (Parser.isDateTime(listOfCommands[i])) {
				if (resultExecution.getStartTime() == null) {
					if (Parser.numberOfTime(listOfCommands[i]) == 2) {
						resultExecution.setStartTime(Parser.retrieveDateTimeFirst(listOfCommands[i]));
						resultExecution.setEndTime(Parser.retrieveDateTimeSecond(listOfCommands[i]));
					} else {
						resultExecution.setStartTime(Parser.retrieveDateTime(listOfCommands[i]));
					}
				} else { 
					resultExecution.setEndTime(Parser.retrieveDateTime(listOfCommands[i]));
				}
			} else 
				name+= listOfCommands[i];
		}

		if (name.equals("")){
			throw new Error ("Wrong format: No name");
		}
		resultExecution.setName(name);
		
		if (!resultExecution.isNullStartTime() && resultExecution.isNullEndTime()){
			resultExecution.setEndTime(resultExecution.getStartTime());
			resultExecution.setStartTime(null);
		}
		return resultExecution;
	}
}
