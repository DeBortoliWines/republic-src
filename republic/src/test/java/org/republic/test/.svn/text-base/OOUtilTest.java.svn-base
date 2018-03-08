/*
 * Created on Apr 19, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.republic.test;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.republic.DataRow;
import org.republic.DataSet;
import org.republic.DataSetDefinition;
import org.republic.OoUtil;

/**
 * @author sfg
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OOUtilTest extends TestCase {
	
	public static Test suite() {
		return new TestSuite(OOUtilTest.class);
	}
	/*
	 * Unravel the Open Office file.
	 */
	public void testUnravel() {
		
		File unravelledDir = OoUtil.unravelOoFile("./TestData/testWriter2.odt","./Testdata/testWriter2");
		
		if (unravelledDir != null){
			File fileList[] = unravelledDir.listFiles();
			int fileCtr = 0;
			while (fileList.length < fileCtr -1){
				System.out.println("File unravelled:"+fileList[fileCtr]);
				fileCtr++;
			}
		}
		assertTrue("Error, unravel directory not created",(unravelledDir!=null));
	}
	public void testUpdateMultiRowContent() {
		String testDir = "./TestData/testWriter2";
		File unravelledDir = OoUtil.unravelOoFile("./TestData/testWriter2.odt",testDir);
		File contentXml = new File(unravelledDir+File.separator+"content.xml");
		String outTestFile =  "./TestData/testWriter2.out.ods";
		try{
			// First update the normal top level fields.
			DataSet dataSet = new DataSet();
			DataSetDefinition dataSetDefinition = new DataSetDefinition();
			dataSetDefinition.setFieldName("firstName");
			dataSetDefinition.setFieldType("string");
			dataSet.addDefinition(dataSetDefinition);
			dataSetDefinition = new DataSetDefinition();
			dataSetDefinition.setFieldName("lastName");
			dataSetDefinition.setFieldType("string");
			dataSet.addDefinition(dataSetDefinition);
			// Now the data.
			DataRow dataRow = new DataRow();
			dataRow.addDataColumn("firstName","Stuart");
			dataRow.addDataColumn("lastName","Guthrie");
			dataSet.addDataRow(dataRow);
			OoUtil.updateWriterContent(contentXml,dataSet);

			dataSet = new DataSet();
			dataSetDefinition = new DataSetDefinition();
			dataSetDefinition.setFieldName("ItemNumber");
			dataSetDefinition.setFieldType("string");
			dataSet.addDefinition(dataSetDefinition);
			dataSetDefinition = new DataSetDefinition();
			dataSetDefinition.setFieldName("ItemValue");
			dataSetDefinition.setFieldType("string");
			dataSet.addDefinition(dataSetDefinition);
			// Now the data.
			dataRow = new DataRow();
			dataRow.addDataColumn("ItemNumber","902-20110 Blue Steel Look & Feel");
			dataRow.addDataColumn("ItemValue","100.00");
			dataSet.addDataRow(dataRow);

			dataRow = new DataRow();
			dataRow.addDataColumn("ItemNumber","902-20112 Red Steel Look & Feel");
			dataRow.addDataColumn("ItemValue","101.00");
			dataSet.addDataRow(dataRow);

			dataRow = new DataRow();
			dataRow.addDataColumn("ItemNumber","902-20119 Black Steel Look & Feel");
			dataRow.addDataColumn("ItemValue","102.00");
			dataSet.addDataRow(dataRow);

			OoUtil.updateWriterMultiRowContent(contentXml,dataSet);
			// Now reform the oo file!
			OoUtil.makeOoFile(testDir,outTestFile);
		}
		catch(Exception e){
			fail("Exception:"+e);
		}
	}
	
	public void testCalcUpdateMultiRowContent() {
		String testDir = "./TestData/testCalc2";
		String outTestFile =  "./TestData/testCalc2.out.ods";
		File unravelledDir = OoUtil.unravelOoFile("./TestData/testCalc2.ods",testDir);
		File contentXml = new File(unravelledDir+File.separator+"content.xml");
		try{
			// First update the normal top level fields.
			DataSet dataSet = new DataSet();
			DataSetDefinition dataSetDefinition = new DataSetDefinition();
			dataSetDefinition.setFieldName("firstName");
			dataSetDefinition.setFieldType("string");
			dataSet.addDefinition(dataSetDefinition);
			dataSetDefinition = new DataSetDefinition();
			dataSetDefinition.setFieldName("lastName");
			dataSetDefinition.setFieldType("string");
			dataSet.addDefinition(dataSetDefinition);
			// Now the data.
			DataRow dataRow = new DataRow();
			dataRow.addDataColumn("firstName","Stuart");
			dataRow.addDataColumn("lastName","Guthrie");
			dataSet.addDataRow(dataRow);
//???			OoUtil.updateCalcContent(contentXml,dataSet);

			dataSet = new DataSet();
			dataSetDefinition = new DataSetDefinition();
			dataSetDefinition.setFieldName("ItemNumber");
			dataSetDefinition.setFieldType("string");
			dataSet.addDefinition(dataSetDefinition);
			dataSetDefinition = new DataSetDefinition();
			dataSetDefinition.setFieldName("ItemValue");
			dataSetDefinition.setFieldType("string");
			dataSet.addDefinition(dataSetDefinition);
			// Now the data.
			dataRow = new DataRow();
			dataRow.addDataColumn("ItemNumber","902-20110 Blue Steel Look & Feel");
			dataRow.addDataColumn("ItemValue","100.00");
			dataSet.addDataRow(dataRow);

			dataRow = new DataRow();
			dataRow.addDataColumn("ItemNumber","902-20112 Red Steel Look & Feel");
			dataRow.addDataColumn("ItemValue","101.00");
			dataSet.addDataRow(dataRow);

			dataRow = new DataRow();
			dataRow.addDataColumn("ItemNumber","902-20119 Black Steel Look & Feel");
			dataRow.addDataColumn("ItemValue","102.00");
			dataSet.addDataRow(dataRow);

//??			OoUtil.updateCalcMultiRowContent(contentXml,dataSet);
			// Now reform the oo file!
			OoUtil.makeOoFile(testDir,outTestFile);
		}
		catch(Exception e){
			fail("Exception:"+e);
		}
	}

}
