//@author A0091372H
package nailit;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.FileHandler;

import nailit.gui.GUIManager;

public class AppLauncher {
	private static final String LOG_FILE = "naillog.txt";
	private static final String START_LOGGING_MSG = "Start up";
	private static final String END_LOGGING_MSG = "Shut down";
	
	private static Logger appLog = Logger.getLogger(AppLauncher.class.getName());
	GUIManager GUI;
	
	public static void main(String[] args){
		AppLauncher launcher = new AppLauncher();
		launcher.startAppLogger();
		appLog.info(START_LOGGING_MSG);
		launcher.run();
	}
	private void startAppLogger(){
		try {
			FileHandler logFileHandler = new FileHandler(LOG_FILE);
			appLog.addHandler(logFileHandler);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Logger getLogger(){
		return appLog;
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
		appLog.info(END_LOGGING_MSG);
		System.exit(0);
	}
}
