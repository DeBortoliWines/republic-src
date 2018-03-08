rem Test RunExample1.bat
cd ..
java -cp lib/republic-0.5.jar;lib/castor-0.9.5.3.jar;lib/xercesImpl.jar;lib/xmlParserAPIs.jar org.republic.Start TestData/TestReport001.rpt TestData/Example1.sxc TestData/Example1Parse.xml NONE
"C:\Program Files\OpenOffice.org1.1.1\program\soffice.exe" -show -quickstart TestData/Example1.sxc
cd shellscripts
