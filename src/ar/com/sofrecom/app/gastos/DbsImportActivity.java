package ar.com.sofrecom.app.gastos;

import android.content.Context;
import android.content.SharedPreferences;
import ar.com.sofrecom.av.abm.ABMDbsImportActivity;

public class DbsImportActivity extends ABMDbsImportActivity {

	@Override
    protected void prepare() {
		// Used when files are exported to file system
		// SharedPreferences prefs = getSharedPreferences(Main.PREF_PACKAGE, Context.MODE_PRIVATE);
		// Used when files are exported to file system
		// setDirectory(prefs.getString("datadir", ""));
		setDropboxApi(Main.dbApi);
		setResourceClass(R.class);
		addAbmObject(new CategoryABM());
		addAbmObject(new SubCategoryABM());
		addAbmObject(new DebtsABM());
		addAbmObject(new LendsABM());
		addAbmObject(new FieldValueABM());
		addAbmObject(new NumbersABM());
		addAbmObject(new AutoKmABM());
    }

	@Override
	public void processCustom() {
		SharedPreferences prefs = getSharedPreferences(Main.PREF_PACKAGE, Context.MODE_PRIVATE);
		String fileNameNew = prefs.getString("datadir", "") + "/gastos.csv";
		String fileNameOri = "/gastos.xml";
		importFile(fileNameOri, fileNameNew);
	}
}
