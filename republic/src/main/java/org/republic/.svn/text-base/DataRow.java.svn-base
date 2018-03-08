/*
 * Created on Sep 5, 2005
 *
 * 
 */
package org.republic;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sfg
 *
 * Generic Data Row. 
 *
 */
public class DataRow {
    Map dataColumns = new HashMap();
	//Collection dataColumnNames = new Vector();
	
	public Object getDataColumn(String dataColumnName) {
		return dataColumns.get((Object) dataColumnName);
	}
	
	public void addDataColumn(String dataColumnName, Object dataColumn) {
		dataColumns.put((Object)dataColumnName,dataColumn);
		
	}

}
