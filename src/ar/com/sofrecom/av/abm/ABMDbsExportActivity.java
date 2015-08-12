package ar.com.sofrecom.av.abm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.database.sqlite.SQLiteDatabase;
import ar.com.sofrecom.av.util.DataXmlExporter;
import ar.com.sofrecom.av.util.GenericException;

import com.dropbox.client2.exception.DropboxException;

public abstract class ABMDbsExportActivity extends ABMDbsActivity {

	@SuppressWarnings("unused")
	private static final String TAG = "ExportDbsActivity";

	protected String getFieldName(int fieldNum) {
		switch (fieldNum) {
		case STR_ASK:
			return "abm_export_ask";
		case STR_YES:
			return "abm_yes";
		case STR_NO:
			return "abm_no";
		case STR_DONE:
			return "abm_export_done";
		case STR_TITLE:
			return "abm_export";
		case STR_PROGRESS:
			return "abm_export_progress";
		default:
			return null;
		}
	}

	public void processData(ABMBase abmObject){
		ABMDatabaseDataMgr abmObjectDbMgr = (ABMDatabaseDataMgr) abmObject;
    	ScriptHelper commandHelper = new ScriptHelper(abmObjectDbMgr.getDatabaseName(), abmObjectDbMgr.getVersionNumber(), ABMDbsExportActivity.this);
    	SQLiteDatabase database = commandHelper.getReadableDatabase();
    	// Used when files are exported to file system
       	// DataXmlExporter exporter = new DataXmlExporter(database, abmObjectDbMgr.getDatabaseName(), directorio);
       	DataXmlExporter exporter = new DataXmlExporter(database, abmObjectDbMgr.getDatabaseName(), dbApi);
		int qtty;
		try {
			qtty = exporter.exportData();
			addResult("\n" + abmObjectDbMgr.getDatabaseName() + ": " + qtty);
		} catch (GenericException e) {
			addResult("\n" + abmObjectDbMgr.getDatabaseName() + ": " + e.toString());
		}
	}

// Used when files are exported to file system
//	protected String getPrefsOri() {
//		String packageName = this.getPackageName();
//		return getDataDir(packageName) + "/shared_prefs/" + packageName + "_preferences.xml";
//		
//	}
	
// Used when files are exported to file system
//	protected String getPrefsNew() {
//		return directory + "/preferences.xml";
//	}

	public void processPreferences() {
		String packageName = this.getPackageName();
		String fileNameOri = getDataDir(packageName) + "/shared_prefs/" + packageName + "_preferences.xml";
		String fileNameNew = "/preferences.xml";
		exportFile(fileNameOri, fileNameNew);
    }
	
	protected void exportFile(String fileNameOri, String fileNameNew) {
		File prefOri = new File(fileNameOri);
		InputStream in;
		try {
			in = new FileInputStream(prefOri);
			dbApi.putFileOverwrite(fileNameNew, in, prefOri.length(), null);
			in.close();
			addResult("\n" + fileNameNew);
		} catch (IOException e) {
			addResult("\n" + fileNameNew + ": " + e.toString());
		} catch (DropboxException e) {
			addResult("\n" + fileNameNew + ": " + e.toString());
		}
	}
	
}