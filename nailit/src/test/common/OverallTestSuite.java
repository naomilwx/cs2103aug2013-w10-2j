package test.common;

import test.logic.LogicManagerTest;
import test.logic.command.CommandTest;
import test.logic.parser.ParserTest;
import test.storage.StorageManagerTest;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	StorageManagerTest.class,
	TaskTest.class,
	TaskTest.class,
	ParserTest.class,
	CommandTest.class,
})

public class OverallTestSuite {
}
