package ar.com.sofrecom.av.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import ar.com.sofrecom.av.abm.ABMBase;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;

public class DataXmlImporter {

	private static final String TAG = "DataXmlImporter";
	
	private SQLiteDatabase db;
	private String dbName;
// Used when files are exported to file system
//	private String directory;
	private DropboxAPI<AndroidAuthSession> dbApi;
	private int[] fieldTypes;
	private String[] fieldNames;
	private boolean foundAccessedDate = false;
	private boolean foundModifiedDate = false;
	private boolean foundCreatedDate = false;
	
	
// Used when files are exported to file system
//	public DataXmlImporter(SQLiteDatabase db, String dbName, String directory, int[] fieldTypes, String[] fieldNames) {
//		this.db = db;
//		this.dbName = dbName;
//		this.directory = directory;
//		this.fieldTypes = fieldTypes;
//		this.fieldNames = fieldNames;
//	}

	public DataXmlImporter(SQLiteDatabase db, String dbName, DropboxAPI<AndroidAuthSession> dbApi, int[] fieldTypes, String[] fieldNames) {
		this.db = db;
		this.dbName = dbName;
		this.dbApi = dbApi;
		this.fieldTypes = fieldTypes;
		this.fieldNames = fieldNames;
	}
	
// Used when files are exported to file system
//	public int importData() throws GenericException {
//		GenLog.debug(TAG, "exporting database - " + dbName);
//		// get the factory
//		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//		int qtty = 0;
//		DocumentBuilder documentBuilder;
//		try {
//			// Using factory get an instance of document builder
//			documentBuilder = dbf.newDocumentBuilder();
//			// parse using builder to get DOM representation of the XML file
//			File dir = new File(directory);
//			File file = new File(dir, dbName + ".xml");
//			if (file.exists()) {
//				InputStream is = new FileInputStream(file);
//				Document dom = documentBuilder.parse(is);
//				Element docEle = dom.getDocumentElement();
//				NodeList nl = docEle.getElementsByTagName("database");
//				if (nl != null && nl.getLength() > 0) {
//					// get the "table" element
//					Element el = (Element) nl.item(0);
//					db.delete(dbName, null, null);
//					nl = el.getElementsByTagName("row");
//					if (nl != null && nl.getLength() > 0) {
//						qtty = nl.getLength();
//						for (int i = 0; i < nl.getLength(); i++) {
//							el = (Element) nl.item(i);
//							db.execSQL(getInsertCmd(el));
//						}
//					}
//				}
//			}
//		} catch (ParserConfigurationException e) {
//			throw new GenericException(e);
//		} catch (SAXException e) {
//			throw new GenericException(e);
//		} catch (IOException e) {
//			throw new GenericException(e);
//		}
//		return qtty;
//	}
	
	public int importData() throws GenericException {
		GenLog.debug(TAG, "exporting database - " + dbName);
		// get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		int qtty = 0;
		DocumentBuilder documentBuilder;
    	String content = "";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			dbApi.getFile("/" + dbName + ".xml", null, outputStream, null);
			content = new String(outputStream.toByteArray());
			// Using factory get an instance of document builder
			documentBuilder = dbf.newDocumentBuilder();
			// parse using builder to get DOM representation of the XML file
			ByteArrayInputStream is = new ByteArrayInputStream(content.getBytes());
			Document dom = documentBuilder.parse(is);
			Element docEle = dom.getDocumentElement();
			NodeList nl = docEle.getElementsByTagName("database");
			if (nl != null && nl.getLength() > 0) {
				// get the "table" element
				Element el = (Element) nl.item(0);
				try {
					db.delete(dbName, null, null);
				} catch (SQLException e) {
					// do nothing
				}
				nl = el.getElementsByTagName("row");
				if (nl != null && nl.getLength() > 0) {
					qtty = nl.getLength();
					for (int i = 0; i < nl.getLength(); i++) {
						el = (Element) nl.item(i);
						db.execSQL(getInsertCmd(el));
					}
				}
			}
		} catch (ParserConfigurationException e) {
			throw new GenericException(e);
		} catch (SAXException e) {
			throw new GenericException(e);
		} catch (IOException e) {
			throw new GenericException(e);
		} catch (DropboxException e) {
			throw new GenericException(e);
		}
		return qtty;
	}
	
	private String getInsertCmd(Element row) {
		String result = null;
		String colNames = "";
		String colValues = "";
		NodeList nl = row.getElementsByTagName("col");
		Element el;
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				//get the employee element
				if (!colNames.equals("")) {
					colNames += ", ";
					colValues += ", ";
				}
				el = (Element) nl.item(i);
				String colName = el.getAttribute("name");
				//if (!colName.equals("_id")) {
				colNames += colName;
				colValues += getElementValue(el, colName);
				//}
				if (colName.equals("ACCESSED_DATE")) {
					foundAccessedDate = true;
				} else if (colName.equals("MODIFIED_DATE")) {
					foundModifiedDate = true;
				} else if (colName.equals("CREATED_DATE")) {
					foundCreatedDate = true;
				}
			}
		}
        Long now = Long.valueOf(System.currentTimeMillis());
		if (!foundAccessedDate) {
			colNames += ", ACCESSED_DATE";
			colValues += ", " + now;
		}
		if (!foundModifiedDate) {
			colNames += ", MODIFIED_DATE";
			colValues += ", " + now;
		}
		if (!foundCreatedDate) {
			colNames += ", CREATED_DATE";
			colValues += ", " + now;
		}
		result = "insert into " + dbName + " (" + colNames + ") values (" + colValues + ")";
		return result;
	}
	
	private String getElementValue(Element col, String colName) {
		String nodeValue = "";
		Node node = col.getFirstChild();
		if (node != null) {
			nodeValue = node.getNodeValue();
		}
		if (colName.equals("_id") ||
				colName.equals("ACCESSED_DATE") ||
				colName.equals("MODIFIED_DATE") ||
				colName.equals("CREATED_DATE") ) {
			return nodeValue;
		} else {
			boolean found = false;
			String value = null;
			for (int i = 0; (i < fieldNames.length) && !found; i++) {
				if (colName.equals(fieldNames[i])) {
					switch (fieldTypes[i]) {
					case ABMBase.TYPE_STRING:
					case ABMBase.TYPE_STRING_AUTOCOMPLETE:
					case ABMBase.TYPE_PASSWORD:
					case ABMBase.TYPE_MULTIPLE_CHOICE:
						value = "\"" + nodeValue + "\"";
						found = true;
						break;
					default:
						value = nodeValue;
						found = true;
						break;
					}
				}
			}
			return value;
		}
	}
}
