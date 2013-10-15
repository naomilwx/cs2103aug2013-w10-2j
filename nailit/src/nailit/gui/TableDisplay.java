package nailit.gui;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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

public class TableDisplay extends ScrollableFocusableDisplay{
	private static final int TABLE_HEADER_HEIGHT = 40;
	private static final int TABLE_ROW_HEIGHT = 30;
	private static final int NO_SELECTED_ROW = -1;
	
	private int containerHeight;
	private int containerWidth;
	private int tableDisplayType;
	
	private JTable table;
	private Vector<Vector<String>> tableRows;
	private DefaultTableModel tableModel;
	private Vector<String> tableHeaderLabel;
	private TableDisplay selfRef = this; //for tableKeyEventListener
	
	private int selectedRow = NO_SELECTED_ROW;
	private int noOfCols;
	
	private KeyAdapter tableKeyEventListener = new KeyAdapter(){
		@Override
		public void keyPressed(KeyEvent keyStroke){
			int keyCode = keyStroke.getKeyCode();
			if(keyCode == KeyEvent.VK_SHIFT){
				selectedRow = NO_SELECTED_ROW;
				table.clearSelection();
				selfRef.requestFocus();
			}else if(keyCode == KeyEvent.VK_TAB){
				int nextSelectedRow = NO_SELECTED_ROW;
				if(selectedRow == NO_SELECTED_ROW){
					nextSelectedRow = 0;
				}else{
					nextSelectedRow = selectedRow + 1;
				}
				if(nextSelectedRow >= tableRows.size()){
					if(tableRows.isEmpty()){
						table.getParent().requestFocus();
					}else{
						selectedRow = 0;
						table.changeSelection(selectedRow, noOfCols, false, false);
					}
				}else{
					selectedRow = nextSelectedRow;
					table.changeSelection(selectedRow, noOfCols, false, false);
				}
			}
		}
	};
	private KeyAdapter moreTableMainFrameKeyEventListener = new KeyAdapter(){
		@Override
		public void keyPressed(KeyEvent keyStroke){
			int keyCode = keyStroke.getKeyCode();
			if(keyCode == KeyEvent.VK_TAB){
				table.requestFocus();
			}
		}
	};
	
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
		addFocusListener(displayFocusListener);
		addKeyListener(moreTableMainFrameKeyEventListener);
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
		table.addKeyListener(tableKeyEventListener);
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
		noOfCols = tableHeaderLabel.size();
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
	
	protected void addContentToTable(Vector<String> row){
		tableRows.add(row);
	}

}
