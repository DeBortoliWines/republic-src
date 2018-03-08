#!/bin/ksh
# Script - republic - 28/7/04 BR & GB
#                          
# Script req to help republic locate required directories & paramaters
# Mfg/Pro's printer def's limited length for its command line parameters

REPUBLIC_VERSION=0.5
JAVA_HOME=/opt/java1.4/jre
REPUBLIC_HOME=/opt/republic;export REPUBLIC_HOME
CLASSPATH=$REPUBLIC_HOME/lib/republic-$REPUBLIC_VERSION.jar:$REPUBLIC_HOME/lib/castor-0.9.5.3.jar:$REPUBLIC_HOME/lib/xercesImpl.jar:$REPUBLIC_HOME/lib/xmlParserAPIs.jar
REPUBLICSTART=org.republic.Start

TEMPFILE=$HOME/prn/oootemp1$PPID;export TEMPFILE

# Redirect report from stdin to temp file
cat $1 > $TEMPFILE

# Clear message file
rm $HOME/prn/republic.msg
touch $HOME/prn/republic.msg

echo "CLASSPATH: $CLASSPATH" >> $HOME/prn/republic.msg

# Extract the Mfg/Pro program name (1st field of first line of temp file)
PROGNAME=`head -1 $TEMPFILE | cut -f 1 -d " "`;export PROGNAME
echo "PROGNAME: $PROGNAME" >> $HOME/prn/republic.msg

# Parm1 - <Report File Name and Location>
REPORTNAME=$TEMPFILE
echo "REPORTNAME: $REPORTNAME" >> $HOME/prn/republic.msg

# Parm2 - <Output Spreadsheet Name and Location>
SPREADSHEETNAME=$HOME/prn/$PROGNAME.sxc
echo "SPREADSHEETNAME: $SPREADSHEETNAME" >> $HOME/prn/republic.msg

# Parm3 - <XML Parse File> , use default parse file if no matching parm file
PARSEPARAMETERS=$REPUBLIC_HOME/Parse/$PROGNAME.xml;export PARSEPARAMETERS
if [ ! -f $PARSEPARAMETERS ]
then
  PARSEPARAMETERS=$REPUBLIC_HOME/Parse/DefaultParse.xml;export PARSEPARAMETERS
fi
echo "PARSEPARAMETERS: $PARSEPARAMETERS" >> $HOME/prn/republic.msg

# Parm4 - DEBUG to printout verbose log of actions or blank for just error s/warnings
DEBUG=

# BE SURE TO RUN THIS FROM THE ROOT DIRECTORY (ie republic-x.xa/.)
cd /opt/republic

$JAVA_HOME/bin/java -classpath $CLASSPATH $REPUBLICSTART\
       $REPORTNAME\
       $SPREADSHEETNAME\
       $PARSEPARAMETERS\
       $DEBUG  >> $HOME/prn/republic.msg  2>> $HOME/prn/republic.msg

# Cleanup tempfile
rm $TEMPFILE

