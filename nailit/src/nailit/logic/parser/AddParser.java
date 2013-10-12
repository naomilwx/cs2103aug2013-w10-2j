package nailit.logic.parser;

import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.common.NIConstants;
import nailit.common.TaskPriority;

public class AddParser extends Parser {

	private String userCommand;
	private String[] listOfCommand;
	
	public AddParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
		listOfCommand = userCommand.split(NIConstants.NORMAL_FIELD_SPLITTER);
		
		resultExecution.setCommand(CommandType.ADD);
		
		for (int i=0; i<listOfCommand.length; i++)
		{
			listOfCommand[i] = listOfCommand[i].trim();
			System.out.println(listOfCommand[i]);
			if (TaskPriority.isTaskPriority(listOfCommand[i])){
				resultExecution.setPriority(TaskPriority.valueOf(listOfCommand[i].toUpperCase()));
			}else if (Parser.isTag(listOfCommand[i])){
				resultExecution.setTag(listOfCommand[i]);
			}else if (Parser.isDateTime(listOfCommand[i])){
				if (resultExecution.getStartTime() == null){
					if (Parser.numberOfTime(listOfCommand[i]) == 2){
						resultExecution.setStartTime(Parser.retrieveDateTimeFirst(listOfCommand[i]));
						resultExecution.setEndTime(Parser.retrieveDateTimeSecond(listOfCommand[i]));
					}else
						resultExecution.setStartTime(Parser.retrieveDateTime(listOfCommand[i]));
				} else 
					resultExecution.setEndTime(Parser.retrieveDateTime(listOfCommand[i]));
			}else 
				resultExecution.setName(listOfCommand[i]);
			
		}
		
		return resultExecution;
	}
}
