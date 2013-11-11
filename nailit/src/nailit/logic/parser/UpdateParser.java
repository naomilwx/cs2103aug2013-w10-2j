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
		
		if (Parser.isNumber(listOfCommands[0])){
			resultExecution.setTaskId(Integer.parseInt(listOfCommands[0]));
		}else{
			throw new InvalidCommandFormatException(CommandType.UPDATE, "Wrong Format: Cannot identify the task ID");
		}
		
		if (listOfCommands.length == 1){
			throw new InvalidCommandFormatException(CommandType.UPDATE, "Wrong Format: The modified information cannot be null");
		}
		
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
			}else if (listOfCommands.length == 3){
				if (Parser.isTag(listOfCommands[2])){
					resultExecution.setTag(listOfCommands[2]);
				}else{
					throw new InvalidCommandFormatException("Wrong format: The string is not a correct tag format");
				}
			}else{
				throw new InvalidCommandFormatException("Wrong format: The string is not a correct tag format");
			}
		}else if (listOfCommands[1].equalsIgnoreCase("Start")){
			String answer = "";
			for (int i=2; i<listOfCommands.length; i++){
				answer += listOfCommands[i]+" ";
			}
			if (answer == ""){
				resultExecution.setStartTimeNull(true);
			}else if (Parser.isDateTime(answer)){
				resultExecution.setStartTime(Parser.retrieveDateTime(answer.substring(0, answer.length()-1)));
			}else{
				throw new InvalidCommandFormatException("Wrong format: The string is not a correct time format or consists resevered string");
			}
		}else if (listOfCommands[1].equalsIgnoreCase("End")|| listOfCommands[1].equalsIgnoreCase("Due")){
			String answer = "";
			for (int i=2; i<listOfCommands.length; i++){
				answer += listOfCommands[i]+" ";
			}
			if (answer == ""){
				resultExecution.setEndTimeNull(true);
			}else if (Parser.isDateTime(answer)){
				resultExecution.setEndTime(Parser.retrieveDateTime(answer.substring(0, answer.length()-1)));
			}else{
				throw new InvalidCommandFormatException("Wrong format: The string is not a correct time format or consists reserved string");
			}
		}else if (listOfCommands[1].equalsIgnoreCase("Date")){
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
			}else if (listOfCommands.length == 3){
				if (Parser.isPriority(listOfCommands[2])){
					resultExecution.isNullPriority();
					resultExecution.setPriority(TaskPriority.valueOf(listOfCommands[2].toUpperCase()));
				}else{
					throw new InvalidCommandFormatException("Wrong format: The string cannot represent priority");
				}
			}else{
				throw new InvalidCommandFormatException("Wrong format: The string cannot represent priority");
			}
		}else if (listOfCommands[1].equalsIgnoreCase("Reminder")){
			String answer = "";
			for (int i=2; i<listOfCommands.length; i++){
				answer += listOfCommands[i]+" ";
			}
			if (answer == ""){
				resultExecution.setReminderTimeNull(true);
			}else{
				if (Parser.isDateTime(answer)){
					resultExecution.setReminderTime(Parser.retrieveDateTime(answer.substring(0, answer.length()-1)));
				}else{
					throw new InvalidCommandFormatException("Wrong format: The string is not a correct time format");
				}
			}
		}else 
		{
			throw new InvalidCommandFormatException(CommandType.UPDATE, "Wrong Format: Cannot identify which field you want to update");
		}
		return resultExecution;
	}
}
