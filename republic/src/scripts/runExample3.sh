# This script executes the first example from the command line to provide you with
# an idea how to write CRON or script jobs to produce OO Spreadsheets from your
# report data.
#
# BE SURE TO RUN THIS FROM THE ROOT DIRECTORY (ie republic-x.xa/.)
#
# Load Config
#
. ./config.sh
#
#
# Parm1 - <Report File Name and Location>
REPORTNAME=TestData/TestReport003.rpt
#	Parm2 - <Output Spreadsheet Name and Location>
SPREADSHEETNAME=TestData/Example3.sxc
#	Parm3 - <XML Parse File>
PARSEPARAMETERS=TestData/Example3Parse.xml
#	Parm4 - DEBUG to printout verbose log of actions or blank for just errors/warnings
DEBUG=DEBUG

echo Removing previous copy of output Spreadsheet $SPREADSHEETNAME

$JAVA_HOME/bin/java -classpath $CLASSPATH $REPUBLICSTART\
	$REPORTNAME\
	$SPREADSHEETNAME\
	$PARSEPARAMETERS\
	$DEBUG

# Now run up the output in oocalc!
	
$OOCALC $SPREADSHEETNAME
