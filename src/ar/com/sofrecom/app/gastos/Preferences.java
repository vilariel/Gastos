package ar.com.sofrecom.app.gastos;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;
import ar.com.sofrecom.av.util.GenLog;

public class Preferences extends PreferenceActivity {
	private static final String TAG = "PreferenceActivity";
	private static final int DROPBOX = 1;
	private static final int FIELDS = 2;
	private static final int DEBTS = 3;
	private static final int LENDS = 4;
	private static final int EXPORT = 5;
	private static final int IMPORT = 6;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			addPreferencesFromResource(R.xml.preferences);
		} catch (Exception e) {
    		GenLog.errorMsg(TAG, getApplicationContext(), e);
		}
	}
	
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.removeGroup(Menu.NONE);
		menu.add(Menu.NONE, DROPBOX, Menu.NONE, R.string.dropboxMenu);
		menu.add(Menu.NONE, FIELDS, Menu.NONE, R.string.titleFieldsManager);
		menu.add(Menu.NONE, DEBTS, Menu.NONE, R.string.titleDebts);
		menu.add(Menu.NONE, LENDS, Menu.NONE, R.string.titleLends);
		menu.add(Menu.NONE, EXPORT, Menu.NONE, R.string.abm_export);
		menu.add(Menu.NONE, IMPORT, Menu.NONE, R.string.abm_import);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == DROPBOX) {
	        Intent auth = new Intent(this, DropboxAuthenticate.class);
	        startActivity(auth);
		}
		if (item.getItemId() == FIELDS) {
    		startActivity(FieldsABM.getIntent(this));
		}
		if (item.getItemId() == DEBTS) {
    		startActivity(DebtsABM.getIntent(this));
		}
		if (item.getItemId() == LENDS) {
    		startActivity(LendsABM.getIntent(this));
		}
		if (item.getItemId() == EXPORT) {
	        Intent auth = new Intent(this, DropboxAuthenticate.class);
	        auth.putExtra(DropboxAuthenticate.START_ACTIVITY_IF_LOGGED_IN, DbsExportActivity.class.getName());
	        startActivity(auth);
		}
		if (item.getItemId() == IMPORT) {
	        Intent auth = new Intent(this, DropboxAuthenticate.class);
	        auth.putExtra(DropboxAuthenticate.START_ACTIVITY_IF_LOGGED_IN, DbsImportActivity.class.getName());
	        startActivity(auth);
		}
	    return super.onOptionsItemSelected(item);
	}
}
