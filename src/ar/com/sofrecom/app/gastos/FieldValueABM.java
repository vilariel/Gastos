package ar.com.sofrecom.app.gastos;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import ar.com.sofrecom.av.abm.ABMBase;
import ar.com.sofrecom.av.abm.ABMDatabaseDataMgr;
import ar.com.sofrecom.av.abm.ABMListResult;

public class FieldValueABM extends ABMBase implements ABMDatabaseDataMgr {

	public static Intent getIntent(Context context) {
		FieldValuesLists.setReloadFieldValues();
		return new Intent(context, FieldValueABM.class);
	}
	
	@Override
	public HashMap<String, Object> getParameters(Context context) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put(ABMBase.ABM_RESOURCE_CLASS, ar.com.sofrecom.app.gastos.R.class);
		result.put(ABMBase.ABM_OPTIONS, ABMBase.OPT_DATAMGR_DATABASE + ABMBase.OPT_DONT_USE_VIEW + ABMBase.OPT_DUPLICATE_IN_CONTEXT);
		result.put(ABMBase.ABM_FIELD_PARENT_ID, "FIELD_ID");
		return result;
	}
		
	@Override
	public void onCreateActions() {
		setTitle(getParentTitle());
	}

	@Override
	public int[] getFieldTitles() {
		int[] fields = {R.string.titleValue};
		return fields;
	}

	public static int[] FIELD_TYPES = {
			ABMBase.TYPE_STRING,
			ABMBase.TYPE_INTEGER
		};

	@Override
	public int[] getFieldTypes() {
		return FIELD_TYPES;
	}

	@Override
	public Object[] getInsertValues() {
		Object[] values = {"", getParentId()};
		return values;
	}

	public static String[] FIELD_NAMES = {
			"VALUE",
			"FIELD_ID"
		};

	@Override
	public String[] getFieldNames() {
		return FIELD_NAMES;
	}

	public static final String DATABASE_NAME = "FIELDVALUES";
	
	@Override
	public String getDatabaseName() {
		return DATABASE_NAME;
	}

	public static final int VERSION_NUMBER = 1;
	
	@Override
	public int getVersionNumber() {
		return VERSION_NUMBER;
	}

	@Override
	public String getListColumns() {
		return "VALUE";
	}

	@Override
	public String getListOrder() {
		return "VALUE";
	}

	@Override
	public ABMListResult getChoiceFieldValues(int fieldNum) {
		return null;
	}

	@Override
	public String[] getAutocompleteFieldValues(int fieldNum) {
		return null;
	}

	@Override
	protected boolean iniWhenEmpty() {
		return false;
	}
}
