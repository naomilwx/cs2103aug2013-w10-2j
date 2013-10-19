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
	private boolean isEnabled;
	
	public NailItGlobalKeyListener(GUIManager theGUI){
		GUI = theGUI;
		initialiseGlobalListener();
	}
	public boolean isEnabled(){
		return isEnabled;
	}
	public void nativeKeyPressed(NativeKeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode == NativeKeyEvent.VK_CONTROL){
			ctrlKeyDown = true;
		}
		if(ctrlKeyDown == true && keyCode == NativeKeyEvent.VK_COMMA){
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						GUI.setVisible(true);
						disableGlobalKeyHook();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}else if(keyCode == NativeKeyEvent.VK_H){
			GUI.toggleHomeWindow();;
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
        isEnabled = true;
	}

	public void disableGlobalKeyHook() {
	        GlobalScreen.getInstance().removeNativeKeyListener(this);
	        ctrlKeyDown = false;
	        isEnabled = false;
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
