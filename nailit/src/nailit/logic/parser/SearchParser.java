package nailit.logic.parser;

//@author A0105559B
import nailit.common.NIConstants;
import nailit.common.TaskPriority;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class SearchParser extends Parser {

	private String userCommand;
	private String[] listOfCommands;
	
	public SearchParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
		listOfCommands = userCommand.split(NIConstants.NORMAL_FIELD_SPLITTER);
		
		resultExecution.setCommand(CommandType.SEARCH);
		
		for (int i=0; i<listOfCommands.length; i++)
		{
			listOfCommands[i] = listOfCommands[i].trim();
			System.out.println(listOfCommands[i]);
			if (TaskPriority.isTaskPriority(listOfCommands[i])){
				resultExecution.setPriority(TaskPriority.valueOf(listOfCommands[i].toUpperCase()));
			}else if (Parser.isTag(listOfCommands[i])){
				resultExecution.setTag(listOfCommands[i]);
			}else if (Parser.isDateTime(listOfCommands[i])){
				if (resultExecution.getStartTime() == null){
					if (Parser.numberOfTime(listOfCommands[i]) == 2){
						resultExecution.setStartTime(Parser.retrieveDateTimeFirst(listOfCommands[i]));
						resultExecution.setEndTime(Parser.retrieveDateTimeSecond(listOfCommands[i]));
					}else
						resultExecution.setStartTime(Parser.retrieveDateTime(listOfCommands[i]));
				} else 
					resultExecution.setEndTime(Parser.retrieveDateTime(listOfCommands[i]));
			}else 
				resultExecution.setName(listOfCommands[i]);
			
		}
		
		if (!resultExecution.isNullStartTime() && resultExecution.isNullEndTime()){
			resultExecution.setEndTime(resultExecution.getStartTime());
			resultExecution.setStartTime(null);
		}
		
		return resultExecution;
	}
}
