package nailit.gui;

import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import nailit.common.Task;

public class TableDisplay extends JPanel{
	private static final int TABLE_HEADER_HEIGHT = 40;
	private static final int TABLE_ROW_HEIGHT = 30;
	
	private JScrollPane tContainer;
	private JTable tableHeader;
	private JTable tableBody;
	
	private int containerHeight;
	private int containerWidth;
	
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
		tContainer = new JScrollPane();
		tContainer.setSize(containerWidth, containerHeight);
		tContainer.setLocation(GUIManager.DEFAULT_COMPONENT_LOCATION);
		createAndConfigureHeader();
		createAndConfigureBody();
	}
	
	private void createAndConfigureHeader() {
		tableHeader = new JTable();
		tableHeader.setSize(containerWidth, TABLE_HEADER_HEIGHT);
		tableHeader.setRowHeight(TABLE_HEADER_HEIGHT);
		tContainer.setRowHeaderView(tableHeader);
	}
	
	private void createAndConfigureBody() {
		tableBody = new JTable();
		tableBody.setRowHeight(TABLE_ROW_HEIGHT);
		tContainer.setViewportView(tableBody);
	}
	private void addRowToTable(){
		
	}
	protected void addContentToTable(Task task){
		
	}
	protected void displayContentsInTable(List<Task> contents){
		
	}
}
