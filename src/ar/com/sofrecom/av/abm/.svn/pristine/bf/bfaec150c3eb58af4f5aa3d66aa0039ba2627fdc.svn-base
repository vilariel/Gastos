package ar.com.sofrecom.av.abm;

import java.util.ArrayList;

public class ABMListResult {
	private ArrayList<String> titles = new ArrayList<String>();
	private ArrayList<Long> ids = new ArrayList<Long>();
	private ArrayList<String[]> allItems = new ArrayList<String[]>();
	public ArrayList<String> getTitles() {
		return titles;
	}
	public ArrayList<Long> getIds() {
		return ids;
	}
	public ArrayList<String[]> getAllItems() {
		return allItems;
	}
	public void add(Long id, String item) {
		titles.add(item);
		ids.add(id);
		allItems.add(null);
	}
	public void add(long id, String item) {
		titles.add(item);
		ids.add(id);
		allItems.add(null);
	}
	public void add(Long id, String item, String[] allFields) {
		titles.add(item);
		ids.add(id);
		allItems.add(allFields);
	}
	public void add(long id, String item, String[] allFields) {
		titles.add(item);
		ids.add(id);
		allItems.add(allFields);
	}
	public void delete(int position) {
		titles.remove(position);
		ids.remove(position);
		allItems.remove(position);
	}
	public void set(long id, String item) {
		int index = ids.indexOf(new Long(id));
		titles.set(index, item);
	}
	public long getId(int position) {
		return ids.get(position);
	}
	public int getPosition(long id){
		return ids.indexOf(new Long(id));
	}
	public String getTitle(long id) {
		int position = getPosition(id);
		if (position > -1) {
			return titles.get(position);
		} else {
			return null;
		}
	}
	public String getTitleAt(int position) {
		return titles.get(position);
	}
	public int size() {
		return titles.size();
	}
}
