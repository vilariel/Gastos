package ar.com.sofrecom.av.util;


public class StringSyncQueue {
	public static final int PUSH = 0;
	public static final int PULL = 1;
	private String queue = "";
	
	public synchronized String pushPull(int action, String text) {
		if (action == PUSH) {
			queue += text;
		} else if (action == PULL) {
			String result = queue;
			queue = "";
			return result;
		}
		return null;
	}
}
