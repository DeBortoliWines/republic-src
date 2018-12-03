# Republic Parse Options

Version 0.5

## Parsing XML Parameters

How republic parses your reports is determined by the parseParameters.xml file. These are the fields of note in that file:

| Level | Field                 | Notes                                                                                                                                                                                         |
| ----- | --------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1     | `<parse-parameters>`  | Denotes the start and end of a parse file.                                                                                                                                                    |
| 2     | `<report-name>`       | Notation only. Used to id the normal input report.                                                                                                                                            |
| 2     | `<report-type>`       | Notation Only.                                                                                                                                                                                |
| 2     | `<parseSheets>`       | Denotes the start/end of the set of parse runs we will be making. For example, you might parse the same report several times in order to create several output sheets on the one spreadsheet. |
| 3     | `<sheetName>`         | Denotes the name given to the output spreadsheet.                                                                                                                                             |
| 3     | `<parseRules>`        | Denotes the start/end of the set of parseRules for this output sheet name.                                                                                                                    |
| 4     | `<parseRule>`         | Denotes the start/end of a specific parseRule.                                                                                                                                                |
| 5     | `<parse-type>`        | A parsing instruction to the parser. These are explained in Parsing Types                                                                                                                     |
| 5     | `<start-col>`         | Denotes the starting column for a parse type.                                                                                                                                                 |
| 5     | `<end-col>`           | Denotes the ending column for a parse type.                                                                                                                                                   |
| 5     | `<start-row>`         | Denotes the starting row for a parse type.                                                                                                                                                    |
| 5     | `<end-row>`           | Denotes the ending column for a parse type.                                                                                                                                                   |
| 5     | `<output-field-name>` | Denotes the name to give a column of parsed data.                                                                                                                                             |
| 5     | `<output-field-type>` | Denotes the field type to give a column of parsed data. Options are: currency, date, percentage, number, string.                                                                              |
| 5     | `<match-string>`      | Denotes for some parse-types the string that must be matched. Special Values are: Blank, NotBlank, Any other values are treated as exact matches.                                             |

## Parsing Strategies

There are currently three main parsing strategies you may utilise to extract data from a report into a spreadsheet via Republic.

1. SingleRow. NewRowIf.
2. MultiRow. NewMultiRowIf.
3. AllData. AllDataIf.

Which ever strategy you pick is really the only one you can easily use for that output sheet. Using multiple strategies will really confuse the parser!

## Parsing Types

These are instructions that can be given to the parser to help you get the data into the right columns and to strip useless rows of data.

| Parse Type              | Comments                                                                                                                                                                                                                                                                            |
| ----------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| IgnorePageRows          | On a new page, ignore the top of page down from `<start-row>` to `<end-row>`.                                                                                                                                                                                                       |
| IgnoreNewPageUntil      | On a new page, ignore the top of page down until `<match-string>`.                                                                                                                                                                                                                  |
| NewRowIf                | If <start-col> to <end-col> match the string in the <match-string> element. Start a new spreadsheet row.                                                                                                                                                                            |
| SelectFieldData         | Select all data from `<start-col>` to `<end-col>` into the next avail column. Call that column the `<output-field-name>` and make it type `<output-field-type>`.                                                                                                                    |
| NewMultiRowIf           | If `<start-col>` to `<end-col>` match the string in the `<match-string>` element. Start a new buffer of the next `<start-row>` to `<end-row>` then allow the user to select any field values in that buffer based on start/end column and a particular row. See SelectMultiRowData. |
| SelectMultiRowFieldData | Select all data from `<start-row>`  and `<start-col>` to `<end-col>` into the next avail column. Call that column the `<output-field-name>` and make it type `<output-field-type>`.                                                                                                 |
| AllDataIf               | If `<start-col>` to `<end-col>` match the string in the `<match-string>` element.  Go into AllDataMode and from here on, object SelectAllData instructions and parse the data into column1.                                                                                         |
| SelectAllData           | Select all data from `<start-col>` to `<end-col>` into col 1 of the output sheet. This is useful for dumping the whole report to col1 of the spreadsheet and manually parsing or using it for a reference.                                                                          |
