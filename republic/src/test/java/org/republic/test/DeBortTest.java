package org.republic.test;

import java.io.File;

import junit.framework.TestCase;

import org.republic.ParseFile;

public class DeBortTest extends TestCase {
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
	public void testDebortExample1(){
		String args[] = new String[4];
		args[0] = "/home/sfg/Documents/customers/debortoli/republic/dbsoinr4.p.txt";
		args[1] = "/home/sfg/Documents/customers/debortoli/republic/dbsoinr4.p.test1.ods";
		args[2] = "/home/sfg/Documents/customers/debortoli/republic/dbsoinr4.p.xml";
		//args[3]="debug";
		args[3]="";
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
