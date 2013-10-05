package nailit.logic.parser;

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
		listOfCommand = userCommand.split(NIConstants.FIELD_SPLITTER);
		
		for (int i=0; i<listOfCommand.length; i++)
		{
			if (TaskPriority.isTaskPriority(listOfCommand[i])){
				resultExecution.setPriority(TaskPriority.valueOf(listOfCommand[i]));
			}else if (Parser.isTag(listOfCommand[i])){
				resultExecution.setTag(listOfCommand[i]);
			}else{
				if (userCommand.toLowerCase().contains("at")){
					int atIndex = userCommand.toLowerCase().indexOf("at");
					int stringLength = userCommand.length();
					
					resultExecution.setName(userCommand.substring(0,atIndex));
					resultExecution.setStartTime(Parser.retrieveDateTime(userCommand.substring(atIndex,stringLength)));
				}
				if (userCommand.toLowerCase().contains("from") && userCommand.toLowerCase().contains("to")){
					int fromIndex = userCommand.toLowerCase().indexOf("from");
					int toIndex = userCommand.toLowerCase().indexOf("to");
					int stringLength = userCommand.length();
					
					resultExecution.setName(userCommand.substring(0,fromIndex));
					resultExecution.setStartTime(Parser.retrieveDateTime(userCommand.substring(fromIndex+4, toIndex)));
					resultExecution.setStartTime(Parser.retrieveDateTime(userCommand.substring(toIndex+2, stringLength)));
				}
			}
		}
		return resultExecution;
	}
}
