/*
 * Created on 19-Jun-2004
 *
 * Holds the ravel and unravel options for OOCalc.
 */
package org.republic;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

/**
 * @author sfg
 *
 * This tool unravels and reravels an ooCalc file.
 *
 * It also can substitute a dataset into a calc2 or writer2 document
 *
 *
 */
public class OoUtil {

	static final int BUFFER = 2048;

	private static Logger logger = Logger
			.getLogger("republic.freelib.OoUtil");

	public static File unravelOoFile(String inputFile) {
		return unravelOoFile(inputFile,null);
	}
	/*
	 * Read an oo file into a tempDir for manipulation. @param oo file. @param
	 * tempDir (if null, uses temp/ooOut)
	 *
	 * @return tempDir created.
	 */
	public static File unravelOoFile(String inputFile, String tempDirName) {

		if (tempDirName == null) {

			tempDirName = System.getProperty("java.io.tmpdir") + File.separator
					+ "ooOut";
		}

		File tempDir = new File(tempDirName);

		try {
			File ooFile = new File(inputFile);
			if (!ooFile.exists()) {
				logger.info("inputFile not found:" + inputFile);
				return null;
			}

			// Clear directory prior to refresh.
			if (tempDir.exists())
				deleteDir(tempDir);

			if (tempDir.exists())
				throw new Exception(
						"Deletion of temp dir failed, perhaps a permission problem?");
			tempDir.mkdirs();
			if (!tempDir.exists()) {
				throw new Exception("Unable to create tempDir:"
						+ tempDir.getAbsolutePath());
			}

			ZipFile zipFile = new ZipFile(ooFile);

			Enumeration entries = zipFile.entries();

			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();

				if (entry.isDirectory()) {
					// Assume directories are stored parents first then
					// children.
					logger.info("Extracting directory: " + entry.getName());
					// This is not robust, just for demonstration purposes.
					(new File(tempDir + File.separator + entry.getName()))
							.mkdirs();
				} else if (entry.getName().indexOf(File.separator) != -1) {
					logger
							.info("Creating Dir even though zip class failed to report it: "
									+ entry.getName());
					File newdir = new File(tempDir + File.separator
							+ entry.getName());
					newdir.getParentFile().mkdirs();
					logger.info("Mkdirs: " + newdir.getParent());
				}

				if (!entry.isDirectory()) {
					logger.info("Extracting file: " + entry.getName());
					copyInputStream(
							zipFile.getInputStream(entry),
							new BufferedOutputStream(new FileOutputStream(
									tempDir + File.separator + entry.getName())));
				}

			}

			zipFile.close();

		} catch (Exception e) {
			System.err
					.println("Icky error in unravel of file from zip, file was:"
							+ inputFile + " error is:" + e);
			return null;
		}
		return tempDir;
	}

	public static final void copyInputStream(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len;

		while ((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);

		in.close();
		out.close();
	}

	/*
	 * This option creates an oo file from a directory of oo content Parameters
	 * @param InputDir - the directory containing the unravelled template.
	 * @param finalFileName
	 */
	public static void makeOoFile(String inputDir, String finalFileName) {
		try {
			zipDirectory(inputDir, finalFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Created Output file:" + finalFileName);
	}

	/*
	 * New OOUtil using DataSet.. type is text for ooWriter type is spreadsheet
	 * for ooCalc
	 */
	public static void updateWriterContent(File contentFile, DataSet dataSet)
			throws Exception {
		SAXBuilder builder = new SAXBuilder();
		try {
			Document doc = builder.build(contentFile);

			resolveOOAliases(doc, dataSet);

			// Access the nodes with XPath.
			XPath xPath = XPath
					.newInstance("//office:document-content/office:body/office:text/text:p/*");

			List nodes = xPath.selectNodes(doc);

			logger.info("This " + contentFile + " has " + nodes.size()
					+ " registered nodes:");

			Iterator i = nodes.iterator();

			while (i.hasNext()) {
				Element element = (Element) i.next();
				replaceElements(element, dataSet);
			}

			XMLOutputter outp = new XMLOutputter(Format.getPrettyFormat());
			FileOutputStream outFileStr = new FileOutputStream(contentFile);
			outp.output(doc, outFileStr);
			outFileStr.close();
			logger.info("Wrote:" + contentFile);

		} catch (Exception e) {
			System.err.println(e.toString());
		}

	}
	public static void updateCalcContent(File contentFile, String sheetName, String databaseName, DataSet dataSet)
	throws Exception {

		SAXBuilder builder = new SAXBuilder();
		try {
			Document doc = builder.build(contentFile);
			// List allNamespaces =
			// doc.getRootElement().getAdditionalNamespaces();
			resolveOOAliases(doc, dataSet);

			Element tableElement = getTableElement( sheetName,doc);
			if (tableElement==null){
				logger.severe("unable to find tablename:"+sheetName);
				return;
			}

			//Attribute attr = sheetElement.getAttribute("name");


			XPath xPath = XPath
			.newInstance("//table:table/table:table-row/*");
			List nodes = xPath.selectNodes(tableElement);


			Element element = substitutionsAvailableCalc(nodes, dataSet);
//			Iterator iter = nodes.iterator();
//			while (iter.hasNext()) {
//				Element element = (Element) iter.next();
//				logger.info(">>Replacing on this node:"+element);
//				replaceCalcElement(element, dataSet);
//			}

			logger.info("Found element?"+element);

			replaceCalcElement(element, dataSet);

//			XPath xPath2 = XPath
//			.newInstance("//office:document-content/office:body/office:spreadsheet/table:table");
//			Element element = (Element)xPath2.selectSingleNode(doc);
//			removeElementRow(1,element);

			XMLOutputter outp = new XMLOutputter(Format.getPrettyFormat());
			FileOutputStream outFileStr = new FileOutputStream(contentFile);
			outp.output(doc, outFileStr);
			outFileStr.close();
			logger.info("Wrote:" + contentFile);

		} catch (Exception e) {
			System.err.println(e.toString());
		}

	}

	/*
	 * Uses new dataset definition.
	 */
	public static void updateWriterMultiRowContent(File contentFile,
			DataSet dataSet) throws Exception {

		// String fileName = "testWriter/content.xml";
		SAXBuilder builder = new SAXBuilder();
		try {
			Document doc = builder.build(contentFile);

			resolveOOAliases(doc, dataSet);

			// Access the nodes with XPath.
			XPath xPath = XPath
					.newInstance("//office:document-content/office:body/office:text/table:table/table:table-row/*");

			List nodes = xPath.selectNodes(doc);

			// logger.info("This "+contentFile + " has "+ nodes.size() +"
			// registered nodes:");
			Element element = substitutionsAvailable(nodes, dataSet);
			if (element != null) {
				// If so, copy the whole node from this point, update it and add
				// it back into the tree.
				Element tableRowElement = element.getParentElement();
				Element tableElement = tableRowElement.getParentElement();
				// Clone the tableRow.
				Element tableRowTemplate = (Element) tableRowElement.clone();
				// Delete the original Row.
				tableRowElement.detach();

				Iterator dataRowIter = dataSet.getDataRows().iterator();
				int iCtr = 0;
				while (dataRowIter.hasNext()) {
					logger.info("create Row:" + iCtr);
					DataRow dataRow = (DataRow) dataRowIter.next();
					tableRowTemplate = replaceWriterMultipleElements(
							tableRowTemplate, dataRow);
					// Attach to parent.
					tableElement.addContent(tableRowTemplate);
					// Clone again..
					tableRowTemplate = (Element) tableRowElement.clone();
					iCtr++;
				}
			}

			XMLOutputter outp = new XMLOutputter(Format.getPrettyFormat());
//			outp.output(doc, System.out);
			FileOutputStream outFileStr = new FileOutputStream(contentFile);
			outp.output(doc, outFileStr);
			outFileStr.close();
			//logger.info("Wrote:" + contentFile);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.toString());
		}
	}
	public static Element getTableElement(String sheetName,Document doc)
		throws Exception{
//		 Need to set the tablename to the dbname
		XPath xPath = XPath.newInstance("//office:document-content/office:body/office:spreadsheet/table:table");
		List tableNodes = xPath.selectNodes(doc);
		Element tableElement= null;

		Iterator tableIter = tableNodes.iterator();
		while (tableIter.hasNext()){
			tableElement = (Element)tableIter.next();
			Attribute attribute = getCalcAttribute(tableElement, "name");
			//logger.info("Check Table Name:"+attribute.getValue()+" equal to Sheet Name:"+sheetName);
			if (attribute!=null
					&& attribute.getValue().equalsIgnoreCase(sheetName)){
				logger.info("Matched!:"+sheetName);
				attribute.setValue(sheetName);
				break;
			}
			tableElement=null;
		}
		return tableElement;

	}
	public static void updateCalcMultiRowContent(File contentFile,	String sheetName,
			String databaseName, DataSet dataSet) throws Exception {

		SAXBuilder builder = new SAXBuilder();
		try {

			logger.info("DS Definitions are: "+dataSet.listDefinitions());

			Document doc = builder.build(contentFile);

			resolveOOAliases(doc, dataSet);
			// Go get the table (sheet with the name provided..
			Element tableElement = getTableElement(sheetName,doc);

			if (tableElement==null){
				logger.severe("unable to find tablename:"+sheetName);
				return;
			}

			Attribute attribute = getCalcAttribute(tableElement, "name");
			if (attribute!=null && !databaseName.equalsIgnoreCase(sheetName) ){
				logger.info("Setting Table name to :"+databaseName);
				attribute.setValue(databaseName);
			}

//			XPath xPath2 = XPath.newInstance("//table:table/table:table-row/*");
			XPath xPath2 = XPath.newInstance("table:table-row/*");
			List nodes2 = xPath2.selectNodes(tableElement);

			Element element = substitutionsAvailableCalc(nodes2, dataSet);
			//logger.info("element found so subs available:" + element);
			//logger.info("Parent found , sheet is:" + element.getParent());
			//Element el = (Element)element.getParent().getParent();
			//Attribute attribute2 = getCalcAttribute((Element)element.getParent(), "name");
			//logger.info("ParentAttrs:"+el.getAttributes());

			if (element != null) {
				// If so, copy the whole node from this point, update it and add
				// it back into the tree.
				Element tableRowElement = element.getParentElement();
				Element mainTableElement = tableRowElement.getParentElement();
				// Clone the tableRow.
				Element newTableRowTemplate = (Element) tableRowElement.clone();
				// Delete the original Rows
//				removeElementRow(1,mainTableElement);
				tableRowElement.detach();
				// Now do the usual to the template.
				Iterator dataRowIter = dataSet.getDataRows().iterator();
				int iCtr = 0;
				while (dataRowIter.hasNext()) {
					DataRow dataRow = (DataRow) dataRowIter.next();
					if (dataRow.dataColumns.keySet().size() != 0){
						newTableRowTemplate = replaceCalcMultipleElements(
								newTableRowTemplate, dataSet, dataRow);
						// Attach to parent.
						mainTableElement.addContent(newTableRowTemplate);
						// Clone again..
						newTableRowTemplate = (Element) tableRowElement.clone();
						iCtr++;
					}
				}
			}

			XMLOutputter outp = new XMLOutputter(Format.getRawFormat());
			FileOutputStream outFileStr = new FileOutputStream(contentFile);
			outp.output(doc, outFileStr);
			outFileStr.close();


		} catch (Exception e) {
			e.printStackTrace();
			logger.severe(e.toString());

		}
	}

	/*
	 * Removes the ith row of a table.
	 * @PARAM row to remove
	 * @PARAM tableElement
	 */
	public static void removeElementRow(int iRowNum,Element tableElement){
		int iRow=0;
		Element removeMe = null;
		Iterator eleIter = tableElement.getChildren().iterator();
		while(eleIter.hasNext()){
			Element el = (Element)eleIter.next();
			logger.info("Element to remove?"+el+"eltype:"+el.getName()+" / ns:"+el.getNamespacePrefix());
			if(el.getName().equalsIgnoreCase("table-row")){
				iRow++;
				logger.info("row:"+iRow);
				if (iRow==iRowNum){
					logger.info("match!: "+iRow);
					removeMe = el;
					break;
				}
			}
		}

		if (removeMe!=null){
			logger.info("detached:"+removeMe);
			removeMe.detach();
		}
	}

	public static Element substitutionsAvailable(List nodes,
			DataSet fieldChanges) {
		Iterator i = nodes.iterator();

		while (i.hasNext()) {
			Element element = (Element) i.next();
			logger.info("Element:" + element);
			// First ascertain if the table Row contains any required styles.
			if (!checkForMatch(element, fieldChanges))
				continue;
			logger.info("found element");
			return element;
		}
		return null;
	}

	public static Element substitutionsAvailableCalc(List nodes, DataSet dataSet) {

		Iterator iter = nodes.iterator();

		while (iter.hasNext()) {

			Element element = (Element) iter.next();
			Attribute attribute = getCalcAttribute(element, "style-name");

			if (attribute != null) {
				String styleValue = attribute.getValue();
				logger.info("Style:" + styleValue);
				DataSetDefinition dataSetDefinition = dataSet
						.getDefinition(styleValue);
				if (dataSetDefinition != null) {
					return element;
				}
			}
		}
		return null;
	}

	/*
	 * Calc2 version
	 */
	private static Attribute getCalcAttribute(Element element, String name) {
		// JDOM broken for simple call.
		Iterator iter = element.getAttributes().iterator();
		while (iter.hasNext()) {
			Attribute attribute = (Attribute) iter.next();
//			logger.info("attr name is:"+attribute.getName()+" must equal "+name
//					+ " does it? "+attribute.getName().equalsIgnoreCase(name));
			if (attribute.getName().equalsIgnoreCase(name)){
				return attribute;
			}
		}
		return null;
	}

	private static Attribute getAttribute(Element element, String name) {
		// Replace this call with an iterator as it required a patch to JDOM!!!
		return element.getAttribute(name, Namespace.NO_NAMESPACE);
	}

	/*
	 * This process works through a tree of data to create multiple rows one per
	 * data set row
	 *
	 * TODO iCtr is used to work out where we are in the process of unravelling
	 * the data. Need to unravel and check the data.
	 */
	private static Element replaceWriterMultipleElements(Element element,
			DataRow dataRow) {

		replaceElements(element, dataRow);
		return element;
	}

	private static void replaceElements(Element element, DataRow dataRow) {

		//logger.info("element:" + element + " attrs:" + element.getAttributes());
		Attribute attribute = getAttribute(element, "style-name");

		if (attribute != null) {
			String styleValue = attribute.getValue();
			//logger.info("Style:" + styleValue);
			String valueField = null;
			try {
				valueField = (String) dataRow.getDataColumn(styleValue);
			} catch (Exception e) {
			}

			if (valueField != null) {
				element.setText(valueField);
			}
		} else {
			logger.info("Style Attribute is NULL!!");
		}

		// Also need to drill down further as odd behaviour showing in OO where
		// styles encompass styles.
		Iterator childIter = element.getChildren().iterator();
		while (childIter.hasNext()) {
			// Same check.
			Element childElement = (Element) childIter.next();
			replaceElements(childElement, dataRow);
		}
	}

	private static Element replaceCalcMultipleElements(Element element,
			DataSet dataSet, DataRow dataRow) {

		//add more columns for this row if the styles are not enough
		//here the attribute style-name is used to index the column
		int numOfColumns=element.getChildren().size()-1;
		int numOfColumnsExpected=dataRow.dataColumns.size();
		Element firstElement=null;
		Attribute firstStyleAttribute =null;
		if(numOfColumnsExpected>numOfColumns){
			firstElement=(Element)element.getChildren().get(0);
			firstStyleAttribute = getCalcAttribute(firstElement, "style-name");
			String value=firstStyleAttribute.getValue();
			String prefix="";
			if(value.contains(CommonNames.DATA)){
				prefix=CommonNames.DATA;
			}else{
				prefix=CommonNames.COLHDG;
			}
			int diffOfNum=numOfColumnsExpected-numOfColumns;
			int i=1;
			while(i<=diffOfNum){
				Element newElement=(Element)firstElement.clone();
				Attribute newAttribute = getCalcAttribute(newElement, "style-name");
				newAttribute.setValue(prefix+(numOfColumns+i));
				element.addContent(numOfColumns+i-1,newElement);
				i=i+1;
			}
		}
		Iterator elementIter = element.getChildren().iterator();
		int i=1;
		while (elementIter.hasNext()) {
			Element childElement = (Element) elementIter.next();
			replaceCalcElement(childElement, dataSet, dataRow);
			//set the style to the first column style for the added columns
			if(numOfColumnsExpected>numOfColumns && i>numOfColumns && i<=numOfColumnsExpected){
				Attribute childAttribute = getCalcAttribute(childElement, "style-name");
				if(childAttribute!=null){
					childAttribute.setValue(firstStyleAttribute.getValue());
				}
			}
			i=i+1;
		}
		return element;
	}

	/*
	 * Specifically designed to replace calc elements..
	 */
	private static void replaceCalcElement(Element element, DataSet dataSet, DataRow dataRow) {

		Attribute attribute = getCalcAttribute(element, "style-name");

		if (attribute != null) {
			String styleValue = attribute.getValue();

			Object dataValue = dataRow.getDataColumn(styleValue);

			if (dataValue != null) {
				DataSetDefinition dsd = dataSet.getDefinition(styleValue);
				//logger.info("DSD.fieldType"+dsd.getFieldType());
				if (dsd.getFieldType().equalsIgnoreCase("string"))
					insertCalcText(element, dataValue);
				else if (dsd.getFieldType().equalsIgnoreCase("date"))
					insertCalcDate(element, dataValue);
				else if (dsd.getFieldType().equalsIgnoreCase("currency"))
					insertCalcCurrency(element, dataValue);
				else if (dsd.getFieldType().equalsIgnoreCase("number"))
					insertCalcNumber(element, dataValue);
				else
					insertCalcText(element, dataValue);
			}
		}
	}

	/*
	 * Specifically designed to replace calc elements..
	 */
	private static void replaceCalcElement(Element element, DataSet dataSet) {

		Attribute attribute = getCalcAttribute(element, "style-name");
		//logger.info("Attr1:"+attribute);
		if (attribute == null) {
			attribute = getCalcAttribute(element, "parent-style-name");
			//logger.info("Attr2:"+attribute);
		}

		if (attribute != null) {
			String styleValue = attribute.getValue();
			//logger.info("Looking for Style:"+styleValue);
			DataSetDefinition dataSetDefinition = dataSet
					.getDefinition(styleValue);
			if (dataSetDefinition != null) {

				Object dataValue = dataSet
						.getFirstDataSetValue(dataSetDefinition);
				//logger.info("Found Style:"+styleValue+" dataValue is:"+dataValue);
				insertCalcText(element, dataValue);
			}
		}
	}
/*
 *
 need this:
 						<table:table-cell office:value-type="date" office:date-value="2005-02-01">
						<text:p>01/02/05</text:p>
						</table:table-cell>
getting this:
						<table:table-cell table:style-name="Data3" xmlns:office="office" office:value-type="date"
									office:date-value="2004-07-15">
							<text:p xmlns:text="text">15/07/2004</text:p>
						</table:table-cell>

	<table:table-cell table:style-name="ce3" office:value-type="float" office:value="121"><t
	ext:p>121</text:p></table:table-cell>
	*/
	private static void insertCalcDate(Element element, Object dataValue) {
//		logger.info("date:"+dataValue);
		Namespace ns = Namespace.getNamespace("text","urn:oasis:names:tc:opendocument:xmlns:text:1.0");
		Namespace tableNs = Namespace.getNamespace("table","urn:oasis:names:tc:opendocument:xmlns:table:1.0");
		Element textEl = new Element("p", "text", ns.getURI());

		String dataString = (String) dataValue;
		Date cellDate = null;
		SimpleDateFormat sdfFinal = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfLocale = new SimpleDateFormat("dd/MM/yy");
		SimpleDateFormat sdfLocaleCC = new SimpleDateFormat("dd/MM/yyyy");
		try{
			cellDate = sdfLocale.parse(dataString);
			dataString = sdfLocaleCC.format(cellDate);
		}
		catch(Exception e){}

		Namespace officeNs = Namespace.getNamespace("office", "office");
		element.setAttribute("value-type","date",officeNs);
		// Added
		logger.info("TableNS:"+tableNs+" " +tableNs.getPrefix());
		removeattribute(element,"style-name");
		// The VITAL line that inserts the template.ods table:style ce2 as the date format!
		element.setAttribute("style-name","ce2",tableNs);

		if (cellDate!=null)
			element.setAttribute("date-value",sdfFinal.format(cellDate),officeNs);
		textEl.setText(dataString);
		element.getChildren().clear();
		element.getChildren().add(textEl);
	}
	public static String replaceString(String oldString, String oldVal, String newVal)
    {
    	// Always escape value to ensure it works.
    	Pattern pat=Pattern.compile("\\"+oldVal);
	    Matcher matcher=pat.matcher(oldString);
		return matcher.replaceAll(newVal);
    }
	//<table:table-cell table:style-name="ce1" office:value-type="currency"
	// office:currency="AUD" office:value="15200"><text:p>$15,200.00</text:p>
		//</table:table-cell>

	/*
	 *

	 Should Be:

  <table:table-cell
  	office:value-type="currency"
  	office:currency="AUD"
  	office:value="0.01">
  		<text:p>$0.01</text:p>
</table:table-cell>
         Currently:

<table:table-cell
	xmlns:office="office"
	office:value-type="currency"
	office:currency="AUD"
	office:value="0.01">
            <text:p xmlns:text="text">$0.01</text:p>
  </table:table-cell>

	 */

	private static void insertCalcCurrency(Element element, Object dataValue) {
		Namespace textNs = Namespace.getNamespace("text","urn:oasis:names:tc:opendocument:xmlns:text:1.0");
		Namespace officeNs = Namespace.getNamespace("office", "office");
		Namespace tableNs = Namespace.getNamespace("table", "table");

		element.setAttribute("value-type","currency",officeNs);
		element.setAttribute("currency","AUD",officeNs);

		removeattribute(element,"style-name");

		String dataString = (String) dataValue;
		String origDataString = dataString;
		dataString = replaceString(dataString,"$","");
		dataString = replaceString(dataString,",","");
		dataString = dataString.trim();

		if (dataString.startsWith("."))
			dataString="0"+dataString;

		element.setAttribute("value",dataString,officeNs);
		origDataString = origDataString.trim();
		if (origDataString.startsWith("."))
			origDataString="0"+origDataString;
		if (!origDataString.startsWith("$"))
			origDataString = "$"+origDataString;
		Element textEl = new Element("p", "text", textNs.getURI());
		textEl.setText(origDataString);
		element.getChildren().clear();
		element.getChildren().add(textEl);
		//removeattribute(textEl,"text");
	}

	private static void removeattribute(Element element,String attrName){
		//logger.info("size"+element.getAttributes().size());
		Iterator childIter = element.getAttributes().iterator();
		while (childIter.hasNext()) {
			// Same check.
			Attribute attribute = (Attribute) childIter.next();
			//logger.info("Checking Attr:"+attribute.getName());
			if (attrName.equalsIgnoreCase(attribute.getName())){
				logger.info("Remove Attr:"+attribute.getName());
				attribute.detach();
				return;
			}
		}
	}

	private static void insertCalcText(Element element, Object dataValue) {
		Namespace ns = Namespace.getNamespace("text","urn:oasis:names:tc:opendocument:xmlns:text:1.0");

		Element textEl = new Element("p", "text", ns.getURI());
		textEl.setText((String) dataValue);
		element.getChildren().clear();
		element.getChildren().add(textEl);
	}

	private static void insertCalcNumber(Element element, Object dataValue) {
		Namespace ns = Namespace.getNamespace("text","urn:oasis:names:tc:opendocument:xmlns:text:1.0");

		String strValue = ((String)dataValue).trim();

		element.setAttribute("value-type", "float", Namespace.getNamespace("office", "office"));
		element.setAttribute("value", strValue, Namespace.getNamespace("office", "office"));
		element.setAttribute("value-type", "float", Namespace.getNamespace("calcext", "calcext"));

		Element textEl = new Element("p", "text", ns.getURI());
		//textEl.setText((String) dataValue);
		textEl.setText(strValue);
		element.getChildren().clear();
		element.getChildren().add(textEl);
	}

	private static void replaceElements(Element element, DataSet dataSet) {

		Attribute attribute = getAttribute(element, "style-name");

		if (attribute != null) {
			String styleValue = attribute.getValue();
			logger.info("Style:" + styleValue);
			DataSetDefinition dataSetDefinition = dataSet
					.getDefinition(styleValue);
			if (dataSetDefinition != null) {
				Object dataValue = dataSet
						.getFirstDataSetValue(dataSetDefinition);
				element.setText((String) dataValue);
			}
		} else {
			logger.info("Style Attribute is NULL!!");
		}

		// Also need to drill down further as odd behaviour showing in OO where
		// styles encompass styles.
		Iterator childIter = element.getChildren().iterator();
		while (childIter.hasNext()) {
			// Same check.
			Element childElement = (Element) childIter.next();
			replaceElements(childElement, dataSet);
		}
	}

	/*
	 * Check to see of this element's children contain any of the required
	 * styles. If so, return true.
	 */
	private static boolean checkForMatch(Element element, DataSet dataSet) {
		Iterator childIter = element.getChildren().iterator();
		while (childIter.hasNext()) {
			Element childElement = (Element) childIter.next();
			Attribute attribute = getAttribute(childElement, "style-name");
			logger.info("child node is:" + childElement);

			if (attribute != null) {
				String style = attribute.getValue();
				logger.info("style=" + style);
				DataSetDefinition dataSetDefinition = dataSet
						.getDefinition(style);
				if (dataSetDefinition != null)
					return true;
			} else {
				logger.info("No style attribute found for " + childElement);
			}
			// Check children as oowriterr has/had weird style handling.

			if (checkForMatch(childElement, dataSet))
				return true;
		}
		return false;
	}

	/*
	 * Delete all files sub-directories.
	 */
	private static void deleteDir(File dirToDelete) throws Exception {

		if (!dirToDelete.exists())
			return;

		File subFiles[] = dirToDelete.listFiles();
		int fileCtr = 0;
		while (fileCtr < subFiles.length) {
			if (subFiles[fileCtr].isDirectory())
				deleteDir(subFiles[fileCtr]);
			subFiles[fileCtr].delete();
			fileCtr++;
		}
		// Now the top level.
		dirToDelete.delete();

	}

	/*
	 * Uses new dataSet definition and changes that name directly if found.
	 */
	private static void resolveOOAliases(Document doc, DataSet dataSet)
			throws Exception {
		XPath xPath = XPath
				.newInstance("//office:document-content/office:automatic-styles/*");
		List styleNodes = xPath.selectNodes(doc);
		Iterator ii = styleNodes.iterator();
		while (ii.hasNext()) {
			Element element = (Element) ii.next();
			// logger.info(">>element:"+element+"
			// attrs:"+element.getAttributes());
			Attribute parentStyleName = getAttribute(element,
					"parent-style-name");
			if (parentStyleName != null) {
				// logger.info("Value:"+parentStyleName.getValue());
				// Now check the HashMap and see if the current value is there..
				if (dataSet.getDefinitions().contains(
						parentStyleName.getValue())) {
					Attribute nameAttribute = getAttribute(element, "name");
					// Got Alias
					String alias = nameAttribute.getValue();
					logger.info("altering hashmap");
					DataSetDefinition dataSetDefinition = dataSet
							.getDefinition(parentStyleName.getValue());
					logger.info("Changing this name:"
							+ dataSetDefinition.getFieldName());
					dataSetDefinition.setFieldName(alias);
				} else {
					logger
							.info("Alias not found in dataset definitions, parent-style, name: "
									+ parentStyleName.getValue());
				}
			}
		}

	}

	public static void zipDirectory(String dir2zip, String endFilename)
			throws Exception {

		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(
				endFilename));
		zipDirectory(dir2zip, dir2zip, zos);
		zos.close();

	}

	public static void zipDirectory(String parentDir, String dir2zip,
			ZipOutputStream zos) {
		try {
			// create a new File object based on the directory we
			// have to zip File
			File zipDir = new File(dir2zip);
			// get a listing of the directory content
			String[] dirList = zipDir.list();
			byte[] readBuffer = new byte[2156];
			int bytesIn = 0;
			// loop through dirList, and zip the files
			for (int i = 0; i < dirList.length; i++) {
				File f = new File(zipDir, dirList[i]);
				if (f.isDirectory()) {
					// if the File object is a directory, call this
					// function again to add its content recursively
					String filePath = f.getPath();
					zipDirectory(parentDir, filePath, zos);
					// loop again
					continue;
				}
				// if we reached here, the File object f was not
				// a directory reate a FileInputStream on top of f
				FileInputStream fis = new FileInputStream(f);
				// create a new zip entry
				// Note, needs to create the directory with the parent dir bit
				// chopped off!
				String zippath = f.getAbsolutePath().substring(
						new File(parentDir).getAbsolutePath().length() + 1);
				ZipEntry anEntry = new ZipEntry(zippath);
				// place the zip entry in the ZipOutputStream object
				zos.putNextEntry(anEntry);
				// now write the content of the file to the ZipOutputStream
				while ((bytesIn = fis.read(readBuffer)) != -1) {
					zos.write(readBuffer, 0, bytesIn);
				}
				// close the Stream
				fis.close();
			}
		} catch (Exception e) {
			// handle exception
		}
	}

}
