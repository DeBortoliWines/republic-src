/*
 * Created on 07-Jun-2004
 *
 */
package org.republic;

/**
 * @author sfg
 */
public class OutputDataColumn {
  public String columnFieldType;
  public String columnValue;

  /**
   * @return Returns the columnValue.
   */
  public String getColumnValue() {
    return columnValue;
  }

  /**
   * @param columnValue The columnValue to set.
   */
  public void setColumnValue(String columnValue) {
    this.columnValue = columnValue;
  }

  /**
   * @return Returns the columnFieldType.
   */
  public String getColumnFieldType() {
    return columnFieldType;
  }

  /**
   * @param columnFieldType The columnFieldType to set.
   */
  public void setColumnFieldType(String columnFieldType) {
    this.columnFieldType = columnFieldType;
  }
}
