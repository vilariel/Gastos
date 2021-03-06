package ar.com.sofrecom.av.abm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import ar.com.sofrecom.av.util.GenLog;

public abstract class ABMBase extends Activity {
	
    private static final String TAG = "ABMBase";

    private ListView list;
    private TableLayout table;
	private LinearLayout viewLayout;
	private LinearLayout editLayout;
	private LinearLayout viewScreenLayout;
	private LinearLayout editScreenLayout;
	private Class<?> stringResourceClass;
	private Class<?> layoutResourceClass;
	private Class<?> idResourceClass;
	private Object[] fieldValues;
	private ABMCustomDataMgr abmCustomDataMgr;
	private ABMDatabaseDataMgr abmDatabaseMgr;
	protected ABMTableProvider abmTableProvider;
	private Button buttonFieldChoosen;
	private boolean[] currentMultiCheckedItems;
    private HashMap<Class<?>, ABMBase> subABMClasses = new HashMap<Class<?>, ABMBase>();
    private ABMAdditionalMenu additionalMenu = null;
	private long itemId;
	private ABMListResult itemList = new ABMListResult();
	private int currScreen;
	
	private int viewButtonTitleId;
	private Intent viewButtonIntent = null;
	private Intent itemClickIntent = null;
	private long parentId;
	private String parentTitle;
	private String parentIdField = null;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	protected final static int LIST = 1;
	protected final static int VIEW = 2;
	protected final static int EDIT = 3;
	protected final static int DELETE = 4;
	protected final static int INSERT = 5;
	protected final static int SAVE = 6;
	protected final static int DISCARD = 7;
	protected final static int SELECT_DATE = 8;
	protected final static int SELECT_TIME = 9;
	protected final static int VIEWBUTTON = 10;
	protected final static int SELECT_SINGLE = 11;
	protected final static int SELECT_MULTIPLE = 12;
	protected final static int DUPLICATE = 13;
	// Must be greater than the last menu constant:
	private final static int FIRST_ADDITIONAL_MENU_ENTRY = 100;
	
	public static final String ABM_OPTIONS = "options";
	public static final String ABM_RESOURCE_CLASS = "R.class";
	public static final String ABM_VIEW_BUTTON_TITLE = "viewbuttontitleid";
	public static final String ABM_VIEW_BUTTON_INTENT = "viewbuttonactivity";
	public static final String ABM_CLICK_ITEM_INTENT = "clickitemactivity";
	public static final String ABM_CALLER_ID = "abmParentId";
	public static final String ABM_CALLER_TITLE = "abmParentTitle";
	public static final String ABM_ADDITIONAL_MENU = "abmAdditionalMenu";
	public static final String ABM_FIELD_PARENT_ID = "fieldToFilterBy";

	public final static int OPT_DEFAULT = 0;
	public final static int OPT_DONT_USE_VIEW = 1;
	public final static int OPT_EDIT_IN_CONTEXT = 2;
	public final static int OPT_VIEW_IN_CONTEXT = 4;
	public final static int OPT_DATAMGR_DATABASE = 8;
	public final static int OPT_DATAMGR_CUSTOM = 16;
	public final static int OPT_REGISTER_ACCESS_TIME = 32;
	public final static int OPT_DUPLICATE_IN_CONTEXT = 64;
	public final static int OPT_READ_ONLY = 128;

	private boolean optDontUseView;
	private boolean optViewInContext;
	private boolean optEditInContext;
	private boolean optDatamgrDatabase;
	private boolean optDatamgrCustom;
	private boolean optRegisterAccessTime;
	private boolean optDuplicateInContext;
	private boolean optReadOnly;
	
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
	public final static int TYPE_DATE_TIME = 11;
	
	public final static String BOOLEAN_LABEL_SPLITTER = " - ";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			fieldValues = new Object[getFieldTypes().length];
			resolveParameters(this);
			onCreateActions();
			createList();
			createView();
			createEdit();
			launchList();
		} catch (Exception e) {
			GenLog.errorMsg(TAG, this, e);
		}
	}

	private void createList() {
		list = new ListView(this);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				clickItem(id);
			}
		});
		list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				createContextMenu(menu, v, menuInfo);
			}
		});
	}
	
	@SuppressWarnings("unused")
	private void createTable() {
		table = new TableLayout(this);
//		table.setOnItemClickListener(new OnItemClickListener() {
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				clickItem(id);
//			}
//		});
		table.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				createContextMenu(menu, v, menuInfo);
			}
		});
	}
	
	private void createView() {
		setContentView(getLayoutValue("abm_view"));
		viewLayout = (LinearLayout) findViewById(getIdValue("abmViewContainer"));
		//viewLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		//viewLayout.setOrientation(LinearLayout.VERTICAL);
		//viewLayout.setScrollContainer(true);

		/*
		LayoutInflater inflater = this.getLayoutInflater();
		View labelLayout = inflater.inflate(getLayoutValue("abm_view_label"), null);
		View valueLayout = inflater.inflate(getLayoutValue("abm_view_value"), null);
		*/
		XmlPullParser labelParser = getResources().getXml(getLayoutValue("abm_view_label"));
		AttributeSet labelAttrs = Xml.asAttributeSet(labelParser);
		XmlPullParser valueParser = getResources().getXml(getLayoutValue("abm_view_value"));
		AttributeSet valueAttrs = Xml.asAttributeSet(valueParser);
		int[] fieldTitles = getFieldTitles();
		int i = 0;
		for (int fieldTitle : fieldTitles) {
			TextView label = new TextView(this, labelAttrs);
			TextView value = new TextView(this, valueAttrs);
			label.setText(fieldTitle);
			value.setTag("abm_field" + i);
			// Como no puedo hacer andar todavia los xml de atributos
			label.setTextSize(20);
			value.setTextSize(14);
			value.setPadding(5, 0, 0, 0);
			viewLayout.addView(label);
			viewLayout.addView(value);
			i++;
		}
		LinearLayout buttonsLayout = new LinearLayout(this);
		buttonsLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
		View value = new Button(this);
		((Button) value).setText(viewButtonTitleId);
		((Button) value).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onMenuButtonSelect(VIEWBUTTON);
			}
		});
		buttonsLayout.addView(value);
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(getIdValue("abmButtonsView"));
		relativeLayout.addView(buttonsLayout);
		viewScreenLayout = (LinearLayout) findViewById(getIdValue("abmViewLayout"));
	}
	
	private void createEdit() {
		editLayout = new LinearLayout(this);
		int i = 0;
		editLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		editLayout.setOrientation(LinearLayout.VERTICAL);
		editLayout.setScrollContainer(true);
		int[] fieldTitles = getFieldTitles();
		int[] fieldTypes = getFieldTypes();
		View value = null;
		Resources res = getResources();
		for (int fieldTitle : fieldTitles) {
			TextView label = new TextView(this);
			label.setText(fieldTitle);
			editLayout.addView(label);
			switch (fieldTypes[i]) {
				case TYPE_STRING:
					value = new EditText(this);
					break;
				case TYPE_STRING_AUTOCOMPLETE:
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getAutocompleteFieldValues(i));
					value = new AutoCompleteTextView(this);
					((AutoCompleteTextView) value).setAdapter(adapter);
					((AutoCompleteTextView) value).setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							((AutoCompleteTextView) v).showDropDown();
						}
					});
					break;
				case TYPE_PASSWORD:
					value = new EditText(this);
					((EditText) value).setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
					break;
				case TYPE_NUMBER:
					value = new EditText(this);
					((EditText) value).setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
					break;
				case TYPE_BOOLEAN:
					value = new CheckBox(this);
					String text = res.getString(fieldTitle);
					String[] labels = text.split(BOOLEAN_LABEL_SPLITTER);
					label.setText(labels[0]);
					if (labels.length > 1) {
						((CheckBox) value).setText(labels[1]);						
					}
					break;
				case TYPE_DIGITS:
					value = new EditText(this);
					((EditText) value).setInputType(InputType.TYPE_CLASS_NUMBER);
					break;
				case TYPE_DATE:
				case TYPE_DATE_TIME:
					value = new Button(this);
			        ((Button) value).setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							buttonFieldChoosen = (Button) v;
							showDialog(SELECT_DATE);
						}
					});
			        ((Button) value).setTextSize(18);
			        ((Button) value).setGravity(Gravity.LEFT);
					break;
				case TYPE_ID_CHOICE:
					value = new Button(this);
			        ((Button) value).setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							showChooseSingleItemDialog((Button) v);
						}
					});
			        ((Button) value).setTextSize(18);
			        ((Button) value).setGravity(Gravity.LEFT);
					break;
				case TYPE_MULTIPLE_CHOICE:
					value = new Button(this);
			        ((Button) value).setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							showChooseMultipleItemDialog((Button) v);
						}
					});
			        ((Button) value).setTextSize(18);
			        ((Button) value).setGravity(Gravity.LEFT);
					break;
				case TYPE_INTEGER:
					value = new EditText(this);
					((EditText) value).setInputType(InputType.TYPE_CLASS_NUMBER);
					break;
			}
			value.setTag("abm_field" + i++);
			editLayout.addView(value);
		}
		LinearLayout buttonsLayout = new LinearLayout(this);
		buttonsLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
		value = new Button(this);
		((Button) value).setText(getStringValue("abm_save"));
		((Button) value).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onMenuButtonSelect(SAVE);
			}
		});
		buttonsLayout.addView(value);
		value = new Button(this);
		((Button) value).setText(getStringValue("abm_discard"));
		((Button) value).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onMenuButtonSelect(DISCARD);
			}
		});
		buttonsLayout.addView(value);
		setContentView(getLayoutValue("abm_edit"));
		ScrollView scrollView = (ScrollView) findViewById(getIdValue("abmScrollEdit"));
		scrollView.addView(editLayout);
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(getIdValue("abmButtonsEdit"));
		relativeLayout.addView(buttonsLayout);
		editScreenLayout = (LinearLayout) findViewById(getIdValue("abmEditLayout"));
	}
	
	public ABMListResult getItemList() {
		try {
			if (optDatamgrCustom) {
				return abmCustomDataMgr.getList(parentIdField, parentId);
			} else if (optDatamgrDatabase) {
				/*
				No puedo hacer andar el SimpleCursorAdapter.
				Dejo el codigo porque es la manera correcta de hacerlo 
				int[] listFields = abmDatabaseMgr.getListFields();
				String[] projection = new String[listFields.length + 1];
				String[] displayFields = new String[listFields.length];
				String[] fieldNames = abmDatabaseMgr.getFieldNames();
				projection[0] = "_ID";
				int i = 0;
				for (int listField : listFields) {
					projection[i + 1] = fieldNames[listField];
					displayFields[i] = fieldNames[listField];
					i++;
				}
				Cursor cursor = abmTableProvider.list(projection);
				int[] displayViews = new int[] { getIdValue("text1") };
				list.setAdapter(new SimpleCursorAdapter(this, getLayoutValue("abm_list"), cursor, displayFields, displayViews));
				cursor.close();
				*/
				return abmTableProvider.list(abmDatabaseMgr.getListColumns(), abmDatabaseMgr.getListOrder(), parentIdField, parentId);
			}
		} catch (Exception e) {
			GenLog.errorMsg(TAG, this, e);
		}
		return null;
	}
	
	protected Object[] getFieldValues() {
		return fieldValues;
	}
	
	protected abstract boolean iniWhenEmpty();
	
	protected void launchList() {
		itemList = getItemList();
		if (itemList.size() == 0) {
			if (iniWhenEmpty()) {
				itemList = getItemList();
				if (itemList.size() == 0) {
					Toast.makeText(this, getStringValue("abm_empty_list_message"), Toast.LENGTH_SHORT).show();
				}				
			}
		}
		list.setAdapter(new ArrayAdapter<String>(this, getLayoutValue("abm_list"), itemList.getTitles()));
		currScreen = LIST;
		setContentView(list);
	}

	protected void launchView() {
		if (optDatamgrCustom) {
			fieldValues = abmCustomDataMgr.getViewValues(itemId);
		} else if (optDatamgrDatabase) {
			fieldValues = abmTableProvider.getViewValues(itemId);
		}
		String text;
		int[] fieldTypes = getFieldTypes();
		int qtty = getFieldTitles().length;
		for (int i = 0; i < qtty; i++) {
			Object value = fieldValues[i];
			TextView field = (TextView) viewLayout.findViewWithTag("abm_field" + i);
			switch (fieldTypes[i]) {
			case TYPE_STRING:
			case TYPE_STRING_AUTOCOMPLETE:
				field.setText((String) value);
				break;
			case TYPE_PASSWORD:
				field.setText((String) value);
				((TextView) field).setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
				break;
			case TYPE_NUMBER:
			case TYPE_DIGITS:
			case TYPE_INTEGER:
				field.setText(value.toString());
				break;
			case TYPE_BOOLEAN:
				if (((Long) value).longValue() == 1) {
					field.setText(getStringValue("abm_yes"));
				} else {
					field.setText(getStringValue("abm_no"));
				}
				break;
			case TYPE_DATE:
				field.setText(dateFormat.format(((Calendar) value).getTime()));
				break;
			case TYPE_DATE_TIME:
				field.setText(dateTimeFormat.format(((Calendar) value).getTime()));
				break;
			case TYPE_ID_CHOICE:
				text = getChoiceFieldValues(i).getTitle((Long) value);
				if (text != null) {
					field.setText(text);
				} else {
					field.setText(getStringValue("abm_sublist_invalid_value"));
				}
				break;
			case TYPE_MULTIPLE_CHOICE:
				String label = "";
				String[] choices = ((String) value).split(",");
				for (String choice : choices) {
					if ((choice != null) && (!choice.equals(""))) {
						text = getChoiceFieldValues(i).getTitle(Long.parseLong(choice));
						if (text != null) {
							if (!label.equals("")) label += ", ";
							label += text;
						}
					}
				}
				if (!label.equals("")) {
					field.setText(label);
				} else {
					field.setText(getStringValue("abm_multiple_empty"));
				}
				break;
			}
		}
		currScreen = VIEW;
		setContentView(viewScreenLayout);
	}

	protected void launchEdit(boolean duplicate) {
		if (itemId >= 0) {
			if (optDatamgrCustom) {
				fieldValues = abmCustomDataMgr.getEditValues(itemId);
			} else if (optDatamgrDatabase) {
				fieldValues = abmTableProvider.getEditValues(itemId);
			}
			if (duplicate) {
				itemId = -1;
			}
		} else {
			fieldValues = getInsertValues();
		}
		String text;
		int[] fieldTypes = getFieldTypes();
		int qtty = getFieldTitles().length;
		for (int i = 0; i < qtty; i++) {
			Object value = fieldValues[i];
			View field = editLayout.findViewWithTag("abm_field" + i);
			switch (fieldTypes[i]) {
				case TYPE_STRING:
				case TYPE_STRING_AUTOCOMPLETE:
				case TYPE_PASSWORD:
					((EditText) field).setText((String) value);
					break;
				case TYPE_NUMBER:
				case TYPE_DIGITS:
					((EditText) field).setText(((Float) value).toString());
					break;
				case TYPE_INTEGER:
					((EditText) field).setText(((Long) value).toString());
					break;
				case TYPE_BOOLEAN:
					((CheckBox) field).setChecked(((Long) value).longValue() == 1);
					break;
				case TYPE_DATE:
				case TYPE_DATE_TIME:
					((Button) field).setText(dateFormat.format(((Calendar) value).getTime()));
					break;
				case TYPE_ID_CHOICE:
					text = getChoiceFieldValues(i).getTitle((Long) value);
					if (text != null) {
						((Button) field).setText(text);
					} else {
						((Button) field).setText(getStringValue("abm_sublist_invalid_value"));
					}
					break;
				case TYPE_MULTIPLE_CHOICE:
					String label = "";
					String[] choices = ((String) value).split(",");
					for (String choice : choices) {
						if ((choice != null) && (!choice.equals(""))) {
							text = getChoiceFieldValues(i).getTitle(Long.parseLong(choice));
							if (text != null) {
								if (!label.equals("")) label += ", ";
								label += text;
							}
						}
					}
					if (!label.equals("")) {
						((Button) field).setText(label);
					} else {
						((Button) field).setText(getStringValue("abm_multiple_empty"));
					}
					break;
			}
		}
		currScreen = EDIT;
		setContentView(editScreenLayout);
	}
	
	private void askForDeletion() {
		showDialog(DELETE);
	}

	protected void launchSave() {
		int i = 0;
		int[] fieldTypes = getFieldTypes();
		for (int fieldType : fieldTypes) {
			View field = editLayout.findViewWithTag("abm_field" + i);
			if (field != null) {
				switch (fieldType) {
					case TYPE_STRING:
					case TYPE_STRING_AUTOCOMPLETE:
					case TYPE_PASSWORD:
						fieldValues[i] = ((EditText) field).getText().toString();
						break;
					case TYPE_NUMBER:
					case TYPE_DIGITS:
						fieldValues[i] = Float.parseFloat(((EditText) field).getText().toString());
						break;
					case TYPE_INTEGER:
						fieldValues[i] = Long.parseLong(((EditText) field).getText().toString());
						break;
					case TYPE_BOOLEAN:
						fieldValues[i] = (((CheckBox) field).isChecked()) ? new Long(1) : new Long(0);
						break;
					case TYPE_DATE:
					case TYPE_DATE_TIME:
					case TYPE_ID_CHOICE:
					case TYPE_MULTIPLE_CHOICE:
						// fieldValues update already done in:
						//  updateDateField()
						//  updateSingleChoiceField()
						//  updateMultiChoiceField()
						break;
				}
			}
			i++;
		}
		if (optDatamgrCustom) {
			abmCustomDataMgr.save(itemId, fieldValues);
		} else if (optDatamgrDatabase) {
			abmTableProvider.save(itemId, fieldValues);
		}
		launchList();
	}
	
	protected void launchDelete() {
		if (optDatamgrCustom) {
	        abmCustomDataMgr.delete(itemId);
		} else if (optDatamgrDatabase) {
			abmTableProvider.delete(itemId);
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

    private void updateDateField(int year, int monthOfYear, int dayOfMonth) {
		int fieldNumber = Integer.parseInt(buttonFieldChoosen.getTag().toString().substring("abm_field".length()));
		Calendar date = new GregorianCalendar();
		date.set(Calendar.YEAR, year);
		date.set(Calendar.MONTH, monthOfYear);
		date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		fieldValues[fieldNumber] = date;
		buttonFieldChoosen.setText(dateFormat.format(date.getTime()));
    }

    private void showChooseSingleItemDialog(Button chooseButton) {
    	buttonFieldChoosen = chooseButton;
    	try {
    		removeDialog(SELECT_SINGLE);
    	} catch (Exception e) {
    	}
		showDialog(SELECT_SINGLE);
    }
    
    private void showChooseMultipleItemDialog(Button chooseButton) {
    	buttonFieldChoosen = chooseButton;
    	try {
    		removeDialog(SELECT_MULTIPLE);
    	} catch (Exception e) {
    	}
		showDialog(SELECT_MULTIPLE);
    }
    
    protected void updateSingleChoiceField(int whichButton) {
		int fieldNumber = Integer.parseInt(buttonFieldChoosen.getTag().toString().substring("abm_field".length()));
		Long id = getChoiceFieldValues(fieldNumber).getId(whichButton);
		fieldValues[fieldNumber] = id;
		buttonFieldChoosen.setText(getChoiceFieldValues(fieldNumber).getTitle(id));
    }
    
	protected void clickMultiChoiceField(int which, boolean isChecked) {
		currentMultiCheckedItems[which] = isChecked;
	}

	protected void updateMultiChoiceField() {
		int fieldNumber = Integer.parseInt(buttonFieldChoosen.getTag().toString().substring("abm_field".length()));
		String value = "";
		int qtty = currentMultiCheckedItems.length;
		ABMListResult choiceFieldValues = getChoiceFieldValues(fieldNumber);
		String label = "";
		for (int i = 0; i < qtty; i++) {
			if (currentMultiCheckedItems[i]) {
				if (!value.equals("")) {
					value += ",";
					label += ", ";
				}
				value += choiceFieldValues.getId(i);
				label += choiceFieldValues.getTitleAt(i);
			}
		}
		fieldValues[fieldNumber] = value;
		if (!label.equals("")) {
			buttonFieldChoosen.setText(label);
		} else {
			buttonFieldChoosen.setText(getStringValue("abm_multiple_empty"));
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		try {
			if (id == DELETE) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(getStringValue("abm_del_msg"))
				       .setCancelable(true)
				       .setPositiveButton(getStringValue("abm_yes"), new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   launchDelete();
				        	   launchList();
				           }
				       })
				       .setNegativeButton(getStringValue("abm_no"), new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				                dialog.cancel();
				           }
				       });
				AlertDialog alert = builder.create();
				return alert;
			} else if (id == SELECT_DATE) {
				GregorianCalendar calendar = new GregorianCalendar();
				calendar.setTime(dateFormat.parse(buttonFieldChoosen.getText().toString()));
		        DatePickerDialog dialog = new DatePickerDialog(this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		        dialog.setTitle(getStringValue("abm_datepicker_title"));
		        return dialog;
			} else if (id == SELECT_SINGLE) {
				int fieldNumber = Integer.parseInt(buttonFieldChoosen.getTag().toString().substring("abm_field".length()));
				String[] items = new String[getChoiceFieldValues(fieldNumber).getTitles().size()];
				getChoiceFieldValues(fieldNumber).getTitles().toArray(items);
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(getStringValue("abm_singlelist_title"))
	            .setNegativeButton(getStringValue("abm_discard"), new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                }
	            })
	            .setSingleChoiceItems(items, getChoiceFieldValues(fieldNumber).getPosition((Long) fieldValues[fieldNumber]), new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	updateSingleChoiceField(whichButton);
	                	dialog.dismiss();
	                }
	            });
				AlertDialog alert = builder.create();
				return alert;
			} else if (id == SELECT_MULTIPLE) {
				int fieldNumber = Integer.parseInt(buttonFieldChoosen.getTag().toString().substring("abm_field".length()));
				ABMListResult choiceFieldValues = getChoiceFieldValues(fieldNumber);
				int qtty = choiceFieldValues.getTitles().size();
				String[] items = new String[qtty];
				choiceFieldValues.getTitles().toArray(items);
				currentMultiCheckedItems = new boolean[qtty];
				String[] choices = ((String) fieldValues[fieldNumber]).split(",");
				for (String choice : choices) {
					if ((choice != null) && (!choice.equals(""))) {
						int position = choiceFieldValues.getPosition(Long.parseLong(choice));
						if (position > -1) {
							currentMultiCheckedItems[position] = true;
						}
					}
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(getStringValue("abm_multiplelist_title"))
	            .setPositiveButton(getStringValue("abm_ok"), new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	updateMultiChoiceField();
	                }
	            })
	            .setNegativeButton(getStringValue("abm_discard"), new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                }
	            })
	            .setMultiChoiceItems(items, currentMultiCheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
	                	clickMultiChoiceField(which, isChecked);
					}
	            });
				AlertDialog alert = builder.create();
				return alert;
			}
		} catch (Exception e) {
			GenLog.errorMsg(TAG, this, e);
		}
		return null;
	}
	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		try {
			if (id == SELECT_DATE) {
				GregorianCalendar calendar = new GregorianCalendar();
				calendar.setTime(dateFormat.parse(buttonFieldChoosen.getText().toString()));
				((DatePickerDialog) dialog).updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			}
		} catch (Exception e) {
			GenLog.errorMsg(TAG, this, e);
		}
	}
	
	/**
	 * Closes databases connections if necessary
	 */
	public void close() {
		if (optDatamgrDatabase) {
			abmTableProvider.close();
		}
		for (ABMBase subABMInstance : subABMClasses.values()) {
			subABMInstance.close();
		}
	}
	
    // TODO Esto es solo valido desde 2.0
    @Override
    public void onBackPressed() {
    	try {
	    	switch (currScreen) {
			case LIST:
				close();
				super.onBackPressed();
				break;
			case VIEW:
				launchList();
				break;
			case EDIT:
				if ((itemId == -1) || optDontUseView) {
					launchList();
				} else {
					launchView();
				}
				break;
			default:
				super.onBackPressed();
				break;
			}
		} catch (Exception e) {
			GenLog.errorMsg(TAG, this, e);
		}
    }
    
    
    private void createContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(itemList.getTitles().get((int) info.id));
        if (optViewInContext) {
            menu.add(0, VIEW, 0, getStringValue("abm_view"));
        }
        if (optEditInContext) {
            menu.add(0, EDIT, 0, getStringValue("abm_edit"));
        }
        if (optDuplicateInContext) {
            menu.add(0, DUPLICATE, 0, getStringValue("abm_duplicate"));
        }
        if (!optReadOnly) {
        	menu.add(0, DELETE, 0, getStringValue("abm_delete"));
        }
    }

    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
		try {
	        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
	        // info.id contains the position of the item in the list
	        itemId = itemList.getId((int) info.id);
	        switch (item.getItemId()) {
	            case DELETE:
	            	askForDeletion();
	                return true;
	            case EDIT:
	            	launchEdit(false);
	            	return true;
	            case DUPLICATE:
	            	launchEdit(true);
	            	return true;
	            case VIEW:
	            	launchView();
	            	return true;
	        }
		} catch (Exception e) {
			GenLog.errorMsg(TAG, this, e);
		}
        return false;
    }

    private void clickItem(long position) {
		itemId = itemList.getId((int) position);
		if (optRegisterAccessTime && optDatamgrDatabase) {
			abmTableProvider.updateAccessTime(itemId);
		}
		if (itemClickIntent != null) {
    		itemClickIntent.putExtra(ABM_CALLER_ID, itemId);
    		itemClickIntent.putExtra(ABM_CALLER_TITLE, itemList.getTitle(itemId));
			startActivity(itemClickIntent);
		} else {
			if (optDontUseView) {
				launchEdit(false);
			} else {
				launchView();
			}
		}
    }
    
    private void viewButtonPressed() {
    	if (viewButtonIntent != null) {
    		viewButtonIntent.putExtra(ABM_CALLER_ID, itemId);
    		viewButtonIntent.putExtra(ABM_CALLER_TITLE, itemList.getTitle(itemId));
			startActivity(viewButtonIntent);
    	} else {
    		launchEdit(false);
    	}
    }
    
    public long getParentId() {
    	return parentId;
    }

    public String getParentTitle() {
    	return parentTitle;
    }
    
    protected int getCurrentScreen() {
    	return currScreen;
    }
    
    protected long getItemId() {
    	return itemId;
    }
    
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        try {
	        menu.removeGroup(Menu.NONE);
	        if (currScreen == LIST) {
	        	if (!optReadOnly) {
	        		menu.add(Menu.NONE, INSERT, Menu.NONE, getStringValue("abm_insert"));
	        	}
	        	if (additionalMenu != null) {
	        		for (ABMAdditionalMenuEntry menuEntry : additionalMenu.getListMenu()) {
						menu.add(Menu.NONE, FIRST_ADDITIONAL_MENU_ENTRY + menuEntry.getMenuId(), Menu.NONE, menuEntry.getMenuString());
					}
	        	}
	        } else if (currScreen == VIEW) {
	        	if (!optReadOnly) {
		        	menu.add(Menu.NONE, EDIT, Menu.NONE, getStringValue("abm_edit"));
		        	menu.add(Menu.NONE, DUPLICATE, Menu.NONE, getStringValue("abm_duplicate"));
		        	menu.add(Menu.NONE, DELETE, Menu.NONE, getStringValue("abm_delete"));
	        	}
	        	if (additionalMenu != null) {
	        		for (ABMAdditionalMenuEntry menuEntry : additionalMenu.getViewMenu()) {
						menu.add(Menu.NONE, FIRST_ADDITIONAL_MENU_ENTRY + menuEntry.getMenuId(), Menu.NONE, menuEntry.getMenuString());
					}
	        	}
	        } else if (currScreen == EDIT) {
	        	menu.add(Menu.NONE, SAVE, Menu.NONE, getStringValue("abm_save"));
	        	menu.add(Menu.NONE, DISCARD, Menu.NONE, getStringValue("abm_discard"));
	        	if (itemId >= 0) {
	        		menu.add(Menu.NONE, DELETE, Menu.NONE, getStringValue("abm_delete"));
	        	}
	        	if (additionalMenu != null) {
	        		for (ABMAdditionalMenuEntry menuEntry : additionalMenu.getEditMenu()) {
						menu.add(Menu.NONE, FIRST_ADDITIONAL_MENU_ENTRY + menuEntry.getMenuId(), Menu.NONE, menuEntry.getMenuString());
					}
	        	}
	        }
		} catch (Exception e) {
			GenLog.errorMsg(TAG, this, e);
		}
        return true;
    }

	private boolean menuOptionSelect(int id) {
        switch (id) {
        case INSERT:
        	itemId = -1;
        	launchEdit(false);
            return true;
        case EDIT:
        	launchEdit(false);
            return true;
        case DUPLICATE:
        	launchEdit(true);
            return true;
        case DELETE:
        	askForDeletion();
            return true;
        case SAVE:
        	launchSave();
        	return true;
        case DISCARD:
        	onBackPressed();
        	return true;
        case VIEWBUTTON:
        	viewButtonPressed();
        	return true;
        }
        if (additionalMenu != null) {
        	Intent menuIntent = additionalMenu.getMenuIntent(id - FIRST_ADDITIONAL_MENU_ENTRY);
        	if (menuIntent != null) {
        		menuIntent.putExtra(ABM_CALLER_ID, itemId);
        		menuIntent.putExtra(ABM_CALLER_TITLE, itemList.getTitle(itemId));
        		startActivity(menuIntent);
        	}
        }
        return false;
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	try {
	    	if (menuOptionSelect(item.getItemId())) {
	    		return true;
	    	}
		} catch (Exception e) {
			GenLog.errorMsg(TAG, this, e);
		}
        return super.onOptionsItemSelected(item);
    }
    
    private void onMenuButtonSelect(int id) {
    	menuOptionSelect(id);
    }
    
    @Override
    protected void onResume() {
    	try {
	    	switch (currScreen) {
			case LIST:
				launchList();
				break;
			case VIEW:
				launchView();
				break;
			}
		} catch (Exception e) {
			GenLog.errorMsg(TAG, this, e);
		}
    	super.onResume();
    }
    
    public ABMListResult getSubABMClassList(Class<?> abmClass, Context context) {
    	ABMBase subABMInstance = null;
    	try {
	    	if (!subABMClasses.containsKey(abmClass)) {
	    		subABMInstance = (ABMBase) abmClass.newInstance();
	    		subABMInstance.resolveParameters(context);
	    		subABMClasses.put(abmClass, subABMInstance);
	    	} else {
	    		subABMInstance = subABMClasses.get(abmClass);
	    	}
    	} catch (Exception e) {
    		GenLog.errorMsg(TAG, this, e);
    	}
    	return subABMInstance.getItemList();
    }

    /**
     * Open and close the database. This method is used to create the database in case
     * it does not exist.
     */
    public void openDatabase(Context context) {
		abmDatabaseMgr = (ABMDatabaseDataMgr) this;
		abmTableProvider = new ABMTableProvider(abmDatabaseMgr.getDatabaseName(), abmDatabaseMgr.getVersionNumber(), abmDatabaseMgr.getFieldNames(), getFieldTypes(), context);
		abmTableProvider.open();
		abmTableProvider.close();
    }

	private void resolveParameters(Context context) {
		HashMap<String, Object> parameters = getParameters(context);
		Class<?> resourceClass = (Class<?>) parameters.get(ABM_RESOURCE_CLASS);
		int options = (Integer) parameters.get(ABM_OPTIONS);
		optDontUseView = (options & OPT_DONT_USE_VIEW) > 0;
		optViewInContext = (options & OPT_VIEW_IN_CONTEXT) > 0;
		optEditInContext = (options & OPT_EDIT_IN_CONTEXT) > 0;
		optDatamgrDatabase = (options & OPT_DATAMGR_DATABASE) > 0;
		optDatamgrCustom = (options & OPT_DATAMGR_CUSTOM) > 0;
		optRegisterAccessTime = (options & OPT_REGISTER_ACCESS_TIME) > 0;
		optDuplicateInContext = (options & OPT_DUPLICATE_IN_CONTEXT) > 0;
		optReadOnly = (options & OPT_READ_ONLY) > 0;
		try {
			String resourcesClassName = resourceClass.getName();
			Class<?>[] classes = resourceClass.getClasses();
			for (Class<?> subClass : classes) {
				if (subClass.getName().equals(resourcesClassName + "$string")) {
					stringResourceClass = subClass;
				} else if (subClass.getName().equals(resourcesClassName + "$layout")) {
					layoutResourceClass = subClass;
				} else if (subClass.getName().equals(resourcesClassName + "$id")) {
					idResourceClass = subClass;
				}
			}
		} catch (Exception e) {
			GenLog.errorMsg(TAG, this, e);
		}
		if (optDatamgrDatabase) {
			abmDatabaseMgr = (ABMDatabaseDataMgr) this;
			abmTableProvider = new ABMTableProvider(abmDatabaseMgr.getDatabaseName(), abmDatabaseMgr.getVersionNumber(), abmDatabaseMgr.getFieldNames(), getFieldTypes(), context);
			abmTableProvider.open();
		} else if (optDatamgrCustom) {
			abmCustomDataMgr = (ABMCustomDataMgr) this;
		}
		if (parameters.containsKey(ABM_CLICK_ITEM_INTENT)) {
			itemClickIntent = (Intent) parameters.get(ABM_CLICK_ITEM_INTENT);
		}
		if (parameters.containsKey(ABM_VIEW_BUTTON_INTENT)) {
			viewButtonIntent = (Intent) parameters.get(ABM_VIEW_BUTTON_INTENT);
		}
		if (parameters.containsKey(ABM_VIEW_BUTTON_TITLE)) {
			viewButtonTitleId = (Integer) parameters.get(ABM_VIEW_BUTTON_TITLE);
		} else {
			viewButtonTitleId = getStringValue("abm_edit");
		}
		if (parameters.containsKey(ABM_ADDITIONAL_MENU)) {
			additionalMenu = (ABMAdditionalMenu) parameters.get(ABM_ADDITIONAL_MENU);
		}
		if (parameters.containsKey(ABM_FIELD_PARENT_ID)) {
			parentIdField = (String) parameters.get(ABM_FIELD_PARENT_ID);
		}
		// Parametros recibidos en el intent:
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (parameters.containsKey(ABM_FIELD_PARENT_ID)) {
				parentId = bundle.getLong(ABM_CALLER_ID);
			}
			if (bundle.containsKey(ABM_CALLER_TITLE)) {
				parentTitle = bundle.getString(ABM_CALLER_TITLE);
			}
		}
	}
	
	private int getStringValue(String fieldName) {
		int result = 0;
		try {
			result = stringResourceClass.getField(fieldName).getInt(null);
		} catch (Exception e) {
			GenLog.errorMsg(TAG, this, e);
		}
		return result;
	}
	
	private int getLayoutValue(String fieldName) {
		int result = 0;
		try {
			result = layoutResourceClass.getField(fieldName).getInt(null);
		} catch (Exception e) {
			GenLog.errorMsg(TAG, this, e);
		}
		return result;
	}

	private int getIdValue(String fieldName) {
		int result = 0;
		try {
			result = idResourceClass.getField(fieldName).getInt(null);
		} catch (Exception e) {
			GenLog.errorMsg(TAG, this, e);
		}
		return result;
	}

	public abstract void onCreateActions();
	public abstract HashMap<String, Object> getParameters(Context context);
	public abstract int[] getFieldTitles();
	public abstract Object[] getInsertValues();
	public abstract int[] getFieldTypes();
	public abstract ABMListResult getChoiceFieldValues(int fieldNum);
	public abstract String[] getAutocompleteFieldValues(int fieldNum);
}
