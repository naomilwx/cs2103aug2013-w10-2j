package nailit.logic.parser;

//@author A0105559B
import nailit.common.NIConstants;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;

public class DisplayParser extends Parser {
	
	private String userCommand;
	private String[] listOfCommands;
	
	public DisplayParser (String command){
		userCommand = command;
	}
	
	@Override
	public ParserResult execute(){
		ParserResult resultExecution = new ParserResult();
		listOfCommands = userCommand.split(NIConstants.NORMAL_FIELD_SPLITTER);
	
		resultExecution.setCommand(CommandType.DISPLAY);
		
		if (listOfCommands[0].equalsIgnoreCase("ALL")){
			resultExecution.setDisplayAll(true);
		}else if (listOfCommands[0].equalsIgnoreCase("HISTORY")){
			resultExecution.setDisplayHistory(true);
		}else if (listOfCommands[0].equalsIgnoreCase("COMPLETE")){
			resultExecution.setDisplayComplete(true);
		}if (listOfCommands[0].equalsIgnoreCase("UNCOMPLETE")){
			resultExecution.setDisplayUncomplete(true);
		}else if (Parser.isTaskID(userCommand)){
			resultExecution.setTaskId(Integer.parseInt(userCommand));
		}else if (Parser.isDateTime(userCommand)){
			resultExecution.setEndTime(Parser.retrieveDateTime(userCommand));
		}else
			System.out.println("Wrong Format");
		
		return resultExecution;
	}
}
