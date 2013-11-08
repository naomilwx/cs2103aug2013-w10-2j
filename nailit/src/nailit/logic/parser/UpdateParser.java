package nailit.logic.parser;

//@author A0105559B
import nailit.common.NIConstants;
import nailit.common.TaskPriority;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class UpdateParser extends Parser {

	private String userCommand;
	private String[] listOfCommands;
	
	public UpdateParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
		
		resultExecution.setCommand(CommandType.UPDATE);
		listOfCommands = userCommand.split(NIConstants.UPDATE_FIELD_SPLITTER);
		
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
				resultExecution.setName(null);
			} else{
				resultExecution.setName(answer.substring(0, answer.length()-1));
			}
		}else if (listOfCommands[1].equalsIgnoreCase("description")){
			String answer = "";
			for (int i=2; i<listOfCommands.length; i++){
				answer += listOfCommands[i]+ " ";
			}
			if (answer == ""){
				resultExecution.setDescription(null);
			} else{
				resultExecution.setDescription(answer.substring(0, answer.length()-1));
			}
		}else if (listOfCommands[1].equalsIgnoreCase("tag")){
			if (listOfCommands.length<=2){
				resultExecution.setTag(null);
			}else{
				resultExecution.setTag(listOfCommands[2]);
			}
		}else if (listOfCommands[1].equalsIgnoreCase("Start")){
			String answer = "";
			for (int i=2; i<listOfCommands.length; i++){
				answer += listOfCommands[i]+" ";
			}
			if (answer == ""){
				resultExecution.setStartTime(null);
			}else{
				resultExecution.setStartTime(Parser.retrieveDateTime(answer.substring(0, answer.length()-1)));
			}
		}else if (listOfCommands[1].equalsIgnoreCase("End")|| listOfCommands[1].equalsIgnoreCase("Due")){
			String answer = "";
			for (int i=2; i<listOfCommands.length; i++){
				answer += listOfCommands[i]+" ";
			}
			if (answer == ""){
				resultExecution.setEndTime(null);
			}else{
				resultExecution.setEndTime(Parser.retrieveDateTime(answer.substring(0, answer.length()-1)));
			}
		}else if (listOfCommands[1].equalsIgnoreCase("Time")){
			String answer = "";
			for (int i=2; i<listOfCommands.length; i++){
				answer += listOfCommands[i]+" ";
			}
			if (answer == ""){
				resultExecution.setEndTime(null);
				resultExecution.setStartTime(null);
			}else{
				resultExecution.setStartTime(Parser.retrieveDateTimeFirst(answer));
				resultExecution.setEndTime(Parser.retrieveDateTimeSecond(answer));
			}
		}else if (listOfCommands[1].equalsIgnoreCase("Priority")){
			if (listOfCommands.length<=2){
				resultExecution.resetPriority();
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
				resultExecution.setReminderTime(null);
			}else{
				resultExecution.setReminderTime(Parser.retrieveDateTime(answer.substring(0, answer.length()-1)));
			}
		}
		System.out.println(resultExecution.getEndTime());
		return resultExecution;
	}
}
