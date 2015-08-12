package ar.com.sofrecom.app.gastos;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class GastosEditText extends EditText {

	private boolean changed = false;
	
	public GastosEditText(Context context) {
		super(context);
	}
	
	public GastosEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public GastosEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public void onTextChanged(CharSequence text, int start, int before, int after) {
		changed = true;
		super.onTextChanged(text, start, before, after);
	}
	
	public boolean isChanged() {
		return changed;
	}
	
	public void setChanged(boolean changed) {
		this.changed = changed;
	}
}
