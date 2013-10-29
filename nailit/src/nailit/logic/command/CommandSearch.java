package nailit.logic.command;

import java.util.Vector;

import org.joda.time.DateTime;

import nailit.common.FilterObject;
import nailit.common.Result;
import nailit.common.Task;
import nailit.common.TaskPriority;
import nailit.logic.CommandType;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

/*
 * comment on the situation that the no search content:
 * in this situation, storer will give back all the tasks in the storage.
 * Filtered Tasks will contain all the tasks, but isEmptySearch is true.
 * This means when use the search obj, remember to check isEmptySearch first.
 * 
 */
public class CommandSearch extends Command{
	
	// the filterOnject instance for the search, which contains all the search information
	private FilterObject filterObjForTheSearch;
	
	// vector of tasks retrieved from the storage according to the search requirements
	private Vector<Task> filteredTasks;
	
	private Result executedResult;
	
	// for the command summary
	private String commandSummary;
	
	// check whether has search content
	private boolean isEmptySearch;
	
	private CommandType commandType;
	
	private final static String COMMAND_SUMMARY_CONTENT = "This is a search command. The search content is: "; 
	private final static String SEARCH_NONE_WARNING = "This is a search command, but the search content is none.";
	private final static String SEARCH_NONE_WARNING_FEEDBACK = "Sorry, please tell us what you want to search.";
	private final static String STORAGE_GIVES_NULL_VECTOR = "Storage gives null pointer.";
	private final static String NO_SUITABLE_TASKS_TO_FETCH_FEEDBACK = "There is no tasks in the storage that fits the search conditions.";
	
	public CommandSearch(ParserResult resultInstance, StorageManager storerToUse) {
		super(resultInstance, storerToUse);
		filterObjForTheSearch = new FilterObject();
		filteredTasks = new Vector<Task>();
		executedResult = null;
		commandSummary = "";
		isEmptySearch = true;
		commandType = CommandType.SEARCH;
	}

	@Override
	public Result executeCommand() {
		translateParserResultToFilterObject();
		searchFromStorage();
		if(isEmptySearch) { // no search content, give notification in returned result
			createResultForNoContentParserResult();
		} else {
			createResult();
		}
		createCommandSummary();
		return executedResult;
	}

	private void createResultForNoContentParserResult() {
		executedResult = new Result(false, false, Result.LIST_DISPLAY, SEARCH_NONE_WARNING_FEEDBACK);
	}

	private void createCommandSummary() {
		if(isEmptySearch) {
			commandSummary = SEARCH_NONE_WARNING;
		} else {
			commandSummary = COMMAND_SUMMARY_CONTENT + commandSummary; 
		}
	}

	private void createResult() {
		if(filteredTasks == null) { // it should not happen, but in case
			executedResult = new Result(false, false, Result.LIST_DISPLAY, STORAGE_GIVES_NULL_VECTOR);
		} else {
			if(filteredTasks.isEmpty()) { // meaning that no suitable tasks to fetch, gives back notification
				executedResult = new Result(false, false, Result.LIST_DISPLAY, NO_SUITABLE_TASKS_TO_FETCH_FEEDBACK);
			} else {
				executedResult = new Result(false, true, Result.LIST_DISPLAY, Result.EMPTY_DISPLAY, null, filteredTasks, null);
			}
			
		}
	}

	private void searchFromStorage() {
		filteredTasks = storer.filter(filterObjForTheSearch); // if all the fields in FilterObject is null, then storer search all
	}

	private void translateParserResultToFilterObject() {
		String searchedName = null;
		DateTime searchedST = null;
		DateTime searchedET = null;
		TaskPriority searchedPriority = null;
		String searchedTag = null;
		
		
		
		if (!parserResultInstance.isNullName()) {
			searchedName = parserResultInstance.getName();
			commandSummary = commandSummary + searchedName;
			isEmptySearch = false;
		}
		if (!parserResultInstance.isNullStartTime()) {
			searchedST = parserResultInstance.getStartTime();
			commandSummary = commandSummary + searchedST;
			isEmptySearch = false;
		}
		if (!parserResultInstance.isNullEndTime()) {
			searchedET = parserResultInstance.getEndTime();
			commandSummary = commandSummary + searchedET;
			isEmptySearch = false;
		}
		if (parserResultInstance.isSetPriority()) {
			searchedPriority = parserResultInstance.getPriority();
			commandSummary = commandSummary + searchedPriority;
			isEmptySearch = false;
		}
		if (!parserResultInstance.isNullTag()) {
			searchedTag = parserResultInstance.getTag();
			commandSummary = commandSummary + searchedTag;
			isEmptySearch = false;
		}
		filterObjForTheSearch = new FilterObject(searchedName, searchedST, searchedET, searchedTag, searchedPriority, null);
	}

	@Override
	public int getTaskID() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	// these three functions are used for updating the currentTaskList 
	// and filterObj in CommandManager
	public Vector<Task> getLatestTaskList() {
		return filteredTasks;
	}
	
	public FilterObject getLatestFilteredResult() {
		return filterObjForTheSearch;
	}
	
	public boolean isFilterNothing() {
		return isEmptySearch;
	}
	
	public CommandType getCommandType() {
		return commandType;
	}

	@Override
	public void undo() {
		// nothing to do
	}

	@Override
	public boolean undoSuccessfully() {
		// nothing to do
		return false;
	}

	@Override
	public String getCommandString() {
		return commandSummary;
	}

	@Override
	public void redo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSuccessRedo() {
		// TODO Auto-generated method stub
		return false;
	}
}
