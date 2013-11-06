//@author A0091372H
package nailit.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Vector;

import javax.swing.Timer;

import nailit.common.NIConstants;
import nailit.common.Result;

public class HistoryWindow extends ExtendedWindow{
	private static final String COMMANDS_EXECUTED_HEADER 
	= "<h1 style = \"padding-left: 5px\">Commands executed: </h1>";
	private static final String COMMANDS_UNDID_HEADER 
	= "<h1 style = \"padding-left: 5px\">Commands undone: </h1>";
	private final Timer fadeOutTimer = new Timer(0, null);
	
	private static final int TIMER_INTERVAL = 100;
	private static final int TIMER_DELAY = 6000; //amount of time before item starts fading out
	private static final float OPACITY_INTERVAL_STEP = 0.1f;
	protected static final float NO_OPACITY = 0.5f;
	protected static final float FULL_OPACITY = 1.0f;
	
	protected final FocusListener historyFocusListener = new FocusListener(){
		public void focusGained(FocusEvent event) {
			fadeOutTimer.stop();
		 }
		public void focusLost(FocusEvent event){
			fadeOutWindow(TIMER_DELAY, TIMER_INTERVAL, OPACITY_INTERVAL_STEP);
		}
	};
	
	public HistoryWindow(GUIManager GUIMain, int width) {
		super(GUIMain, width);
		displayPane.addFocusListener(historyFocusListener);
	}
	
	protected void displayHistoryList(Vector<Vector <String>> list){
		if(list == null){
			return;
		}
		StringBuilder str = new StringBuilder();
		str.append("<html>");
		
		Vector<String> undidCommands = list.get(NIConstants.HISTORY_UNDO_INDEX);
		str.append(COMMANDS_EXECUTED_HEADER);
		str.append("<table>");
		for(String command: undidCommands){
			str.append("<tr>");
			str.append(command);
			str.append("</tr>");
		}
		str.append("</table>");
		
		Vector<String> redoableCommands = list.get(NIConstants.HISTORY_REDO_INDEX);
		if(!redoableCommands.isEmpty()){
			str.append(COMMANDS_UNDID_HEADER);
			str.append("<table>");
			for(String command: redoableCommands){
				str.append("<tr>");
				str.append(command);
				str.append("</tr>");
			}
			str.append("</table>");
		}
		
		str.append("</html>");
		((TextDisplay) displayPane).displayHTMLFormattedText(str.toString());
	}
	@Override
	public void setVisible(boolean visible){
		super.setVisible(visible);
		fadeOutWindow(TIMER_DELAY, TIMER_INTERVAL, OPACITY_INTERVAL_STEP);
		GUIBoss.setFocusOnCommandBar();
	}
	private void fadeOutWindow(int displayTime, int timeInterval, final float opacityStep){
		fadeOutTimer.setInitialDelay(displayTime);
		fadeOutTimer.setDelay(timeInterval);
		fadeOutTimer.addActionListener(new ActionListener(){
			float nextOpacity = FULL_OPACITY;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				nextOpacity -= opacityStep;
				if(nextOpacity <= NO_OPACITY){
					fadeOutTimer.stop();
					setVisible(false);
				}else{
//					setOpacity(nextOpacity);
				}
			}
		});
		fadeOutTimer.restart();
	}
}
