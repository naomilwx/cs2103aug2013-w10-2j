package nailit.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Utilities {
	private static final Timer fadeTimer = new Timer(0, null);
	protected static final float FULL_OPACITY = 1.0f;
	protected static final float NO_OPACITY = 0.5f;
	public static final float MAX_OPACITY_VALUE = 255;
	
	public static void fadeOutComponent(final Component component, int displayTime, int timeInterval, final float opacityStep){
		fadeTimer.setInitialDelay(displayTime);
		fadeTimer.setDelay(timeInterval);
		fadeTimer.addActionListener(new ActionListener(){
			int originalOpacity = Utilities.getComponentOpacity(component);
			float nextOpacityRatio = 1;
			@Override
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				nextOpacityRatio -= opacityStep;
				System.out.println(nextOpacityRatio);
				if(nextOpacityRatio <= NO_OPACITY){
					component.getParent().setVisible(false);
					//restore original opacity so component's original background settings is restored when it is made visible again
					Utilities.setComponenetOpacity(component, originalOpacity);
					fadeTimer.stop();
					fadeTimer.removeActionListener(this);
				}else{
					int nextOpacity = Math.round(nextOpacityRatio*MAX_OPACITY_VALUE);
					Utilities.setComponenetOpacity(component, nextOpacity);
				}
				
			}
			
		});
		fadeTimer.restart();
	}
	
	public static int getComponentOpacity(Component component){
		return component.getBackground().getAlpha();
	}
	public static void setComponenetOpacity(Component component, int opacity){
		Color componentColor = component.getBackground();
		Color newColor = new Color(componentColor.getRed(),
								componentColor.getGreen(),
								componentColor.getBlue(),
								opacity);
		Graphics componentGraphics = component.getGraphics();
		componentGraphics.setColor(newColor);
		componentGraphics.fillRect(0, 0, component.getWidth(), component.getHeight());
		component.revalidate();
	}
}
