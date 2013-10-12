package nailit.logic.parser;

import nailit.common.NIConstants;
import nailit.common.TaskPriority;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class SearchParser extends Parser {

	private String userCommand;
	private String[] listOfCommand;
	
	public SearchParser (String command){
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
				if (resultExecution.getStartTime() == null)
					resultExecution.setStartTime(Parser.retrieveDateTime(listOfCommand[i]));
				else 
					resultExecution.setEndTime(Parser.retrieveDateTime(listOfCommand[i]));
			}else 
				resultExecution.setName(listOfCommand[i]);
			
		}
		
		return resultExecution;
	}
}
