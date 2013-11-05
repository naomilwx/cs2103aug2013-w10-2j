//@author A0091372H
package nailit.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

public abstract class TableDisplay extends ScrollableFocusableDisplay{
	protected static final int TABLE_HEADER_HEIGHT = 35;
	protected static final int TABLE_ROW_HEIGHT = 50;
	protected static final int NO_SELECTED_ROW = -1;
	protected static final int SINGLE_SCROLLDOWN = 1;
	protected static final int SINGLE_SCROLLUP = -1;
	protected static final int QUICK_SCROLLDOWN_OFFSET = 6;
	protected static final int QUICK_SCROLLUP_OFFSET = 6;
	
	protected int containerHeight;
	protected int containerWidth;

	protected JTable table;
	protected Vector<Vector<String>> tableRows;
	protected DefaultTableModel tableModel;
	protected Vector<String> tableHeaderLabel;
	protected TableDisplay selfRef = this; //for AbstractAction
	
	protected int noOfCols;

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
	protected final MouseAdapter tableMouseEventListener = new MouseAdapter(){
		@Override
		public void mouseEntered(MouseEvent event){
			showScrollBars();
		}
		@Override
		public void mouseExited(MouseEvent event){
			if(event.getComponent() instanceof TableDisplay){
				hideScrollBars();
			}
		}
	};
	//actions
	private final AbstractAction quickScrollToRowBelow = new AbstractAction(){
		@Override
		public void actionPerformed(ActionEvent event){
			scrollToNextRow(QUICK_SCROLLDOWN_OFFSET);
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
	
	public TableDisplay(int width, int height){
		configureMainFrame(width, height);
		createAndConfigureTable();
	}
	
	protected abstract void setHeaderText();
	protected abstract void setRowWidths();
	protected void configureMainFrame(int width, int height){
		containerWidth = width;
		containerHeight = height;
		setSize(containerWidth, containerHeight);
		setBorder(UNFOCUS_LINE_BORDER);
		hideScrollBars();
		setFocusTraversalKeysEnabled(false);
		addFocusListener(displayFocusListener);
		addKeyListener(moreTableMainFrameKeyEventListener);
		addMouseListener(tableMouseEventListener);
	}
	protected void hideScrollBars(){
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}
	protected void showScrollBars(){
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		 setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
	protected void createAndConfigureTable() {
		initialiseTableStructures();
		configureTableHeader();
		setHeaderText();
		configureTable();
	}
	
	protected void initialiseTableStructures(){
		tableModel = new DefaultTableModel(){
			@Override
			public boolean isCellEditable(int row, int col){
				return false;
			}
		};
	}
	
	protected void configureTable() {
		table.setModel(tableModel);
		table.setRowHeight(TABLE_ROW_HEIGHT);
		table.setFocusTraversalKeysEnabled(false);
		table.setShowGrid(false);
		createTableKeyBindings();
		setRowWidths();
		setViewportView(table);
		table.addMouseListener(tableMouseEventListener);
	}
	
	protected void configureTableHeader(){
		JTableHeader header = table.getTableHeader();
		header.setReorderingAllowed(false);
		header.setDefaultRenderer(new TableHeaderRenderer());
		header.setPreferredSize(new Dimension(containerWidth, TABLE_HEADER_HEIGHT));
	}
	
	
	protected void setRowWidths(JTable tab, int[] widths){
		TableColumnModel columnModel = tab.getColumnModel();
		TableColumn column;
		for(int i = 0; i < widths.length; i++){
			column = columnModel.getColumn(i);
			column.setWidth(widths[i]);
			column.setPreferredWidth(widths[i]);
		}
	}
	protected void quickScrollUp(){
		scrollToNextRow(QUICK_SCROLLUP_OFFSET);
	}
	protected void quickScrollDown(){
		scrollToNextRow(QUICK_SCROLLDOWN_OFFSET);
	}
	protected void scrollUp(){
		scrollToNextRow(SINGLE_SCROLLUP);
	}
	protected void scrollDown(){
		scrollToNextRow(SINGLE_SCROLLDOWN);
	}
	private void scrollToNextRow(int offSet){
		if(tableRows.isEmpty()){
			table.getParent().requestFocus();
		}else{
			int currentRow = table.getSelectedRow();
			int selectedRow =currentRow;
			
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
				Point viewPos = getViewport().getViewPosition();
				int XPos = viewPos.x;
				int YPos = viewPos.y;
				YPos += (selectedRow - currentRow) * TABLE_ROW_HEIGHT;
				if(YPos < 0){
					YPos = 0;
				}
				getViewport().setViewPosition(new Point(XPos, YPos));
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
		tableActionMap.put("Tab Key", quickScrollToRowBelow);
		tableActionMap.put("Down Key", scrollToRowBelowOnKeyEvent);
		tableActionMap.put("Up Key", scrollToRowAboveOnKeyEvent);
		table.setInputMap(JTable.WHEN_FOCUSED, tableInputMap);
		table.setActionMap(tableActionMap);
	}
	
	protected int getSelectedRowDisplayID(){
		return table.getSelectedRow() + 1;
	}

	protected void addContentToTable(int pos, Vector<String> row){
		tableRows.add(pos, row);
	}
	protected void addContentToTable(Vector<String> row){
		tableRows.add(row);
	}
	protected void addKeyListenerToTable(KeyAdapter tableKeyEventListener) {
		table.addKeyListener(tableKeyEventListener);
	}

}
