package nailit.gui;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import nailit.common.Result;
import nailit.common.Task;

public class TableDisplay extends JScrollPane{
	private static final int TABLE_HEADER_HEIGHT = 40;
	private static final int TABLE_ROW_HEIGHT = 30;
	
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
		this.setSize(containerWidth, containerHeight);
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
		table = new JTable();
	}
	private void configureTable() {
		table.setModel(tableModel);
		table.setRowHeight(TABLE_ROW_HEIGHT);
		setRowWidths();
		String[] test = {"1", "test", "","","<html><p style = \"color: red\">"+"d"+"</p></html>"};
		Vector<String> row = new Vector<String>();
		for(int i = 0; i < test.length; i++){
			row.add(test[i]);
		}
		tableRows.add(row);
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

	protected void addContentToTable(Vector<String> row){
		tableRows.add(row);
	}
	protected void displayContentsInTable(List<Task> contents){
		Vector<String> row = new Vector<String>();
		for(int i = 0; i < contents.size(); i++){
			addContentToTable(row);
		}
	}
}
