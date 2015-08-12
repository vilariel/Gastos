package ar.com.sofrecom.app.gastos;

import java.util.GregorianCalendar;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import ar.com.sofrecom.av.abm.ABMBase;
import ar.com.sofrecom.av.abm.ABMDatabaseDataMgr;
import ar.com.sofrecom.av.abm.ABMListResult;

public class NumbersABM extends ABMBase implements ABMDatabaseDataMgr {

	public static Intent getIntent(Context context) {
		return new Intent(context, NumbersABM.class);
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
		setTitle(R.string.titleNumbers);
	}

	@Override
	public int[] getFieldTitles() {
		int[] fields = {
				R.string.titleAmout,
				R.string.titleDate
			};
		return fields;
	}

	public static int[] FIELD_TYPES = {
		ABMBase.TYPE_NUMBER,
		ABMBase.TYPE_DATE_TIME
	};

	@Override
	public int[] getFieldTypes() {
		return FIELD_TYPES;
	}

	@Override
	public Object[] getInsertValues() {
		Object[] values = {new Float(0), new GregorianCalendar()};
		return values;
	}

	public static String[] FIELD_NAMES = {
		"AMOUNT",
		"DATETIME"
	};

	@Override
	public String[] getFieldNames() {
		return FIELD_NAMES;
	}

	public static final String DATABASE_NAME = "NUMBERS";
	
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
		return "DATETIME(DATETIME / 1000, 'unixepoch') || ' - ' || AMOUNT";
	}

	@Override
	public String getListOrder() {
		return "DATETIME DESC";
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
	}}
