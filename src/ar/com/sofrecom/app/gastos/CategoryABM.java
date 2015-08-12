package ar.com.sofrecom.app.gastos;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import ar.com.sofrecom.av.abm.ABMAdditionalMenu;
import ar.com.sofrecom.av.abm.ABMBase;
import ar.com.sofrecom.av.abm.ABMDatabaseDataMgr;
import ar.com.sofrecom.av.abm.ABMListResult;

public class CategoryABM extends ABMBase implements ABMDatabaseDataMgr {

	public static Intent getIntent(Context context) {
		return new Intent(context, CategoryABM.class);
	}
	
	@Override
	public HashMap<String, Object> getParameters(Context context) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put(ABMBase.ABM_RESOURCE_CLASS, ar.com.sofrecom.app.gastos.R.class);
		result.put(ABMBase.ABM_OPTIONS, ABMBase.OPT_EDIT_IN_CONTEXT + ABMBase.OPT_DONT_USE_VIEW +
				ABMBase.OPT_DATAMGR_DATABASE + ABMBase.OPT_REGISTER_ACCESS_TIME);
		result.put(ABMBase.ABM_CLICK_ITEM_INTENT, SubCategoryABM.getIntent(this));
		ABMAdditionalMenu addMenu = new ABMAdditionalMenu();
		addMenu.addListMenu(R.string.preferences, new Intent(context, Preferences.class));
		addMenu.addListMenu(R.string.editFile, new Intent(context, GastosEditActivity.class));
		addMenu.addListMenu(R.string.titleNumbers, new Intent(context, NumberNowActivity.class));
		addMenu.addListMenu(R.string.titleCar, AutoKmABM.getIntent(this));
		result.put(ABMBase.ABM_ADDITIONAL_MENU, addMenu);
		return result;
	}
		
	@Override
	public void onCreateActions() {
		setTitle(R.string.titleCategory);
		//init();
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
	public String[] getFieldNames() {
		String[] fields = {"NAME"};
		return fields;
	}

	public static final String DATABASE_NAME = "CATEGORY";
	
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
		return "NAME";
	}

	@Override
	public String getListOrder() {
		return "ACCESSED_DATE DESC";
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

/*	
	private void init() {
		abmTableProvider.save(-1, new Object[] {"Transporte"});
		abmTableProvider.save(-1, new Object[] {"Varios"});
		abmTableProvider.save(-1, new Object[] {"Vacaciones"});
		abmTableProvider.save(-1, new Object[] {"Sofrecom"});
		abmTableProvider.save(-1, new Object[] {"Salidas"});
		abmTableProvider.save(-1, new Object[] {"Medicina"});
		abmTableProvider.save(-1, new Object[] {"Excepcion"});
		abmTableProvider.save(-1, new Object[] {"Electronica"});
		abmTableProvider.save(-1, new Object[] {"Depto"});
		abmTableProvider.save(-1, new Object[] {"Deporte"});
		abmTableProvider.save(-1, new Object[] {"Comida"});
		abmTableProvider.save(-1, new Object[] {"Auto"});
		abmTableProvider.save(-1, new Object[] {"Ahorro"});
		abmTableProvider.save(-1, new Object[] {"Finanzas"});
	}
*/	
}
