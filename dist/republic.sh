#!/usr/bin/env bash

if (( $# != 2 )); then
    echo "Incorrect parameters fed to Republic (we need 2)";
    echo "\$1 = INPUT_REPORT";
    echo "\$2 = INPORT_REPORT_SPEC";
    exit 1;
fi

REPUBLIC=$(dirname $0)/republic.jar;
INPUT_REPORT=$1;
INPUT_REPORT_SPEC=$2;
OUTPUT_REPORT=$(mktemp);

/usr/bin/java -jar $REPUBLIC \
  $INPUT_REPORT \
  $OUTPUT_REPORT \
  $INPUT_REPORT_SPEC > /dev/null 2>&1

cat $OUTPUT_REPORT
