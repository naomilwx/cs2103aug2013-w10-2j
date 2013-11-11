package nailit.storage;

// @author a0105683e

public class FileCorruptionException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7556975386904818618L;

	public FileCorruptionException(String message){
		super(message);
	}
}
