package nailit.logic.command;

//@author A0105789R

import java.util.Vector;

import org.joda.time.DateTime;

import nailit.common.CommandType;
import nailit.common.FilterObject;
import nailit.common.NIConstants;
import nailit.common.Result;
import nailit.common.Task;
import nailit.common.TaskPriority;
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
	// static final fields
	private final static String COMMAND_SUMMARY_CONTENT = "This is a search " +
			"command. The search content is: ";

	private final static String SEARCH_NONE_WARNING = "This is a search command, " +
			"but the search content is none.";
	
	private final static String SEARCH_NONE_WARNING_FEEDBACK = "Sorry, please tell " +
					"us what you want to search.";
	
	private final static String STORAGE_GIVES_NULL_VECTOR = "Storage gives null pointer.";
	
	private final static String NO_SUITABLE_TASKS_TO_FETCH_FEEDBACK = "No tasks matching the conditions: ";
	private static final String PARAMETER_SEPARATER = "; ";
	
	// private fields
	// the filterOnject instance for the search, which contains all the search information
	private FilterObject filterObjForTheSearch;
	
	// vector of tasks retrieved from the storage according to the search requirements
	private Vector<Task> filteredTasks;
	
	// check whether has search content
	private boolean isEmptySearch;
	
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
				executedResult = new Result(false, false, Result.LIST_DISPLAY, NO_SUITABLE_TASKS_TO_FETCH_FEEDBACK + commandSummary);
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
			commandSummary = commandSummary + searchedName + PARAMETER_SEPARATER;
			isEmptySearch = false;
		}
		if (!parserResultInstance.isNullStartTime()) {
			searchedST = parserResultInstance.getStartTime();
			commandSummary = commandSummary + "before " 
			+ searchedST.toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT) + PARAMETER_SEPARATER;
			isEmptySearch = false;
		}
		if (!parserResultInstance.isNullEndTime()) {
			searchedET = parserResultInstance.getEndTime();
			commandSummary = commandSummary + "after "
			+ searchedET.toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT) + PARAMETER_SEPARATER;
			isEmptySearch = false;
		}
		if (!parserResultInstance.isNullPriority()) {
			searchedPriority = parserResultInstance.getPriority();
			commandSummary = commandSummary + searchedPriority + ". ";
			isEmptySearch = false;
		}
		if (!parserResultInstance.isNullTag()) {
			searchedTag = parserResultInstance.getTag();
			commandSummary = commandSummary + searchedTag + PARAMETER_SEPARATER;
			isEmptySearch = false;
		}
		filterObjForTheSearch = new FilterObject(searchedName, searchedST, searchedET, searchedTag, searchedPriority, null);
	}

	@Override
	public int getTaskId() {
		// not used in this class
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
	public boolean isUndoSuccessfully() {
		// nothing to do
		return false;
	}

	@Override
	public String getCommandString() {
		return commandSummary;
	}

	@Override
	public void redo() {
		// nothing to do
	}

	@Override
	public boolean isRedoSuccessfully() {
		// nothing to do
		return false;
	}

	@Override
	public Task getTaskRelated() {
		// Nothing to do
		return null;
	}
}
