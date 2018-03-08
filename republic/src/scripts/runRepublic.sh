# This script executes the first example from the command line to provide you with
# an idea how to write CRON or script jobs to produce OO Spreadsheets from your
# report data.
#
# BE SURE TO RUN THIS FROM THE ROOT DIRECTORY (ie republic-x.xa/.)
#

REPUBLIC_VERSION=0.3
JAVA_HOME=/home/sfg/j2sdk_nb/j2sdk1.4.2/jre
CLASSPATH=lib/republic-$REPUBLIC_VERSION.jar:lib/castor-0.9.5.3.jar:lib/xercesImpl.jar:lib/xmlParserAPIs.jar
echo $CLASSPATH
REPUBLICSTART=org.republic.Start

# Parm1 - <Report File Name and Location>
REPORTNAME="$1"
#TestData/TestReport001.rpt
#	Parm2 - <Output Spreadsheet Name and Location>
SPREADSHEETNAME="$2"
#TestData/Example1.sxc
#	Parm3 - <XML Parse File>
PARSEPARAMETERS="$3"
#TestData/CustomerListingParse.xml
#	Parm4 - DEBUG to printout verbose log of actions or blank for just errors/warnings
#DEBUG=DEBUG
DEBUG=
printf "Republic - REPORT: $REPORTNAME SPREADHEET: $SPREADSHEETNAME PARSEXML: $PARSEPARAMETERS"
echo Removing previous copy of output Spreadsheet $SPREADSHEETNAME

$JAVA_HOME/bin/java -classpath $CLASSPATH $REPUBLICSTART\
	$REPORTNAME\
	$SPREADSHEETNAME\
	$PARSEPARAMETERS\
	$DEBUG

# Now run up the output in oocalc!
	
oocalc $SPREADSHEETNAME
