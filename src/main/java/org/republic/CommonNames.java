package org.republic;

public class CommonNames {
	public static final String
	APPNAME		= "Republic",
	VERSION 	= "0.6";
	static final int BUFFER = 2048;

	static final String IGNORELINEIF = "IgnoreLineIf";

	static final String IGNOREPAGEROWS = "IgnorePageRows";
	static final String IGNORENEWPAGEUNTIL = "IgnoreNewPageUntil";

	static final String SELECTFIELDDATA = "SelectFieldData";
	static final String SELECTMULTIROWFIELDDATA = "SelectMultiRowFieldData";
	static final String SELECTSAVEDROWFIELDDATA = "SelectSavedRowFieldData";
	static final String SELECTSAVEDMULTIROWFIELDDATA = "SelectSavedMultiRowFieldData";
	static final String SELECTALLDATA = "SelectAllData";

	static final String NEWROWIF = "NewRowIf";
	static final String NEWMULTIROWIF = "NewMultiRowIf";
	static final String NEWSAVEDROWIF = "NewSavedRowIf";
	static final String NEWSAVEDMULTIROWIF = "NewSavedMultiRowIf";
	static final String ALLDATAIF ="AllDataIf";
	static final String ENDSHEETIF ="EndSheetIf";

	static final String BLANK = "Blank";

	static final String NOTBLANK = "NotBlank";

	static final String ANY = "Any";

	static final String CONFIGDIR="src/conf/";
	static final String SPREADSHEETTEMPLATESXC= "./Spreadsheets/template-sxc";
	static final String SPREADSHEETTEMPLATEODS= "./Spreadsheets/template-ods";
	static final String OUTFILE= "./TestData/OutputFile.xml";
	 static final String OUTPUTRESULTS = "./TestData/OutputResults.xml";
	 static final String PARSEPARAMETERS = "ParseParametersMapping.xml";

	 static final String PARSEPARAMETERSLOCATION = CONFIGDIR+PARSEPARAMETERS;
	 static final String OUTPUTDATAMAPPING = "OutputDataMapping.xml";
	 static final String OUTPUTDATAMAPPINGLOCATION = CONFIGDIR+OUTPUTDATAMAPPING;

	 static final String CONTENT2DATASXC = "content2Data-sxc.xsl";
	 static final String DATA2CONTENTSXC = "data2Content-sxc.xsl";
	 static final String DATA2CONTENTSXCLOCATION = CONFIGDIR+DATA2CONTENTSXC;
	 //static final String CONTENT2DATAODS = "content2Data-ods.xsl";
	 //static final String DATA2CONTENTODS = "data2Content-ods.xsl";
	 //static final String DATA2CONTENTODSLOCATION = CONFIGDIR+DATA2CONTENTODS;
	 static public String ODSTEMPLATE=CONFIGDIR+"template.ods";
	 static final String COLHDG="ColHdg";
	 static public String DATA="Data";


}
