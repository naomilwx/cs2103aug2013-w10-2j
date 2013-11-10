package nailit.logic.parser;

//@author A0105559B
import nailit.common.NIConstants;
import nailit.common.TaskPriority;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.logic.exception.InvalidCommandFormatException;

public class UpdateParser extends Parser {

	private String userCommand;
	private String[] listOfCommands;
	
	public UpdateParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute() throws InvalidCommandFormatException{
		ParserResult resultExecution = new ParserResult();
		
		resultExecution.setCommand(CommandType.UPDATE);
		if (userCommand.equals("")){
			throw new InvalidCommandFormatException(CommandType.UPDATE,"Wrong Format: Cannot update an empth task, please specify your task name");
		}
		listOfCommands = userCommand.split(NIConstants.NORMAL_FIELD_SPLITTER);
		
		for (int i=0; i<listOfCommands.length; i++){
			listOfCommands[i] = listOfCommands[i].trim();
		}
		resultExecution.setTaskId(Integer.parseInt(listOfCommands[0]));
		
		if (listOfCommands[1].equalsIgnoreCase("name")){
			String answer = "";
			for (int i=2; i<listOfCommands.length; i++){
				answer += listOfCommands[i]+ " ";
			}
			if (answer == ""){
				throw new InvalidCommandFormatException(CommandType.UPDATE,"Wrong Format: Cannot update name to be null");
			} else{
				resultExecution.setName(answer.substring(0, answer.length()-1));
			}
		}else if (listOfCommands[1].equalsIgnoreCase("description")){
			String answer = "";
			for (int i=2; i<listOfCommands.length; i++){
				answer += listOfCommands[i]+ " ";
			}
			if (answer == ""){
				resultExecution.setDescriptionNull(true);
			} else{
				resultExecution.setDescription(answer.substring(0, answer.length()-1));
			}
		}else if (listOfCommands[1].equalsIgnoreCase("tag")){
			if (listOfCommands.length<=2){
				resultExecution.setTagNull(true);
			}else{
				resultExecution.setTag(listOfCommands[2]);
			}
		}else if (listOfCommands[1].equalsIgnoreCase("Start")){
			String answer = "";
			for (int i=2; i<listOfCommands.length; i++){
				answer += listOfCommands[i]+" ";
			}
			if (answer == ""){
				resultExecution.setStartTimeNull(true);
			}else{
				resultExecution.setStartTime(Parser.retrieveDateTime(answer.substring(0, answer.length()-1)));
			}
		}else if (listOfCommands[1].equalsIgnoreCase("End")|| listOfCommands[1].equalsIgnoreCase("Due")){
			String answer = "";
			for (int i=2; i<listOfCommands.length; i++){
				answer += listOfCommands[i]+" ";
			}
			if (answer == ""){
				resultExecution.setEndTimeNull(true);
			}else{
				resultExecution.setEndTime(Parser.retrieveDateTime(answer.substring(0, answer.length()-1)));
			}
		}else if (listOfCommands[1].equalsIgnoreCase("Time")){
			String answer = "";
			for (int i=2; i<listOfCommands.length; i++){
				answer += listOfCommands[i]+" ";
			}
			if (answer == ""){
				resultExecution.setEndTimeNull(true);
				resultExecution.setStartTimeNull(true);
			}else{
				resultExecution.setStartTime(Parser.retrieveDateTimeFirst(answer));
				resultExecution.setEndTime(Parser.retrieveDateTimeSecond(answer));
			}
		}else if (listOfCommands[1].equalsIgnoreCase("Priority")){
			if (listOfCommands.length<=2){
				resultExecution.setPriorityNull(true);
			}else{
				resultExecution.isNullPriority();
				resultExecution.setPriority(TaskPriority.valueOf(listOfCommands[2].toUpperCase()));
			}
		}else if (listOfCommands[1].equalsIgnoreCase("Reminder")){
			String answer = "";
			for (int i=2; i<listOfCommands.length; i++){
				answer += listOfCommands[i]+" ";
			}
			if (answer == ""){
				resultExecution.setReminderTimeNull(true);
			}else{
				resultExecution.setReminderTime(Parser.retrieveDateTime(answer.substring(0, answer.length()-1)));
			}
		}else 
		{
			throw new InvalidCommandFormatException(CommandType.UPDATE, "Wrong Format: Cannot identify which field you want to update");
		}
		return resultExecution;
	}
}
