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
		
		for (int i=0; i<listOfCommand.length; i++)
			listOfCommand[i] = listOfCommand[i].trim();
		resultExecution.setTaskID(Integer.parseInt(listOfCommand[0]));
		if (listOfCommand[1].equalsIgnoreCase("name")){
			String answer = "";
			for (int i=2; i<listOfCommand.length; i++)
				answer += listOfCommand[i]+ " ";
			resultExecution.setName(answer.substring(0, answer.length()-1));
		}else if (listOfCommand[1].equalsIgnoreCase("tag")){
			resultExecution.setTag(listOfCommand[2]);
		}else if (listOfCommand[1].equalsIgnoreCase("StartTime")){
			String answer = "";
			for (int i=2; i<listOfCommand.length; i++)
				answer += listOfCommand[i]+" ";
			resultExecution.setStartTime(Parser.retrieveDateTime(answer.substring(0, answer.length()-1)));
		}else if (listOfCommand[1].equalsIgnoreCase("EndTime")){
			String answer = "";
			for (int i=2; i<listOfCommand.length; i++)
				answer += listOfCommand[i]+" ";
			resultExecution.setEndTime(Parser.retrieveDateTime(answer.substring(0, answer.length()-1)));
		}else if (listOfCommand[1].equalsIgnoreCase("Priority")){
			resultExecution.setPriority(TaskPriority.valueOf(listOfCommand[2]));
		}
		
		return resultExecution;
	}
}
