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
		listOfCommand = userCommand.split(NIConstants.NORMAL_FIELD_SPLITTER);
		
		for (int i=0; i<listOfCommand.length; i++)
		{
			listOfCommand[i] = listOfCommand[i].trim();
			if (TaskPriority.isTaskPriority(listOfCommand[i])){
				resultExecution.setPriority(TaskPriority.valueOf(listOfCommand[i]));
			}else if (Parser.isTag(listOfCommand[i])){
				resultExecution.setTag(listOfCommand[i]);
			}else{
				if (listOfCommand[i].toLowerCase().contains("at")){
					int atIndex = listOfCommand[i].toLowerCase().indexOf("at");
					int stringLength = listOfCommand[i].length();
					
					resultExecution.setStartTime(Parser.retrieveDateTime(listOfCommand[i].substring(atIndex,stringLength)));
				}else if (listOfCommand[i].toLowerCase().contains("from") && listOfCommand[i].toLowerCase().contains("to")){
					int fromIndex = listOfCommand[i].toLowerCase().indexOf("from");
					int toIndex = listOfCommand[i].toLowerCase().indexOf("to");
					int stringLength = listOfCommand[i].length();
			
					resultExecution.setStartTime(Parser.retrieveDateTime(listOfCommand[i].substring(fromIndex+4, toIndex)));
					resultExecution.setEndTime(Parser.retrieveDateTime(listOfCommand[i].substring(toIndex+2, stringLength)));
				}else
					resultExecution.setName(listOfCommand[i]);
			}
		}
		
		//System.out.println(resultExecution.getPriority());
		//System.out.println(resultExecution.getName());
		//System.out.println(resultExecution.getTag());
		//System.out.println(resultExecution.getStartTime().toString(NIConstants.DISPLAY_DATE_FORMAT));
		return resultExecution;
	}
}
