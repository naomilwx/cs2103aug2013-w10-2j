package nailit.gui;

import java.util.HashMap;
import java.util.Vector;

import nailit.logic.CommandType;

public class HelpWindowConstants {
	public static final String TEXT_DISPLAY_STYLE = "<head><style type = \"text/css\">"
			+ "em {font-weight: bold; }"
			+ "p.title {font-size: 14px; font-weight: bold; text-decoration:underline;}"
			+ "p.subtitle {font-weight: bold; text-decoration:underline;}"
			+ "</style></head>"; 
	public static final String TITLE_TEXT_HTML_FORMAT = "<tr><p class =\"title\"> %1s </p></tr>";
	public static final String SUBTITLE_TEXT_HTML_FORMAT = "<tr><p class =\"subtitle\"> %1s </p></tr>";
	public static final String[] PRIORITY_TYPES = {"low", "medium", "high"};
	public static final String DATE_SYNTAX_LABEL = "date";
	public static final String[] ACCEPTABLE_DATE_SYNTAX = 
		{"Accepted <b>date</b> field formats:", 
		"[end] &ensp"
		+ "|&ensp [start], [end] &ensp"
		+ "|&ensp <em>from</em> [start] <em>to</em> [end]", 
		DATE_SYNTAX_LABEL};
	public static final int COMMAND_DESC_POS = 0;
	public static final int COMMAND_SYNTAX_POS = 1;
	public static final String[] ADD_SYNTAX = {"<b>Add Task</b>", "<em>add</em> [<em>name</em>], [date], #[tag]#, [priority], ([description])"};
	public static final String[] UPDATE_SYNTAX = {"<b>Update Task</b>", "<em>update</em> [<em>ID</em>] [field name] [updated details]"};
	public static final String[] UPDATE_REMINDER_SYNTAX = {"<b>Update Task Reminder</b>", "<em>update</em> [<em>ID</em>] reminder [updated details]"};
	public static final String[] DELETE_SYNTAX = {"<b>Delete Task</b>", "<em>delete</em> [<em>ID</em>]"};
	public static final String[] DISPLAY_SYNTAX = {"<b>Display all tasks</b>", "<em>display all</em>"};
	public static final String[] DISPLAY_TASK_SYNTAX = {"<b>Display Task Details</b>", "<em>display</em> [<em>ID</em>]"};
	public static final String[] DISPLAY_DATE_SYNTAX = {"<b>Display the Day's Task List</b>", "<em>display</em> [<em>date</em>]"};
	public static final String[] DISPLAY_COMPLETE_SYNTAX = {"<b>Display Completed Tasks</b>", "<em>display complete</em>"};
	public static final String[] DISPLAY_UNCOMPLETE_SYNTAX = {"<b>Display Uncompleted Tasks</b>", "<em>display uncomplete</em>"};
	public static final String[] DISPLAY_HISTORY_SYNTAX = {"<b>Display History</b>", "<em>display history</em>"};
	public static final String[] SEARCH_SYNTAX = {"<b>Task Search</b>", "<em>search</em> [name], [date], #[tag]#"};
	public static final String[] COMPLETE_SYNTAX = {"<b>Mark Task As Completed</b>", "<em>complete</em> [<em>ID</em>]"};
	public static final String[] UNCOMPLETE_SYNTAX = {"<b>Unmark Completed Task</b>", "<em>uncomplete</em> [<em>ID</em>]"};
	public static final String[] ADDREM_SYNTAX = {"<b>Add Reminder for Task</b>", "<em>addReminder</em> [<em>ID</em>], [start date]"};
	public static final String[] DELREM_SYNTAX = {"<b>Remove Task Reminder</b>", "<em>delReminder</em> [<em>ID</em>]"};
	
	public static final HashMap<String, Vector<String[]>> COMMAND_SYNTAX_LIST = new HashMap<String, Vector<String[]>>();
	static{
		Vector<String[]> addCommandSyntax = new Vector<String[]>();
		addCommandSyntax.add(ADD_SYNTAX);
		addCommandSyntax.add(ACCEPTABLE_DATE_SYNTAX);
		Vector<String[]> updateCommandSyntax = new Vector<String[]>();
		updateCommandSyntax.add(UPDATE_SYNTAX);
		updateCommandSyntax.add(UPDATE_REMINDER_SYNTAX);
		updateCommandSyntax.add(ACCEPTABLE_DATE_SYNTAX);
		Vector<String[]> deleteCommandSyntax = new Vector<String[]>();
		deleteCommandSyntax.add(DELETE_SYNTAX);
		Vector<String[]> displayCommandSyntax = new Vector<String[]>();
		displayCommandSyntax.add(DISPLAY_SYNTAX);
		displayCommandSyntax.add(DISPLAY_TASK_SYNTAX);
		displayCommandSyntax.add(DISPLAY_DATE_SYNTAX);
		displayCommandSyntax.add(DISPLAY_COMPLETE_SYNTAX);
		displayCommandSyntax.add(DISPLAY_UNCOMPLETE_SYNTAX);
		displayCommandSyntax.add(DISPLAY_HISTORY_SYNTAX);
		Vector<String[]> searchCommandSyntax = new Vector<String[]>();
		searchCommandSyntax.add(SEARCH_SYNTAX);
		searchCommandSyntax.add(ACCEPTABLE_DATE_SYNTAX);
		Vector<String[]> completeCommandSyntax = new Vector<String[]>();
		completeCommandSyntax.add(COMPLETE_SYNTAX);
		Vector<String[]> uncompleteCommandSyntax = new Vector<String[]>();
		uncompleteCommandSyntax.add(UNCOMPLETE_SYNTAX);
		Vector<String[]> addremCommandSyntax = new Vector<String[]>();
		addremCommandSyntax.add(ADDREM_SYNTAX);
		Vector<String[]> delremCommandSyntax = new Vector<String[]>();
		delremCommandSyntax.add(DELREM_SYNTAX);
		
		COMMAND_SYNTAX_LIST.put(CommandType.ADD.toString(), addCommandSyntax);
		COMMAND_SYNTAX_LIST.put(CommandType.UPDATE.toString(), updateCommandSyntax);
		COMMAND_SYNTAX_LIST.put(CommandType.DELETE.toString(), deleteCommandSyntax);
		COMMAND_SYNTAX_LIST.put(CommandType.DISPLAY.toString(), displayCommandSyntax);
		COMMAND_SYNTAX_LIST.put(CommandType.SEARCH.toString(), searchCommandSyntax);
		COMMAND_SYNTAX_LIST.put(CommandType.COMPLETE.toString(), completeCommandSyntax);
		COMMAND_SYNTAX_LIST.put(CommandType.UNCOMPLETE.toString(), uncompleteCommandSyntax);
		COMMAND_SYNTAX_LIST.put(CommandType.ADDREMINDER.toString(), addremCommandSyntax);
		COMMAND_SYNTAX_LIST.put(CommandType.DELETEREMINDER.toString(), delremCommandSyntax);
	}
	
	public static final HashMap<String, String> COMMANDBAR_KEYBOARD_COMMANDS = new HashMap<String, String>();
	static{
		COMMANDBAR_KEYBOARD_COMMANDS.put("Page Down", "Scroll to next page in task list");
		COMMANDBAR_KEYBOARD_COMMANDS.put("Page Up", "Scroll to previous page in task list");
		COMMANDBAR_KEYBOARD_COMMANDS.put("Ctrl+-", "Reduce Main Window Size");
		COMMANDBAR_KEYBOARD_COMMANDS.put("Ctrl+=", "Restore Main Window Size");
		COMMANDBAR_KEYBOARD_COMMANDS.put("Ctrl+N", "Put highlighted task's name in command bar");
		COMMANDBAR_KEYBOARD_COMMANDS.put("Ctrl+D", "Put highlighted task's description in command bar");
		COMMANDBAR_KEYBOARD_COMMANDS.put("Ctrl+H", "Toggle Home Window");
		COMMANDBAR_KEYBOARD_COMMANDS.put("Ctrl+,", "Toggle Main Window");
		COMMANDBAR_KEYBOARD_COMMANDS.put("Ctrl+J", "Toggle History Window");
		COMMANDBAR_KEYBOARD_COMMANDS.put("Ctrl+W", "Hide Task Details Display");
		COMMANDBAR_KEYBOARD_COMMANDS.put("Ctrl+Enter or Shift+Enter", "Add new line in command bar");
		COMMANDBAR_KEYBOARD_COMMANDS.put("Tab", "Set Focus on Display Area");
		COMMANDBAR_KEYBOARD_COMMANDS.put("Ctrl+/", "Display Help Window For all KeyBoard Commands");
	}
	public static final HashMap<String, String> DISPLAYAREA_KEYBOARD_COMMANDS = new HashMap<String, String>();
	static{
		DISPLAYAREA_KEYBOARD_COMMANDS.put("Shift", "Toggle focus between elements in display area");
		DISPLAYAREA_KEYBOARD_COMMANDS.put("ESC", "Set focus on command bar");
		DISPLAYAREA_KEYBOARD_COMMANDS.put("TAB", "Set focus on task table (when focus is on pane with task table");
	}
	public static final HashMap<String, String> TASKTABLE_KEYBOARD_COMMANDS = new HashMap<String, String>();
	static{
		TASKTABLE_KEYBOARD_COMMANDS.put("Down", "Scrolldown");
		TASKTABLE_KEYBOARD_COMMANDS.put("Up", "Scrollup");
		TASKTABLE_KEYBOARD_COMMANDS.put("Enter", "Display Highlighted Task");
		TASKTABLE_KEYBOARD_COMMANDS.put("Del", "Delete Highlighted Task");
		TASKTABLE_KEYBOARD_COMMANDS.put("Ctrl+N", "Put highlighted task's name in command bar");
		TASKTABLE_KEYBOARD_COMMANDS.put("Ctrl+D", "Put highlighted task's description in command bar");
	}
	public static final HashMap<String, String> HELPWINDOW_KEYBOARD_COMMANDS = new HashMap<String, String>();
	static{
		HELPWINDOW_KEYBOARD_COMMANDS.put("ESC", "Close Help Window");
	}
	
	public static final HashMap<String, HashMap<String, String>> ALL_KEYBOARD_COMMANDS = new HashMap<String, HashMap<String, String>>();
	static{
		ALL_KEYBOARD_COMMANDS.put("Command Bar Keyboard Shortcuts", COMMANDBAR_KEYBOARD_COMMANDS);
		ALL_KEYBOARD_COMMANDS.put("Display Area Keyboard Shortcuts", DISPLAYAREA_KEYBOARD_COMMANDS);
		ALL_KEYBOARD_COMMANDS.put("Task Table Keyboard Shortcuts", TASKTABLE_KEYBOARD_COMMANDS);
		ALL_KEYBOARD_COMMANDS.put("Help Window Keyboard Shortcuts", HELPWINDOW_KEYBOARD_COMMANDS);
	}
	public static String generateListOfSupportedCommands(){
		String list = "<center><b>Supported Commands</b><br>";
		for(CommandType type: CommandType.values()){
			if(!type.equals(CommandType.INVALID)){
				list += type.toString().toLowerCase() + " | ";
			}
		}
		return list +"</center>";
	}
}
