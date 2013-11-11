package nailit.logic.parser;

// @author A0105559B
public class ParserExceptionConstants {
	public static final String EMPTY_COMMAND_TYPE = "Wrong Format: Your input string cannot be empty";
	public static final String INVLID_COMMAND = "Wrong Format: This is an invalid command";
	public static final String EMPTY_INPUT_STRING_ADD = "Wrong Format: Cannot add an empty task, please specify your task name";
	public static final String EMPTY_INPUT_STRING_ADDREMINDER = "Wrong Format: Cannot add reminder for an empty task, please specify your task name";
	public static final String EMPTY_INPUT_STRING_COMPLETE = "Wrong Format: Cannot set an empty task to be completed, please specify your task name";
	public static final String EMPTY_INPUT_STRING_DELETE = "Wrong Format: Cannot delete an empty task, please specify your task name";
	public static final String EMPTY_INPUT_STRING_DELETEREMINDER = "Wrong Format: Cannot delete reminder an empty task, please specify your task name";
	public static final String EMPTY_INPUT_STRING_DISPLAY = "Wrong Format: Cannot display an empty task, please specify your task name";
	public static final String EMPTY_INPUT_STRING_SEARCH = "Wrong Format: Cannot search an empty task";
	public static final String EMPTY_INPUT_STRING_UNCOMPLETE = "Wrong Format: Cannot set an empty task to be uncompleted, please specify the task ID";
	public static final String EMPTY_INPUT_STRING_UPDATE = "Wrong Format: Wrong Format: Cannot update an empty task, please specify your task ID";
	public static final String EMPTY_INPUT_STRING = "Wrong Format: Your input string cannot be empty";
	public static final String RESERVE_WORD_CLASH = "Wrong Format: Your input string contains reserved string: \"$%\"";
	public static final String BRACKET_UNMATCHED = "Wrong Format: Your brackets are not matched";
	public static final String WRONG_TIME_FORMAT = "Wrong format: The string is not a correct time format";
	public static final String WRONG_TAG_FORMAT = "Wrong format: The string cannot represent tag";
	public static final String WRONG_PRIORITY_FORMAT = "Wrong format: The string cannot represent priority";
	public static final String NO_NAME = "Wrong Format: Your name cannot be empty";
	public static final String NO_TASK_ID = "Wrong Format: Your TaskID cannot be empty";
	public static final String INVALID_STRING = "Wrong Format: The string cannot be identified.";
}
