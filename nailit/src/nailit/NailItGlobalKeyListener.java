package nailit;

import java.awt.EventQueue;

import nailit.gui.GUIManager;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class NailItGlobalKeyListener implements NativeKeyListener{
	private GUIManager GUI;
	private boolean ctrlKeyDown = false;
	
	public NailItGlobalKeyListener(GUIManager theGUI){
		GUI = theGUI;
	}
	public void nativeKeyPressed(NativeKeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode == NativeKeyEvent.VK_CONTROL){
			ctrlKeyDown = true;
		}
		if(ctrlKeyDown == true && keyCode == NativeKeyEvent.VK_STOP){
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						GUI.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}   
	}
	
	public void nativeKeyReleased(NativeKeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode == NativeKeyEvent.VK_CONTROL){
			ctrlKeyDown = false;
		}
	}
	
	public void nativeKeyTyped(NativeKeyEvent e) {
	}
	
	public void registerGlobalKeyHook() {
        GlobalScreen.getInstance().addNativeKeyListener(this);
}

	public void disableGlobalKeyHook() {
	        GlobalScreen.getInstance().removeNativeKeyListener(this);
	        ctrlKeyDown = false;
	}
	
	public void initialiseGlobalListener(){
		try {
            GlobalScreen.registerNativeHook();
	    } catch (NativeHookException ex) {
	            System.err.println("There was a problem registering the native hook.");
	            System.err.println(ex.getMessage());
	            System.exit(1);
	    }
	}
}
