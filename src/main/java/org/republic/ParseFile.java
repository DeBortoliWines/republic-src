/*
 * Created on Jun 6, 2004
 *
 * @author Stuart Guthrie
 */
package org.republic;

import com.github.jferard.fastods.AnonymousOdsFileWriter;
import com.github.jferard.fastods.OdsDocument;
import com.github.jferard.fastods.OdsFactory;
import com.github.jferard.fastods.Table;
import com.github.jferard.fastods.TableCell;
import com.github.jferard.fastods.Text;
import com.github.jferard.fastods.style.TableCellStyle;
import com.github.jferard.fastods.util.XMLUtil;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

/**
 * @author sfg
 *     <p>To change the template for this generated type comment go to Window - Preferences - Java -
 *     Code Generation - Code and Comments
 */
public class ParseFile {

  private Logger logger = Logger.getLogger(this.getClass().getName());

  // List of all parsed Output Data Beans so far..
  private Collection<OutputDataBean> parsedOutputDataBeans = new LinkedList<>();

  public void executeParseFile(
      String inputReportFileString,
      String outputFileString,
      String xmlParseFileString,
      String loggerLevelString) {

    if (loggerLevelString == null) logger.setLevel(Level.WARNING);
    else if (loggerLevelString.equalsIgnoreCase("debug")) logger.setLevel(Level.FINEST);
    else logger.setLevel(Level.WARNING);

    File inputReport = new File(inputReportFileString);
    File outputXML = new File(outputFileString);
    // Unmarshall xml if present.
    File xmlParseFile = new File(xmlParseFileString);
    // Basic Checks..
    if (!inputReport.exists()) {
      logger.severe("Specified Report file does not exist");
      return;
    }

    // Always wipe the output File if there.
    // Use this point as a test to ensure we can create the output.
    if (outputXML.exists()) {
      outputXML.delete();
      try {
        outputXML.createNewFile();
      } catch (Exception e) {
        e.printStackTrace();
        logger.severe("Specified Output file can not be created!");
        return;
      }
      if (!outputXML.exists()) {
        logger.severe("Specified Output file can not be created!");
        return;
      }
    }

    // XML unmarshall to TestRunParmeters class!
    ParseParameters parseParameters = unMarshallParameters(xmlParseFile.getAbsolutePath());

    // We are going to parse the input file once for each set of output
    // sheet parse rules.
    // Later we hope to make all sheets parse simultaneously thus only
    // reading the report once.
    Iterator<ParseSheet> sheetIter = parseParameters.getParseSheets().iterator();

    while (sheetIter.hasNext()) {
      ParseSheet thisParseSheet = (ParseSheet) sheetIter.next();
      ParseFileSheet parseFileSheet = new ParseFileSheet();
      OutputDataBean odb = parseFileSheet.executeParseFileSheet(thisParseSheet, inputReport);

      this.addOutputDataBean(odb);
    }

    Iterator<OutputDataBean> iter = this.getParsedOutputDataBeans().iterator();
    try {
      final OdsFactory odsFactory = OdsFactory.create();
      final AnonymousOdsFileWriter writer = odsFactory.createWriter();
      final OdsDocument document = writer.document();

      // For each data sheet
      while (iter.hasNext()) {
        OutputDataBean odb = (OutputDataBean) iter.next();

        String baseName = odb.getDataBaseName();
        String currentName = baseName;
        Table table = document.addTable(odb.getDataBaseName());
        int attempt = 2;

        // If addTable returns null (duplicate), try _2, _3, etc.
        while (table == null) {
          currentName = baseName + "_" + attempt++;
          table = document.addTable(currentName);

          // Safety break to prevent infinite loops
          if (attempt > 100) {
             throw new RuntimeException("Unable to create unique sheet name for: " + baseName);
          }
        }
        logger.info("Populating sheet: " + table.getName());

        // First we add headers
        String[] headers =
            odb.getOutputDataColumnNames()
                .toArray(new String[odb.getOutputDataColumnNames().size()]);
        final TableCellStyle boldTextStyle =
            TableCellStyle.builder("header-bold-style").fontWeightBold().build();
        for (int i = 0; i < odb.getOutputDataColumnNames().size(); i++) {
          final TableCell cell = table.getRow(0).getOrCreateCell(i);
          final String headerString = XMLUtil.create().escapeXMLContent(headers[i]);
          final Text text = Text.builder().parContent(headerString).build();
          cell.setText(text);
          cell.setStringValue(headers[i]);
          cell.setStyle(boldTextStyle);
        }

        // Then we add the Data
        int rowNum = 0;
        int colNum = 0;
        Iterator<OutputDataRow> rowIter = odb.getOutputDataRows().iterator();
        while (rowIter.hasNext()) {
          OutputDataRow odr = rowIter.next();
          Iterator<OutputDataColumn> dataRowColIter = odr.getOutputDataColumns().iterator();
          while (dataRowColIter.hasNext()) {
            OutputDataColumn odc = dataRowColIter.next();
            String dataValue = odc.getColumnValue();
            String dataType = odc.getColumnFieldType();
            final TableCell cell = table.getRow(rowNum).getOrCreateCell(colNum++);

            if (dataValue == null || dataValue.trim().isEmpty()) {
              cell.setVoidValue();
              continue;
            }
            switch (dataType.toLowerCase()) {
              case "number":
              case "decimal":
                String strValue = dataValue.trim().replace(",", "").replace("$", "");
                try {
                  cell.setFloatValue(Float.parseFloat(strValue));
                } catch (NumberFormatException e) {
                  final Text text =
                      Text.builder()
                          .parContent(XMLUtil.create().escapeXMLContent(dataValue))
                          .build();
                  cell.setText(text);
                  cell.setStringValue(dataValue);
                }
                break;
              case "percentage":
                cell.setPercentageValue(Double.parseDouble(dataValue));
                break;
              case "date":
                try {
                  SimpleDateFormat sdfLocale = new SimpleDateFormat("dd/MM/yy");
                  Date cellDate = sdfLocale.parse(dataValue);
                  cell.setDateValue(cellDate);
                } catch (ParseException e) {
                  cell.setStringValue(dataValue);
                  e.printStackTrace();
                }
                break;
              case "string":
              default:
                final Text text =
                    Text.builder().parContent(XMLUtil.create().escapeXMLContent(dataValue)).build();
                cell.setText(text);
                cell.setStringValue(dataValue);
                break;
            }
          }
          colNum = 0;
          rowNum++;
        }
      }
      writer.saveAs(new File(outputFileString));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public ParseParameters unMarshallParameters(String fileName) {
    try {

      Mapping mapping = new Mapping();
      mapping.loadMapping(
          new InputSource(
              getLocalOrJarResource(
                  CommonNames.PARSEPARAMETERS, CommonNames.PARSEPARAMETERSLOCATION)));
      Unmarshaller un = new Unmarshaller(ParseParameters.class);
      un.setMapping(mapping);
      // -- Read in the TestRunResult using the mapping
      FileReader in = new FileReader(fileName);
      ParseParameters pp = (ParseParameters) un.unmarshal(in);
      in.close();
      return pp;
    } catch (Exception e) {
      e.printStackTrace();
      logger.severe("Error unmarshalling ParseParametersFile: " + e);
      return null;
    }
  }

  public InputStream getLocalOrJarResource(String inputResource, String inputResourceLocation) {
    InputStream is = null;
    // first get the user defined resource
    File mappingFile = new File(inputResourceLocation);
    try {
      is = new BufferedInputStream(new FileInputStream(mappingFile));
    } catch (Exception e) {
      // This exception is fine, we will check for the real mapping file
      // in the source..
    }
    // otherwise use the default resource
    if (is == null) {
      try {
        is = this.getClass().getResourceAsStream(inputResource);
      } catch (Exception e1) {
        logger.severe("Unable to access directly:" + mappingFile);
      }
    }

    return is;
  }

  /**
   * @return Returns the outputDataBeans.
   */
  public Collection<OutputDataBean> getParsedOutputDataBeans() {
    return parsedOutputDataBeans;
  }

  /**
   * @param outputDataBeans The outputDataBeans to set.
   */
  public void setOutputDataBeans(Collection<OutputDataBean> outputDataBeans) {
    this.parsedOutputDataBeans = outputDataBeans;
  }

  public boolean addOutputDataBean(OutputDataBean arg0) {
    return parsedOutputDataBeans.add(arg0);
  }
}
