package ar.com.sofrecom.app.gastos;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import ar.com.sofrecom.av.abm.ABMTableProvider;
import ar.com.sofrecom.av.numbers.EnterNumbersActivity;

public class NumberNowActivity extends Activity {

    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent enterNumbers = new Intent(this, EnterNumbersActivity.class);
		enterNumbers.putExtra(EnterNumbersActivity.ENTER_NUMBER_TITLE, getString(R.string.app_name));
		enterNumbers.putExtra(EnterNumbersActivity.ENTER_NUMBER_FIELDNAME, dateTimeFormat.format((new GregorianCalendar()).getTime()));
		enterNumbers.putExtra(EnterNumbersActivity.ENTER_NUMBER_PARAMS, EnterNumbersActivity.ENTER_NUMBER_NO_NEXT + EnterNumbersActivity.ENTER_NUMBER_NO_SAVE);
		enterNumbers.putExtra(EnterNumbersActivity.ENTER_NUMBER_PREV_TITLE, getString(R.string.titleList));
    	startActivityForResult(enterNumbers, 0);
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO: Esta actividad podría haberse terminado mientras la llamada estaba en foreground (ver ciclo de vida de las actividades)
		String resultNumber = null;
		if (data != null) {
			resultNumber = data.getStringExtra(EnterNumbersActivity.ENTER_NUMBER_RESULT);
		}
		if (resultCode == EnterNumbersActivity.ENTER_NUMBER_BUTTON_OK) {
			insertNumber(resultNumber);
		} else if (resultCode == EnterNumbersActivity.ENTER_NUMBER_BUTTON_PREV) {
	        startActivity(NumbersABM.getIntent(this));
		}
        finish();
	}
	
	private void insertNumber(String numberStr) {
		ABMTableProvider numbersTableProvider = new ABMTableProvider(NumbersABM.DATABASE_NAME, NumbersABM.VERSION_NUMBER, NumbersABM.FIELD_NAMES, NumbersABM.FIELD_TYPES, this);
		numbersTableProvider.open();
    	Object[] values = {Float.parseFloat(numberStr), new GregorianCalendar()};
    	numbersTableProvider.save(-1, values);
		numbersTableProvider.close();
	}

}