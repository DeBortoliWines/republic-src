package org.republic;

/*
 * Created on Sep 1, 2005
 *
 */


import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author sfg
 *
 * Each report process can require one or more datasets. This defines one.
 * and its data values.
 * 
 */
public class DataSet {
	// Not used in OO, used in BiRT
	private String dataSetFileName;
	// Definitions of dataset.
	private Collection definitions = new Vector();
	// Collection of object data.
	private Collection dataRows = new Vector();
	
	public Collection getDefinitions() {
		return definitions;
	}
	public DataSetDefinition getDefinition(String name) {
		Iterator iter = this.getDefinitions().iterator();
		while(iter.hasNext()){
			DataSetDefinition dataSetDefinition = (DataSetDefinition)iter.next();
			if (dataSetDefinition.getFieldName().equalsIgnoreCase(name))
				return dataSetDefinition;
		}
		return null;
	}
	public String listDefinitions(){
		String definitions = "";
		Iterator iter = this.getDefinitions().iterator();
		while(iter.hasNext()){
			DataSetDefinition dataSetDefinition = (DataSetDefinition)iter.next();
			definitions= definitions+","+dataSetDefinition.getFieldName()+"/"+dataSetDefinition.getFieldType();
			//if (dataSetDefinition.getFieldName().equalsIgnoreCase(name))
				//return dataSetDefinition;
		}
		return definitions;
	}
	/*
	 * Given a datadefinition, return the first row definition of that 
	 */
	public Object getFirstDataSetValue(DataSetDefinition dsd){
		Iterator iter = this.getDataRows().iterator();
		while(iter.hasNext()){
			DataRow dataRow = (DataRow)iter.next();
			Object dataValue = dataRow.getDataColumn(dsd.getFieldName());
			if (dataValue != null){
				return dataValue;
			}
		}
		return "";
	}
	public void setDefinitions(Collection definitions) {
		this.definitions = definitions;
	}
	public void addDefinition(DataSetDefinition dsd){
		definitions.add(dsd);
	}
	public void addDataRow(DataRow dataRow){
		dataRows.add(dataRow);
	}
	public String getDataSetFileName() {
		return dataSetFileName;
	}
	public void setDataSetFileName(String dataSetFileName) {
		this.dataSetFileName = dataSetFileName;
	}
	public Collection getDataRows() {
		return dataRows;
	}
	public void setDataRows(Collection dataRows) {
		this.dataRows = dataRows;
	}
}
