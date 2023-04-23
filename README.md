# Spreadsheet_Evaluator

This is an implementation of an application that evaluates data structure representing a spreadsheet (a two-dimensional array of cells). It installs a connection with a hub, downloads the sheets, computes them, and sends them back.

The application recognizes three types of cells: values (numeric, boolean, or text), error cells, and cells with a formula. If a cell has a formula it gets computed. Cells can be referred to using A1 notation.

There are 10 types of formulas the application can work with:

- SUM
- MULTIPLY
- DIVIDE
- GT (Greater Then)
- EQ (Equals)
- NOT
- AND
- OR
- IF
- CONCAT

Each formula has parameters that the application computes as well (in case there are more formulas in parameters or in case a parameter contains a reference to another cell.
