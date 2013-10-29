//@author A0091372H
package nailit.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Utilities {
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
	public static void scrollTextDisplayToTop(final TextDisplay textDisplay){
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				textDisplay.getViewport().setViewPosition(new Point(0, 0));
			}
		});
	}
}
