package nailit.gui;

import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import nailit.common.Task;

public class TableDisplay extends JScrollPane{
	private static final int TABLE_HEADER_HEIGHT = 40;
	private static final int TABLE_ROW_HEIGHT = 30;
	
	protected static final String[] ALL_TASKS_TABLE_HEADER = 
		{"ID", "Name" ,"Start Time" , "End Time" , "Tag", "Status"};
	protected static final String[] COMMAND_HISTORY_HEADER =
		{"ID", "Command"};
	
	private JTable tableHeader;
	private JTable tableBody;
	
	private int containerHeight;
	private int containerWidth;
	
	private DefaultTableModel tableHeaderModel;
	private DefaultTableModel tableBodyModel;
	
	public TableDisplay(int width, int height){
		configureMainFrame(width, height);
		createAndConfigureTable();
	}
	private void configureMainFrame(int width, int height){
		containerWidth = width;
		containerHeight = height;
		this.setSize(containerWidth, containerHeight);
		this.setLayout(null);
	}
	private void createAndConfigureTable() {
		initialiseTableModels();
		createAndConfigureHeader();
		createAndConfigureBody();
	}
	private void initialiseTableModels(){
		tableHeaderModel = new DefaultTableModel();
		tableBodyModel = new DefaultTableModel();
	}
	private void createAndConfigureHeader() {
		tableHeader = new JTable();
		tableHeader.setSize(containerWidth, TABLE_HEADER_HEIGHT);
		tableHeader.setRowHeight(TABLE_HEADER_HEIGHT);
		this.setRowHeaderView(tableHeader);
	}
	
	private void createAndConfigureBody() {
		tableBody = new JTable();
		tableBody.setRowHeight(TABLE_ROW_HEIGHT);
		this.setViewportView(tableBody);
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
