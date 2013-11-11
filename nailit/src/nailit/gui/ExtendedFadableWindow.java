package nailit.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Timer;

@SuppressWarnings("serial")
//class with position configured according to MainWindow and can fadeout after some time
public class ExtendedFadableWindow extends ExtendedWindow{
	protected static final int TIMER_INTERVAL = 250;
	protected static final int TIMER_DELAY = 10000; //amount of time before item starts fading out
	protected static final float OPACITY_INTERVAL_STEP = 0.05f;
	protected static final float NO_OPACITY = 0.5f;
	protected static final float FULL_OPACITY = 1.0f;
	protected final Timer fadeOutTimer = new Timer(0, null);
	
	protected final FocusListener fadableWindowFocusListener = new FocusListener(){
		public void focusGained(FocusEvent event) {
			fadeOutTimer.stop();
		 }
		public void focusLost(FocusEvent event){
			fadeOutWindow(TIMER_DELAY, TIMER_INTERVAL, OPACITY_INTERVAL_STEP);
		}
	};
	protected final KeyAdapter keyListener = new KeyAdapter(){
		@Override
		public void keyPressed(KeyEvent keyStroke){
			int keyCode = keyStroke.getKeyCode();
			if(keyCode == KeyEvent.VK_ESCAPE){
				setVisible(false);
				GUIBoss.setFocusOnCommandBar();
			}
		}
	};
	
	
	public ExtendedFadableWindow(GUIManager GUIMain, int width) {
		super(GUIMain, width);
		addKeyListenerToDisplayPaneAndChild(keyListener);
		addAdditionalFocusListenerToDisplayPaneAndChild();
	}
	protected void addAdditionalFocusListenerToDisplayPaneAndChild(){
		displayPane.addFocusListener(fadableWindowFocusListener);
		Component componentInDisplayPane = displayPane.getViewport().getComponent(0);
		if(componentInDisplayPane != null){
			componentInDisplayPane.addFocusListener(fadableWindowFocusListener);
		}
	}
	protected void startFadeOutTimer(){
		fadeOutWindow(TIMER_DELAY, TIMER_INTERVAL, OPACITY_INTERVAL_STEP);
	}
	
	protected void fadeOutWindow(int displayTime, int timeInterval, final float opacityStep){
		fadeOutTimer.setInitialDelay(displayTime);
		fadeOutTimer.setDelay(timeInterval);
		fadeOutTimer.addActionListener(new ActionListener(){
			float nextOpacity = FULL_OPACITY;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				nextOpacity -= opacityStep;
				if(nextOpacity <= NO_OPACITY){
					fadeOutTimer.stop();
					setOpacity(FULL_OPACITY);
					setVisible(false);
				}else{
					setOpacity(nextOpacity);
				}
			}
		});
		fadeOutTimer.restart();
	}

}
