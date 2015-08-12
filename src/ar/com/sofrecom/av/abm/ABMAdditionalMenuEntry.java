package ar.com.sofrecom.av.abm;

import android.content.Intent;

public class ABMAdditionalMenuEntry {
	private int menuId;
	private int menuString;
	private Intent menuIntent;
	
	public ABMAdditionalMenuEntry(int menuId, int menuString, Intent menuIntent) {
		this.menuId = menuId;
		this.menuString = menuString;
		this.menuIntent = menuIntent;
	}

	public int getMenuId() {
		return menuId;
	}

	public int getMenuString() {
		return menuString;
	}

	public Intent getMenuIntent() {
		return menuIntent;
	}
}