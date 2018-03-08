# This script executes the first example from the command line to provide you with
# an idea how to write CRON or script jobs to produce OO Spreadsheets from your
# report data.
#
# BE SURE TO RUN THIS FROM THE ROOT DIRECTORY (ie republic-x.xa/.)
#
# Load Config
#
. ./src/scripts/config.sh
#

# Parm1 - <Report File Name and Location>
REPORTNAME=TestData/TestReport001.rpt
#	Parm2 - <Output Spreadsheet Name and Location>
SPREADSHEETNAME=TestData/Example1.sxc
#	Parm3 - <XML Parse File>
PARSEPARAMETERS=TestData/Example1Parse.xml
#	Parm4 - DEBUG to printout verbose log of actions or blank for just errors/warnings
DEBUG=

echo Removing previous copy of output Spreadsheet $SPREADSHEETNAME

rm -f $SPREADSHEETNAME

$JAVA_HOME/bin/java -classpath $CLASSPATH $REPUBLICSTART\
	$REPORTNAME\
	$SPREADSHEETNAME\
	$PARSEPARAMETERS\
	$DEBUG

RETURNSTATUS=$?

# Now run up the output in oocalc if we exited from parsing with a 0.
if [ ! "$RETURNSTATUS" = 9 ]; then
	$OOCALC $SPREADSHEETNAME
else
	echo "Parsing Failed"
fi

exit $RETURNSTATUS
