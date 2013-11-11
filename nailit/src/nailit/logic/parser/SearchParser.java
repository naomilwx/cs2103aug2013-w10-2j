package nailit.logic.parser;

//@author A0105559B
import nailit.common.CommandType;
import nailit.common.NIConstants;
import nailit.common.TaskPriority;
import nailit.logic.ParserResult;
import nailit.logic.exception.InvalidCommandFormatException;

public class SearchParser extends Parser {

	private String userCommand;
	private String[] listOfCommands;
	
	public SearchParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute() throws InvalidCommandFormatException{
		ParserResult resultExecution = new ParserResult();
		listOfCommands = userCommand.split(NIConstants.NORMAL_FIELD_SPLITTER);
		// Set the commandType
		resultExecution.setCommand(CommandType.SEARCH);
		// Check whether the input string is empty
		if (userCommand.equals("")){
			throw new InvalidCommandFormatException(CommandType.SEARCH,ParserExceptionConstants.EMPTY_INPUT_STRING_SEARCH);
		}
		
		for (int i=0; i<listOfCommands.length; i++)
		{
			listOfCommands[i] = listOfCommands[i].trim();
			// search for priority
			if (TaskPriority.isTaskPriority(listOfCommands[i])){
				resultExecution.setPriority(TaskPriority.valueOf(listOfCommands[i].toUpperCase()));
			// search for tag
			}else if (Parser.isTag(listOfCommands[i])){
				resultExecution.setTag(listOfCommands[i]);
			// search for time
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
