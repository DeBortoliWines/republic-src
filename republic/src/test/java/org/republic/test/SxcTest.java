package org.republic.test;

import java.io.File;

import junit.framework.TestCase;

import org.republic.Start;

public class SxcTest extends TestCase {
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
	public void testSxcExample1(){
		String args[] = new String[4];
		args[0] = "TestData/TestReport001.rpt";
		args[1] = "TestData/Example1.sxc";
		args[2] = "TestData/Example1Parse.xml";
		args[3]="";
		
		Start.main(args);
		
		// Now examine the output.
		// Is the file there?
		File outputFile = new File(args[1]);
		if (!outputFile.exists()){
			fail("Output file does not exist!");
		}
	}
	public void testSxcExample2(){
		String args[] = new String[4];
		args[0] = "TestData/TestReport002.rpt";
		args[1] = "TestData/Example2.sxc";
		args[2] = "TestData/Example2Parse.xml";
		args[3]="";
		
		Start.main(args);
		
		// Now examine the output.
		// Is the file there?
		File outputFile = new File(args[1]);
		if (!outputFile.exists()){
			fail("Output file does not exist!");
		}
	}
	public void testSxcExample3(){
		String args[] = new String[4];
		args[0] = "TestData/TestReport003.rpt";
		args[1] = "TestData/Example3.sxc";
		args[2] = "TestData/Example3Parse.xml";
		args[3]="";
		
		Start.main(args);
		
		// Now examine the output.
		// Is the file there?
		File outputFile = new File(args[1]);
		if (!outputFile.exists()){
			fail("Output file does not exist!");
		}
	}

}
