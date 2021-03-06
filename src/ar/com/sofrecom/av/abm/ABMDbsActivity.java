package ar.com.sofrecom.av.abm;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import ar.com.sofrecom.av.util.GenLog;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;

public abstract class ABMDbsActivity extends Activity {

	private static final String TAG = "ABMDbsActivity";
	private static final int CONFIRM_DLG = 1;
	protected static final int STR_ASK = 1;
	protected static final int STR_YES = 2;
	protected static final int STR_NO = 3;
	protected static final int STR_DONE = 4;
	protected static final int STR_TITLE = 5;
	protected static final int STR_PROGRESS = 6;
	private ProgressDialog progressDialog = null;
	private ArrayList<ABMBase> basesList = null;
	private String result = "";
	private Class<?> stringResourceClass;
	// Used only to write in files:
	// protected String directory = null;
	// Used only to write in dropbox:
	protected DropboxAPI<AndroidAuthSession> dbApi;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        basesList = new ArrayList<ABMBase>();
        prepare();
    }
	
	protected abstract void prepare();
	
	@Override
	public void onStart() {
		super.onStart();
		showDialog(CONFIRM_DLG);
	}
	
	protected void processAll() {
        Thread syncThread = new Thread() {
        	public void run() {
				process();
				runOnUiThread(returnRes);
        	}
        };
        syncThread.start();
        progressDialog = ProgressDialog.show(this, getString(getStringValue(STR_TITLE)), getString(getStringValue(STR_PROGRESS)), true);
	}
	
    private Runnable returnRes = new Runnable() {
        @Override
        public void run() {
    		showResult();
        	progressDialog.dismiss();
    		finish();
        }
    };

    protected void showResult() {
		GenLog.infoMsg(TAG, this, getText(getStringValue(STR_DONE)) + result);
    }

	private void process() {
		for (ABMBase eachBase : basesList) {
			processData(eachBase);
		}
		processPreferences();
		processCustom();
	}
	
//	protected void setDirectory(String directory) {
//		this.directory = directory;
//	}

	protected void setDropboxApi(DropboxAPI<AndroidAuthSession> dbApi) {
		this.dbApi = dbApi;
	}
	
	protected void setResourceClass(Class<?> resourceClass) {
		try {
			String resourcesClassName = resourceClass.getName();
			Class<?>[] classes = resourceClass.getClasses();
			for (Class<?> subClass : classes) {
				if (subClass.getName().equals(resourcesClassName + "$string")) {
					stringResourceClass = subClass;
				}
			}
		} catch (Exception e) {
			GenLog.errorMsg(TAG, this, e);
		}
	}
	
	private int getStringValue(int fieldNum) {
		String fieldName = getFieldName(fieldNum);
		int result = 0;
		try {
			result = stringResourceClass.getField(fieldName).getInt(null);
		} catch (Exception e) {
			GenLog.errorMsg(TAG, this, e);
		}
		return result;
	}
	
	protected abstract String getFieldName(int fieldNum); 
	
	protected void addAbmObject(ABMBase abmObject) {
		basesList.add(abmObject);
	}
	
	public abstract void processData(ABMBase abmObject);

	public abstract void processCustom();

	public abstract void processPreferences();
		
// Used when files are exported to file system
//	public void processPreferences() {
//		File prefOri = new File(getPrefsOri());
//		File prefNew = new File(getPrefsNew());
//		InputStream in;
//		OutputStream out;
//		try {
//			in = new FileInputStream(prefOri);
//			out = new FileOutputStream(prefNew);
//			byte[] buf = new byte[1024];
//			int len;
//			while ((len = in.read(buf)) > 0) {
//				out.write(buf, 0, len);
//			}
//			in.close();
//			out.close();
//		} catch (FileNotFoundException e) {
//			GenLog.errorMsg(TAG, this, e);
//		} catch (IOException e) {
//			GenLog.errorMsg(TAG, this, e);
//		}
//    }
	
// Used when files are exported to file system
//	protected abstract String getPrefsOri();
	
// Used when files are exported to file system
//	protected abstract String getPrefsNew();
	
	protected String getDataDir(String packageName) { 
        try { 
            PackageInfo packageInfo = getPackageManager().getPackageInfo(packageName, 0); 
            if (packageInfo == null) return null; 
            ApplicationInfo applicationInfo = packageInfo.applicationInfo; 
            if (applicationInfo == null) return null; 
            if (applicationInfo.dataDir == null) return null; 
            return applicationInfo.dataDir; 
        } catch (NameNotFoundException ex) { 
            return null; 
        } 
    }	
	
    protected static class ScriptHelper extends SQLiteOpenHelper {
		@Override
		public void onCreate(SQLiteDatabase arg0) {
		}
		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		}
    	ScriptHelper(String name, int version, Context context) {
            super(context, name, null, version);
    	}
    }

	@Override
	protected Dialog onCreateDialog(int id) {
		try {
			if (id == CONFIRM_DLG) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(getStringValue(STR_ASK))
				       .setCancelable(true)
				       .setPositiveButton(getStringValue(STR_YES), new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   processAll();
				           }
				       })
				       .setNegativeButton(getStringValue(STR_NO), new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				                dialog.cancel();
				        		finish();
				           }
				       });
				AlertDialog alert = builder.create();
				return alert;
			}
		} catch (Exception e) {
			GenLog.errorMsg(TAG, this, e);
		}
		return null;
	}

	protected void addResult(String result) {
		this.result += result;
	}
	
}
