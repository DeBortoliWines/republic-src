/*
 * Created on Jun 6, 2004
 *
 * @author Stuart Guthrie
 */
package org.republic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

/**
 * @author sfg
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class ParseFile {

	private Logger logger = Logger.getLogger(this.getClass().getName());

//	private ParseParameters parseParameters;

//	private ParseSheet parseSheet;

	//private OutputDataColumn outputDataCol;

	// New Row OutputDataColumns
//	private Collection newRowOutputDataColumns = new LinkedList();
	
	// These hold the current parse sheet being created
//	private Collection parseSheetColumnNames = new LinkedList();
//	private Collection parseSheetOutputDataRows = new LinkedList();

	// List of all parsed Output Data Beans so far..
	private Collection parsedOutputDataBeans = new LinkedList();

//	private int multiRowLine = -1;

//	private int multiRowMaxLines = -1;

	private String multiRowReportLine[] = new String[200];

	private boolean newPage = true;

	private boolean allDataMode = false;

	public void executeParseFile(String inputReportFileString,
			String outputFileString, String xmlParseFileString,
			String loggerLevelString) {

		if (loggerLevelString == null)
			logger.setLevel(Level.WARNING);
		else if (loggerLevelString.equalsIgnoreCase("debug"))
			logger.setLevel(Level.FINEST);
		else
			logger.setLevel(Level.WARNING);
		
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

		File unravelledDir = null;
		File contentXml = null;
		
		// Now run the OoUtil to create the relevant spreadsheet!
		try {
			logger.info("ODS Template:"+CommonNames.ODSTEMPLATE);
			String finalTemplate=CommonNames.ODSTEMPLATE;
			File odsTemplate=new File(CommonNames.ODSTEMPLATE);
			if(!odsTemplate.exists()){
				//try to use the default 
				String tempTemplateName = System.getProperty("java.io.tmpdir") + File.separator
				+ "template.ods";				
				File tmpTemplate=new File(tempTemplateName);
				if(tmpTemplate.exists()){
					tmpTemplate.delete();
				}
				tmpTemplate.createNewFile();
				FileOutputStream os=new FileOutputStream(tmpTemplate);				
				InputStream is=this.getClass().getResourceAsStream("template.ods");
				OoUtil.copyInputStream(is, os);
				os.close();
				is.close();				 
				finalTemplate=tempTemplateName;
			}
			logger.info("final template:"+finalTemplate);
			unravelledDir = OoUtil.unravelOoFile(
					finalTemplate);
			contentXml = new File(unravelledDir + File.separator
					+ "content.xml");
			
		} catch (Exception e) {
			logger.severe("Problems creating spreadsheet" + e);
		}

//		String resultsFile = CommonNames.OUTPUTRESULTS;

		// We are going to parse the input file once for each set of output
		// sheet parse rules.
		// Later we hope to make all sheets parse simultaneously thus only
		// reading the report once.
		
		
		
		Iterator sheetIter = parseParameters.getParseSheets().iterator();

		while (sheetIter.hasNext()) {

			ParseSheet thisParseSheet = (ParseSheet) sheetIter.next();

			// Initialise for processing.
			allDataMode = false;
			
			
			ParseFileSheet parseFileSheet = new ParseFileSheet();

			OutputDataBean odb = parseFileSheet.executeParseFileSheet(thisParseSheet, inputReport);
			
			this.addOutputDataBean(odb);
			
		}
			
			Iterator iter = this.getParsedOutputDataBeans().iterator();
			
			logger.info("Number of Parse Sheets:"+this.getParsedOutputDataBeans().size());
			// This is used to first substitute the column names in the
			// Template Spreadsheet.
			int sheetCtr = 0;
			while (iter.hasNext()) {
				OutputDataBean odb = (OutputDataBean) iter.next();
				logger.info("New Sheet:"+odb.getDataBaseName());
				logger.info("Cols:"+odb.getOutputDataColumnNames().size());
				logger.info("Rows:"+odb.getOutputDataRows().size());
				// Moved here so as to allow extra sheets.
				DataSet dataSetColumnNames = new DataSet();
				DataRow dataRowColumn = new DataRow();
				DataSet dataSetRowValues = new DataSet();
				sheetCtr++;
				
				// Iterate through the datacolumn names. 
				// Create one dataSetColumnNames Definition per column.
				Iterator colIter = odb.getOutputDataColumnNames().iterator();
				int iCtr = 1;
				while (colIter.hasNext()) {
					String odc = (String) colIter.next();
					logger.info("1.Column Value:"+odc+", Sheet:"+odb.getDataBaseName());
					DataSetDefinition dsd = new DataSetDefinition();
					dsd.setFieldName(CommonNames.COLHDG + iCtr);
					dsd.setFieldType("string");
					dataSetColumnNames.addDefinition(dsd);
					// Add to DataRowColumn for COLHDGx, Val
					dataRowColumn.addDataColumn(CommonNames.COLHDG + iCtr, odc);

					// Also useful for value columns.
					DataSetDefinition dsd2 = new DataSetDefinition();
					dsd2.setFieldName(CommonNames.DATA + iCtr);
					dsd2.setFieldType("string");
					// Add to DataSetRowValues
					dataSetRowValues.addDefinition(dsd2);
					iCtr++;
				}
				
				// Add the single row of col data to the col dataset.
				dataSetColumnNames.addDataRow(dataRowColumn);
				
				// Now load the data..
				iCtr = 1;
				logger.info("Output Data Rows:"+odb.getOutputDataRows().size());
				Iterator rowIter = odb.getOutputDataRows().iterator();
				while (rowIter.hasNext()) {
					OutputDataRow odr = (OutputDataRow) rowIter.next();
					DataRow dataRow = new DataRow();
					Iterator dataRowColIter = odr.getOutputDataColumns()
							.iterator();
					int colCtr = 1;
					//logger.info("Output Data Column:"+odr.getOutputDataColumns().size());
					while (dataRowColIter.hasNext()) {
						OutputDataColumn odc = (OutputDataColumn) dataRowColIter
								.next();
						logger.info("data is :"+ odc.getColumnValue());
						logger.info("dataType is:"+odc.getColumnFieldType());
						logger.info("Getting For:"+CommonNames.DATA+ colCtr);
						DataSetDefinition dsd = dataSetRowValues.getDefinition(CommonNames.DATA+ colCtr);
						if (dsd!=null){
							//logger.info("dsd is:"+dsd);
							dsd.setFieldType(odc.getColumnFieldType());
							dataRow.addDataColumn(CommonNames.DATA+ colCtr, odc.getColumnValue());
							colCtr++;
						}
					}
					dataSetRowValues.addDataRow(dataRow);
					iCtr++;
				}
				
				try{
					// This is the point at which to create a worksheet...
					logger.info(" updateCalcMultiRowContent1 size of rows: "+dataSetColumnNames.getDataRows().size());
//if (sheetCtr !=2)					
					OoUtil.updateCalcMultiRowContent(contentXml, "Sheet"+sheetCtr, "Sheet"+sheetCtr, dataSetColumnNames);
					logger.info("Finished updateCalcMultiRowContent1: "+contentXml.length()+" for DB:"+odb.getDataBaseName());
					OoUtil.updateCalcMultiRowContent(contentXml, "Sheet"+sheetCtr, odb.getDataBaseName(), dataSetRowValues);
					logger.info("Finished updateCalcMultiRowContent2: "+contentXml.length()+" for DB:"+odb.getDataBaseName());
				}
				catch(Exception e){
					e.printStackTrace();
					logger.severe("Problems creating spreadsheet" + e);
				}

			}

			// Now run the OoUtil to create the relevant spreadsheet!
			try {
				// Now reform the oo file!
				OoUtil.makeOoFile(unravelledDir.getAbsolutePath(), outputFileString);
				logger.info("Make ooFile complete: "+outputFileString);

			} catch (Exception e) {
				logger.severe("Problems creating spreadsheet" + e);
			}
			
//		}
	
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


	public InputStream getLocalOrJarResource(String inputResource,
			String inputResourceLocation) {
		InputStream is = null;
		//first get the user defined resource
		File mappingFile = new File(inputResourceLocation);
		try {
			is = new BufferedInputStream(new FileInputStream(mappingFile));
		} catch (Exception e) {
			// This exception is fine, we will check for the real mapping file
			// in the source..					
		}		
		//otherwise use the default resource
		if (is == null) {
			try {
				is = this.getClass().getResourceAsStream(
						inputResource);
			} catch (Exception e1) {
				logger.severe("Unable to access directly:" + mappingFile);
			}
		}

		return is;
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
