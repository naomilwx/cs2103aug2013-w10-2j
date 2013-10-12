package nailit.logic.command;

import java.util.Vector;

import nailit.common.FilterObject;
import nailit.common.Result;
import nailit.common.Task;
import nailit.logic.ParserResult;
import nailit.storage.StorageManager;

public class CommandSearch extends Command{

	public CommandSearch(ParserResult resultInstance, StorageManager storerToUse) {
		super(resultInstance, storerToUse);
		// TODO Auto-generated constructor stub
	}

	public CommandSearch(StorageManager storer) {
		super(null, storer);
	}

	@Override
	public Result executeCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTaskID() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Vector<Task> search(FilterObject filterContentForCurrentTaskList) {
		// TODO Auto-generated method stub
		return null;
	}

}
