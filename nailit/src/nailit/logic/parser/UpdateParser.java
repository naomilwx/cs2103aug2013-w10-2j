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
			if (answer == ""){
				resultExecution.setName(null);
			} else{
				resultExecution.setName(answer.substring(0, answer.length()-1));
			}
		}else if (listOfCommand[1].equalsIgnoreCase("tag")){
			if (listOfCommand.length<2){
				resultExecution.setTag(null);
			}else{
				resultExecution.setTag(listOfCommand[2]);
			}
		}else if (listOfCommand[1].equalsIgnoreCase("Start")){
			String answer = "";
			for (int i=2; i<listOfCommand.length; i++)
				answer += listOfCommand[i]+" ";
			if (answer == ""){
				resultExecution.setStartTime(null);
			}else{
				resultExecution.setStartTime(Parser.retrieveDateTime(answer.substring(0, answer.length()-1)));
			}
		}else if (listOfCommand[1].equalsIgnoreCase("End")){
			String answer = "";
			for (int i=2; i<listOfCommand.length; i++)
				answer += listOfCommand[i]+" ";
			if (answer == ""){
				resultExecution.setEndTime(null);
			}else{
				resultExecution.setEndTime(Parser.retrieveDateTime(answer.substring(0, answer.length()-1)));
			}
		}else if (listOfCommand[1].equalsIgnoreCase("Priority")){
			if (listOfCommand.length<2){
				resultExecution.setSetPriority(false);
			}else{
				resultExecution.setSetPriority(true);
				resultExecution.setPriority(TaskPriority.valueOf(listOfCommand[2].toUpperCase()));
			}
		}
		System.out.println(resultExecution.getEndTime());
		return resultExecution;
	}
}
