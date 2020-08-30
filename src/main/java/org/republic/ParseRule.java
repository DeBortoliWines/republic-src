/*
 * Created on Jun 6, 2004
 * 
 * Swiftel - 1300360196
 *
 * @author Stuart Guthrie
 */
package org.republic;

/**
 * @author eureka1
 *
 * A parse rule provides a means to generate new formatted output based upon specific
 * parse types.
 * 
 */
public class ParseRule{
	String parseType;
	String matchString;
	int startCol;
	int endCol;
	int startRow;
	int endRow;
	int rowIndex;
	//int outputColumnNumber;
	String outputFieldName;
	String outputFieldType; // String, Currency
	/**
	 * @return Returns the endCol.
	 */
	public int getEndCol() {
		return endCol;
	}

	/**
	 * @param endCol The endCol to set.
	 */
	public void setEndCol(int endCol) {
		this.endCol = endCol;
	}

	/**
	 * @return Returns the endRow.
	 */
	public int getEndRow() {
		return endRow;
	}

	/**
	 * @param endRow The endRow to set.
	 */
	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	/**
	 * @return Returns the rowIndex.
	 */
	public int getRowIndex() {
		return rowIndex;
	}

	/**
	 * @param rowIndex The rowIndex to set.
	 */
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}


	/**
	 * @return Returns the matchString.
	 */
	public String getMatchString() {
		return matchString;
	}

	/**
	 * @param matchString The matchString to set.
	 */
	public void setMatchString(String matchString) {
		this.matchString = matchString;
	}

	/**
	 * @return Returns the outputFieldName.
	 */
	public String getOutputFieldName() {
		return outputFieldName;
	}

	/**
	 * @param outputFieldName The outputFieldName to set.
	 */
	public void setOutputFieldName(String outputFieldName) {
		this.outputFieldName = outputFieldName;
	}

	/**
	 * @return Returns the parseType.
	 */
	public String getParseType() {
		return parseType;
	}

	/**
	 * @param parseType The parseType to set.
	 */
	public void setParseType(String parseType) {
		this.parseType = parseType;
	}

	/**
	 * @return Returns the startCol.
	 */
	public int getStartCol() {
		return startCol;
	}

	/**
	 * @param startCol The startCol to set.
	 */
	public void setStartCol(int startCol) {
		this.startCol = startCol;
	}

	/**
	 * @return Returns the startRow.
	 */
	public int getStartRow() {
		return startRow;
	}

	/**
	 * @param startRow The startRow to set.
	 */
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	/**
	 * @return Returns the outputFieldType.
	 */
	public String getOutputFieldType() {
		return outputFieldType;
	}

	/**
	 * @param outputFieldType The outputFieldType to set.
	 */
	public void setOutputFieldType(String outputFieldType) {
		this.outputFieldType = outputFieldType;
	}

}
