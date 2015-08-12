package ar.com.sofrecom.av.abm;

import java.util.ArrayList;

import android.content.Intent;

public class ABMAdditionalMenu {
	private int lastId = 0;
	private ArrayList<ABMAdditionalMenuEntry> listMenu = new ArrayList<ABMAdditionalMenuEntry>();
	private ArrayList<ABMAdditionalMenuEntry> viewMenu = new ArrayList<ABMAdditionalMenuEntry>();
	private ArrayList<ABMAdditionalMenuEntry> editMenu = new ArrayList<ABMAdditionalMenuEntry>();
	public ArrayList<ABMAdditionalMenuEntry> getListMenu() {
		return listMenu;
	}
	public ArrayList<ABMAdditionalMenuEntry> getViewMenu() {
		return viewMenu;
	}
	public ArrayList<ABMAdditionalMenuEntry> getEditMenu() {
		return editMenu;
	}
	public void addListMenu(int menuString, Intent menuIntent) {
		listMenu.add(new ABMAdditionalMenuEntry(lastId++, menuString, menuIntent));
	}
	public void addViewMenu(int menuString, Intent menuIntent) {
		viewMenu.add(new ABMAdditionalMenuEntry(lastId++, menuString, menuIntent));
	}
	public void addEditMenu(int menuString, Intent menuIntent) {
		editMenu.add(new ABMAdditionalMenuEntry(lastId++, menuString, menuIntent));
	}
	public Intent getMenuIntent(int menuId) {
		for (ABMAdditionalMenuEntry menuEntry : listMenu) {
			if (menuEntry.getMenuId() == menuId) {
				return menuEntry.getMenuIntent();
			}
		}
		for (ABMAdditionalMenuEntry menuEntry : viewMenu) {
			if (menuEntry.getMenuId() == menuId) {
				return menuEntry.getMenuIntent();
			}
		}
		for (ABMAdditionalMenuEntry menuEntry : editMenu) {
			if (menuEntry.getMenuId() == menuId) {
				return menuEntry.getMenuIntent();
			}
		}
		return null;
	}
}
