package nailit.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ComponentInputMap;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import nailit.common.Result;
import nailit.common.Task;
import nailit.gui.renderer.TableHeaderRenderer;
import nailit.gui.renderer.TaskDateTimeDisplayRenderer;
import nailit.gui.renderer.TaskNameDisplayRenderer;
import nailit.gui.renderer.IDDisplayRenderer;

public class TableDisplay extends ScrollableFocusableDisplay{
	private static final int TABLE_HEADER_HEIGHT = 35;
	private static final int TABLE_ROW_HEIGHT = 50;
	private static final int NO_SELECTED_ROW = -1;
	private static final int SINGLE_SCROLLDOWN = 1;
	private static final int SINGLE_SCROLLUP = -1;
	private static final int TAB_SCROLL_OFFSET = 5;
	
	private int containerHeight;
	private int containerWidth;
	private int tableDisplayType;
	
	private JTable table;
	private Vector<Vector<String>> tableRows;
	private DefaultTableModel tableModel;
	private Vector<String> tableHeaderLabel;
	private TableDisplay selfRef = this; //for AbstractAction
	
	private int noOfCols;
	//listeners
	
	private KeyAdapter moreTableMainFrameKeyEventListener = new KeyAdapter(){
		@Override
		public void keyPressed(KeyEvent keyStroke){
			int keyCode = keyStroke.getKeyCode();
			if(keyCode == KeyEvent.VK_TAB){
				table.requestFocus();
			}
		}
	};
	//actions
	private final AbstractAction quickScrollToRowBelowOnTab = new AbstractAction(){
		@Override
		public void actionPerformed(ActionEvent event){
			scrollToNextRow(TAB_SCROLL_OFFSET);
		}
	};
	private final AbstractAction scrollToRowBelowOnKeyEvent = new AbstractAction(){
		@Override
		public void actionPerformed(ActionEvent event){
			scrollToNextRow(SINGLE_SCROLLDOWN);
		}
	};
	private final AbstractAction scrollToRowAboveOnKeyEvent = new AbstractAction(){
		@Override
		public void actionPerformed(ActionEvent event){
			scrollToNextRow(SINGLE_SCROLLUP);
		}
	};
	private final AbstractAction focusOnMainTableOnShift = new AbstractAction(){
		@Override
		public void actionPerformed(ActionEvent event){
			table.clearSelection();
			selfRef.requestFocus();
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
		hideScrollBars();
		setFocusTraversalKeysEnabled(false);
		addFocusListener(displayFocusListener);
		addKeyListener(moreTableMainFrameKeyEventListener);
	}
	private void hideScrollBars(){
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}
	private void createAndConfigureTable() {
		initialiseTableStructures();
		configureTableHeader();
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
			private final IDDisplayRenderer idDisplayRenderer = new IDDisplayRenderer();
			@Override
			public TableCellRenderer getCellRenderer(int row, int col){
				switch(tableDisplayType){
				case Result.HISTORY_DISPLAY:
					return super.getCellRenderer(row, col);
				case Result.LIST_DISPLAY:
					String colName = GUIManager.ALL_TASKS_TABLE_HEADER[col];
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
				default:
					return super.getCellRenderer(row, col);
				}
			}
		};
	}
	
	private void configureTable() {
		table.setModel(tableModel);
		table.setRowHeight(TABLE_ROW_HEIGHT);
		table.setFocusTraversalKeysEnabled(false);
		createTableKeyBindings();
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
	private void configureTableHeader(){
		JTableHeader header = table.getTableHeader();
		header.setDefaultRenderer(new TableHeaderRenderer());
		header.setPreferredSize(new Dimension(containerWidth, TABLE_HEADER_HEIGHT));;
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
	private void performEnterEvent(){
		if(tableDisplayType == Result.LIST_DISPLAY){
			
		}
	}
	private void scrollToNextRow(int offSet){
		if(tableRows.isEmpty()){
			table.getParent().requestFocus();
		}else{
			int selectedRow = table.getSelectedRow();
			
			if(selectedRow == NO_SELECTED_ROW){
				selectedRow = 0;
			}else{
				selectedRow = selectedRow + offSet;
				if(selectedRow < 0){//negative offset: scrolling up. go to end of table
					selectedRow = tableRows.size() - 1;
				}
			}
			
			if(selectedRow >= tableRows.size()){
				selectedRow = 0;
				table.changeSelection(selectedRow, noOfCols, false, false);
			}else{
				table.changeSelection(selectedRow, noOfCols, false, false);
			}
		}
	}
	
	private void createTableKeyBindings(){
		ComponentInputMap tableInputMap = new ComponentInputMap(table);
		ActionMap tableActionMap = new ActionMap();
		tableInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Esc Event");
		tableInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "Tab Key");
		tableInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "Down Key");
		tableInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "Up Key");
		tableActionMap.put("Esc Event", focusOnMainTableOnShift);
		tableActionMap.put("Tab Key", quickScrollToRowBelowOnTab);
		tableActionMap.put("Down Key", scrollToRowBelowOnKeyEvent);
		tableActionMap.put("Up Key", scrollToRowAboveOnKeyEvent);
		table.setInputMap(JTable.WHEN_FOCUSED, tableInputMap);
		table.setActionMap(tableActionMap);
	}
	
	protected int getSelectedRowDisplayID(){
		return table.getSelectedRow() + 1;
	}
	protected void addContentToTable(Vector<String> row){
		tableRows.add(row);
	}
	protected void addKeyListenerToTable(KeyAdapter tableKeyEventListener) {
		table.addKeyListener(tableKeyEventListener);
	}

}
