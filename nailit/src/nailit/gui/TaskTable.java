//@author A0091372H
package nailit.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import nailit.common.Task;
import nailit.gui.renderer.IDDisplayRenderer;
import nailit.gui.renderer.TaskDateTimeDisplayRenderer;
import nailit.gui.renderer.TaskNameDisplayRenderer;

public class TaskTable extends TableDisplay{
	private int deletedTaskRowsNum = 0;
			
	public TaskTable(int width, int height) {
		super(width, height);
	}
	
	@Override
	protected void initialiseTableStructures(){
		tableModel = new DefaultTableModel(){
			@Override
			public boolean isCellEditable(int row, int col){
				return false;
			}
		};
		tableRows = new Vector<Vector<String>>();
		table = new JTable(){
			private final TaskNameDisplayRenderer taskNameRenderer = new TaskNameDisplayRenderer();
			private final TaskDateTimeDisplayRenderer taskDateTimeRenderer = new TaskDateTimeDisplayRenderer();
			private final IDDisplayRenderer idDisplayRenderer = new IDDisplayRenderer();
			@Override
			public TableCellRenderer getCellRenderer(int row, int col){
				String colName = "";
				colName = GUIManager.ALL_TASKS_TABLE_HEADER[col];
				if(colName.equals(GUIManager.TASK_NAME_COL_NAME)){
					return taskNameRenderer;
				}else if(colName.equals(GUIManager.TASK_START_TIME_COL_NAME)){
					return taskDateTimeRenderer;
				}else if(colName.equals(GUIManager.TASK_END_TIME_COL_NAME)){
					return taskDateTimeRenderer;
				}else if(colName.equals(GUIManager.ID_COL_NAME)){
					return idDisplayRenderer;
				}else{
					return super.getCellRenderer(row, col);
				}
			}
		};
	}
	@Override
	protected int getSelectedRowDisplayID(){
		return table.getSelectedRow() + 1 - deletedTaskRowsNum;
	}
	@Override
	protected void setHeaderText(){
		tableHeaderLabel = new Vector<String>(Arrays.asList(GUIManager.ALL_TASKS_TABLE_HEADER));
		noOfCols = tableHeaderLabel.size();
		tableModel.setDataVector(tableRows, tableHeaderLabel);
	}
	@Override
	protected void setRowWidths(){
		setRowWidths(table, GUIManager.TASKS_TABLE_COLUMN_WIDTH);
	}
	@Override
	protected void addContentToTable(int pos, Vector<String> row){
		super.addContentToTable(pos, row);
		resizeColumnToFitText(pos, GUIManager.TASK_NAME_COLUMN_NUMBER);
	}
	@Override
	protected void addContentToTable(Vector<String> row){
		super.addContentToTable(row);
		resizeColumnToFitText(tableRows.size() - 1, GUIManager.TASK_NAME_COLUMN_NUMBER);
	}
	protected void resizeColumnToFitText(int rowNum, int colNum){
      DefaultTableColumnModel colModel = (DefaultTableColumnModel) table.getColumnModel();
      TableColumn col = colModel.getColumn(colNum);
      int width = col.getWidth();

      TableCellRenderer renderer = table.getCellRenderer(rowNum, colNum);
      Component component = renderer.getTableCellRendererComponent(table, table.getValueAt(rowNum, colNum),
            false, false, rowNum, colNum);
      int newWidth = Math.max(width, component.getPreferredSize().width);
      if(newWidth > width){
	      col.setWidth(newWidth);
	      col.setPreferredWidth(newWidth);
      }
	}
	protected void addDeletedTaskToTable(Task task){
		deletedTaskRowsNum += 1;
		Vector<String> row = formatTaskForRowDisplay(task, GUIManager.DELETED_TASK_DISPLAY_ID);
		addContentToTable(0, row);
	}
	protected void clearDeletedTaskRowsFromTable(){
		while(deletedTaskRowsNum>0){
			tableRows.remove(0);
			deletedTaskRowsNum -= 1;
		}
		revalidate();
	}
	protected Vector<String> formatTaskForRowDisplay(Task task, String IDVal){
		Vector<String> row = new Vector<String>();
		row.add(IDVal);
		String nameAndTag = TaskNameDisplayRenderer.formatTaskNameCellDisplay(task);
		row.add(nameAndTag);
		String timeStartDet = TaskDateTimeDisplayRenderer.formatTaskDateTimeCellDisplay(task.getStartTime());
		row.add(timeStartDet);
		boolean highlightIfOverdue = !task.isEvent() && !task.checkCompleted();
		String timeEndDet = TaskDateTimeDisplayRenderer.formatTaskDateTimeCellDisplay(task.getEndTime(), highlightIfOverdue);
		row.add(timeEndDet);
		return row;
	}
	protected void displayTaskList(Vector<Task> tasks, Task taskToLookOutFor){
		int taskIDToLookOutFor = Task.TASKID_NULL;
		if(taskToLookOutFor != null){
			taskIDToLookOutFor = taskToLookOutFor.getID();
		}
		Vector<String> row;
		for(int i = 0; i < tasks.size(); i++){
			Task currTask = tasks.get(i);
			String IDVal = i+1 + "";
			row = formatTaskForRowDisplay(currTask, IDVal);
			addContentToTable(row);
			if(taskIDToLookOutFor != Task.TASKID_NULL && taskIDToLookOutFor == currTask.getID()){
				table.changeSelection(i, noOfCols, false, false);
				getViewport().setViewPosition(new Point(0, i * TABLE_ROW_HEIGHT));
			}
		}
	}
}
