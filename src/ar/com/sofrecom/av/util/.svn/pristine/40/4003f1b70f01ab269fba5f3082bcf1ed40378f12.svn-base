package ar.com.sofrecom.av.util;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.code.microlog4android.Logger;

public class GenLog {
	private static Handler handler = null;
	private static Runnable runnable = null;
	private static StringSyncQueue textToSend;
	private static Logger logger = null;
	
	public static void setLogger(Logger newLogger) {
		logger = newLogger;
	}
	
	public static void setLogField(Handler aHandler, Runnable aRunnable, StringSyncQueue aTextToSend) {
		handler = aHandler;
		runnable = aRunnable;
		textToSend = aTextToSend;
	}
	
	private static void additionalLog(String type, String tag, String wha) {
		if (handler != null) {
			try {
				textToSend.pushPull(StringSyncQueue.PUSH, type + " - " + tag + " - " + wha + "\n");
				handler.post(runnable);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void log(String tag, String wha) {
		Log.i(tag, wha);
		additionalLog("I", tag, wha);
		if (logger != null) {
			logger.info(tag + " - " + wha);
		}
	}
	public static void infoMsg(String tag, Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
		log(tag, msg);
	}
	public static void warning(String tag, String wha) {
		Log.w(tag, wha);
		additionalLog("W", tag, wha);
		if (logger != null) {
			logger.warn(tag + " - " + wha);
		}
	}
	public static void err(String tag, String wha) {
		Log.e(tag, wha);
		additionalLog("E", tag, wha);
		if (logger != null) {
			logger.error(tag + " - " + wha);
		}		
	}
	public static void errorMsg(String tag, Context context, Exception e) {
		Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
		err(tag, e);
	}
	public static void err(String tag, Exception e) {
		if (e != null) {
			Log.e(tag, e.toString());
			additionalLog("E", tag, e.toString());
			if (logger != null) {
				logger.error(tag, e);
			}		
			if (e.getMessage() != null) {
				Log.e(tag, e.getMessage());
				additionalLog("E", tag, e.getMessage());
				if (logger != null) {
					logger.error(tag + e.getMessage());
				}		
			}
			for (StackTraceElement stackTrace : e.getStackTrace()) {
				Log.e(tag, stackTrace.toString());
				additionalLog("E", tag, stackTrace.toString());
				if (logger != null) {
					logger.error(tag + stackTrace.toString());
				}		
			}
		}
	}
	public static void debug(String tag, String wha) {
		Log.d(tag, wha);
		//additionalLog("D", tag, wha);
	}
}
