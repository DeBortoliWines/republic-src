# This script executes the first example from the command line to provide you with
# an idea how to write CRON or script jobs to produce OO Spreadsheets from your
# report data.
#
# BE SURE TO RUN THIS FROM THE ROOT DIRECTORY (ie republic-x.xa/.)
#

# Setup standard Republic Parameters for use in other runs.
export REPUBLIC_VERSION=0.6.6
#JAVA_HOME=/home/sfg/j2sdk_nb/j2sdk1.4.2/jre
export MAVEN_HOME=$HOME/.m2
export CLASSPATH=$MAVEN_HOME/repository/republic/republic/$REPUBLIC_VERSION/republic-$REPUBLIC_VERSION.jar:\
$MAVEN_HOME/repository/castor/castor/0.9.5.3/castor-0.9.5.3.jar:\
$MAVEN_HOME/repository/xerces/xercesImpl/2.6.2/xercesImpl-2.6.2.jar:\
$MAVEN_HOME/repository/jdom/jdom/1.0/jdom-1.0.jar:\
$MAVEN_HOME/repository/jaxen/jaxen/1.1-beta-8/jaxen-1.1-beta-8.jar
#$MAVEN_HOME/repository/xmlParserAPIs-2.2.1.jar\

echo $CLASSPATH
export REPUBLICSTART=org.republic.Start
export OOCALC=oocalc2

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

/usr/bin/java -classpath $CLASSPATH $REPUBLICSTART\
	$REPORTNAME\
	$SPREADSHEETNAME\
	$PARSEPARAMETERS\
	$DEBUG

# Now run up the output in oocalc!

# $OOCALC $SPREADSHEETNAME
