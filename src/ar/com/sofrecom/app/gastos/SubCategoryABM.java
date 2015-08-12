package ar.com.sofrecom.app.gastos;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import ar.com.sofrecom.av.abm.ABMBase;
import ar.com.sofrecom.av.abm.ABMDatabaseDataMgr;
import ar.com.sofrecom.av.abm.ABMListResult;

public class SubCategoryABM extends ABMBase implements ABMDatabaseDataMgr {

	public static Intent getIntent(Context context) {
		return new Intent(context, SubCategoryABM.class);
	}
	
	@Override
	public HashMap<String, Object> getParameters(Context context) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put(ABMBase.ABM_RESOURCE_CLASS, ar.com.sofrecom.app.gastos.R.class);
		result.put(ABMBase.ABM_OPTIONS, ABMBase.OPT_DATAMGR_DATABASE + 
				ABMBase.OPT_REGISTER_ACCESS_TIME + ABMBase.OPT_EDIT_IN_CONTEXT + 
				ABMBase.OPT_DUPLICATE_IN_CONTEXT);
		result.put(ABMBase.ABM_FIELD_PARENT_ID, "CAT_ID");
		/*
		TODO: Borrar - Prueba con EnterNumbersActivity
		Intent enterNumbers = new Intent(context, EnterNumbersActivity.class);
		enterNumbers.putExtra(EnterNumbersActivity.ENTER_NUMBER_TITLE, "Entrando Numero");
		enterNumbers.putExtra(EnterNumbersActivity.ENTER_NUMBER_FIELDNAME, "Precio Real");
		enterNumbers.putExtra(EnterNumbersActivity.ENTER_NUMBER_PARAMS, EnterNumbersActivity.ENTER_NUMBER_NO_NEXT);
		result.put(ABMBase.ABM_CLICK_ITEM_INTENT, enterNumbers);
		*/
		result.put(ABMBase.ABM_CLICK_ITEM_INTENT, new Intent(context, EnterExpenseActivity.class));
//		Intent syncIntent = new Intent(context, SyncActivity.class);
//		syncIntent.putExtra(SyncActivity.ACTION, SyncActivity.ACTION_CHECK_SYNC);
//		ABMAdditionalMenu addMenu = new ABMAdditionalMenu();
//		addMenu.addViewMenu(R.string.configCheck, syncIntent);
//		result.put(ABMBase.ABM_ADDITIONAL_MENU, addMenu);
//		result.put(ABMBase.ABM_VIEW_BUTTON_TITLE, new Integer(R.string.processSend));
//		Intent syncIntent = new Intent(context, SendActivity.class);
//		syncIntent.putExtra(SendActivity.ACTION, SendActivity.ACTION_SEND);
//		result.put(ABMBase.ABM_VIEW_BUTTON_INTENT, syncIntent);
//		ABMAdditionalMenu addMenu = new ABMAdditionalMenu();
//		addMenu.addListMenu(R.string.titleViewResult, new Intent(context, ResultActivity.class));
//		addMenu.addEditMenu(R.string.titleViewResult, new Intent(context, ResultActivity.class));
//		addMenu.addViewMenu(R.string.titleViewResult, new Intent(context, ResultActivity.class));
//		result.put(ABMBase.ABM_ADDITIONAL_MENU, addMenu);
		return result;
	}
		
	@Override
	public void onCreateActions() {
		setTitle(getParentTitle());
		//init();
	}

	@Override
	public int[] getFieldTitles() {
		int[] fields = {
				R.string.titleName,
				R.string.titleSummary,
				R.string.titleRealPrice,
				R.string.titleTicketPrice,
				R.string.titleDiscount,
				R.string.titlePlace,
				R.string.titlePayMethod,
				R.string.titleField1,
				R.string.titleField2,
				R.string.titleField3,
				R.string.titleField4,
				R.string.titleField5,
				R.string.titleField6,
				R.string.titleScript
			};
		return fields;
	}

	@Override
	public int[] getFieldTypes() {
		int[] types = {
				ABMBase.TYPE_STRING,
				ABMBase.TYPE_STRING,
				ABMBase.TYPE_NUMBER,
				ABMBase.TYPE_NUMBER,
				ABMBase.TYPE_NUMBER,
				ABMBase.TYPE_STRING_AUTOCOMPLETE,
				ABMBase.TYPE_STRING_AUTOCOMPLETE,
				ABMBase.TYPE_STRING_AUTOCOMPLETE,
				ABMBase.TYPE_STRING_AUTOCOMPLETE,
				ABMBase.TYPE_STRING_AUTOCOMPLETE,
				ABMBase.TYPE_STRING_AUTOCOMPLETE,
				ABMBase.TYPE_STRING_AUTOCOMPLETE,
				ABMBase.TYPE_STRING_AUTOCOMPLETE,
				ABMBase.TYPE_STRING,
				ABMBase.TYPE_INTEGER
			};
		return types;
	}

	@Override
	public Object[] getInsertValues() {
		Object[] values = {"", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", getParentId()};
		return values;
	}

	@Override
	public String[] getFieldNames() {
		String[] fields = {
				"NAME", 
				"SUMMARY", 
				"REALPRICE", 
				"TICKETPRICE", 
				"DISCOUNT", 
				"PLACE", 
				"PAYMETHOD", 
				"FIELD1", 
				"FIELD2", 
				"FIELD3", 
				"FIELD4", 
				"FIELD5", 
				"FIELD6", 
				"SCRIPT", 
				"CAT_ID"
			};
		return fields;
	}

	public static final String DATABASE_NAME = "SUBCATEGORY";
	
	@Override
	public String getDatabaseName() {
		return DATABASE_NAME;
	}

	public static final int VERSION_NUMBER = 2;
	
	@Override
	public int getVersionNumber() {
		return VERSION_NUMBER;
	}

	@Override
	public String getListColumns() {
		return "NAME || ' ' || SUMMARY";
	}

	@Override
	public String getListOrder() {
		return "ACCESSED_DATE DESC";
	}

	@Override
	protected void launchSave() {
		super.launchSave();
		Object[] values = getFieldValues();
		FieldValuesLists.startUpdateFieldValues(this);
		for (int i = 5; i <= 12; i++) {
			if (!values[i].toString().equals("")) {
				FieldValuesLists.updateFieldValue(i - 5, values[i].toString());
			}
		}
		FieldValuesLists.stopUpdateFieldValues();
	}
	
	@Override
	public ABMListResult getChoiceFieldValues(int fieldNum) {
		return null;
	}

	@Override
	public String[] getAutocompleteFieldValues(int fieldNum) {
		if ((fieldNum >= 5) && (fieldNum <= 12)) {
			return FieldValuesLists.getFieldValueList(fieldNum - 5, this);
		}
		return null;
	}

	@Override
	protected boolean iniWhenEmpty() {
		return false;
	}

/*
	private void init() {
		abmTableProvider.save(-1, new Object[] {"Nafta y peaje", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(1)});
		abmTableProvider.save(-1, new Object[] {"Tren", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(1)});
		abmTableProvider.save(-1, new Object[] {"Trapito", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(1)});
		abmTableProvider.save(-1, new Object[] {"Taxi", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(1)});
		abmTableProvider.save(-1, new Object[] {"Subte", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(1)});
		abmTableProvider.save(-1, new Object[] {"Remis", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(1)});
		abmTableProvider.save(-1, new Object[] {"Garage", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(1)});
		abmTableProvider.save(-1, new Object[] {"Estacionamiento", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(1)});
		abmTableProvider.save(-1, new Object[] {"Colectivo", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(1)});
		abmTableProvider.save(-1, new Object[] {"Charter", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(1)});
		abmTableProvider.save(-1, new Object[] {"Nafta", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(1)});
		abmTableProvider.save(-1, new Object[] {"Peaje cap", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(1)});
		abmTableProvider.save(-1, new Object[] {"Peaje prov", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(1)});
		abmTableProvider.save(-1, new Object[] {"Peajes", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(1)});
		abmTableProvider.save(-1, new Object[] {"Peaje otro", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(1)});
		abmTableProvider.save(-1, new Object[] {"Bus", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(1)});
		abmTableProvider.save(-1, new Object[] {"Tramite", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(2)});
		abmTableProvider.save(-1, new Object[] {"Teatro-Hace", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(2)});
		abmTableProvider.save(-1, new Object[] {"Skype", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(2)});
		abmTableProvider.save(-1, new Object[] {"Ropa", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(2)});
		abmTableProvider.save(-1, new Object[] {"Revista", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(2)});
		abmTableProvider.save(-1, new Object[] {"Regalos-NC", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(2)});
		abmTableProvider.save(-1, new Object[] {"Regalos", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(2)});
		abmTableProvider.save(-1, new Object[] {"Regalo-NC", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(2)});
		abmTableProvider.save(-1, new Object[] {"Perfumeria", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(2)});
		abmTableProvider.save(-1, new Object[] {"Peluqueria", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(2)});
		abmTableProvider.save(-1, new Object[] {"Pavadas", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(2)});
		abmTableProvider.save(-1, new Object[] {"Nada", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(2)});
		abmTableProvider.save(-1, new Object[] {"Lustrado", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(2)});
		abmTableProvider.save(-1, new Object[] {"Locutorio", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(2)});
		abmTableProvider.save(-1, new Object[] {"Limpieza C", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(2)});
		abmTableProvider.save(-1, new Object[] {"Limosna", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(2)});
		abmTableProvider.save(-1, new Object[] {"Libreria", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(2)});
		abmTableProvider.save(-1, new Object[] {"Kiosco", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(2)});
		abmTableProvider.save(-1, new Object[] {"Info Sync", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(2)});
		abmTableProvider.save(-1, new Object[] {"Forros", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(2)});
		abmTableProvider.save(-1, new Object[] {"Donaciones", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(2)});
		abmTableProvider.save(-1, new Object[] {"DVD", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(2)});
		abmTableProvider.save(-1, new Object[] {"Propina", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(2)});
		abmTableProvider.save(-1, new Object[] {"c-Almacen", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(3)});
		abmTableProvider.save(-1, new Object[] {"Pasaje", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(3)});
		abmTableProvider.save(-1, new Object[] {"Comienzo", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(3)});
		abmTableProvider.save(-1, new Object[] {"Equipo", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(3)});
		abmTableProvider.save(-1, new Object[] {"Pase ski", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(3)});
		abmTableProvider.save(-1, new Object[] {"Hostel", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(3)});
		abmTableProvider.save(-1, new Object[] {"Fin", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(3)});
		abmTableProvider.save(-1, new Object[] {"Tren", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(4)});
		abmTableProvider.save(-1, new Object[] {"Taxi", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(4)});
		abmTableProvider.save(-1, new Object[] {"Subte", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(4)});
		abmTableProvider.save(-1, new Object[] {"Remis", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(4)});
		abmTableProvider.save(-1, new Object[] {"Reintegro varios", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(4)});
		abmTableProvider.save(-1, new Object[] {"Merienda", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(4)});
		abmTableProvider.save(-1, new Object[] {"Estacionamiento", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(4)});
		abmTableProvider.save(-1, new Object[] {"Desayuno", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(4)});
		abmTableProvider.save(-1, new Object[] {"Comida", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(4)});
		abmTableProvider.save(-1, new Object[] {"Cena", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(4)});
		abmTableProvider.save(-1, new Object[] {"Almuerzo", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(4)});
		abmTableProvider.save(-1, new Object[] {"Insumos", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(4)});
		abmTableProvider.save(-1, new Object[] {"Facturas", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(4)});
		abmTableProvider.save(-1, new Object[] {"Varios", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Trapito", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Teatro", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Taxi", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Regalos", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Recital", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Prestado", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Poker", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Otros", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Nada", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Museo", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Kiosco", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Helado", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Guardarropas", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Familia", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Estacionamiento", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Deporte", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Delivery", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Clase", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Cine", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Cena-Restaurant", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Cena", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Bowling", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Boliche", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Bebidas", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Bar", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Almuerzo", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Cigarrillos", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(5)});
		abmTableProvider.save(-1, new Object[] {"Vitamina C", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Varios", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Twinrix", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Solucion fisiologica", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Remedios", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Relajante", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Presion", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Plantillas", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Parches Chinos", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Off", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Ibuprofeno", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Gotas", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Derrumal", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Crema", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Coto", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Aspirinas", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Antigripales", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Alcohol", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Agua Blanca Codex", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Actron", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Optica", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Podologo", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Eucasol", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Clonacepan", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Tiras respira mejor", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Dexarinospray", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Dormir", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(6)});
		abmTableProvider.save(-1, new Object[] {"Problema", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(7)});
		abmTableProvider.save(-1, new Object[] {"Prestado", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(7)});
		abmTableProvider.save(-1, new Object[] {"Intereses HSBC", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(7)});
		abmTableProvider.save(-1, new Object[] {"Grua", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(7)});
		abmTableProvider.save(-1, new Object[] {"Gastos Vero", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(7)});
		abmTableProvider.save(-1, new Object[] {"Exceso Cajero", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(7)});
		abmTableProvider.save(-1, new Object[] {"Flete", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(7)});
		abmTableProvider.save(-1, new Object[] {"Palm", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(8)});
		abmTableProvider.save(-1, new Object[] {"Insumos", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(8)});
		abmTableProvider.save(-1, new Object[] {"Hardware", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(8)});
		abmTableProvider.save(-1, new Object[] {"Ferreteria", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(8)});
		abmTableProvider.save(-1, new Object[] {"Celular", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(8)});
		abmTableProvider.save(-1, new Object[] {"Camara", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(8)});
		abmTableProvider.save(-1, new Object[] {"Audio Video", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(8)});
		abmTableProvider.save(-1, new Object[] {"Otros", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(8)});
		abmTableProvider.save(-1, new Object[] {"Tramites", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Tintoreria", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Telefonica", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Supermercado", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Planchado", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Otros", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Metrogas", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Limpieza", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Lavanderia", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Kiosco", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Jumbo", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Insumos", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Ferreteria", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Expensas", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Equipamiento", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"EDESUR", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Coto", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Costurero", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Cocina", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Carrefour", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Cafe", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Cablevision", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Balance", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"AySA", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Alquiler", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Almacen", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"ABL", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Muebles", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Easy", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Disco", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Libreria", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Carrefour", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"Farmacity", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(9)});
		abmTableProvider.save(-1, new Object[] {"YMCA-Toallas", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"YMCA", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Tenis-Tournament", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Tenis-Toalla", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Tenis-Pelotas", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Tenis-Insumos", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Tenis-Hollywood", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Tenis-Estacionamiento", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Tenis-Encordado", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Tenis-Clase", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Tenis-Circuito-Torneo", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Tenis-Circuito-Carnet", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Tenis-Cancha", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Tenis-Bebidas", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Running", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Ropa", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Pelotas Tenis", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Golf-Palos", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Golf-Equipo", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Golf-Clase", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Golf-Canasto", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Futbol-toalla", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Futbol-partido", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Futbol-bebidas", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Estacionamiento", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Bici", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Bebidas", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Tenis-Taxi", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Gel energizante", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(10)});
		abmTableProvider.save(-1, new Object[] {"Vino", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Viejos", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Sofre-Simple", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Sofre-Roncal", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Sofre-Restaurant", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Sofre-Reserva", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Sofre-Plaza", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Sofre-Piacere", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Sofre-Mineral", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Sofre-McD", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Sofre-Kiosco", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Sofre-Ford", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Sofre-Facon", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Sofre-Eat", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Sofre-Citi", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Sofre-BuenLibro", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Sofre-Brioche", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Sofre-Alimentari", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"San Jose", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Miel", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Merienda-super", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Merienda", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"McDonalds", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"MP3", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Lights Coffee", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Kiosco", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Jumbo", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Gaseosa", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Facturas", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Expendedora", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"El ombu", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Desayuno", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Coto", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Comedor", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Chino", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Cena-Restaurant", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Cena-Delivery", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Cena", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Carrefour", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Cafe", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Asado", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Almuerzo-Restaurant", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Almuerzo-Delivery", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Almuerzo", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Almacen", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Alcohol", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Sofre-Nextel", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Disco", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Delivery-propina", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Deliv-CrazyCheese", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Deliv-Pasadena", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Sofre-LeBar", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Carrefour", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Sofre-DownTown", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Super", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Sofre-Sabatico", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Verduleria", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Carrito", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(11)});
		abmTableProvider.save(-1, new Object[] {"Seguro", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(12)});
		abmTableProvider.save(-1, new Object[] {"Patente", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(12)});
		abmTableProvider.save(-1, new Object[] {"Otros", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(12)});
		abmTableProvider.save(-1, new Object[] {"Lavado", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(12)});
		abmTableProvider.save(-1, new Object[] {"Cambio aceite", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(12)});
		abmTableProvider.save(-1, new Object[] {"Arreglo", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(12)});
		abmTableProvider.save(-1, new Object[] {"Multa", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(12)});
		abmTableProvider.save(-1, new Object[] {"Zurich Int", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(13)});
		abmTableProvider.save(-1, new Object[] {"HSBC NYL", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(13)});
		abmTableProvider.save(-1, new Object[] {"Euros", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(13)});
		abmTableProvider.save(-1, new Object[] {"Dolares", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(13)});
		abmTableProvider.save(-1, new Object[] {"Mantenimiento", "", new Float(0), new Float(0), new Float(0), "", "", "", "", "", "", "", "", "", new Long(14)});
	}
*/
}
