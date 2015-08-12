package ar.com.sofrecom.app.gastos;

import java.util.GregorianCalendar;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import ar.com.sofrecom.av.abm.ABMBase;
import ar.com.sofrecom.av.abm.ABMDatabaseDataMgr;
import ar.com.sofrecom.av.abm.ABMListResult;

public class DebtsABM extends ABMBase implements ABMDatabaseDataMgr {

	public static Intent getIntent(Context context) {
		return new Intent(context, DebtsABM.class);
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
		setTitle(R.string.titleDebts);
	}

	@Override
	public int[] getFieldTitles() {
		int[] fields = {
				R.string.titleName,
				R.string.titleAmout,
				R.string.titleDescription,
				R.string.titleDebtDate,
				R.string.titlePaid,
				R.string.titlePaidDate
			};
		return fields;
	}

	@Override
	public int[] getFieldTypes() {
		int[] types = {
				ABMBase.TYPE_STRING,
				ABMBase.TYPE_NUMBER,
				ABMBase.TYPE_STRING,
				ABMBase.TYPE_DATE,
				ABMBase.TYPE_BOOLEAN,
				ABMBase.TYPE_DATE
			};
		return types;
	}

	@Override
	public Object[] getInsertValues() {
		Object[] values = {"", new Float(0), "", Main.getDefaultDate(this), new Long(0), new GregorianCalendar()};
		return values;
	}

	@Override
	public String[] getFieldNames() {
		String[] fields = {
				"NAME",
				"AMOUNT",
				"DESC",
				"DEBTDATE",
				"PAID",
				"PAIDDATE"
			};
		return fields;
	}

	public static final String DATABASE_NAME = "DEBTS";
	
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
		return "NAME || ' - ' || REPLACE(REPLACE(REPLACE(PAID, 1, 'S') || REPLACE (PAID, 0, 'N'), 1, ''), 0, '') || ' - ' || AMOUNT";
	}

	@Override
	public String getListOrder() {
		return "PAID, DEBTDATE";
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
