package ar.com.sofrecom.app.gastos;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import ar.com.sofrecom.av.abm.ABMBase;
import ar.com.sofrecom.av.abm.ABMCustomDataMgr;
import ar.com.sofrecom.av.abm.ABMListResult;

public class FieldsABM extends ABMBase implements ABMCustomDataMgr {

	public static Intent getIntent(Context context) {
		return new Intent(context, FieldsABM.class);
	}
	
	@Override
	public HashMap<String, Object> getParameters(Context context) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put(ABMBase.ABM_RESOURCE_CLASS, ar.com.sofrecom.app.gastos.R.class);
		result.put(ABMBase.ABM_OPTIONS, ABMBase.OPT_DATAMGR_CUSTOM + ABMBase.OPT_READ_ONLY + ABMBase.OPT_DONT_USE_VIEW);
		result.put(ABMBase.ABM_CLICK_ITEM_INTENT, FieldValueABM.getIntent(this));
		return result;
	}
		
	@Override
	public void onCreateActions() {
		setTitle(R.string.titleFieldsManager);
	}

	@Override
	public int[] getFieldTitles() {
		int[] fields = {R.string.titleName};
		return fields;
	}

	@Override
	public int[] getFieldTypes() {
		int[] types = {
				ABMBase.TYPE_STRING
			};
		return types;
	}

	@Override
	public Object[] getInsertValues() {
		Object[] values = {""};
		return values;
	}

	@Override
	public ABMListResult getChoiceFieldValues(int fieldNum) {
		return null;
	}

	@Override
	public void delete(long position) {
	}

	@Override
	public Object[] getEditValues(long id) {
		return null;
	}

	private static ABMListResult fields = null;
	
	public static final String[] fieldNames = {"Lugar", "Método de Pago", "Campo 1", "Campo 2", "Campo 3", "Campo 4", "Campo 5", "Campo 6", "Accion Auto"};
	
	@Override
	public ABMListResult getList(String parentIdField, long parentId) {
		if (fields == null) {
			fields = new ABMListResult();
			for (int i = 0; i < fieldNames.length; i++) {
				fields.add(i + 1, fieldNames[i]);
			}
		}
		return fields;
	}

	@Override
	public Object[] getViewValues(long id) {
		return null;
	}

	@Override
	public void save(long id, Object[] values) {
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
