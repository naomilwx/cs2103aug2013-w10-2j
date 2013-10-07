package nailit.gui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

public abstract class ScrollableFocusableDisplay extends JScrollPane{
	protected final LineBorder FOCUS_LINE_BORDER = new LineBorder(GUIManager.FOCUSED_BORDER_COLOR);
	protected final LineBorder UNFOCUS_LINE_BORDER = new LineBorder(GUIManager.BORDER_COLOR);
	protected final FocusListener displayFocusListener = new FocusListener(){
		public void focusGained(FocusEvent event) {
			setBorder(FOCUS_LINE_BORDER);
		 }
		public void focusLost(FocusEvent event){
			setBorder(UNFOCUS_LINE_BORDER);
		}
	};

}