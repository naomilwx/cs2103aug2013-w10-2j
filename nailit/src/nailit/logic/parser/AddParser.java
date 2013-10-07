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
				resultExecution.setPriority(TaskPriority.valueOf(listOfCommand[i]));
			}else if (Parser.isTag(listOfCommand[i])){
				resultExecution.setTag(listOfCommand[i]);
			}else{
				boolean flag = false;
				if (listOfCommand[i].toLowerCase().contains("at")){
			
					int atIndex = listOfCommand[i].toLowerCase().indexOf("at");
					System.out.println(listOfCommand[i]);
					System.out.println(atIndex);
					if (atIndex ==0){
						if (listOfCommand[i].charAt(atIndex+2) == ' '){
							flag = true;
							int stringLength = listOfCommand[i].length();					
							resultExecution.setStartTime(Parser.retrieveDateTime(listOfCommand[i].substring(atIndex,stringLength)));
						}
					}else if ((listOfCommand[i].charAt(atIndex-1) == ' ') && (listOfCommand[i].charAt(atIndex+2) == ' ')){
						flag = true;
						int stringLength = listOfCommand[i].length();					
						resultExecution.setStartTime(Parser.retrieveDateTime(listOfCommand[i].substring(atIndex,stringLength)));
					}
	
				}else if (listOfCommand[i].toLowerCase().contains("from") && listOfCommand[i].toLowerCase().contains("to")){
					int fromIndex = listOfCommand[i].toLowerCase().indexOf("from");
					int toIndex = listOfCommand[i].toLowerCase().indexOf("to");
					int stringLength = listOfCommand[i].length();
					
					System.out.println(fromIndex);
					if (fromIndex == 0)
					{
						if ((fromIndex<toIndex) && (listOfCommand[i].charAt(fromIndex+4) == ' ') 
								&& (listOfCommand[i].charAt(toIndex-1) == ' ') && (listOfCommand[i].charAt(toIndex+2) == ' ')){
							flag = true;
							resultExecution.setStartTime(Parser.retrieveDateTime(listOfCommand[i].substring(fromIndex+4, toIndex)));
							resultExecution.setEndTime(Parser.retrieveDateTime(listOfCommand[i].substring(toIndex+2, stringLength)));
						}
					}else if ((fromIndex<toIndex) && (listOfCommand[i].charAt(fromIndex-1) == ' ') && (listOfCommand[i].charAt(fromIndex+4) == ' ')
				
							&& (listOfCommand[i].charAt(toIndex-1) == ' ') && (listOfCommand[i].charAt(toIndex+2) == ' ')){
						flag = true;
						resultExecution.setStartTime(Parser.retrieveDateTime(listOfCommand[i].substring(fromIndex+4, toIndex)));
						resultExecution.setEndTime(Parser.retrieveDateTime(listOfCommand[i].substring(toIndex+2, stringLength)));}
				}else
					resultExecution.setName(listOfCommand[i]);
				if (flag == false)
					resultExecution.setName(listOfCommand[i]);
			}
			
		}
		
		return resultExecution;
	}
}
