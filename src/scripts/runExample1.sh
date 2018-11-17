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
SPREADSHEETNAME=TestData/Example1.ods
#	Parm3 - <XML Parse File>
PARSEPARAMETERS=TestData/Example1Parse.xml
#	Parm4 - DEBUG to printout verbose log of actions or blank for just errors/warnings
DEBUG=

echo Removing previous copy of output Spreadsheet $SPREADSHEETNAME

rm -f $SPREADSHEETNAME

/usr/bin/java -classpath /opt/republic/target/republic-0.6.2.jar:/opt/rep_repository/castor/castor/0.9.5.3/castor-0.9.5.3.jar:/opt/rep_repository/xerces/xercesimpl/2.6.2/xercesImpl-2.6.2.jar:/opt/rep_repository/jdom/jdom/1.0/jdom-1.0.jar:/opt/rep_repository/jaxen/jaxen/1.1-beta-8/jaxen-1.1-beta-8.jar org.republic.Start\
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
