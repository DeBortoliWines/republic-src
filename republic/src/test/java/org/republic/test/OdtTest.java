package org.republic.test;

import java.io.File;

import junit.framework.TestCase;

import org.republic.ParseFile;

public class OdtTest extends TestCase {
	/*
	 * Test that the first example works.
	 * This runs the Republic parse and then tests its results 
	 * against expectations.
	 * 
	 * This currently only tests that the whole thing completes and produces 
	 * a file. Later would be nice to get it to check for 
	 * content correctly.
	 * 
	 */
	public void testExample1(){
		String args[] = new String[4];
		args[0] = "TestData/TestReport001.rpt";
		args[1] = "TestData/Example1.ods";
		args[2] = "TestData/Example1Parse.xml";
		args[3]="debug";
		ParseFile parseFile = new ParseFile();
		parseFile.executeParseFile(args[0],args[1],args[2],args[3]);

		//Start.main(args);
		
		// Now examine the output.
		// Is the file there?
		File outputFile = new File(args[1]);
		if (!outputFile.exists()){
			fail("Output file does not exist!");
		}
	}
	public void testExample2(){
		String args[] = new String[4];
		args[0] = "TestData/TestReport002.rpt";
		args[1] = "TestData/Example2.ods";
		args[2] = "TestData/Example2Parse.xml";
		args[3]="debug";
		
		ParseFile parseFile = new ParseFile();
		parseFile.executeParseFile(args[0],args[1],args[2],args[3]);

		
		// Now examine the output.
		// Is the file there?
		File outputFile = new File(args[1]);
		if (!outputFile.exists()){
			fail("Output file does not exist!");
		}
	}
	public void testExample3(){
		String args[] = new String[4];
		args[0] = "TestData/TestReport003.rpt";
		args[1] = "TestData/Example3.ods";
		args[2] = "TestData/Example3Parse.xml";
		args[3]="debug";
		
		ParseFile parseFile = new ParseFile();
		parseFile.executeParseFile(args[0],args[1],args[2],args[3]);
		
		// Now examine the output.
		// Is the file there?
		File outputFile = new File(args[1]);
		if (!outputFile.exists()){
			fail("Output file does not exist!");
		}
	}
//	public void testExample4(){
//		// Now run the OoUtil to create the relevant spreadsheet!
//		try {
//			//String tempDir = "/tmp";
//			
//			File unravelledDir = OoUtil.unravelOoFile(
//					CommonNames.ODSTEMPLATE);
//			File contentXml = new File(unravelledDir + File.separator
//					+ "content.xml");
//			
//			DataSet dataSetColumnNames = new DataSet();
//			DataSet dataSetRowValues = new DataSet();
//			DataSetDefinition dsd = new DataSetDefinition();
//			dsd.setFieldName("ColHdg1");
//			dsd.setFieldType("string");
//			dataSetColumnNames.addDefinition(dsd);
//			 dsd = new DataSetDefinition();
//			dsd.setFieldName("ColHdg2");
//			dsd.setFieldType("string");
//			dataSetColumnNames.addDefinition(dsd);
//			DataRow dr = new DataRow();
//			dr.addDataColumn("ColHdg1","Test1");
//			dr.addDataColumn("ColHdg2","Test2");
//			dataSetColumnNames.addDataRow(dr);
//			
//			System.out.println("rows:"+dataSetColumnNames.getDataRows().size());
//			
//			OoUtil.updateCalcContent(contentXml, dataSetColumnNames);
//			
//			dsd = new DataSetDefinition();
//			dsd.setFieldName(CommonNames.DATA+"1");
//			dsd.setFieldType("string");
//			dataSetRowValues.addDefinition(dsd);
//			
//			OoUtil.updateCalcMultiRowContent(contentXml, dataSetRowValues);
//
//			// Now reform the oo file!
//			OoUtil.makeOoFile(unravelledDir.getAbsolutePath(), "TestData/Example4.ods");
//			System.out.println("Make ooFile complete: "+"TestData/Example4.ods");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.err.println("Problems creating spreadsheet" + e);
//		}
//	}
	public void testMoreThan30Columns(){
		String args[] = new String[4];
		args[0] = "TestData/TestReport005.rpt";
		args[1] = "TestData/Example5.ods";
		args[2] = "TestData/Example5Parse.xml";
		args[3]="debug";
		ParseFile parseFile = new ParseFile();
		parseFile.executeParseFile(args[0],args[1],args[2],args[3]);

		//Start.main(args);
		
		// Now examine the output.
		// Is the file there?
		File outputFile = new File(args[1]);
		if (!outputFile.exists()){
			fail("Output file does not exist!");
		}
	}
	public void testMoreThan30Columns2(){
		String args[] = new String[4];
		args[0] = "TestData/vnvtrp04.p.txt";
		args[1] = "TestData/vnvtrp04.p.ods";
		args[2] = "TestData/vnvtrp04.p.xml";
		args[3]="debug";
		ParseFile parseFile = new ParseFile();
		parseFile.executeParseFile(args[0],args[1],args[2],args[3]);

		//Start.main(args);
		
		// Now examine the output.
		// Is the file there?
		File outputFile = new File(args[1]);
		if (!outputFile.exists()){
			fail("Output file does not exist!");
		}
	}
}
