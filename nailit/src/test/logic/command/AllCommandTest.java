package test.logic.command;

//@author A0105789R

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	CommandAddAndDeleteReminderTest.class,
	CommandAddTest.class,
	CommandCompleteAndUncompleteTest.class,
	CommandDeleteTest.class,
	CommandSearchTest.class,
	CommandUpdateTest.class,
	RedoAfterUndoAfterAdd.class,
	RedoAfterUndoAfterDelete.class,
	RedoAfterUndoAfterUpdate.class,
	UndoAfterAddTest.class,
	UndoAfterDeleteTest.class,
	UndoAfterUpdateTest.class,
})
public class AllCommandTest {
}
