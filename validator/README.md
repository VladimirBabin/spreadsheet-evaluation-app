# Validator

This service evaluates the computed spreadsheet and returns the success message and the placeholder for passcode in case 
the spreadsheet was computed correctly or the detailed reports on the mistakes the spreadsheet has in case it was computed incorrectly.
The reports may contain information on the incorrect type or value of the cell, or of the whole sheet or spreadsheet.

To send the computed spreadsheet to validator, you can send a POST request to to http://localhost:8181/api/validate 
with a spreadsheet in json format with email field and field with list of computed sheets.