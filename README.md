README FIRST

CHANGES
=======
v0.2 (minor changes - split of main parser to diff objects)
v0.3 (multiRow parsing to one spreadsheet row)
v0.4 (more parsing options - see runExample3.sh)
v0.5 - More parsing improvements, 
     - Ability to parse the same report into multiple sheets 
     within an OO worksheet.
     - Shellscript provided by DeBortoli Wines to take a report file
     and match it to a parsing parameter file, parse then store the
     result. Thank you Gleny Baca and Bill Robertson.
v0.6 - Configured for OO Documents v2.0

If you've found this application to be worthwhile, why not sponsor some of the
development effort... http://www.sf.net/projects/republic


TESTING ENVIRONMENTS
====================
Release 0.6 has been tested on HP-UX and Linux. 
There is a good chance it works on Windows too.

MAJOR SPONSOR
=============
This project was sponsored by De Bortoli Wines of Australia.
http://www.debortoli.com.au, who open sourced it hoping others may find the 
project interesting and useful.

De Bortoli use Republic with MFG-PRO to take reports directly to spreadsheet.

Introduction
============
This project's aim is to provide an OSS ascii report parser that takes data from
legacy application report files (those old ones that go to the line printer) andextracts the useful bits into a nice OASIS Open Office oocalc document for 
your accountants to get excited about. 

Please use and abuse this software it is yours for the keeping. Don't forget to
hand back bug fixes and improvements. That way we all win.

The Examples:

shellscripts/runExample1.sh
shellscripts/runExample2.sh
shellscripts/runExample3.sh
shellscripts/runRepublic.sh

There are also some .bat scripts but they are a bit out of date. I've altered the parameters for the new release but not tested them.

To get an idea about how this software works, if your are running linux, execute these shell scripts. Republic is VERY scriptable, it's written for organisations to automate from the command line after a computer report finishes. What should happen by the end of the shell script is open office calc should start up and load a database of the data from the ASCII report in TestData/TestReport001.rpt. Of course, on your site, you might want to auto-email the resultant spreadsheet to the distribution list or something.

An example of scripting from De Bortoli is provided: reportFileIntoRepublic.sh which takes an input report file from their ERP system (MFG-PRO), looks for a valid match on parse file name = report file name, if not found, runs a default parse xml what loads to OO in a single column load otherwise does the customised parse and loads for that specific report layout.

There is also a docs directory which:
- Shows the overall process flow of republic from a print file to an OO spreadsheet. 
- Provides documentation on the various parsing options and how to use them.

This should be enough for you to work out how to do it you your own reports.


Happy parsing.


Stuart Guthrie
stuart@polonious.com.au
Polonious
Sydney Australia
To thine ownself be true.

