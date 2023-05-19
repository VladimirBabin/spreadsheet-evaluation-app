# Spreadsheet_Evaluator

This is an implementation of an application that evaluates data structure representing a spreadsheet (a two-dimensional array of cells). It installs a connection with a hub, downloads the sheets, computes them, and sends them back.

The application recognizes three types of cells: values (numeric, boolean, or text), error cells, and cells with a formula. If a cell has a formula it gets computed. Cells can be referred to using A1 notation.

There are 11 types of formulas the application can work with:

- SUM
- MULTIPLY
- DIVIDE
- GT (Greater than)
- LT (Less than)
- EQ (Equals)
- NOT
- AND
- OR
- IF
- CONCAT

Each formula has parameters that the application computes as well (in case there are more formulas in parameters or in 
case a parameter contains a reference to another cell). After the parameters are resolved, the formulas compute and
return the result. The application also can handle multiple referencing in the cells and can detect circular reference.

## Requirements
For building and running the application you need:
- JDK 17.0.1
- Maven 3.8.7

## Running the application locally
There are several ways to run a Spring Boot application on your local machine. One way is to execute the main method in 
the com.vladimirbabin.wixgrow.spreadsheetevaluator.SpreadsheetEvaluatorApplication class from your IDE.

Alternatively you can use the Spring Boot Maven plugin like so:
mvn spring-boot:run

To start the evaluation, send a GET request to localhost:8080/api/spreadsheet-evaluation.
