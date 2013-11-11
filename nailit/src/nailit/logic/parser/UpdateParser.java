package nailit.logic.parser;

//@author A0105559B
import nailit.common.CommandType;
import nailit.common.NIConstants;
import nailit.common.TaskPriority;
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
			throw new InvalidCommandFormatException(CommandType.UPDATE,ParserExceptionConstants.EMPTY_INPUT_STRING_UPDATE);
		}
		listOfCommands = userCommand.split(NIConstants.NORMAL_FIELD_SPLITTER);
		
		for (int i=0; i<listOfCommands.length; i++){
			listOfCommands[i] = listOfCommands[i].trim();
		}
		
		if (Parser.isNumber(listOfCommands[0])){
			resultExecution.setTaskId(Integer.parseInt(listOfCommands[0]));
		}else{
			throw new InvalidCommandFormatException(CommandType.UPDATE, ParserExceptionConstants.NO_TASK_ID);
		}
		
		if (listOfCommands.length == 1){
			throw new InvalidCommandFormatException(CommandType.UPDATE, ParserExceptionConstants.INVALID_STRING);
		}
		
		if (listOfCommands[1].equalsIgnoreCase("name")){
			String answer = "";
			for (int i=2; i<listOfCommands.length; i++){
				answer += listOfCommands[i]+ " ";
			}
			if (answer == ""){
				throw new InvalidCommandFormatException(CommandType.UPDATE,ParserExceptionConstants.NO_NAME);
			} else{
				resultExecution.setName(answer.substring(0, answer.length()-1));
			}
		}else if (listOfCommands[1].equalsIgnoreCase("description")){
			String answer = "";
			for (int i=2; i<listOfCommands.length; i++){
				answer += listOfCommands[i]+ ",";
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
					throw new InvalidCommandFormatException(CommandType.UPDATE,ParserExceptionConstants.WRONG_TAG_FORMAT);
				}
			}else{
				throw new InvalidCommandFormatException(CommandType.UPDATE,ParserExceptionConstants.WRONG_TAG_FORMAT);
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
				throw new InvalidCommandFormatException(CommandType.UPDATE,ParserExceptionConstants.WRONG_TIME_FORMAT);
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
				throw new InvalidCommandFormatException(CommandType.UPDATE,ParserExceptionConstants.WRONG_TIME_FORMAT);
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
					throw new InvalidCommandFormatException(ParserExceptionConstants.WRONG_PRIORITY_FORMAT);
				}
			}else{
				throw new InvalidCommandFormatException(ParserExceptionConstants.WRONG_PRIORITY_FORMAT);
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
					throw new InvalidCommandFormatException(ParserExceptionConstants.WRONG_TIME_FORMAT);
				}
			}
		}else{
			throw new InvalidCommandFormatException(CommandType.UPDATE, ParserExceptionConstants.WRONG_TIME_FORMAT);
		}
		return resultExecution;
	}
}
