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
			if (listOfCommand[i] == LOW)
		}
		return resultExecution;
	}
}
