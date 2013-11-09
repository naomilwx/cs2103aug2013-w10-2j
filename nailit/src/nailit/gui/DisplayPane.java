package nailit.gui;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import nailit.common.Result;
import nailit.common.Task;
import nailit.gui.renderer.TaskDetailsFormatter;

@SuppressWarnings("serial")
public class DisplayPane extends JPanel{
	private static final int NULL_FOCUS = -1;
	
	private GUIManager GUIBoss;
	private TaskTable taskTable;
	private TextDisplay textDisplay;
	private int lastDisplayedTaskID = Task.TASKID_NULL;
	
	
	public DisplayPane(final GUIManager GUIMain){
		GUIBoss = GUIMain;
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setFocusable(true);
		addFocusListener(displayPaneFocusListener);
	}
	
	private int currentFocusElement = NULL_FOCUS;
	private final FocusListener displayPaneFocusListener = new FocusListener(){
		public void focusGained(FocusEvent event) {
			displayPaneSetFocusHandler();
		 }
		public void focusLost(FocusEvent event){
		}
	};
	private KeyAdapter keyEventListener = new KeyAdapter(){
		boolean ctrlPressed = false;
		@Override
		public void keyPressed(KeyEvent keyStroke){
			int keyCode = keyStroke.getKeyCode();
			if(keyCode == KeyEvent.VK_CONTROL){
				ctrlPressed = true;
			}else if(ctrlPressed && keyCode == KeyEvent.VK_TAB){
				displayPaneSetFocusHandler();
			}else if(keyCode == KeyEvent.VK_ESCAPE){
				GUIBoss.setFocusOnCommandBar();
			}else if(keyCode == KeyEvent.VK_COMMA){
				GUIBoss.setVisible(false);
			}else if(keyCode == KeyEvent.VK_H){
				GUIBoss.toggleHomeWindow();
			}
		}
		@Override
		public void keyReleased(KeyEvent keyStroke){
			int keyCode = keyStroke.getKeyCode();
			if(keyCode == KeyEvent.VK_CONTROL){
				ctrlPressed = false;
			}
		}
	};
	//
	
	private void displayPaneSetFocusHandler(){
		int nextFocusElement = NULL_FOCUS;
		if(currentFocusElement == NULL_FOCUS){
			nextFocusElement = 0;
		}else{
			nextFocusElement = currentFocusElement + 1;
		}
		//toggle between table and task display
		Component[] components = getComponents();
		if(components.length == 0){
			currentFocusElement = NULL_FOCUS;
			GUIBoss.setFocusOnCommandBar();
		}else{
			if(components.length <= nextFocusElement){
				nextFocusElement = 0;
			}
			components[nextFocusElement].requestFocus();
			currentFocusElement = nextFocusElement;
		}
	}
	
	private void addContent(Component component){
		component.addKeyListener(keyEventListener);
		component.setFocusTraversalKeysEnabled(false);
		add(component);
	}
	private void addContent(Component component, int pos){
		component.addKeyListener(keyEventListener);
		component.setFocusTraversalKeysEnabled(false);
		add(component, pos);
	}
	
	private void clearContents(){
		removeAll();
		textDisplay = null;
		taskTable = null;
		lastDisplayedTaskID = Task.TASKID_NULL;
	}
	protected void displayTaskList(Result result){
		Vector<Task>  tasks = result.getTaskList();
		Task task = result.getTaskToDisplay();
		if(tasks == null){
			return;
		}
		addNewTaskTable();
		displayTaskListInTaskTable(tasks, task);
		revalidate();
	}
	private void addNewTaskTable(){
		clearContents();
		createAndConfigureTaskTable();
	}
	private void displayTaskListInTaskTable(Vector<Task>  tasks, Task task){
		taskTable.displayTaskList(tasks, task);
	}
	private void createAndConfigureTaskTable(){
		taskTable = new TaskTable(getWidth(), getHeight());
		addAdditionalKeyListenerToTaskTable();
		addContent(taskTable);
	}
	protected void displayExecutionResultDisplay(Result result){
		int currDisplayTask = lastDisplayedTaskID;
		displayTaskList(result);
		Task taskDisp = result.getTaskToDisplay();
		if(result.getDeleteStatus() == true){
			showDeletedTaskInTaskListTable(taskDisp);
		}else{
			if(taskDisp != null && taskDisp.getID() ==  currDisplayTask){
				displayTaskDetails(taskDisp);
				lastDisplayedTaskID =  currDisplayTask;
			}
		}
	}
	protected void displayTaskDetails(Task task){
		if(task!=null){
			addNewTextDisplay();
			displayTaskInTextDisplay(task);
			revalidate();
			lastDisplayedTaskID = task.getID();
		}else{
			lastDisplayedTaskID = Task.TASKID_NULL;
		}
	}
	private void addNewTextDisplay(){
		removeExistingTextDisplay();
		createAndConfigureNewTextDisplay();
	}
	private void displayTaskInTextDisplay(Task task){
		String details = TaskDetailsFormatter.formatTaskForDisplay(task);
		textDisplay.displayHTMLFormattedText(details);
		GUIUtilities.scrollDisplayToTop(textDisplay);
	}
	private void createAndConfigureNewTextDisplay(){
		int textDisplayWidth = getWidth();
		int textDisplayHeight = getHeight();
		textDisplay = new TextDisplay(textDisplayWidth, textDisplayHeight);
		addContent(textDisplay, 0);
	}
	private void removeExistingTextDisplay(){
		if(textDisplay != null){
			remove(textDisplay);
			textDisplay = null;
		}
	}
	protected void removeTaskDisplay(){
		removeExistingTextDisplay();
		revalidate();
	}
	protected void showDeletedTaskInTaskListTable(Task task){
		if(task != null){
			taskTable.addDeletedTaskToTable(task);
		}
	}
	protected void removeDeletedTasksFromTaskListTable(){
		if(taskTable != null){
			taskTable.clearDeletedTaskRowsFromTable();
		}
	}
	
	protected void quickTaskTableScroll(boolean up){
		if(taskTable != null){
			if(up){
				taskTable.quickScrollUp();
			}else{
				taskTable.quickScrollDown();
			}
		}
	}
	protected void taskTableScroll(boolean up){
		if(taskTable != null){
			if(up){
				taskTable.scrollUp();
			}else{
				taskTable.scrollDown();
			}
		}
	}
	protected void scrollToTaskDisplayID(int ID){
		if(taskTable != null){
			taskTable.selectAndScrollToRow(0, ID - 1);
		}
	}
	private void addAdditionalKeyListenerToTaskTable(){
		KeyAdapter taskTableKeyEventListener = new KeyAdapter(){
			private boolean ctrlPressed = false;
			@Override
			public void keyPressed(KeyEvent keyStroke){
				int keyCode = keyStroke.getKeyCode();
				if(keyCode == KeyEvent.VK_CONTROL){
					ctrlPressed = true;
				}else if(ctrlPressed && keyCode == KeyEvent.VK_D){
					ctrlPressed = false;
					taskTableOnCtrlDEvent();
				}else if(ctrlPressed && keyCode == KeyEvent.VK_N){
					taskTableOnCtrlNEvent();
				}else if(keyCode == KeyEvent.VK_ENTER){
					taskTableOnEnterEvent();
				}else if(keyCode == KeyEvent.VK_DELETE){
					taskTableOnDeleteEvent();
				}else if(keyCode == KeyEvent.VK_COMMA){
					GUIBoss.setVisible(false);
				}else if(keyCode == KeyEvent.VK_H){
					GUIBoss.toggleHomeWindow();
				}
			}
			@Override
			public void keyReleased(KeyEvent keyStroke){
				int keyCode = keyStroke.getKeyCode();
				if(keyCode == KeyEvent.VK_CONTROL){
					ctrlPressed = false;
				}
			}
		};
		taskTable.addKeyListenerToTable(taskTableKeyEventListener);
	}
	protected int getTaskTableSelectedRowID(){
		if(taskTable != null){
			return taskTable.getSelectedRowDisplayID();
		}else{
			return -1;
		}
	}
	private void taskTableOnDeleteEvent() {
		GUIBoss.setFocusOnCommandBar();
		int displayID = taskTable.getSelectedRowDisplayID();
		if(displayID >= 1){
			GUIBoss.executeTriggeredTaskDelete(displayID);
		}
	}
	protected void taskTableOnEnterEvent(){
		GUIBoss.setFocusOnCommandBar();
		int displayID = taskTable.getSelectedRowDisplayID();
		if(displayID >= 1){
			GUIBoss.executeTriggeredTaskDisplay(displayID);
		}
	}
	protected void taskTableOnCtrlDEvent(){
		GUIBoss.setFocusOnCommandBar();
		int displayID = taskTable.getSelectedRowDisplayID();
		if(displayID >= 1){
			GUIBoss.loadExistingTaskDescriptionInCommandBar(displayID);
		}
	}
	protected void taskTableOnCtrlNEvent(){
		GUIBoss.setFocusOnCommandBar();
		int displayID = taskTable.getSelectedRowDisplayID();
		if(displayID >= 1){
			GUIBoss.loadExistingTaskNameInCommandBar(displayID);
		}
	}
}
