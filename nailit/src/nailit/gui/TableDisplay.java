package nailit.gui;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import nailit.common.Result;
import nailit.common.Task;
import nailit.gui.renderer.TaskStatusDisplayRenderer;
import nailit.gui.renderer.TaskDateTimeDisplayRenderer;
import nailit.gui.renderer.TaskNameDisplayRenderer;

public class TableDisplay extends JScrollPane{
	private static final int TABLE_HEADER_HEIGHT = 40;
	private static final int TABLE_ROW_HEIGHT = 30;
	private final LineBorder FOCUS_LINE_BORDER = new LineBorder(GUIManager.FOCUSED_BORDER_COLOR);
	private final LineBorder UNFOCUS_LINE_BORDER = new LineBorder(GUIManager.BORDER_COLOR);
	private final FocusListener tableDisplayFocusListener = new FocusListener(){
		public void focusGained(FocusEvent event) {
			setBorder(FOCUS_LINE_BORDER);
		 }
		public void focusLost(FocusEvent event){
			setBorder(UNFOCUS_LINE_BORDER);
		}
	};
	
	private int containerHeight;
	private int containerWidth;
	private int tableDisplayType;
	
	private JTable table;
	private Vector<Vector<String>> tableRows;
	private DefaultTableModel tableModel;
	private Vector<String> tableHeaderLabel;
	
	public TableDisplay(int width, int height, int displayType){
		tableDisplayType = displayType;
		configureMainFrame(width, height);
		createAndConfigureTable();
	}
	private void configureMainFrame(int width, int height){
		containerWidth = width;
		containerHeight = height;
		setSize(containerWidth, containerHeight);
		setBorder(UNFOCUS_LINE_BORDER);
		addFocusListener(tableDisplayFocusListener);
	}
	
	private void createAndConfigureTable() {
		initialiseTableStructures();
		setHeaderText();
		configureTable();
	}
	
	private void initialiseTableStructures(){
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
			private final TaskStatusDisplayRenderer statusDisplayRenderer = new TaskStatusDisplayRenderer();
			@Override
			public TableCellRenderer getCellRenderer(int row, int col){
				switch(tableDisplayType){
				case Result.HISTORY_DISPLAY:
					return super.getCellRenderer(row, col);
				case Result.LIST_DISPLAY:
					String colName = GUIManager.ALL_TASKS_TABLE_HEADER[col];
					if(colName.equals(GUIManager.TASK_NAME_COL_NAME)){
						return taskNameRenderer;
					}else if(colName.equals(GUIManager.TASK_TIME_DET_COL_NAME)){
						return taskDateTimeRenderer;
					}else if(colName.equals(GUIManager.TASK_STATUS_COL_NAME)){
						return statusDisplayRenderer;
					}else{
						return super.getCellRenderer(row, col);
					}
				default:
					return super.getCellRenderer(row, col);
				}
			}
		};
	}
	
	private void configureTable() {
		table.setModel(tableModel);
		table.setRowHeight(TABLE_ROW_HEIGHT);
		table.setFocusable(false);
		setRowWidths();
		setViewportView(table);
	}
	
	private void setHeaderText(){
		switch(tableDisplayType){
			case Result.HISTORY_DISPLAY:
				tableHeaderLabel = new Vector<String>(Arrays.asList(GUIManager.COMMAND_HISTORY_HEADER));
				break;
			case Result.LIST_DISPLAY:
				tableHeaderLabel = new Vector<String>(Arrays.asList(GUIManager.ALL_TASKS_TABLE_HEADER));
				break;
			default:
				tableHeaderLabel = new Vector<String>();
				break;
		}
		tableModel.setDataVector(tableRows, tableHeaderLabel);
	}
	
	private void setRowWidths(){
		switch(tableDisplayType){
			case Result.HISTORY_DISPLAY:
				setRowWidths(table, GUIManager.COMMAND_HISTORY_COLUMN_WIDTH);
				break;
			case Result.LIST_DISPLAY:
				setRowWidths(table, GUIManager.TASKS_TABLE_COLUMN_WIDTH);
				break;
			default:
				break;
		}
	}
	
	private void setRowWidths(JTable tab, int[] widths){
		TableColumnModel columnModel = tab.getColumnModel();
		TableColumn column;
		for(int i = 0; i < widths.length; i++){
			column = columnModel.getColumn(i);
			column.setWidth(widths[i]);
			column.setPreferredWidth(widths[i]);
		}
	}
	
	protected void addContentToTable(Task task){
		Vector<String> row = new Vector<String>();
		String ID = "" + task.getID();
		row.add(ID);
		String nameAndTag = TaskNameDisplayRenderer.formatTaskNameCellDisplay(task.getName(), task.getTag());
		row.add(nameAndTag);
		String timeDet = TaskDateTimeDisplayRenderer.formatTaskDateTimeCellDisplay(task);
		row.add(timeDet);
		String status = TaskStatusDisplayRenderer.formatStatusCellDisplay(task);
		row.add(status);
		tableRows.add(row);
	}
	protected void addContentToTable(Vector<String> list){
		@SuppressWarnings("unchecked")
		Vector<String> row = (Vector<String>) list.clone();
		tableRows.add(row);
	}

}
