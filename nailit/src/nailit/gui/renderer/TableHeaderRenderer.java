//@author A0091372H
package nailit.gui.renderer;

import javax.swing.table.DefaultTableCellRenderer;

public class TableHeaderRenderer extends DefaultTableCellRenderer{
	private static final String cellStyle = "<head><style type = \"text/css\">" 
			+ "p.header{font-size: 14px; text-align:center;}"
			+ "</style></head>";
	public TableHeaderRenderer(){
		setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
	}
	@Override
	protected void setValue(Object value){
		String	str = (String) value;
		String outStr = "<html>" + cellStyle + "<p class = \"header\">" + str + "</p><html>";
		setText(outStr);
	}
}
