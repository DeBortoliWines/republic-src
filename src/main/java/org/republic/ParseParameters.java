/*
 * Created on Jun 6, 2004
 *
 * @author Stuart Guthrie
 */
package org.republic;

import java.util.Collection;

/**
 * @author eureka1
 *     <p>To change the template for this generated type comment go to Window - Preferences - Java -
 *     Code Generation - Code and Comments
 */
public class ParseParameters {
  String reportName;
  String reportType;

  // Collection parseRules;
  Collection<ParseSheet> parseSheets;

  // Changed to part of ParseRules..	String startDataLine;

  /*
  *  Initial Speced Parser.
  *
  * Altered (see below)
  *
  <reportparse>
    <reportname>dbictrrp.p<reportname>
    <reporttype>simpleoneline</reporttype>
    <startdataline>10</startdataline>
    <ignoredataIf>
      <fieldname>ID</fieldname>
      <ifequal>BLANK</ifequal>
    <ignoredataIf>
  <fields>
     <field>
        <fieldname>ID</fieldname>
        <fieldStart>1</fieldStart>
        <fieldLength>8</fieldLength>
     </field>
  etc
  etc
  </fields>
  </reportparse>
  *
  * Alterations...
  *
  * To provide better extensibility, all parseRules are a collection under the main
  * report parser class.
  *
  * Examples of ParseRules:
  *
  * RuleType    Conditions.
  * Opt 1: SelectFieldData StartCol 1, Length 8, Name CUSTOMERID
  * Opt 1: NewRowIf StartCol 1,    Length 8    > ''
  * Opt 1: IgnoreLineIf StartCol 1, Length 8 EQUALS ''
  * Opt 1: IgnoreLineIf StartCol 1, Length 10 EQUALS '============'
  * Opt 1: IgnorePageRows StartRow 1, EndRow 8
  * Later: SelectPageRow StartRow 1, StartCol 1 Length 10 Name REPORTID
  * Later: SelectPageRow StartRow 1, StartCol 70 Length 8 Name REPORTRUNDATE
  * Later: DefineRecordMultiRows??? RowLength 3
  *  Thinking for multi-rows, something indicates the start of a multi-row and the start
  * of a new multi-row indicates the end of the previous one...?
  *
  */

  /**
   * @return Returns the reportName.
   */
  public String getReportName() {
    return reportName;
  }

  /**
   * @param reportName The reportName to set.
   */
  public void setReportName(String reportName) {
    this.reportName = reportName;
  }

  /**
   * @return Returns the reportType.
   */
  public String getReportType() {
    return reportType;
  }

  /**
   * @param reportType The reportType to set.
   */
  public void setReportType(String reportType) {
    this.reportType = reportType;
  }

  /**
   * @return Returns the parseSheets.
   */
  public Collection<ParseSheet> getParseSheets() {
    return parseSheets;
  }

  /**
   * @param parseSheets The parseSheets to set.
   */
  public void setParseSheets(Collection<ParseSheet> parseSheets) {
    this.parseSheets = parseSheets;
  }
}
