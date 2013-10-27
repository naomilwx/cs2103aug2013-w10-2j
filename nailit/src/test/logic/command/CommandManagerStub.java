package test.logic.command;

import java.util.Vector;

import test.storage.StorageManagerFuncStub;
import nailit.common.Task;
import nailit.logic.command.CommandManager;
import nailit.storage.FileCorruptionException;

public class CommandManagerStub extends CommandManager{

	public CommandManagerStub() throws FileCorruptionException {
		super();
		storer = new StorageManagerFuncStub();
		// TODO Auto-generated constructor stub
	}
	public Vector<Task> getTaskList(){
		return storer.retrieveAll();
	}
}
