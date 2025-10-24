/*
 * Created on Jun 6, 2004
 *
 * @author Stuart Guthrie
 */
package org.republic;

/**
 * @author sfg
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

public class Start {
	
	public static void main(String[] args) {
		// @TODO Read input parameters.
		/*
		 * Arguments:
		 * 
		 * 1. Input File (Print File to be converted)
		 * 2. Output File (Text Output after parsing)
		 * 3. XML Parsing Instructions. (This goes to that)
		 * 4. Debug/No Debug (Values: 'DEBUG' or null)
		 * 5. Extract Data From Spreadsheet.
		 *  
		 */
		if (args.length == 0
				|| args[0].equalsIgnoreCase("--help")) {
			System.out.println(
					"================================================================================\n"+
					"G'day, you've found the new Australian Republic!\n\n"+
					CommonNames.APPNAME + "(" + CommonNames.VERSION +")"+"\n"+
					"\n"+
					"Your Options:\n"+
					" --help to get this text or....\n" +
					" --makeOOCalc <templatedir> <content.xmlfile> <oocalcfiletobecreated>\n" +
					" --xslt <xsl> <content.xmlfile> <outputfile>\n" +
					"Or vanilla - \n"+
					"Parm1 - <Report File Name and Location>\n" +
					"Parm2 - <Output Spreadsheet Name and Location>\n" +
					"Parm3 - <XML Parse File>\n"+
					"Parm4 - DEBUG to printout verbose log of actions\n"+
			       "================================================================================");
		}
		else if (args[0].equalsIgnoreCase("--xslt"))
		{
			if (args.length != 4)
			{
				System.err.println(
						"Option --xslt must have 3 additional parameters:\n"+
						"Parm1 .xsl file\n "+
						"Parm2 Location of content.xml\n"+
						"Parm3 Location of output file");
			}
			// We want to end up with the content.xml in this  file: outputFileString
			//parseFile.transformToContent(args[1],args[2],args[3]);
			System.out.println("Created output :"+args[3]);
		}
		else if (args.length == 4)
		{	
			ParseFile parseFile = new ParseFile();
			parseFile.executeParseFile(args[0],args[1],args[2],args[3]);
		}
		else if (args.length == 3)
		{	
			ParseFile parseFile = new ParseFile();
			parseFile.executeParseFile(args[0],args[1],args[2],null);
		}
		else
		{
			System.err.println("Incorrect parameters fed to Republic (we need at least 3): "); 
			int parmCtr = 0;
			while (parmCtr < args.length)
			{
				System.err.println("Parm " +(parmCtr+1) + " has a value of "+args[parmCtr]);
				parmCtr++;
			}
			System.exit(9);
			//return;
		}
		// Exit normally
		System.exit(0);
	}
}
