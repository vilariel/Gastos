package ar.com.sofrecom.app.gastos;

import java.util.GregorianCalendar;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import ar.com.sofrecom.av.abm.ABMBase;
import ar.com.sofrecom.av.abm.ABMDatabaseDataMgr;
import ar.com.sofrecom.av.abm.ABMListResult;

public class AutoKmABM extends ABMBase implements ABMDatabaseDataMgr {

	ABMListResult syncTypes = null;

	public static Intent getIntent(Context context) {
		return new Intent(context, AutoKmABM.class);
	}
	
	@Override
	public HashMap<String, Object> getParameters(Context context) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put(ABMBase.ABM_RESOURCE_CLASS, ar.com.sofrecom.app.gastos.R.class);
		result.put(ABMBase.ABM_OPTIONS, ABMBase.OPT_DATAMGR_DATABASE);
		return result;
	}
		
	@Override
	public void onCreateActions() {
		setTitle(R.string.titleCar);
	}

	@Override
	public int[] getFieldTitles() {
		int[] fields = {
				R.string.titleDate,
				R.string.titleKm,
				R.string.titleAction,
				R.string.titlePlace,
				R.string.titleLts
			};
		return fields;
	}

	@Override
	public int[] getFieldTypes() {
		int[] types = {
				ABMBase.TYPE_DATE,
				ABMBase.TYPE_INTEGER,
				ABMBase.TYPE_STRING_AUTOCOMPLETE,
				ABMBase.TYPE_STRING_AUTOCOMPLETE,
				ABMBase.TYPE_NUMBER
			};
		return types;
	}

	@Override
	public Object[] getInsertValues() {
		Object[] values = {new GregorianCalendar(), new Long(0), "", "", new Float(0)};
		return values;
	}

	@Override
	public String[] getFieldNames() {
		String[] fields = {
				"DATEAUTO",
				"KM",
				"ACTION",
				"PLACE",
				"LTS"
			};
		return fields;
	}

	public static final String DATABASE_NAME = "AUTOKM";
	
	@Override
	public String getDatabaseName() {
		return DATABASE_NAME;
	}

	public static final int VERSION_NUMBER = 5;
	
	@Override
	public int getVersionNumber() {
		return VERSION_NUMBER;
	}

	@Override
	public String getListColumns() {
		return "DATE(DATEAUTO / 1000, 'unixepoch') || ' - ' || KM || ' - ' || ACTION";
	}

	@Override
	public String getListOrder() {
		return "DATEAUTO DESC";
	}

	@Override
	public ABMListResult getChoiceFieldValues(int fieldNum) {
		return null;
	}

	@Override
	public String[] getAutocompleteFieldValues(int fieldNum) {
		if (fieldNum == 2) {
			return FieldValuesLists.getFieldValueList(8, this);
		} else if (fieldNum == 3) {
			return FieldValuesLists.getFieldValueList(0, this);
		}
		return null;
	}

	@Override
	protected boolean iniWhenEmpty() {
		return false;
	}}
