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

import nailit.common.Task;

public class TableDisplay extends JScrollPane{
	private static final int TABLE_HEADER_HEIGHT = 40;
	private static final int TABLE_ROW_HEIGHT = 30;
	
	protected static final String[] ALL_TASKS_TABLE_HEADER = 
		{"ID", "Name" ,"Start Time" , "End Time" , "Tag", "Status"};
	protected static final String[] COMMAND_HISTORY_HEADER =
		{"ID", "Command"};
	
	private JTable table;
	private Vector<Vector<String>> tableRows;
	private int containerHeight;
	private int containerWidth;
	
	private DefaultTableModel tableModel;
	
	public TableDisplay(int width, int height){
		tableRows = new Vector<Vector<String>>();
		configureMainFrame(width, height);
		createAndConfigureTable();
	}
	private void configureMainFrame(int width, int height){
		containerWidth = width;
		containerHeight = height;
		this.setSize(containerWidth, containerHeight);
		this.setBackground(Color.blue);
	}
	private void createAndConfigureTable() {
		initialiseTableModels();
		createAndConfigureBody();
	}
	private void initialiseTableModels(){
		tableModel = new DefaultTableModel();
	}

	private void setHeaderText(){//TODO:
		Vector<String> headerRow = 
				new Vector<String>(Arrays.asList(ALL_TASKS_TABLE_HEADER));
		
//		tableRows.add(headerRow);
		tableModel.setDataVector(tableRows, headerRow);
	}
	private void createAndConfigureBody() {
		table = new JTable();
		table.setRowHeight(TABLE_ROW_HEIGHT);
		table.setModel(tableModel);
		setHeaderText();
		setViewportView(table);
	}
	private void addRowToTable(Vector<Object> row){
		
	}
	protected void addContentToTable(Task task){
		Vector<Object> row = new Vector<Object>();
		//Insert code to convert task data to row
		addRowToTable(row);
	}
	protected void addContentToTable(String str){
		Vector<Object> row = new Vector<Object>();
		//insert code to convert given string to row
		addRowToTable(row);
	}
	protected void displayContentsInTable(List<Task> contents){
		
	}
}
