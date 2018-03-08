/*
 * Created on Jun 6, 2004
 *
 * @author Stuart Guthrie
 */
package org.republic;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * @author sfg
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class ParseFileSheet {

	private Logger logger = Logger.getLogger(this.getClass().getName());


//	private ParseSheet parseSheet;

	//private OutputDataColumn outputDataCol;

	// New Row OutputDataColumns
	private Collection newRowOutputDataColumns = new LinkedList();
	
	// These hold the current parse sheet being created
	private Collection parseSheetColumnNames = new LinkedList();
	private Collection parseSheetOutputDataRows = new LinkedList();

	// List of all parsed Output Data Beans so far..
	private Collection parsedOutputDataBeans = new LinkedList();

	private int multiRowLine = -1;

	private int multiRowMaxLines = -1;

	private String multiRowReportLine[] = new String[200];

	private boolean newPage = true;

	private boolean allDataMode = false;
	private boolean endParse=false;
	public OutputDataBean executeParseFileSheet(ParseSheet thisParseSheet, File inputReport) {


			// Initialise for processing.
			allDataMode = false;
			
			setParseSheetOutputDataRows(new LinkedList());
			setParseSheetColumnNames(new LinkedList());
			
			//setParseSheet(thisParseSheet);


			OutputDataBean odb = new OutputDataBean();
			// Get the column names we are expecting into the outputdatabean.
			setColumnNames(thisParseSheet, odb);
			
			// Now Parse the input file into Java Objects.
			parseInputFile(thisParseSheet,inputReport);
			
			odb.setOutputDataRows(this.getParseSheetOutputDataRows());

			odb.setOutputDataColumnNames(this.getParseSheetColumnNames());

			logger.fine("Db Name: " + thisParseSheet.getSheetName());

			odb.setDataBaseName(thisParseSheet.getSheetName());
			
			return odb;
			
	}

	public void parseInputFile(ParseSheet parseSheet, File inputReport) {

		endParse=false;
		try {
			logger.fine("inputReport:" + inputReport.getAbsolutePath());
			BufferedReader in = new BufferedReader(new FileReader(inputReport));
			int reportPagePos = 0;
			logger.fine("inputReport 2:" + in);
			// String reportLine = in.readLine();
			// logger.fine("inputReport 3:" + reportLine);
			// MultiRowMode indicates we are buffering some data for a data
			// parse.
			// It is based upon the multirow rules.
			boolean multiRowMode = false;
			int iLineCtr = 0;
			String reportLine = in.readLine();
			while (reportLine != null) {
				// Check the first char, if ascii 12, we have a formfeed.
				iLineCtr++;
				logger.fine("line:" + iLineCtr + " , line length() "
						+ reportLine.length());
				if (reportLine.length() >= 1) {
					int ascii = (int) reportLine.charAt(0);
					logger.fine(">>>>First char:" + ascii + " new page?: "
							+ (ascii == 12));
					if (ascii == 12) {
						reportPagePos = 0;
						newPage = true;
						// Added 26-07-2004. If we are changing page, finish the
						// last row.
						logger.fine("new page, row exists?" + rowExists());
						reportLine = "";
					}
				}

				reportPagePos++;

				logger.fine("File Line:" + iLineCtr + ", report page pos "
						+ reportPagePos + " , line:" + reportLine);

				multiRowMode = checkReportLine(multiRowMode, parseSheet, reportLine,
						reportPagePos, in);
				// Catches end of all parsing for this sheet.
				if (endParse){
					logger.info("End Of Sheet reached for "+parseSheet.getSheetName()+"... Exiting...");
					break;
				}
				reportLine = in.readLine();
			}
			// Check if New Rows remains unwritten, if so write them out.
			if (rowExists())
				writeRow();
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("Unable to Read Input File: " + inputReport
					+ " Exception: " + e);
			return;
		}
	}

	public InputStream getLocalOrJarResource(String inputResource,
			String inputResourceLocation) {
		InputStream is = null;
		try {
			is = ParseFileSheet.class.getClassLoader().getResourceAsStream(
					inputResource);
		} catch (Exception e1) {
			// This exception is fine, we will check for the real mapping file
			// in the source..
		}
		if (is == null) {
			File mappingFile = new File(inputResourceLocation);
			try {
				is = new BufferedInputStream(new FileInputStream(mappingFile));
			} catch (Exception e) {
				logger.severe("Unable to access directly:" + mappingFile);
			}
		}

		return is;
	}

	public ParseParameters unMarshallParameters(String fileName) {
		try {

			Mapping mapping = new Mapping();
			mapping.loadMapping(new InputSource(getLocalOrJarResource(
					CommonNames.PARSEPARAMETERS,
					CommonNames.PARSEPARAMETERSLOCATION)));
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

	/*
	 * This routine does the heavy lifting of parsing report lines. It can, of
	 * it's own accord read extra lines should the parsing instructions require
	 * it..
	 */
	public boolean checkReportLine(boolean multiRowMode, ParseSheet parseSheet, String reportLine,
			int reportPagePos, BufferedReader in) {

		Iterator iter = parseSheet.getParseRules().iterator();
		while (iter.hasNext()) {
			ParseRule parseRule = (ParseRule) iter.next();

			if (multiRowMode) {
				if (parseRule.getParseType().equalsIgnoreCase(
						CommonNames.ENDSHEETIF)) {
						if (ruleMatchesReportLine(parseRule, reportLine)) {
							logger.info("END SHEET....");
							endParse=true;
							// Exit now, do not construct this line...
							return false;
						}
					}
				if (parseRule.getParseType().equalsIgnoreCase(
						CommonNames.IGNOREPAGEROWS)) {
					if (reportPagePos >= parseRule.getStartRow()
							&& reportPagePos <= parseRule.getEndRow()) {
						// Ignore this row. It is between the row points to
						// ignore.
						logger.fine("Ignore;IgnorePageRows:" + reportPagePos
								+ "ReportLine:" + reportLine);
						return multiRowMode;
					}
				}
				
				if (parseRule.getParseType().equalsIgnoreCase(
						CommonNames.IGNORENEWPAGEUNTIL)
						&& newPage == true) {
					// Look for this string match.. If found we are now off
					// ignore mode.
					if (ruleMatchesReportLine(parseRule, reportLine)) {
						newPage = false;
						return multiRowMode;
					} else {
						logger.fine(CommonNames.IGNORENEWPAGEUNTIL
								+ reportPagePos + "ReportLine:" + reportLine);
						return multiRowMode;
					}
				} else {
					// We have another multi-row!!!
					int line = getMultiRowLine();
					line++;
					setMultiRowLine(line);
					logger.fine("MultiRow to Buffer:" + getMultiRowLine()
							+ reportLine);
					multiRowReportLine[line] = new String(reportLine);

					logger.fine("Stored Value is :" + multiRowReportLine[line]);
					if (getMultiRowLine() >= getMultiRowMaxLines()) {
						logger.fine("About to process the buffer, lines:"
								+ getMultiRowLine() + " max lines >= "
								+ getMultiRowMaxLines());
						// Now we have the multi-row details, look for parse
						// rules
						// to create data.
						loadMultiRows(parseSheet);
						return false;
					}
					return multiRowMode;
				}
			} else {
				if (parseRule.getParseType().equalsIgnoreCase(
					CommonNames.ENDSHEETIF)) {
					if (ruleMatchesReportLine(parseRule, reportLine)) {
						logger.info("END SHEET....");
						endParse=true;
						// Exit now, do not construct this line...
						return false;
					}
				}
				else if (parseRule.getParseType().equalsIgnoreCase(
						CommonNames.IGNORELINEIF)) {
					logger.fine(CommonNames.IGNORELINEIF);
					if (ruleMatchesReportLine(parseRule, reportLine)) {
						logger.fine(CommonNames.IGNORELINEIF + " ignoring..!"
								+ reportLine);
						return multiRowMode;
					}

				} else if (parseRule.getParseType().equalsIgnoreCase(
						CommonNames.IGNOREPAGEROWS)) {
					if (reportPagePos >= parseRule.getStartRow()
							&& reportPagePos <= parseRule.getEndRow()) {
						// Ignore this row. It is between the row points to
						// ignore.
						logger.fine("Ignore;IgnorePageRows:" + reportPagePos
								+ "ReportLine:" + reportLine);
						return multiRowMode;
					}
				} else if (parseRule.getParseType().equalsIgnoreCase(
						CommonNames.IGNORENEWPAGEUNTIL)
						&& newPage == true) {
					// Look for this string match.. If found we are now off
					// ignore mode.
					if (ruleMatchesReportLine(parseRule, reportLine)) {
						newPage = false;
						return multiRowMode;
					} else {
						logger.fine(CommonNames.IGNORENEWPAGEUNTIL
								+ reportPagePos + "ReportLine:" + reportLine);
						return multiRowMode;
					}
				} else if (parseRule.getParseType().equalsIgnoreCase(
						CommonNames.ALLDATAIF)) {
					if (ruleMatchesReportLine(parseRule, reportLine)) {
						allDataMode = true;
					}
				} else if (parseRule.getParseType().equalsIgnoreCase(
						CommonNames.NEWROWIF)) {
					logger.fine(CommonNames.NEWROWIF);
					try {
						// // Debug info:
						// String reportCompareString = getReportCompareString(
						// reportLine, parseRule.getStartCol(), parseRule
						// .getEndCol());
						// logger.fine("CompareString:" + reportCompareString
						// + " Start " + parseRule.getStartCol() + " End "
						// + parseRule.getEndCol());
						// Match new row if rule.
						if (ruleMatchesReportLine(parseRule, reportLine)) {
							if (rowExists())
								writeRow();
							startNewRow();
						} else
							return multiRowMode;

					} catch (Exception e) {
						e.printStackTrace();
						logger.severe("Exception Parsing IgnoreLineIf: "
								+ parseRule.getMatchString() + e);
					}

				} else if (parseRule.getParseType().equalsIgnoreCase(
						CommonNames.SELECTFIELDDATA)) {
					logger.fine(CommonNames.SELECTFIELDDATA);
					try {
						OutputDataColumn outputDataCol= makeNewDataColumn(parseRule, reportLine);
						addColumnToRow(outputDataCol);
					} catch (Exception e) {
						logger.severe("Could not write report line out!");
						// return;
					}
				} else if (parseRule.getParseType().equalsIgnoreCase(
						CommonNames.SELECTMULTIROWFIELDDATA)) {
					logger.fine(CommonNames.SELECTMULTIROWFIELDDATA
							+ " ignoring rule!");
					// ignore except when buffering multi-row..
				} else if (parseRule.getParseType().equalsIgnoreCase(
						CommonNames.NEWMULTIROWIF)) {
					if (ruleMatchesReportLine(parseRule, reportLine)) {
						logger.fine("newMultiRowif triggered for line: "
								+ reportLine);
						// set MultiRow # to 0., create first line. expect to
						// iterate the next X valid lines.
						setMultiRowLine(1);
						multiRowReportLine = new String[200];
						multiRowReportLine[1] = reportLine;
						// Also record how many lines to load.
						logger
								.info("MultiRowMaxLines:"
										+ parseRule.getEndRow());
						setMultiRowMaxLines(parseRule.getEndRow());
						return true;
					}
				} else if (parseRule.getParseType().equalsIgnoreCase(
						CommonNames.SELECTALLDATA)
						&& allDataMode) {
					logger.fine(CommonNames.SELECTALLDATA);
					try {
						if (rowExists())
							writeRow();
						// If this is a new row, create a new
						// outputDataColumn collection.
						startNewRow();

						OutputDataColumn outputDataCol = makeNewDataColumn(parseRule, reportLine);
						addColumnToRow(outputDataCol);

					} catch (Exception e) {
						e.printStackTrace();
						logger.severe("Could not write report line out!");
						// return;
					}
				}
			}
		}
		return multiRowMode;
	}

	public void marshallParsedResults(String outputFile) {
		// OutputDataBean odb = new OutputDataBean();
		// odb.setOutputDataRows(this.getParseSheetOutputDataRows());
		// odb.setOutputDataColumnNames(this.getNewRowOutputDataColumns());
		OutputData outputData = new OutputData();
		logger.fine("About to send this to xml: " + getParsedOutputDataBeans());
		outputData.setOutputDataBeans(this.getParsedOutputDataBeans());
		try {
			FileOutputStream fos = new FileOutputStream(outputFile);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			Marshaller marshaller = new Marshaller(osw);
			Mapping mapping = new Mapping();
			mapping.loadMapping(new InputSource(getLocalOrJarResource(
					CommonNames.OUTPUTDATAMAPPING,
					CommonNames.OUTPUTDATAMAPPINGLOCATION)));
			marshaller.setMapping(mapping);
			marshaller.marshal(outputData);
			osw.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("Exception marshalling the final results:" + e);
		}
	}

	public boolean rowExists() {
		return (this.getNewRowOutputDataColumns() != null);
	}

	public void writeRow() {
		OutputDataRow odr = new OutputDataRow();
		odr.setOutputDataColumns(this.getNewRowOutputDataColumns());
		Collection workColl = new LinkedList();
		workColl = getParseSheetOutputDataRows();
		workColl.add(odr);
		this.setParseSheetOutputDataRows(workColl);
		// Indicate new Row not ready for writing.
		nostartNewRow();
		
	}
	/*
	 * Start New Row
	 */
	public void startNewRow() {
		this.setNewRowOutputDataColumns(new LinkedList());
		// outputDataColumns = new LinkedList();
	}
	/*
	 * Indicate new Row not ready for writing.
	 */
	public void nostartNewRow(){
		this.setNewRowOutputDataColumns(null);
	}
	public void addColumnToRow(OutputDataColumn outputDataCol) {
		Collection workColl = new LinkedList();
		workColl = getNewRowOutputDataColumns();
		workColl.add(outputDataCol);
		this.setNewRowOutputDataColumns(workColl);
		// outputDataColumns.add(outputDataCol);
	}

	public String getReportCompareString(String reportLine, int startCol,
			int endCol) {
		if (reportLine.length() < endCol)
			return "";
		// Java starts counting from 0!!!
		return reportLine.substring(startCol - 1, endCol);
	}

	public String fillString(int stringLength, String fillChar) {

		String filledString = "";
		int iCtr = 0;
		while (iCtr < stringLength) {
			filledString = filledString + " ";
			iCtr++;
		}
		return filledString;
		// String[] StringArr = new String[stringLength];
		// Arrays.fill(StringArr, fillChar);
		// return StringArr.toString();
	}

	public void transformToContent(InputStream xslFile, String inputXMLFile,
			String outputFileName) {
		try {
			StreamSource streamSource = new StreamSource(xslFile);
			transformToContent(streamSource, inputXMLFile, outputFileName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("Error Transforming output XML:" + e);
		}
	}

	public void transformToContent(StreamSource streamSource,
			String inputXMLFile, String outputFileName) {
		try {
			// StreamSource streamSource = new StreamSource(xslFile);

			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer(streamSource);

			/* Create an XML reader which will ignore the <!DOCTYPE office.dtd> */
			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setEntityResolver(new ResolveOfficeDTD());

			InputSource inputSource;

			/* This is an unpacked file. */
			inputSource = new InputSource(new FileInputStream(inputXMLFile));

			SAXSource saxSource = new SAXSource(reader, inputSource);
			saxSource.setSystemId(inputXMLFile);

			/* We want a regular file as output */
			FileOutputStream outputStream = new FileOutputStream(outputFileName);
			transformer.transform(saxSource, new StreamResult(outputStream));

		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("Error Transforming output XML:" + e);
		}

	}

	public void transformToContent(String xslFile, String inputXMLFile,
			String outputFileName) {
		try {
			/* Set up the XSLT transformation based on the XSLT file */
			File xsltFile = new File(xslFile);
			StreamSource streamSource = new StreamSource(xsltFile);
			transformToContent(streamSource, inputXMLFile, outputFileName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("Error Transforming output XML:" + e);
		}
	}

	/*
	 * Build a list of ColumnNames into SheetColumnNames that represent 
	 * the output column names we desire.
	 */
	public void setColumnNames(ParseSheet parseSheet, OutputDataBean odb) {
		Iterator iter = parseSheet.getParseRules().iterator();
		while (iter.hasNext()) {
			ParseRule parseRule = (ParseRule) iter.next();
			logger.fine("Column Names, "+ parseRule.getParseType()
					+" ParseType:"+parseRule.getParseType());

			if (
					(parseRule.getParseType().equalsIgnoreCase(
					CommonNames.SELECTFIELDDATA))
					|| (parseRule.getParseType()
							.equalsIgnoreCase(CommonNames.SELECTMULTIROWFIELDDATA))
					|| (parseRule.getParseType()
							.equalsIgnoreCase(CommonNames.SELECTALLDATA))
							) {
				Collection workColl = new LinkedList();
				workColl = getParseSheetColumnNames();
				workColl.add(parseRule.getOutputFieldName());
				this.setParseSheetColumnNames(workColl);
				
				logger.fine("Adding Col Name:"+parseRule.getOutputFieldName());
				
//				odb.getOutputDataColumnNames().add(parseRule.getOutputFieldName());
				
				// columnNames.add(parseRule.getOutputFieldName());
			}
		}
	}

	public boolean ruleMatchesReportLine(ParseRule parseRule, String reportLine) {
		if (parseRule.getStartCol() == 0 || parseRule.getEndCol() == 0) {
			logger
					.severe("Rule Requiring Match has NO Start or End Column defined!");
			return false;
		}
		String reportCompareString = getReportCompareString(reportLine,
				parseRule.getStartCol(), parseRule.getEndCol());
		// Check for the string and if found, ignore this line
		if (parseRule.getMatchString().equalsIgnoreCase(CommonNames.ANY)) {
			return true;
		}
		if (parseRule.getMatchString().equalsIgnoreCase(CommonNames.BLANK)) {
			String compareBlank = fillString(reportCompareString.length(), " ");
			// logger.fine("blank"+(reportCompareString.equalsIgnoreCase(compareBlank)));
			if (reportCompareString.equalsIgnoreCase(compareBlank))
				return true;
		} else if (parseRule.getMatchString().equalsIgnoreCase(
				CommonNames.NOTBLANK)) {
			String compareBlank = fillString(reportCompareString.length(), " ");
			logger.fine("comparing: '" + reportCompareString + "' with '"
					+ compareBlank + "'");
			logger.fine("not blank: "
					+ (!reportCompareString.equalsIgnoreCase(compareBlank)));
			logger
					.info("not blank Len: '" + reportCompareString.length()
							+ "'");
			if (reportCompareString.length() == 0)
				return false;
			if (!reportCompareString.equalsIgnoreCase(compareBlank))
				return true;

		} else if (reportCompareString.equalsIgnoreCase(parseRule
				.getMatchString()))
			return true;
		logger.fine("rule matches FAILS");
		return false;
	}

	/**
	 * @return Returns the multiRowLine.
	 */
	public int getMultiRowLine() {
		return multiRowLine;
	}

	/**
	 * @param multiRowLine
	 *            The multiRowLine to set.
	 */
	public void setMultiRowLine(int multiRowLine) {
		this.multiRowLine = multiRowLine;
	}

	/**
	 * @return Returns the multiRowMaxLines.
	 */
	public int getMultiRowMaxLines() {
		return multiRowMaxLines;
	}

	/**
	 * @param multiRowMaxLines
	 *            The multiRowMaxLines to set.
	 */
	public void setMultiRowMaxLines(int multiRowMaxLines) {
		this.multiRowMaxLines = multiRowMaxLines;
	}

	public void loadMultiRows(ParseSheet parseSheet) {

		logger.fine("inside loadMultiRows");
		if (rowExists())
			writeRow();
		// If this is a new row, create a new outputDataColumn collection.
		startNewRow();

		// logger.fine("Inside multirow loading. Selecting rowfield data:
		// "+parseRule.getStartCol() +" end: "+parseRule.getEndCol());
		logger.fine("total multilines: " + getMultiRowLine());
		for (int lineCtr = 1; lineCtr < getMultiRowLine(); lineCtr++) {
			logger.fine("line: " + lineCtr + " val: "
					+ multiRowReportLine[lineCtr]);
		}

		Iterator iter = parseSheet.getParseRules().iterator();
		while (iter.hasNext()) {
			ParseRule parseRule = (ParseRule) iter.next();
			if (parseRule.getParseType().equalsIgnoreCase(
					CommonNames.SELECTMULTIROWFIELDDATA))
				try {
					OutputDataColumn outputDataCol = new OutputDataColumn();
					// Only extract if there is enough line left!

					if (parseRule.getStartRow() <= getMultiRowLine()) {
						logger.fine(">MultiLineExtract row="
								+ parseRule.getStartRow() + " startCol:"
								+ parseRule.getStartCol() + " endCol:"
								+ parseRule.getEndCol());
						String reportLine = multiRowReportLine[parseRule
								.getStartRow()];
						logger.fine(">Multi2: " + reportLine);
						extractSubstringFromReportLine(reportLine, parseRule
								.getStartCol(), parseRule.getEndCol());
						String newValue = extractSubstringFromReportLine(
								reportLine, parseRule.getStartCol(), parseRule
										.getEndCol());
						outputDataCol.setColumnValue(newValue);

						logger.fine("Value: " + reportLine
								+ outputDataCol.getColumnValue());
						logger.fine("Type: " + parseRule.getOutputFieldType());
						outputDataCol.setColumnFieldType(parseRule
								.getOutputFieldType());
						addColumnToRow(outputDataCol);
					}
				} catch (Exception e) {
					logger.severe("Could not write report line out!" + e);
				}
		}
	}

	public String extractSubstringFromReportLine(String reportLine,
			int startCol, int endCol) {
		if (startCol == 0)
			return "";
		if (reportLine.length() >= endCol)
			return reportLine.substring(startCol - 1, endCol);
		else if (reportLine.length() >= (startCol - 1))
			return reportLine.substring(startCol - 1, reportLine.length());
		else
			return "";
	}

	public OutputDataColumn makeNewDataColumn(ParseRule parseRule,
			String reportLine) {
		OutputDataColumn outputDataCol = new OutputDataColumn();
		// Only extract if there is enough line left!
		logger.fine("Output Data: " + parseRule.getStartCol() + " end: "
				+ parseRule.getEndCol());
		String newValue = extractSubstringFromReportLine(reportLine, parseRule
				.getStartCol(), parseRule.getEndCol());
		outputDataCol.setColumnValue(newValue);
		// if (reportLine.length() >=parseRule.getEndCol())
		// outputDataCol.setColumnValue(reportLine.substring(parseRule.getStartCol()-1,parseRule.getEndCol()));
		logger.fine("Report Line: " + reportLine + ", Value Extracted: "
				+ outputDataCol.getColumnValue());
		outputDataCol.setColumnFieldType(parseRule.getOutputFieldType());
		return outputDataCol;
	}
	/**
	 * @return Returns the parseSheetColumnNames.
	 */
	public Collection getParseSheetColumnNames() {
		return parseSheetColumnNames;
	}

	/**
	 * @param parseSheetColumnNames
	 *            The parseSheetColumnNames to set.
	 */
	public void setParseSheetColumnNames(Collection parseSheetColumnNames) {
		this.parseSheetColumnNames = parseSheetColumnNames;
	}

	/**
	 * @return Returns the sheetOutputDataColumns.
	 */
	public Collection getNewRowOutputDataColumns() {
		return newRowOutputDataColumns;
	}

	/**
	 * @param sheetOutputDataColumns
	 *            The sheetOutputDataColumns to set.
	 */
	public void setNewRowOutputDataColumns(Collection newRowOutputDataColumns) {
		this.newRowOutputDataColumns = newRowOutputDataColumns;
	}

	/**
	 * @return Returns the parseSheetOutputDataRows.
	 */
	public Collection getParseSheetOutputDataRows() {
		return parseSheetOutputDataRows;
	}

	/**
	 * @param parseSheetOutputDataRows
	 *            The parseSheetOutputDataRows to set.
	 */
	public void setParseSheetOutputDataRows(Collection parseSheetOutputDataRows) {
		this.parseSheetOutputDataRows = parseSheetOutputDataRows;
	}

	/**
	 * @return Returns the outputDataBeans.
	 */
	public Collection getParsedOutputDataBeans() {
		return parsedOutputDataBeans;
	}

	/**
	 * @param outputDataBeans
	 *            The outputDataBeans to set.
	 */
	public void setOutputDataBeans(Collection outputDataBeans) {
		this.parsedOutputDataBeans = outputDataBeans;
	}

	public boolean addOutputDataBean(Object arg0) {
		return parsedOutputDataBeans.add(arg0);
	}
}
