/*
 * Created on Jun 6, 2004
 * 
 * Swiftel - 1300360196
 *
 * @author Stuart Guthrie
 */
package org.republic;

import java.util.Collection;

/**
 * @author eureka1
 *
 * A parse sheet is a set of rules that generate an output data sheet.
 * 
 */
public class ParseSheet{
	private String sheetName;
	private Collection<ParseRule> parseRules;
	

	/**
	 * @return Returns the parseRules.
	 */
	public Collection<ParseRule> getParseRules() {
		return parseRules;
	}
	/**
	 * @param parseRules The parseRules to set.
	 */
	public void setParseRules(Collection<ParseRule> parseRules) {
		this.parseRules = parseRules;
	}
	/**
	 * @return Returns the sheetName.
	 */
	public String getSheetName() {
		return sheetName;
	}
	/**
	 * @param sheetName The sheetName to set.
	 */
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
}
