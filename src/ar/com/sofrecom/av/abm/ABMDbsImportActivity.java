package ar.com.sofrecom.av.abm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import android.database.sqlite.SQLiteDatabase;
import ar.com.sofrecom.av.util.DataXmlImporter;
import ar.com.sofrecom.av.util.GenericException;

import com.dropbox.client2.exception.DropboxException;

public abstract class ABMDbsImportActivity extends ABMDbsActivity {

	@SuppressWarnings("unused")
	private static final String TAG = "ImportDbsActivity";

	protected String getFieldName(int fieldNum) {
		switch (fieldNum) {
		case STR_ASK:
			return "abm_import_ask";
		case STR_YES:
			return "abm_yes";
		case STR_NO:
			return "abm_no";
		case STR_DONE:
			return "abm_import_done";
		case STR_TITLE:
			return "abm_import";
		case STR_PROGRESS:
			return "abm_import_progress";
		default:
			return null;
		}
	}

	public void processData(ABMBase abmObject) {
		ABMDatabaseDataMgr abmObjectDbMgr = (ABMDatabaseDataMgr) abmObject;
		abmObject.openDatabase(this);
    	ScriptHelper commandHelper = new ScriptHelper(abmObjectDbMgr.getDatabaseName(), abmObjectDbMgr.getVersionNumber(), ABMDbsImportActivity.this);
    	SQLiteDatabase database = commandHelper.getWritableDatabase();
    	// Used when files are exported to file system
    	// DataXmlImporter importer = new DataXmlImporter(database, abmObjectDbMgr.getDatabaseName(), directory, abmObject.getFieldTypes(), abmObjectDbMgr.getFieldNames());
    	DataXmlImporter importer = new DataXmlImporter(database, abmObjectDbMgr.getDatabaseName(), dbApi, abmObject.getFieldTypes(), abmObjectDbMgr.getFieldNames());
		int qtty;
		try {
			qtty = importer.importData();
			addResult("\n" + abmObjectDbMgr.getDatabaseName() + ": " + qtty);
		} catch (GenericException e) {
			addResult("\n" + abmObjectDbMgr.getDatabaseName() + ": " + e.toString());
		}
	}

// Used when files are exported to file system
//	protected String getPrefsOri() {
//		return directory + "/preferences.xml";
//	}
	
// Used when files are exported to file system
//	protected String getPrefsNew() {
//		String packageName = this.getPackageName();
//		return getDataDir(packageName) + "/shared_prefs/" + packageName + "_preferences.xml";
//	}

	public void processPreferences() {
		String packageName = this.getPackageName();
		String fileNameNew = getDataDir(packageName) + "/shared_prefs/" + packageName + "_preferences.xml";
		String fileNameOri = "/preferences.xml";
		importFile(fileNameOri, fileNameNew);
    }

	protected void importFile(String fileNameOri, String fileNameNew) {
		File prefNew = new File(fileNameNew);
    	String content = "";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			dbApi.getFile(fileNameOri, null, outputStream, null);
			content = new String(outputStream.toByteArray());
			prefNew.createNewFile();
			ByteBuffer buff = ByteBuffer.wrap(content.getBytes());
			FileChannel channel = new FileOutputStream(prefNew).getChannel();
			try {
				channel.write(buff);
			} finally {
				if (channel != null) channel.close();
			}
			addResult("\n" + fileNameOri);
		} catch (IOException e) {
			addResult("\n" + fileNameOri + ": " + e.toString());
		} catch (DropboxException e) {
			addResult("\n" + fileNameOri + ": " + e.toString());
		}
	}
}
