//@author A0091372H
package nailit.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JViewport;
import javax.swing.SwingUtilities;

public class GUIUtilities {
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
	public static void scrollDisplayToTop(final ScrollableFocusableDisplay display){
		scrollDisplayToPosition(display, new Point(0, 0));
	}
	
	public static void scrollDisplayToPosition(final ScrollableFocusableDisplay display, final Point point){
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				display.getViewport().setViewPosition(point);
			}
		});
	}
	public static void scrollDisplayToLeft(final ScrollableFocusableDisplay display){
		
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				JViewport viewport = display.getViewport();
				int yPos = viewport.getViewPosition().y;
				display.getViewport().setViewPosition(new Point(0, yPos));
			}
		});
	}
	
	
}
