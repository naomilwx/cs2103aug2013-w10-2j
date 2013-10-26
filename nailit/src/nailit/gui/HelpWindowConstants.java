package nailit.gui;

import java.util.HashMap;
import java.util.Vector;

import nailit.logic.CommandType;

public class HelpWindowConstants {
	public static final String ACCEPTABLE_DATE_SYNTAX[] = 
		{"from <start date> to <end date>",
		"<start date>, <end date>"
		};
	public static final String[] ADD_SYNTAX = {"Add Task", "add <name>, <date>, #<tag>#, <priority>, [<description>]"};
	public static final String[] UPDATE_SYNTAX = {"Update Task", "update <ID> <field name> <updated details>"};
	public static final String[] DELETE_SYNTAX = {"Delete Task", "delete <ID>"};
	public static final String[] DISPLAY_SYNTAX = {"Display all tasks", "display all"};
	public static final String[] DISPLAY_TASK_SYNTAX = {"Display Task Details", "display <ID>"};
	public static final String[] SEARCH_SYNTAX = {"Task Search", "search <name>, <date>, #<tag>#"};
	public static final String[] COMPLETE_SYNTAX = {"Mark Task As Completed", "complete <ID>"};
	public static final String[] UNCOMPLETE_SYNTAX = {"Unmark Completed Task", "uncomplete <ID>"};
	public static final String[] ADDREM_SYNTAX = {"Add Reminder for Task", "addReminder <ID>, <start date>"};
	public static final String[] DELREM_SYNTAX = {"Remove Task Reminder", "delReminder <ID>"};
	
	public static final HashMap<String, Vector<String[]>> COMMAND_SYNTAX_LIST = new HashMap<String, Vector<String[]>>();
	static{
		Vector<String[]> addCommandSyntax = new Vector<String[]>();
		addCommandSyntax.add(ADD_SYNTAX);
		Vector<String[]> updateCommandSyntax = new Vector<String[]>();
		updateCommandSyntax.add(UPDATE_SYNTAX);
		Vector<String[]> deleteCommandSyntax = new Vector<String[]>();
		deleteCommandSyntax.add(DELETE_SYNTAX);
		Vector<String[]> displayCommandSyntax = new Vector<String[]>();
		displayCommandSyntax.add(DISPLAY_SYNTAX);
		displayCommandSyntax.add(DISPLAY_TASK_SYNTAX);
		Vector<String[]> searchCommandSyntax = new Vector<String[]>();
		searchCommandSyntax.add(SEARCH_SYNTAX);
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
//		COMMAND_SYNTAX_LIST.put(CommandType.DELETEREMINDER.toString(), delremCommandSyntax);
	}
	
	public static final HashMap<String, String> COMMANDBAR_KEYBOARD_COMMANDS = new HashMap<String, String>();
	static{
		COMMANDBAR_KEYBOARD_COMMANDS.put("Ctrl+H", "Toggle Home Window");
		COMMANDBAR_KEYBOARD_COMMANDS.put("Ctrl+,", "Toggle Main Window");
		COMMANDBAR_KEYBOARD_COMMANDS.put("Ctrl+J", "Toggle History Window");
		COMMANDBAR_KEYBOARD_COMMANDS.put("Ctrl+W", "Hide Task Details Display");
		COMMANDBAR_KEYBOARD_COMMANDS.put("Ctrl+Enter or Shift+Enter", "Add new line in command bar");
		COMMANDBAR_KEYBOARD_COMMANDS.put("Tab", "Set Focus on Display Area");
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
		TASKTABLE_KEYBOARD_COMMANDS.put("Del", "Deleted Highlighted Task");
		TASKTABLE_KEYBOARD_COMMANDS.put("Ctrl+Enter", "Put updated task details in command bar");
	}
}
