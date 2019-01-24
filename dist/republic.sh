#!/bin/bash

TMPFILE=$(mktemp);

/usr/bin/java -jar $(dirname $0)/republic-0.6.6-jar-with-dependencies.jar \
  /home/bletchfo/repos/dbw/republic/TestData/arcsrp05.p.txt \
  $TMPFILE \
  /home/bletchfo/repos/dbw/republic/Parse/arcsrp05.p.xml > /dev/null 2>&1

cat $TMPFILE
