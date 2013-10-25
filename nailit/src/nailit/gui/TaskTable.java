package nailit.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import nailit.common.Result;
import nailit.common.Task;
import nailit.gui.renderer.IDDisplayRenderer;
import nailit.gui.renderer.TaskDateTimeDisplayRenderer;
import nailit.gui.renderer.TaskNameDisplayRenderer;

public class TaskTable extends TableDisplay{
	private int deletedTaskRowsNum = 0;
	
	public TaskTable(int width, int height, int displayType) {
		super(width, height, displayType);
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
	protected void setHeaderText(){
		tableHeaderLabel = new Vector<String>(Arrays.asList(GUIManager.ALL_TASKS_TABLE_HEADER));
		noOfCols = tableHeaderLabel.size();
		tableModel.setDataVector(tableRows, tableHeaderLabel);
	}
	@Override
	protected void setRowWidths(){
		setRowWidths(table, GUIManager.TASKS_TABLE_COLUMN_WIDTH);
	}
	protected void addDeletedTaskToTable(Vector<String> row){
		deletedTaskRowsNum += 1;
		addContentToTable(0, row);
	}
	protected void clearDeletedTaskRowsFromTable(){
		while(deletedTaskRowsNum>0){
			tableRows.remove(0);
			deletedTaskRowsNum -= 1;
		}
		revalidate();
	}
}
