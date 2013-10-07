package nailit.logic.parser;

import nailit.common.NIConstants;
import nailit.common.TaskPriority;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class UpdateParser extends Parser {

	private String userCommand;
	private String[] listOfCommand;
	
	public UpdateParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
		
		resultExecution.setCommand(CommandType.UPDATE);
		listOfCommand = userCommand.split(NIConstants.UPDATE_FIELD_SPLITTER);
		
		if (listOfCommand[0].equalsIgnoreCase("name")){
			String answer = "";
			for (int i=1; i<listOfCommand.length; i++)
				answer += listOfCommand[i];
			resultExecution.setName(answer);
		}else if (listOfCommand[0].equalsIgnoreCase("tag")){
			resultExecution.setTag(listOfCommand[1]);
		}else if (listOfCommand[0].equalsIgnoreCase("StartTime")){
			String answer = "";
			for (int i=1; i<listOfCommand.length; i++)
				answer += listOfCommand[i];
			resultExecution.setStartTime(Parser.retrieveDateTime(answer));
		}else if (listOfCommand[0].equalsIgnoreCase("EndTime")){
			String answer = "";
			for (int i=1; i<listOfCommand.length; i++)
				answer += listOfCommand[i];
			resultExecution.setEndTime(Parser.retrieveDateTime(answer));
		}else if (listOfCommand[0].equalsIgnoreCase("Priority")){
			resultExecution.setPriority(TaskPriority.valueOf(listOfCommand[1]));
		}
		
		return resultExecution;
	}
}
