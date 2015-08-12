package ar.com.sofrecom.av.abm;


public interface ABMCustomDataMgr {
	public ABMListResult getList(String parentIdField, long parentId);
	public Object[] getViewValues(long id);
	public Object[] getEditValues(long id);
	public void delete(long position);
	public void save(long id, Object[] values);
}
