/*
 * Created on 07-Jun-2004
 *
 * Enables output data to be captured as a bean which is then rendered to XML.
 * 
 */
package org.republic;

import java.util.Collection;

/**
 * @author sfg
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class OutputDataBean 
{
	private String dataBaseName;
	public Collection<String> outputDataColumnNames;
	public Collection<OutputDataRow> outputDataRows;
	
	/**
	 * @return Returns the dataBaseName.
	 */
	public String getDataBaseName() {
		return dataBaseName;
	}

	/**
	 * @param dataBaseName The dataBaseName to set.
	 */
	public void setDataBaseName(String dataBaseName) {
		this.dataBaseName = dataBaseName;
	}

	/**
	 * @return Returns the outputDataRows.
	 */
	public Collection<OutputDataRow> getOutputDataRows() {
		return outputDataRows;
	}

	/**
	 * @param outputDataRows The outputDataRows to set.
	 */
	public void setOutputDataRows(Collection<OutputDataRow> outputDataRows) {
		this.outputDataRows = outputDataRows;
	}

	/**
	 * @return Returns the outputDataColumnNames.
	 */
	public Collection<String> getOutputDataColumnNames() {
		return outputDataColumnNames;
	}

	/**
	 * @param outputDataColumnNames The outputDataColumnNames to set.
	 */
	public void setOutputDataColumnNames(Collection<String> outputDataColumnNames) {
		this.outputDataColumnNames = outputDataColumnNames;
	}

}
