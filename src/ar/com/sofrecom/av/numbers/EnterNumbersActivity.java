package ar.com.sofrecom.av.numbers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import ar.com.sofrecom.app.gastos.R;
import ar.com.sofrecom.av.util.GenLog;

public class EnterNumbersActivity extends Activity {
	private static final String TAG = "EnterNumbersActivity";
	private final static int ENTER_NUMBER_FIRST_INTEGER = 1;
	private final static int ENTER_NUMBER_NEXTS_INTEGER = 2;
	private final static int ENTER_NUMBER_FIRST_DECIMAL = 3;
	private final static int ENTER_NUMBER_NEXTS_DECIMAL = 4;

	private final static int ENTER_NUMBER_BUTTON_DOT = 10;
	private final static int ENTER_NUMBER_BUTTON_CLEAR = 11;

	private final static int ENTER_NUMBER_BUTTON_MULT = 12;
	private final static int ENTER_NUMBER_BUTTON_MINUS = 13;
	private final static int ENTER_NUMBER_BUTTON_DIV = 14;
	private final static int ENTER_NUMBER_BUTTON_ADD = 15;
	private final static int ENTER_NUMBER_BUTTON_MEM_M = 16;
	private final static int ENTER_NUMBER_BUTTON_MEM_C = 17;
	private final static int ENTER_NUMBER_BUTTON_MEM_A = 18;
	private final static int ENTER_NUMBER_BUTTON_EQUALS = 19;
	private final static int ENTER_NUMBER_BUTTON_MEM_R = 20;
	public final static int ENTER_NUMBER_BUTTON_OK = 21;
	public final static int ENTER_NUMBER_BUTTON_CANCEL = 22;
	public final static int ENTER_NUMBER_BUTTON_PREV = 23;
	public final static int ENTER_NUMBER_BUTTON_NEXT = 24;
	public final static int ENTER_NUMBER_BUTTON_SAVE = 25;
	
	public final static String ENTER_NUMBER_VALUE_STR = "VALUESTR";
	public final static String ENTER_NUMBER_TITLE = "TITLE";
	public final static String ENTER_NUMBER_FIELDNAME = "FIELDNAME";
	public final static String ENTER_NUMBER_PARAMS = "PARAMS";
	public final static String ENTER_NUMBER_RESULT = "RESULT";
	public final static String ENTER_NUMBER_PREV_TITLE = "PREV_TITLE";
	public final static String ENTER_NUMBER_NEXT_TITLE = "NEXT_TITLE";
	public final static String ENTER_NUMBER_SAVE_TITLE = "SAVE_TITLE";	
	public final static int ENTER_NUMBER_NO_PREV = 1;
	public final static int ENTER_NUMBER_NO_NEXT = 2;
	public final static int ENTER_NUMBER_NO_SAVE = 4;	

	private final static int ENTER_NUMBER_NO_OPERATION = 0;
	private EditText enterNumberText;
	private float enterNumberValue;
	private float enterNumberValuePrev;
	private String enterNumberValueStr;
	private int enterNumberCurrDigit;
	private int enterNumberOperation;
	private Button buttonMemory;
	private static float enterNumberMemory = 0;
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
        setContentView(R.layout.enter_numbers);
        enterNumberText = (EditText) findViewById(R.id.en_text);
        String valueStr = bundle.getString(ENTER_NUMBER_VALUE_STR);
        if (valueStr == null) valueStr = "0.0";
        enterNumberText.setText(valueStr);
        enterNumberValue = Float.parseFloat(enterNumberText.getText().toString());
        enterNumberValueStr = Float.toString(enterNumberValue);
        enterNumberCurrDigit = ENTER_NUMBER_FIRST_INTEGER;
		enterNumberOperation = ENTER_NUMBER_NO_OPERATION;
        setTitle(bundle.getString(ENTER_NUMBER_TITLE));
        for (int i = 0; i < 26; i++) {
	        Button button = (Button) findViewById(getIdValue("en_btn_" + i));
	        button.setTag(new Integer(i));
	        button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					updateNumberText((Button) v);
				}
			});
        }
        TextView fieldName = (TextView) findViewById(R.id.en_btn_fieldname);
        fieldName.setText(bundle.getString(ENTER_NUMBER_FIELDNAME));
        String title;
        if ((title = bundle.getString(ENTER_NUMBER_PREV_TITLE)) == null) {
        	((Button) findViewById(R.id.en_btn_23)).setText(R.string.enBtnPrev);
        } else {
        	((Button) findViewById(R.id.en_btn_23)).setText(title);
        }
        if ((title = bundle.getString(ENTER_NUMBER_NEXT_TITLE)) == null) {
        	((Button) findViewById(R.id.en_btn_24)).setText(R.string.enBtnNext);
        } else {
        	((Button) findViewById(R.id.en_btn_24)).setText(title);
        }
        if ((title = bundle.getString(ENTER_NUMBER_SAVE_TITLE)) == null) {
        	((Button) findViewById(R.id.en_btn_25)).setText(R.string.enBtnSave);
        } else {
        	((Button) findViewById(R.id.en_btn_25)).setText(title);
        }
        int params = bundle.getInt(ENTER_NUMBER_PARAMS, 0);
        if ((params & ENTER_NUMBER_NO_PREV) > 0) {
        	((Button) findViewById(R.id.en_btn_23)).setEnabled(false);
        	((Button) findViewById(R.id.en_btn_23)).setText("");
        }
        if ((params & ENTER_NUMBER_NO_NEXT) > 0) {
        	((Button) findViewById(R.id.en_btn_24)).setEnabled(false);
        	((Button) findViewById(R.id.en_btn_24)).setText("");
        }
        if ((params & ENTER_NUMBER_NO_SAVE) > 0) {
        	((Button) findViewById(R.id.en_btn_25)).setEnabled(false);
        	((Button) findViewById(R.id.en_btn_25)).setText("");
        }
        buttonMemory = (Button) findViewById(R.id.en_btn_20);
		showMemory();
    }

    private void updateNumberText(Button button) {
    	int buttonChosen = (Integer) button.getTag();
    	if (buttonChosen >= 0 && buttonChosen <= ENTER_NUMBER_BUTTON_CLEAR) {
    		commonDigitPressed(buttonChosen);
    	} else if (buttonChosen == ENTER_NUMBER_BUTTON_ADD ||
    			buttonChosen == ENTER_NUMBER_BUTTON_MINUS ||
    			buttonChosen == ENTER_NUMBER_BUTTON_MULT ||
    			buttonChosen == ENTER_NUMBER_BUTTON_DIV ||
    			buttonChosen == ENTER_NUMBER_BUTTON_EQUALS) {
			enterNumberCurrDigit = ENTER_NUMBER_FIRST_INTEGER;
			if (enterNumberOperation != ENTER_NUMBER_NO_OPERATION) {
				enterNumberValue = performOperation();
	    		enterNumberValuePrev = enterNumberValue;
			} else {
	    		enterNumberValuePrev = enterNumberValue;
				enterNumberValue = 0;
			}
			enterNumberValueStr = Float.toString(enterNumberValue);
			if (buttonChosen != ENTER_NUMBER_BUTTON_EQUALS) {
				enterNumberOperation = buttonChosen;
			} else {
				enterNumberOperation = ENTER_NUMBER_NO_OPERATION;
			}
    	} else if (buttonChosen == ENTER_NUMBER_BUTTON_MEM_C) {
	        enterNumberCurrDigit = ENTER_NUMBER_FIRST_INTEGER;
    		enterNumberMemory = 0;
    		showMemory();
    	} else if (buttonChosen == ENTER_NUMBER_BUTTON_MEM_A) {
	        enterNumberCurrDigit = ENTER_NUMBER_FIRST_INTEGER;
    		enterNumberMemory = round(enterNumberMemory + enterNumberValue);
    		showMemory();
    	} else if (buttonChosen == ENTER_NUMBER_BUTTON_MEM_M) {
	        enterNumberCurrDigit = ENTER_NUMBER_FIRST_INTEGER;
    		enterNumberMemory = round(enterNumberMemory - enterNumberValue);
    		showMemory();
    	} else if (buttonChosen == ENTER_NUMBER_BUTTON_MEM_R) {
	        enterNumberCurrDigit = ENTER_NUMBER_FIRST_INTEGER;
			enterNumberValue = enterNumberMemory;
			enterNumberValueStr = Float.toString(enterNumberValue);
    	} else if (buttonChosen == ENTER_NUMBER_BUTTON_OK || 
    			buttonChosen == ENTER_NUMBER_BUTTON_CANCEL || 
    			buttonChosen == ENTER_NUMBER_BUTTON_PREV || 
    			buttonChosen == ENTER_NUMBER_BUTTON_NEXT || 
    			buttonChosen == ENTER_NUMBER_BUTTON_SAVE) {
    		Intent data = new Intent();
    		data.putExtra(ENTER_NUMBER_RESULT, enterNumberValueStr);
    		setResult(buttonChosen, data);
    		finish();
    	}
    	enterNumberText.setText(enterNumberValueStr);
    }
    
    // Esto es solo valido desde 2.0
    @Override
    public void onBackPressed() {
		setResult(ENTER_NUMBER_BUTTON_CANCEL, null);
    	super.onBackPressed();
    }
    
    private void commonDigitPressed(int buttonChosen) {
    	if ((buttonChosen >= 0) && (buttonChosen <= 9)) {
    		switch (enterNumberCurrDigit) {
			case ENTER_NUMBER_FIRST_INTEGER:
				enterNumberValue = buttonChosen;
				if (enterNumberValue != 0) {
					enterNumberCurrDigit = ENTER_NUMBER_NEXTS_INTEGER;
				}
				enterNumberValueStr = Float.toString(enterNumberValue);
				break;
			case ENTER_NUMBER_NEXTS_INTEGER:
				enterNumberValue = enterNumberValue * 10 + buttonChosen;
				enterNumberValueStr = Float.toString(enterNumberValue);
				break;
			case ENTER_NUMBER_FIRST_DECIMAL:
				enterNumberValue += ((float) buttonChosen) / 10;
				enterNumberCurrDigit = ENTER_NUMBER_NEXTS_DECIMAL;
				enterNumberValueStr = Float.toString(enterNumberValue);
				break;
			case ENTER_NUMBER_NEXTS_DECIMAL:
				enterNumberValueStr += Integer.toString(buttonChosen);
				enterNumberValue = Float.parseFloat(enterNumberValueStr);
				break;
			}
    	} else if (buttonChosen == ENTER_NUMBER_BUTTON_DOT) {
    		if ((enterNumberCurrDigit == ENTER_NUMBER_FIRST_INTEGER) || (enterNumberCurrDigit == ENTER_NUMBER_NEXTS_INTEGER)) {
    			enterNumberCurrDigit = ENTER_NUMBER_FIRST_DECIMAL;
    		}
    	} else if (buttonChosen == ENTER_NUMBER_BUTTON_CLEAR) {
			enterNumberValue = 0;
			enterNumberValueStr = Float.toString(enterNumberValue);
			enterNumberCurrDigit = ENTER_NUMBER_FIRST_INTEGER;
			enterNumberOperation = ENTER_NUMBER_NO_OPERATION;
    	}
    }
    
    private float performOperation() {
    	float result = 0;
    	switch (enterNumberOperation) {
		case ENTER_NUMBER_BUTTON_ADD:
			result =  enterNumberValuePrev + enterNumberValue;
			break;
		case ENTER_NUMBER_BUTTON_MINUS:
			result = enterNumberValuePrev - enterNumberValue;
			break;
		case ENTER_NUMBER_BUTTON_MULT:
			result = enterNumberValuePrev * enterNumberValue;
			break;
		case ENTER_NUMBER_BUTTON_DIV:
			result = enterNumberValuePrev / enterNumberValue;
			break;
		}
    	return round(result);
    }
    
    private float round(float number) {
    	return (float) (Math.round(number * 100.0f) / 100.0f);
    }
    
    private void showMemory() {
    	buttonMemory.setText("M: " + Float.toString(enterNumberMemory));
    }
    
    private int getIdValue(String fieldName) {
		int result = 0;
		try {
			result = R.id.class.getField(fieldName).getInt(null);
		} catch (Exception e) {
			GenLog.err(TAG, e);
		}
		return result;
	}

	@SuppressWarnings("unused")
	private int getStringValue(String fieldName) {
		int result = 0;
		try {
			result = R.string.class.getField(fieldName).getInt(null);
		} catch (Exception e) {
			GenLog.err(TAG, e);
		}
		return result;
	}
	
}
