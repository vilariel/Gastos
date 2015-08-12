package ar.com.sofrecom.av.util;

@SuppressWarnings("serial")
public class GenericException extends Exception {
	public GenericException(String tag, String message) {
		super(tag + " - " + message);
	}
	public GenericException(String message) {
		super(message);
	}
	public GenericException(Exception e) {
		super(e);
	}
}
