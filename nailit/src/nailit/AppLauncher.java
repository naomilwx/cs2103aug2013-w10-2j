package nailit;

import java.awt.EventQueue;

import nailit.gui.GUIManager;

public class AppLauncher {
	GUIManager GUI;
	public static void main(String[] args){
		AppLauncher launcher = new AppLauncher();
		launcher.run();
	}
	
	public void run(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initialiseAndRunGUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	private void initialiseAndRunGUI(){
		GUI = new GUIManager(this);
		GUI.setVisible(true);
		GUI.setFocusOnCommandBar();
	}
	public void exit(){
		System.exit(0);
	}
}
