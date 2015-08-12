package ar.com.sofrecom.app.gastos;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import ar.com.sofrecom.av.abm.ABMListResult;
import ar.com.sofrecom.av.abm.ABMTableProvider;

public class FieldValuesLists {

	private static ArrayList<String[]> fieldValues = null;
	
	private static boolean reloadFieldValues = true;
	
	private static ABMTableProvider fieldValuesTableProvider;
	
	public static void setReloadFieldValues() {
		reloadFieldValues = true;
	}

	public static String[] getFieldValueList(int id, Context context) {
		if (fieldValues == null || reloadFieldValues) {
			loadFieldValues(context);
		}
		return fieldValues.get(id);

	}

	private static void loadFieldValues(Context context) {
		fieldValuesTableProvider = new ABMTableProvider(FieldValueABM.DATABASE_NAME, FieldValueABM.VERSION_NUMBER, FieldValueABM.FIELD_NAMES, FieldValueABM.FIELD_TYPES, context);
		fieldValuesTableProvider.open();
		fieldValues = new ArrayList<String[]>();
    	for (int i = 0; i < FieldsABM.fieldNames.length; i++) {
    		ABMListResult list = fieldValuesTableProvider.list("VALUE", "ACCESSED_DATE DESC", "FIELD_ID", i + 1);
    		int qtty = list.getTitles().size();
    		String[] values = new String[qtty];
    		if (qtty > 0) {
    			list.getTitles().toArray(values);
    		}
    		fieldValues.add(values);
    	}
		reloadFieldValues = false;
		fieldValuesTableProvider.close();
	}

	public static void startUpdateFieldValues(Context context) {
		fieldValuesTableProvider = new ABMTableProvider(FieldValueABM.DATABASE_NAME, FieldValueABM.VERSION_NUMBER, FieldValueABM.FIELD_NAMES, FieldValueABM.FIELD_TYPES, context);
		fieldValuesTableProvider.open();
	}
	
	public static void stopUpdateFieldValues() {
		fieldValuesTableProvider.close();
	}
	
	public static void updateFieldValue(long fieldId, String fieldValue) {
		long id = -1;
    	Cursor cursor = fieldValuesTableProvider.executeQuery("SELECT _id FROM " + FieldValueABM.DATABASE_NAME + " WHERE FIELD_ID = " + (fieldId + 1) + " AND VALUE = '" + fieldValue + "'");
    	if ((cursor != null) && (cursor.getCount() == 1) && cursor.moveToFirst()) {
    		id = cursor.getLong(0);
		}
    	cursor.close();
    	Object[] values = {fieldValue, new Long(fieldId + 1)};
    	fieldValuesTableProvider.save(id, values);
	}
}
