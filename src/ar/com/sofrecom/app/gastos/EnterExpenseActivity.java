package ar.com.sofrecom.app.gastos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import ar.com.sofrecom.av.abm.ABMBase;
import ar.com.sofrecom.av.numbers.EnterNumbersActivity;
import ar.com.sofrecom.av.util.GenLog;

public class EnterExpenseActivity extends Activity {

    private static final String TAG = "EnterExpenseActivity";

    private SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dateFormatHour = new SimpleDateFormat("HH:mm:ss");
	private LinearLayout editLayout;
	private LinearLayout editScreenLayout;
	private Button dateButton = null;
	private Object[] fieldValues = null;
	private String[] fieldTitles = new String[12];
	private int[] fieldTypes = new int[12];
	@SuppressWarnings("unused")
	private String script = null;
	private String catName = null;
	private String subCatName = null;
	private String subCatSummary = null;
	private int fieldQtty;
	private int fieldChoosenPos;
	private Button buttonFieldChoosen;
	private EditText enterNumberText;
	private EditText enterNumberTextAdd;
	private float enterNumberValue;
	private float enterNumberValuePrev;
	private static float enterNumberMemory = 0;
	private Button buttonMemory;
	private String enterNumberValueStr;
	private float enterNumberValueOrig;
	private int enterNumberCurrDigit;
	private int enterNumberOperation;
	private Dialog currentDialog;
	
	private final static int ENTER_NUMBER_FIRST_INTEGER = 1;
	private final static int ENTER_NUMBER_NEXTS_INTEGER = 2;
	private final static int ENTER_NUMBER_FIRST_DECIMAL = 3;
	private final static int ENTER_NUMBER_NEXTS_DECIMAL = 4;

	private final static int ENTER_NUMBER_BUTTON_DOT = 10;
	private final static int ENTER_NUMBER_BUTTON_CLEAR = 11;
	private final static int ENTER_NUMBER_BUTTON_PREV = 12;
	private final static int ENTER_NUMBER_BUTTON_NEXT = 13;
	private final static int ENTER_NUMBER_BUTTON_MORE = 14;
	private final static int ENTER_NUMBER_BUTTON_SAVE = 15;

	private final static int ENTER_NUMBER_BUTTON_MULT = 12;
	private final static int ENTER_NUMBER_BUTTON_MINUS = 13;
	private final static int ENTER_NUMBER_BUTTON_DIV = 14;
	private final static int ENTER_NUMBER_BUTTON_ADD = 15;
	private final static int ENTER_NUMBER_BUTTON_MEM_M = 16;
	private final static int ENTER_NUMBER_BUTTON_MEM_C = 17;
	private final static int ENTER_NUMBER_BUTTON_MEM_A = 18;
	private final static int ENTER_NUMBER_BUTTON_EQUALS = 19;
	private final static int ENTER_NUMBER_BUTTON_MEM_R = 20;

	private final static int ENTER_NUMBER_NO_OPERATION = 0;

	
	public final static int TYPE_STRING = 1;
	public final static int TYPE_DIGITS = 2;
	public final static int TYPE_NUMBER = 3;
	public final static int TYPE_DATE = 4;
	public final static int TYPE_INTEGER = 5;
	public final static int TYPE_ID_CHOICE = 6;
	public final static int TYPE_MULTIPLE_CHOICE = 7;
	public final static int TYPE_BOOLEAN = 8;
	public final static int TYPE_PASSWORD = 9;
	public final static int TYPE_STRING_AUTOCOMPLETE = 10;

	private final static int SAVE = 1;
	private final static int DISCARD = 2;
	private final static int SELECT_DATE = 3;
	private final static int ENTER_NUMBER = 4;
	private final static int ENTER_NUMBER_ADD = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			initialize();
			createEdit();
			loadDefaultValues();
			launchEdit();
			getViewAt(1).performClick();
		} catch (Exception e) {
			GenLog.err(TAG, e);
		}
	}

	private void initialize() {
		fieldTitles[0] = getString(R.string.titleDate);
		fieldTitles[1] = getString(R.string.titleRealPrice);
		fieldTitles[2] = getString(R.string.titleTicketPrice);
		fieldTitles[3] = getString(R.string.titleDiscount);
		fieldTitles[4] = getString(R.string.titlePlace);
		fieldTitles[5] = getString(R.string.titlePayMethod);
		fieldTitles[6] = getString(R.string.titleField1);
		fieldTitles[7] = getString(R.string.titleField2);
		fieldTitles[8] = getString(R.string.titleField3);
		fieldTitles[9] = getString(R.string.titleField4);
		fieldTitles[10] = getString(R.string.titleField5);
		fieldTitles[11] = getString(R.string.titleField6);
		fieldTypes[0] = TYPE_DATE;
		fieldTypes[1] = TYPE_NUMBER;
		fieldTypes[2] = TYPE_NUMBER;
		fieldTypes[3] = TYPE_NUMBER;
		fieldTypes[4] = TYPE_STRING_AUTOCOMPLETE;
		fieldTypes[5] = TYPE_STRING_AUTOCOMPLETE;
		fieldTypes[6] = TYPE_STRING_AUTOCOMPLETE;
		fieldTypes[7] = TYPE_STRING_AUTOCOMPLETE;
		fieldTypes[8] = TYPE_STRING_AUTOCOMPLETE;
		fieldTypes[9] = TYPE_STRING_AUTOCOMPLETE;
		fieldTypes[10] = TYPE_STRING_AUTOCOMPLETE;
		fieldTypes[11] = TYPE_STRING_AUTOCOMPLETE;
	}
	
	private void loadDefaultValues() {
		Bundle bundle = getIntent().getExtras();
		long id = bundle.getLong(ABMBase.ABM_CALLER_ID);
    	SubCategoryHelper subCategoryHelper = new SubCategoryHelper(SubCategoryABM.DATABASE_NAME, SubCategoryABM.VERSION_NUMBER, this);
    	SQLiteDatabase subCategoryDB = subCategoryHelper.getReadableDatabase();
    	Cursor cursor = subCategoryDB.rawQuery("SELECT REALPRICE, TICKETPRICE, DISCOUNT, PLACE, PAYMETHOD, FIELD1, FIELD2, FIELD3, FIELD4, FIELD5, FIELD6, SCRIPT, NAME, SUMMARY, CAT_ID FROM " + SubCategoryABM.DATABASE_NAME + " WHERE _id = " + id, null);
    	long catId = 0;
		if ((cursor != null) && (cursor.getCount() == 1) && cursor.moveToFirst()) {
			fieldValues = new Object[12];
			fieldValues[0] = Main.getDefaultDate(this);
			for (int i = 0; i < 11; i++) {
				if ((i >= 0) && (i <= 2)) {
					fieldValues[i + 1] = cursor.getFloat(i);
				} else {
					fieldValues[i + 1] = cursor.getString(i);
					if ((i >= 5) && (i <= 10)) {
						// TODO 
//						{n,titulo,valor}: el campo correspondiente será de tipo numérico, con el título indicado y el valor por 
//						omisión
//						{s,titulo,valor}: el campo correspondiente será de tipo alfanumérico, con el título indicado y el valor 
//						por omisión
//						{i} el campo correspondiente y los siguientes no aparecen en el formulario
					}
				}
			}
			fieldQtty = 12;
			script = cursor.getString(11);
			subCatName = cursor.getString(12);
			subCatSummary = cursor.getString(13);
			catId = cursor.getLong(14);
		}
		cursor.close();
		subCategoryHelper.close();
    	CategoryHelper categoryHelper = new CategoryHelper(CategoryABM.DATABASE_NAME, CategoryABM.VERSION_NUMBER, this);
    	SQLiteDatabase categoryDB = categoryHelper.getReadableDatabase();
    	cursor = categoryDB.rawQuery("SELECT NAME FROM " + CategoryABM.DATABASE_NAME + " WHERE _id = " + catId, null);
		if ((cursor != null) && (cursor.getCount() == 1) && cursor.moveToFirst()) {
			catName = cursor.getString(0);
		}
		cursor.close();
		categoryHelper.close();
		setTitle(catName + " - " + subCatName + " " + subCatSummary + " - " + dateFormatDay.format(Main.getDefaultDate(this).getTime()));
	}
	
	private void createEdit() {
		editLayout = new LinearLayout(this);
		editLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		editLayout.setOrientation(LinearLayout.VERTICAL);
		editLayout.setScrollContainer(true);

		LinearLayout buttonsLayout = new LinearLayout(this);
		buttonsLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
		View value = new Button(this);
		((Button) value).setText(R.string.abm_save);
		((Button) value).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onMenuButtonSelect(SAVE);
			}
		});
		buttonsLayout.addView(value);
		value = new Button(this);
		((Button) value).setText(R.string.abm_discard);
		((Button) value).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onMenuButtonSelect(DISCARD);
			}
		});
		buttonsLayout.addView(value);
		setContentView(R.layout.abm_edit);
		ScrollView scrollView = (ScrollView) findViewById(R.id.abmScrollEdit);
		scrollView.addView(editLayout);
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.abmButtonsEdit);
		relativeLayout.addView(buttonsLayout);
		editScreenLayout = (LinearLayout) findViewById(R.id.abmEditLayout);
	}
	
	private void launchEdit() {
		View valueView = null;
		editLayout.removeAllViews();
		for (int i = 0; i < fieldQtty; i++) {
			TextView label = new TextView(this);
			label.setText(fieldTitles[i]);
			editLayout.addView(label);
			Object value = fieldValues[i];
			switch (fieldTypes[i]) {
				case TYPE_STRING:
					valueView = new EditText(this);
					((EditText) valueView).setText((String) value);
					break;
				case TYPE_STRING_AUTOCOMPLETE:
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, FieldValuesLists.getFieldValueList(i - 4, this));
					valueView = new AutoCompleteTextView(this);
					((AutoCompleteTextView) valueView).setAdapter(adapter);
					((AutoCompleteTextView) valueView).setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							((AutoCompleteTextView) v).showDropDown();
						}
					});
					((AutoCompleteTextView) valueView).setText((String) value);
					break;
				case TYPE_NUMBER:
					valueView = new Button(this);
			        ((Button) valueView).setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							buttonFieldChoosen = (Button) v;
							//showDialog(ENTER_NUMBER);
							enterNumber();
						}
					});
			        ((Button) valueView).setTextSize(18);
			        ((Button) valueView).setGravity(Gravity.LEFT);
					((Button) valueView).setText(((Float) value).toString());
					break;
				case TYPE_DATE:
					valueView = new Button(this);
			        ((Button) valueView).setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							showDialog(SELECT_DATE);
						}
					});
			        ((Button) valueView).setTextSize(18);
			        ((Button) valueView).setGravity(Gravity.LEFT);
					((Button) valueView).setText(dateFormatDay.format(((Calendar) value).getTime()));
					dateButton = (Button) valueView;
					break;
			}
			valueView.setTag(new Integer(i));
			editLayout.addView(valueView);
		}
		setContentView(editScreenLayout);
	}
	
	
	
    private void onMenuButtonSelect(int id) {
    	if (id == SAVE) {
    		saveValues();
    		finish();
    	} else if (id == DISCARD) {
    		finish();
    	}
    }
    
    private void enterNumber() {
        fieldChoosenPos = ((Integer) buttonFieldChoosen.getTag());
        Intent enterNumbers = new Intent(this, EnterNumbersActivity.class);
        enterNumbers.putExtra(EnterNumbersActivity.ENTER_NUMBER_VALUE_STR, buttonFieldChoosen.getText());
		enterNumbers.putExtra(EnterNumbersActivity.ENTER_NUMBER_TITLE, catName + " - " + subCatName + " " + subCatSummary + " - " + dateFormatDay.format(Main.getDefaultDate(this).getTime()));
		enterNumbers.putExtra(EnterNumbersActivity.ENTER_NUMBER_FIELDNAME, fieldTitles[fieldChoosenPos]);
		enterNumbers.putExtra(EnterNumbersActivity.ENTER_NUMBER_PARAMS, 0);
    	startActivityForResult(enterNumbers, fieldChoosenPos);
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO: Esta actividad podría haberse terminado mientras la llamada estaba en foreground (ver ciclo de vida de las actividades)
		String resultNumber = null;
		if (data != null) {
			resultNumber = data.getStringExtra(EnterNumbersActivity.ENTER_NUMBER_RESULT);
		}
		if (resultCode == EnterNumbersActivity.ENTER_NUMBER_BUTTON_OK) {
			acceptNumber(resultNumber);
		} else if (resultCode == EnterNumbersActivity.ENTER_NUMBER_BUTTON_NEXT) {
			acceptNumber(resultNumber);
			nextField();
		} else if (resultCode == EnterNumbersActivity.ENTER_NUMBER_BUTTON_PREV) {
			acceptNumber(resultNumber);
			previousField();
		} else if (resultCode == EnterNumbersActivity.ENTER_NUMBER_BUTTON_SAVE) {
			acceptNumber(resultNumber);
			saveValues();
			finish();
		}
	}

    private void acceptNumber(String resultNumber) {
    	buttonFieldChoosen.setText(resultNumber);
    	fieldValues[fieldChoosenPos] = Float.parseFloat(resultNumber);
    }
		
	@Override
	protected Dialog onCreateDialog(int id) {
		try {
			if (id == SELECT_DATE) {
				GregorianCalendar calendar = new GregorianCalendar();
				calendar.setTime(Main.getDefaultDate(this).getTime());
		        DatePickerDialog dialog = new DatePickerDialog(this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		        dialog.setTitle(R.string.abm_datepicker_title);
		        return dialog;
			} else if (id == ENTER_NUMBER) {
		        LayoutInflater factory = LayoutInflater.from(this);
		        final View textEntryView = factory.inflate(R.layout.enter_number, null);
		        for (int i = 0; i < 16; i++) {
			        Button button = (Button) textEntryView.findViewById(getIdValue("enter_number_button_" + i));
			        button.setTag(new Integer(i));
			        button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							updateNumberText((Button) v);
						}
					});
		        }
		        AlertDialog dialog = new AlertDialog.Builder(this)
	            	.setTitle("dummy")
		            .setView(textEntryView)
		            .setPositiveButton(R.string.abm_ok, new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int whichButton) {
		                	saveNumber();
		                }
		            })
		            .setNegativeButton(R.string.abm_discard, new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int whichButton) {
		                }
		            })
		            .setOnCancelListener(new DialogInterface.OnCancelListener() {
						public void onCancel(DialogInterface arg0) {
						}
		            })
		            .create();
		        return dialog;
			} else if (id == ENTER_NUMBER_ADD) {
		        LayoutInflater factory = LayoutInflater.from(this);
		        final View textEntryView = factory.inflate(R.layout.enter_number_add, null);
		        for (int i = 0; i < 21; i++) {
			        Button button = (Button) textEntryView.findViewById(getIdValue("enter_number_add_button_" + i));
			        button.setTag(new Integer(i));
			        button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							updateNumberTextAdd((Button) v);
						}
					});
		        }
		        buttonMemory = (Button) textEntryView.findViewById(getIdValue("enter_number_add_button_20"));
		        AlertDialog dialog = new AlertDialog.Builder(this)
	            	.setTitle("dummy")
		            .setView(textEntryView)
		            .setPositiveButton(R.string.abm_ok, new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int whichButton) {
		                	numberAddOk();
		                }
		            })
		            .setNegativeButton(R.string.abm_discard, new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int whichButton) {
		                	numberAddCancel();
		                }
		            })
		            .setOnCancelListener(new DialogInterface.OnCancelListener() {
						public void onCancel(DialogInterface arg0) {
		                	numberAddCancel();
						}
		            })
		            .create();
		        return dialog;
			}
		} catch (Exception e) {
			GenLog.err(TAG, e);
		}
		return null;
	}
	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		try {
			if (id == SELECT_DATE) {
				GregorianCalendar calendar = new GregorianCalendar();
				calendar.setTime(Main.getDefaultDate(this).getTime());
				((DatePickerDialog) dialog).updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			} else if (id == ENTER_NUMBER) {
		        enterNumberText = (EditText) dialog.findViewById(R.id.enter_number_text);
		        enterNumberText.setText(buttonFieldChoosen.getText());
		        enterNumberValue = Float.parseFloat(buttonFieldChoosen.getText().toString());
		        enterNumberValueStr = Float.toString(enterNumberValue);
		        enterNumberCurrDigit = ENTER_NUMBER_FIRST_INTEGER;
		        fieldChoosenPos = ((Integer) buttonFieldChoosen.getTag());
	            dialog.setTitle(fieldTitles[fieldChoosenPos]);
	            currentDialog = dialog;
			} else if (id == ENTER_NUMBER_ADD) {
				enterNumberValueOrig = enterNumberValue;
		        enterNumberTextAdd = (EditText) dialog.findViewById(R.id.enter_number_add_text);
		        enterNumberTextAdd.setText(enterNumberValueStr);
	            dialog.setTitle(fieldTitles[fieldChoosenPos]);
		        enterNumberCurrDigit = ENTER_NUMBER_FIRST_INTEGER;
				enterNumberOperation = ENTER_NUMBER_NO_OPERATION;
				showMemory();
			}
		} catch (Exception e) {
			GenLog.err(TAG, e);
		}
	}
	
    // the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, 
                                      int monthOfYear, int dayOfMonth) {
                    updateDateField(year, monthOfYear, dayOfMonth);
                }
            };

    private void updateNumberText(Button button) {
    	int buttonChosen = (Integer) button.getTag();
    	if (buttonChosen >= 0 && buttonChosen <= ENTER_NUMBER_BUTTON_CLEAR) {
    		commonDigitPressed(buttonChosen);
    	} else if (buttonChosen == ENTER_NUMBER_BUTTON_NEXT) {
    		saveNumber();
    		currentDialog.dismiss();
    		nextField();
    	} else if (buttonChosen == ENTER_NUMBER_BUTTON_PREV) {
    		saveNumber();
    		currentDialog.dismiss();
    		previousField();
    	} else if (buttonChosen == ENTER_NUMBER_BUTTON_SAVE) {
    		saveNumber();
    		currentDialog.dismiss();
    		saveValues();
    		finish();
    	} else if (buttonChosen == ENTER_NUMBER_BUTTON_MORE) {
    		showDialog(ENTER_NUMBER_ADD);
    	}
    	enterNumberText.setText(enterNumberValueStr);
    }
    
    private void updateNumberTextAdd(Button button) {
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
    		enterNumberMemory += enterNumberValue;
    		showMemory();
    	} else if (buttonChosen == ENTER_NUMBER_BUTTON_MEM_M) {
	        enterNumberCurrDigit = ENTER_NUMBER_FIRST_INTEGER;
    		enterNumberMemory -= enterNumberValue;
    		showMemory();
    	} else if (buttonChosen == ENTER_NUMBER_BUTTON_MEM_R) {
	        enterNumberCurrDigit = ENTER_NUMBER_FIRST_INTEGER;
			enterNumberValue = enterNumberMemory;
			enterNumberValueStr = Float.toString(enterNumberValue);
    	}
    	enterNumberTextAdd.setText(enterNumberValueStr);
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
    	switch (enterNumberOperation) {
		case ENTER_NUMBER_BUTTON_ADD:
			return enterNumberValuePrev + enterNumberValue;
		case ENTER_NUMBER_BUTTON_MINUS:
			return enterNumberValuePrev - enterNumberValue;
		case ENTER_NUMBER_BUTTON_MULT:
			return enterNumberValuePrev * enterNumberValue;
		case ENTER_NUMBER_BUTTON_DIV:
			return enterNumberValuePrev / enterNumberValue;
		}
    	return 0;
    }
    
    private void saveNumber() {
    	buttonFieldChoosen.setText(enterNumberValueStr);
    	fieldValues[fieldChoosenPos] = new Float(enterNumberValue);
    }
    
    private void numberAddOk() {
    	enterNumberText.setText(enterNumberValueStr);
    }
    
    private void numberAddCancel() {
		enterNumberValue = enterNumberValueOrig;
		enterNumberValueStr = Float.toString(enterNumberValue);
    	enterNumberText.setText(enterNumberValueStr);
    }
    
    private void nextField() {
		if ((fieldChoosenPos + 1) < fieldQtty) {
			getViewAt(fieldChoosenPos + 1).performClick();
		}
    }
    
    private void previousField() {
		if (fieldChoosenPos > 0) {
			getViewAt(fieldChoosenPos - 1).performClick();
		}
    }
    
    private void showMemory() {
    	buttonMemory.setText("M: " + Float.toString(enterNumberMemory));
    }
    
    private void updateDateField(int year, int monthOfYear, int dayOfMonth) {
		Calendar date = new GregorianCalendar();
		date.set(Calendar.YEAR, year);
		date.set(Calendar.MONTH, monthOfYear);
		date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		fieldValues[0] = date;
		Main.setDefaultDate(date);
		dateButton.setText(dateFormatDay.format(date.getTime()));
    }

    private View getViewAt(int position) {
    	return editLayout.getChildAt(position * 2 + 1);
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

	private void loadTextFields() {
		int i = 0;
		for (int fieldType : fieldTypes) {
			View field = editLayout.findViewWithTag(i);
			if (field != null) {
				switch (fieldType) {
					case TYPE_STRING:
					case TYPE_STRING_AUTOCOMPLETE:
						fieldValues[i] = ((EditText) field).getText().toString();
						break;
					case TYPE_NUMBER:
					case TYPE_DATE:
					case TYPE_ID_CHOICE:
					case TYPE_MULTIPLE_CHOICE:
						// fieldValues update already done
						break;
				}
			}
			i++;
		}
	}
	
	private void saveValues() {
		loadTextFields();
		SharedPreferences prefs = getSharedPreferences(Main.PREF_PACKAGE, Context.MODE_PRIVATE);
		String fileName = prefs.getString("datadir", "") + "/gastos.csv";
		StringBuffer line = new StringBuffer(catName + ";" + subCatName + ";" + dateFormatDay.format(((Calendar) fieldValues[0]).getTime()));
		for (int i = 1; i < 12; i++) {
			line.append(';');
			line.append(fieldValues[i].toString());
		}
		GregorianCalendar now = new GregorianCalendar();
		line.append(';');
		line.append(dateFormatDay.format(now.getTime()));
		line.append(';');
		line.append(dateFormatHour.format(now.getTime()));
		line.append('\n');
		try {
			BufferedWriter outWriter = new BufferedWriter(new FileWriter(fileName, true));
			outWriter.write(line.toString(), 0, line.length());
			outWriter.close();
			updateFieldValues();
		} catch (Exception e) {
			GenLog.err(TAG, e);
		}
	}
	
	private void updateFieldValues() {
		FieldValuesLists.startUpdateFieldValues(this);
		for (int i = 4; i <= 11; i++) {
			if (!fieldValues[i].toString().equals("")) {
				FieldValuesLists.updateFieldValue(i - 4, fieldValues[i].toString());
			}
		}
		FieldValuesLists.stopUpdateFieldValues();		
	}

    private static class SubCategoryHelper extends SQLiteOpenHelper {
		@Override
		public void onCreate(SQLiteDatabase arg0) {
		}
		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		}
    	SubCategoryHelper(String name, int version, Context context) {
            super(context, name, null, version);
    	}
    }
    private static class CategoryHelper extends SQLiteOpenHelper {
		@Override
		public void onCreate(SQLiteDatabase arg0) {
		}
		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		}
    	CategoryHelper(String name, int version, Context context) {
            super(context, name, null, version);
    	}
    }
}
