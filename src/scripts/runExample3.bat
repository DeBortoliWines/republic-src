rem Test RunExample2.bat
cd ..
java -cp lib/republic-0.5.jar;lib/castor-0.9.5.3.jar;lib/xercesImpl.jar;lib/xmlParserAPIs.jar org.republic.Start TestData/TestReport003.rpt TestData/Examplei3.sxc TestData/Example3Parse.xml none
"C:\Program Files\OpenOffice.org1.1.1\program\soffice.exe" -show -quickstart TestData/example2.sxc
cd shellscripts
