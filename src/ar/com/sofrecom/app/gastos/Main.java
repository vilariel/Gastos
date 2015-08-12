package ar.com.sofrecom.app.gastos;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import ar.com.sofrecom.av.util.GenLog;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.google.code.microlog4android.config.PropertyConfigurator;

public class Main extends Activity {
	private static final String TAG = "Main";
	
	private boolean debugEnabled = false;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static final String PREF_PACKAGE = "ar.com.sofrecom.app.gastos_preferences";
	public static DropboxAPI<AndroidAuthSession> dbApi;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PropertyConfigurator.getConfigurator(this).configure();
        defaultPreferences();
        if (debugEnabled) {
	    	Logger logger = LoggerFactory.getLogger();
	    	GenLog.setLogger(logger);
	    	GenLog.log(getString(R.string.app_name), "**** " + dateFormat.format((new GregorianCalendar()).getTime()) + " ****");
        }
        setContentView(R.layout.main);
        startActivity(CategoryABM.getIntent(this));
		finish();
    }
    
    private void defaultPreferences() {
		SharedPreferences prefs = getSharedPreferences(PREF_PACKAGE, Context.MODE_PRIVATE);
		if (prefs.getString("datadir", "").equals("")) {
			SharedPreferences.Editor editor = prefs.edit();
			try {
				editor.putString("datadir", getPackageManager().getPackageInfo(this.getPackageName(), 0).applicationInfo.dataDir);
			} catch (NameNotFoundException e) {
	    		GenLog.errorMsg(TAG, getApplicationContext(), e);
			} 
			editor.commit();
		}
		debugEnabled = prefs.getBoolean("debug", false);
    }

	private static Calendar defaultDate = null;
	
	public static Calendar getDefaultDate(Activity activity) {
		if (defaultDate == null) {
			SharedPreferences prefs = activity.getSharedPreferences(Main.PREF_PACKAGE, Context.MODE_PRIVATE);
			String startHourStr = prefs.getString("hour", "0");
			long startHour = Long.parseLong(startHourStr);
			Date now = new Date();
			Date newNow = new Date(now.getTime() - startHour * 3600000);
			defaultDate = new GregorianCalendar();
			defaultDate.setTimeInMillis(newNow.getTime());
		}
		return defaultDate;
	}
	
	public static void setDefaultDate(Calendar date) {
		defaultDate = date;
	}
}