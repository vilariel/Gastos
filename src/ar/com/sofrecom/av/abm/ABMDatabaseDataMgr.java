package ar.com.sofrecom.av.abm;

public interface ABMDatabaseDataMgr {
	public String getDatabaseName();
	public int getVersionNumber();
	public String[] getFieldNames();
	public String getListColumns();
	public String getListOrder();
}
