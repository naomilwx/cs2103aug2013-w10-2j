//@author A0091372H
package nailit.gui.renderer;

import javax.swing.table.DefaultTableCellRenderer;

import nailit.gui.GUIManager;

public class IDDisplayRenderer  extends DefaultTableCellRenderer{
	public IDDisplayRenderer(){
		setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
	}
	@Override
	protected void setValue(Object value){
		String displayID = (String) value;
		if(displayID.equals(GUIManager.DELETED_TASK_DISPLAY_ID)){
			displayID = "<html><p color = \"red\">" + displayID + "</p><html>";
		}else{
			displayID = "<html><p style = \"font-weight: bold; font-size: 12px\">" + displayID + "</p><html>";
		}
		setText(displayID);
	}
}
