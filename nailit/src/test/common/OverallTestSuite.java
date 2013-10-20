package test.common;

import test.logic.LogicManagerTest;
import test.storage.StorageManagerTest;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	LogicManagerTest.class,
	StorageManagerTest.class
})

public class OverallTestSuite {
}
