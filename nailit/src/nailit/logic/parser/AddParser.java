package nailit.logic.parser;

import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.common.NIConstants;
import nailit.common.TaskPriority;

public class AddParser extends Parser {

	private String userCommand;
	private String[] listOfCommand;
	private String name="";
	
	public AddParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
		
		resultExecution.setCommand(CommandType.ADD);
		if (userCommand.equals(""))
			throw new Error("Wrong Format");
		int startIndex = -1, endIndex = userCommand.length();
		for (startIndex=0; startIndex<userCommand.length(); startIndex++){
			if (userCommand.charAt(startIndex) == '(' && userCommand.substring(startIndex).contains(")")){
				break;
			}
		}
		if (startIndex!=-1 && startIndex != userCommand.length()-1){
			for (int i=startIndex+1; i<userCommand.length(); i++)
				if (userCommand.charAt(i)==')'){
					endIndex = i;
				}
		}
		if (endIndex == userCommand.length())
			throw new Error ("Wrong Format: Bracket is not matched");
		resultExecution.setDescription(userCommand.substring(startIndex+1, endIndex));
		userCommand = userCommand+" ";
		userCommand = userCommand.substring(0,startIndex)+userCommand.substring(endIndex+1,userCommand.length());
		
		listOfCommand = userCommand.split(NIConstants.NORMAL_FIELD_SPLITTER);
		for (int i=0; i<listOfCommand.length; i++) {
			listOfCommand[i] = listOfCommand[i].trim();
			System.out.println(listOfCommand[i]);
			if (TaskPriority.isTaskPriority(listOfCommand[i])) {
				resultExecution.setPriority(TaskPriority.valueOf(listOfCommand[i].toUpperCase()));
			}else if (Parser.isTag(listOfCommand[i])) {
				resultExecution.setTag(listOfCommand[i]);
			}else if (Parser.isDateTime(listOfCommand[i])) {
				if (resultExecution.getStartTime() == null) {
					if (Parser.numberOfTime(listOfCommand[i]) == 2) {
						resultExecution.setStartTime(Parser.retrieveDateTimeFirst(listOfCommand[i]));
						resultExecution.setEndTime(Parser.retrieveDateTimeSecond(listOfCommand[i]));
					} else {
						resultExecution.setStartTime(Parser.retrieveDateTime(listOfCommand[i]));
					}
				} else { 
					resultExecution.setEndTime(Parser.retrieveDateTime(listOfCommand[i]));
				}
			} else 
				name+= listOfCommand[i];
		}

		if (name.equals("")){
			throw new Error ("Wrong format: No name");
		}
		resultExecution.setName(name);
		return resultExecution;
	}
}
