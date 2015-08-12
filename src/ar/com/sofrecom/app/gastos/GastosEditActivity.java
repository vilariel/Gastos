package ar.com.sofrecom.app.gastos;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import ar.com.sofrecom.av.util.GenLog;

public class GastosEditActivity extends Activity {
	
	private static final String TAG = "GastosEditActivity";
	private GastosEditText box;
    private boolean wrap = true;
    private int dip = 10;
    private String fileName;
    private int length = 0;
    private final static int CONFIRM_SAVE_DLG = 1;
	private boolean changed = false;
   
    // convert value to dips
    public int dip(int value) {
        float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        return (int)(value * scale);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.texteditor);
        
        // get file path
		SharedPreferences prefs = getSharedPreferences(Main.PREF_PACKAGE, Context.MODE_PRIVATE);
		fileName = prefs.getString("datadir", "") + "/gastos.csv";

		// initialize and populate the EditText view
        box = (GastosEditText) findViewById(R.id.box);
        box.setTextSize(dip(dip));
        box.setText(open());
        if (length > 0) {
        	box.setSelection(length);
        }
        box.setChanged(false);
		/*
        final ScrollView scroll = (ScrollView) findViewById(R.id.boxScroll);
        box.post(new Runnable() { 
		    public void run() { 
		        scroll.fullScroll(ScrollView.FOCUS_DOWN); 
		    } 
		});
		*/ 
    }
    
    @Override
    public void onBackPressed() {
    	if (box.isChanged()) {
    		showDialog(CONFIRM_SAVE_DLG);
    	} else {
    		super.onBackPressed();
    	}
    }
    
	@Override
	protected Dialog onCreateDialog(int id) {
		try {
			if (id == CONFIRM_SAVE_DLG) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.editConfirmSaveOnExit)
				       .setCancelable(true)
				       .setPositiveButton(R.string.abm_yes, new DialogInterface.OnClickListener() {
				    	   public void onClick(DialogInterface dialog, int id) {
								save();
								dialog.cancel();
								finish();
				           }
				       })
				       .setNegativeButton(R.string.abm_no, new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				                dialog.cancel();
				        		finish();
				           }
				       });
				AlertDialog alert = builder.create();
				return alert;
			}
		} catch (Exception e) {
			GenLog.err(TAG, e);
		}
		return null;
	}

	public String open() {
        String content = "";
        try
        {
            FileInputStream instream = new FileInputStream(fileName);
            byte[] buffer = new byte[4096];
            int read = 0;
            while ((read = instream.read(buffer)) >= 0) {
            	content = content + new String(buffer, 0, read);
            }
            instream.close();
            length = content.length();
        }
        catch (Exception e) {
    		GenLog.errorMsg(TAG, getApplicationContext(), e);
        }
        return content;
    }

    public void save()
    {
    	try
    	{
            String content = box.getText().toString();
            FileOutputStream outstream = new FileOutputStream(fileName);
            outstream.write(content.getBytes());
            outstream.close();
            box.setChanged(false);
        }
        catch (Exception e) {
    		GenLog.errorMsg(TAG, getApplicationContext(), e);
        }
    }    
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
       populateMenu(menu);
       return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
       return applyMenuChoice(item) || super.onOptionsItemSelected(item);
    }

    // menu items' Ids
    public static final int Menu0 = Menu.FIRST + 0;
    public static final int Menu1 = Menu.FIRST + 1;
    public static final int Menu2 = Menu.FIRST + 2;
    public static final int Menu3 = Menu.FIRST + 3;
    public static final int Menu4 = Menu.FIRST + 4;

    public void populateMenu(Menu menu)
    {
       menu.add(0, Menu0, 0, R.string.editMenuWordwrap);
       menu.add(0, Menu1, 1, R.string.editMenuSizeUp);
       menu.add(0, Menu2, 2, R.string.editMenuSizeDw);
       menu.add(0, Menu3, 3, R.string.editMenuSaveExit);
       menu.add(0, Menu4, 3, R.string.editMenuSave);
    }
    
    public boolean applyMenuChoice(MenuItem item)
    {
       switch (item.getItemId())
       {
       case Menu0: box.setHorizontallyScrolling(wrap); wrap = !wrap; return true;
       case Menu1: dip++; box.setTextSize(dip(dip)); return true;
       case Menu2: dip--; box.setTextSize(dip(dip)); return true;
       case Menu3: save(); finish(); return true;
       case Menu4: save(); return true;
       }
       return false;
    }    

    public boolean isChanged() {
		return changed;
	}
	
	public void setChanged(boolean changed) {
		this.changed = changed;
	}
}
