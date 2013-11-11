package nailit.logic.command;

//@author A0105789R

import java.util.Vector;
import org.joda.time.DateTime;
import nailit.common.CommandType;
import nailit.common.NIConstants;
import nailit.common.Result;
import nailit.common.Task;
import nailit.common.TaskPriority;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

public class CommandUpdate extends Command{
	// static final variables
	private static final String UPDATE_UNSUCCESSFUL_FEEDBACK = "Sorry, Task [ID: %1d] not " +
															"found. Please check and try again.";

	private static final String UPDATE_UNSUCCESSFUL_FEEDBACK_FOR_TASK_NOT_EXIST_IN_CURRENT_LIST = "Sorry, Task [ID: %1d] " +
																						"is not in the current task list. " +
																						"Please check and try again.";

	private static final String EXCEPTION_MESSAGE_FOR_DISPLAY_ID_IS_NULL = "Display ID in the parserResult instance is null.";

	private static final String COMMAND_SUMMARY_FOR_UPDATING_TASK_NOT_IN_TASK_LIST = "The task you are trying to update does not " +
																					"exist in the task list. Please try again.";

	private static final String COMMAND_SUMMARY_FOR_UPDATING_TASK_NOT_IN_STORAGE = "The task you are trying to update does not " +
																					"exist in the storage. Please try again.";

	// private fields
	// previous version of task
	private Task taskRetrieved;
	
	// updated version of task
	private Task updatedTask; 
	
	// for the use of commandSummary
	private String updatedContent;
	
	private Vector<Task> taskList;
	private int taskToRetrieveDisplayId;
	
	private boolean isUpdateSuccessfully;
	private boolean isUndoSuccess;
	private boolean isRedoSuccess;
	
	// constructor
	public CommandUpdate(ParserResult resultInstance, StorageManager storerToUse, Vector<Task> taskList) {
		super(resultInstance, storerToUse);
		updatedContent = "";
		commandType = CommandType.UPDATE;
		this.taskList = taskList;
		updatedTask = new Task();
		isUndoSuccess = false;
		isRedoSuccess = false;
	}

	@Override
	public Result executeCommand() throws Exception {
		taskToRetrieveDisplayId = getDisplayID();
		if(taskToRetrieveDisplayId == 0) { // 0 means no display ID but the original one. Later need changes
			throw new Exception(EXCEPTION_MESSAGE_FOR_DISPLAY_ID_IS_NULL);
		} else {
			try {
				if(isExistToUpdateTaskInTaskList()) {
					retrieveTheTask();
					setTaskID(); // set taskToRetrieveID
				} else {
					markIsUpdateSuccess(false);
					createResultObjectForTaskToUpdateNotExistingInTaskList();
					createCommandSummaryForUpdatingTaskNotInTaskList();
					return executedResult;
				}
				
			} catch(Exception e) {
				markIsUpdateSuccess(false);
				createUnsuccessfulResultObject();
				createCommandSummaryForUpdatingTaskNotInStorage();
				return executedResult;
			}
			markIsUpdateSuccess(true);
			updateTheRetrievedTask();
			addTheUpdatedTaskObjOnStorage();
			createCommandSummary();
			createResultObject();
			return executedResult;
		}
	}
	
	private void markIsUpdateSuccess(boolean isUpdateSuccess) {
		this.isUpdateSuccessfully = isUpdateSuccess;
	}
	
	private void setTaskID() {
		taskId = taskRetrieved.getID();
	}

	private void createCommandSummaryForUpdatingTaskNotInStorage() {
		commandSummary = COMMAND_SUMMARY_FOR_UPDATING_TASK_NOT_IN_STORAGE;
	}

	private void createCommandSummaryForUpdatingTaskNotInTaskList() {
		commandSummary = COMMAND_SUMMARY_FOR_UPDATING_TASK_NOT_IN_TASK_LIST;
	}

	private Result createResultObjectForTaskToUpdateNotExistingInTaskList() {
		String notificationStr = String.format(UPDATE_UNSUCCESSFUL_FEEDBACK_FOR_TASK_NOT_EXIST_IN_CURRENT_LIST, taskToRetrieveDisplayId);
		executedResult = new Result(false, false, Result.NOTIFICATION_DISPLAY, notificationStr);
		return executedResult;
	}

	private boolean isExistToUpdateTaskInTaskList() {
		if(taskList == null) {
			return false;
		} else if(taskList.size() < taskToRetrieveDisplayId) {
			return false;
		} else if(taskToRetrieveDisplayId < 1) {
			return false;
		} else {
			return true;
		}
	}

	public int getDisplayID() {
		// taskId is displayId
		return parserResultInstance.getTaskID();
	}

	private void createCommandSummary() {
		commandSummary = "UPDATE " + taskRetrieved.getName() + ": " + updatedContent;
	}

	private void createResultObject() {
		executedResult = new Result(false, true, Result.EXECUTION_RESULT_DISPLAY, 
				commandSummary, updatedTask, null, null);
	}
	
	// there is no such task record in the storage to display
	private void createUnsuccessfulResultObject() {
		String notificationStr = String.format(UPDATE_UNSUCCESSFUL_FEEDBACK, taskToRetrieveDisplayId);
		executedResult = new Result(false, false, Result.NOTIFICATION_DISPLAY,
									notificationStr);
	}

	private void addTheUpdatedTaskObjOnStorage() {
		// since there is taskID in the taskRetrieved, storer knows that it is update operation
		storer.add(updatedTask);
	}


	private void updateTheRetrievedTask() {
		if(!parserResultInstance.isNullName()) {
			String newName = parserResultInstance.getName();
			updatedTask.setName(newName);
			updatedContent = updatedContent + "[Name] " + newName + " \n";
		} else { // means name use the original one, since no update
			updatedTask.setName(taskRetrieved.getName());
		}
		
		if(parserResultInstance.isStartTimeNull()) { // means user wants to set start time as empty
			updatedTask.setStartTime(null);
			updatedContent = updatedContent + "[Start] " + "empty" + " \n";
		} else {
			if(!parserResultInstance.isNullStartTime()) {
				DateTime newStartTime = parserResultInstance.getStartTime();
				updatedTask.setStartTime(newStartTime);
				updatedContent = updatedContent + "[Start] "+newStartTime.toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT) + " \n";
			} else { // means name use the original one, since no update
				updatedTask.setStartTime(taskRetrieved.getStartTime());
			}
		}
		
		if(parserResultInstance.isEndTimeNull()) {
			updatedTask.setEndTime(null);
			updatedContent = updatedContent + "[End] " + "empty" + " \n";
		} else {
			if(!parserResultInstance.isNullEndTime()) {
				DateTime newEndTime = parserResultInstance.getEndTime();
				updatedTask.setEndTime(newEndTime);
				updatedContent = updatedContent + "[End] " +newEndTime.toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT) + " \n";
			} else { // means name use the original one, since no update
				updatedTask.setEndTime(taskRetrieved.getEndTime());
			}
		}
		
		if(parserResultInstance.isTagNull()) {
			updatedTask.setTag(null);
			updatedContent = updatedContent + "[Tag] " + "empty" + " \n";
		} else {
			if(!parserResultInstance.isNullTag()) {
				 String newTag = parserResultInstance.getTag();
				updatedTask.setTag(newTag);
				updatedContent = updatedContent + "[Tag] " + newTag + " \n";
			} else { // means name use the original one, since no update
				updatedTask.setTag(taskRetrieved.getTag());
			}
		}
		
		if(parserResultInstance.isPriorityNull()) {
			updatedTask.setPriority(TaskPriority.DEFAULT_TASK_PRIORITY);
			updatedContent = updatedContent + "[Priority] " + "Medium" + " \n";
		} else {
			if(!parserResultInstance.isNullPriority()) { // currently use this method, although the name is not suitable
				 TaskPriority newPriority = parserResultInstance.getPriority();
				 updatedTask.setPriority(newPriority);
				 updatedContent = updatedContent + "[Priority] " + newPriority + " ";
			} else { // means name use the original one, since no update
				updatedTask.setPriority(taskRetrieved.getPriority());
			}
		}
		
		if(parserResultInstance.isDescriptionNull()) {
			updatedTask.setDescription(null);
			updatedContent = updatedContent + "[Description] " + "Empty" + " \n";
		} else {
			if(!parserResultInstance.isNullDescription()) {
				String newDesc = parserResultInstance.getDescription();
				updatedTask.setDescription(newDesc);
				updatedContent = updatedContent + "[Description] " + newDesc + " \n";
			} else { // means name use the original one, since no update
				updatedTask.setDescription(taskRetrieved.getDescription());
			}
		}
		
		if(parserResultInstance.isReminderTimeNull()) {
			updatedTask.setReminder(null);
			updatedContent = updatedContent + "[Reminder] " + "Deleted" + " \n";
		} else {
			if(!parserResultInstance.isNullReminderTime()) {
				DateTime newReminder = parserResultInstance.getReminderTime();
				updatedTask.setReminder(newReminder);
				updatedContent = updatedContent + "[Reminder Date] " + newReminder.toString(NIConstants.DISPLAY_FULL_DATETIME_FORMAT) + " \n";
			} else { // means name use the original one, since no update
				// no update for the reminder
				updatedTask.setReminder(taskRetrieved.getReminder());
			}
		}
		
		updatedTask.setID(taskRetrieved.getID());
		updatedTask.setCompleted(taskRetrieved.isCompleted());
	}

	private void retrieveTheTask() {
		taskRetrieved = taskList.get(taskToRetrieveDisplayId - 1);
	}
	
	public int getTaskId() {
		return taskId;
	}

	public boolean updateSuccess() {
		return isUpdateSuccessfully;
	}
	
	public CommandType getCommandType() {
		return commandType;
	}

	@Override
	public void undo() {
		// update back to the previous version
		storer.add(taskRetrieved); 
		isUndoSuccess = true;
		this.isRedoSuccess = false;
	}

	@Override
	public boolean isUndoSuccessfully() {
		return isUndoSuccess;
	}
	
	public Task getRetrievedTask() { // get the previous version of task
		return taskRetrieved; 
	}
	
	public Task getUpdatedTask() {
		return updatedTask;
	}

	@Override
	public String getCommandString() {
		return commandSummary;
	}

	@Override
	public void redo() {
		storer.add(updatedTask);
		isRedoSuccess = true;
		this.isUndoSuccess = false;
	}

	@Override
	public boolean isRedoSuccessfully() {
		return isRedoSuccess;
	}

	@Override
	public Task getTaskRelated() {
		return updatedTask;
	}
}
