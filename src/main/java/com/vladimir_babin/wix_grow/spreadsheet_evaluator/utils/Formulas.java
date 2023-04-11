package com.vladimir_babin.wix_grow.spreadsheet_evaluator.utils;


import com.vladimir_babin.wix_grow.spreadsheet_evaluator.Sheet;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Formulas {

    /*    This method checks if cell value contains a formula. If it does - it computes formula and returns
    a Cell object with Cell.type of a Type that the method normally should return if computation was successful or throws
    a RuntimeException if the formula couldn't be computed.
     */
    static Cell checkIfCellHasFormulaAndCompute(Cell cell, Sheet<Cell> sheet) {
        Cell result;
        Object cellObj = cell.getValue();
        if (cell.getType().equals(Cell.Type.FORMULA)
                || (cellObj instanceof String && (isFormula(cellObj.toString()) || isNotation(cellObj.toString())))) {
            result = computeFormula(cell.getValue().toString(), sheet);
        } else {
            result = cell;
        }
        return result;
    }

    private static Cell computeFormula(String cellStringOrParameterValue, Sheet<Cell> sheet) {
        String formulaName = "";
        String formulaContents = "";
        String[] arrayOfParameters = null;
        if (cellStringOrParameterValue.startsWith("="))
            cellStringOrParameterValue = cellStringOrParameterValue.substring(1);
        if (isFormula(cellStringOrParameterValue)) {
            formulaName = getFormulaName(cellStringOrParameterValue);
            formulaContents = getFormulaContents(cellStringOrParameterValue);
            arrayOfParameters = formulaContents.split(",\s(?![^(]*?\\))");
        }

        Cell result = switch (formulaName) {
            case "SUM" -> sum(arrayOfParameters, sheet);
            case "MULTIPLY" -> multiply(arrayOfParameters, sheet);
            case "DIVIDE" -> divide(arrayOfParameters, sheet);
            case "GT" -> greaterThan(arrayOfParameters, sheet);
            case "EQ" -> equal(arrayOfParameters, sheet);
            case "NOT" -> not(formulaContents, sheet);
            case "AND" -> and(arrayOfParameters, sheet);
            case "OR" -> or(arrayOfParameters, sheet);
            case "IF" -> ifFormula(arrayOfParameters, sheet);
            case "CONCAT" -> concat(formulaContents, sheet);
            default -> notationRerefence(cellStringOrParameterValue, sheet);
        };
        return result;
    }

    private static boolean isFormula(String cellOrParameterStringValue) {
        return cellOrParameterStringValue.contains("(");
    }

    private static boolean isNotation(String parameter) {
        return parameter.matches("[A-Z][0-9]+");
    }

    private static String getFormulaName(String cellStringOrParameterValue) {
        return cellStringOrParameterValue.substring(0, cellStringOrParameterValue.indexOf("("));
    }

    private static String getFormulaContents(String cellStringOrParameterValue) {
        return cellStringOrParameterValue.substring(cellStringOrParameterValue.indexOf("(") + 1,
                cellStringOrParameterValue.lastIndexOf(")"));
    }

    private static BigDecimal computeFormulaNumericParameter(String parameter, Sheet<Cell> sheet) {
        Cell tempCell;
        if (NumberUtils.isParsable(parameter)) {
            return new BigDecimal(parameter);
        } else if (isFormula(parameter)) {
            tempCell = computeFormula(parameter, sheet);
        } else {
            if (parameter.startsWith("=")) {
                parameter = parameter.substring(1);
            }
            try {
                tempCell = sheet.getElementByNotation(parameter);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Sheet doesn't contain such notation");
            }
            tempCell = checkIfCellHasFormulaAndCompute(tempCell, sheet);
        }
        if (!tempCell.getType().equals(Cell.Type.NUMERIC)) {
            throw new RuntimeException("Cell type in incompatible");
        }
        return new BigDecimal(tempCell.getValue().toString());
    }

    private static boolean computeFormulaBooleanParameter(String parameter, Sheet<Cell> sheet) {
        Cell tempCell;
        if (parameter.equalsIgnoreCase("true")) {
            return true;
        } else if (parameter.equalsIgnoreCase("false")) {
            return false;
        } else if (isFormula(parameter)) {
            tempCell = computeFormula(parameter, sheet);
        } else {
            if (parameter.startsWith("=")) {
                parameter = parameter.substring(1);
            }
            try {
                tempCell = sheet.getElementByNotation(parameter);
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                throw new RuntimeException("Sheet doesn't contain such notation");
            }
            tempCell = checkIfCellHasFormulaAndCompute(tempCell, sheet);
        }
        if (!tempCell.getType().equals(Cell.Type.BOOLEAN)) {
            throw new RuntimeException("Cell type is incompatible");
        }
        return Boolean.parseBoolean(tempCell.getValue().toString());
    }

    private static String computeFormulaConcatParameter(String parameter, Sheet<Cell> sheet) {
        Cell tempCell;
        if (parameter.startsWith("\"")) {
            return parameter.substring(1, parameter.length() - 1);
        }
        if (isFormula(parameter)) {
            tempCell = computeFormula(parameter, sheet);
        } else {
            if (parameter.startsWith("=")) {
                parameter = parameter.substring(1);
            }
            try {
                tempCell = sheet.getElementByNotation(parameter);
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                throw new RuntimeException("Sheet doesn't contain such notation");
            }
            tempCell = checkIfCellHasFormulaAndCompute(tempCell, sheet);
        }
        return tempCell.getValue().toString();
    }

    static Cell sum(String[] arrayOfParameters, Sheet<Cell> sheet) {
        BigDecimal[] params = new BigDecimal[arrayOfParameters.length];
        for (int i = 0; i < arrayOfParameters.length; i++) {
            try {
                params[i] = computeFormulaNumericParameter(arrayOfParameters[i], sheet);
            } catch (RuntimeException exception) {
                Cell errorCell = new Cell("#ERROR: " + exception.getMessage());
                errorCell.setType(Cell.Type.ERROR);
                return errorCell;
            }
        }

        BigDecimal resultOfSum = Arrays.stream(params).reduce(BigDecimal.ZERO, BigDecimal::add);

        Cell cellResult = new Cell(resultOfSum);
        cellResult.setType(Cell.Type.NUMERIC);
        return cellResult;
    }

    static Cell multiply(String[] arrayOfParameters, Sheet<Cell> sheet) {
        BigDecimal[] params = new BigDecimal[arrayOfParameters.length];

        for (int i = 0; i < arrayOfParameters.length; i++) {
            try {
                params[i] = computeFormulaNumericParameter(arrayOfParameters[i], sheet);
            } catch (RuntimeException exception) {
                Cell errorCell = new Cell("#ERROR: " + exception.getMessage());
                errorCell.setType(Cell.Type.ERROR);
                return errorCell;
            }
        }

        BigDecimal resultOfMultiplication = Arrays.stream(params).reduce(BigDecimal.ONE, BigDecimal::multiply);

        Cell cellResult = new Cell(resultOfMultiplication);
        cellResult.setType(Cell.Type.NUMERIC);
        return cellResult;
    }

    static Cell divide(String[] arrayOfParameters, Sheet<Cell> sheet) {
        BigDecimal[] params = new BigDecimal[arrayOfParameters.length];

        for (int i = 0; i < arrayOfParameters.length; i++) {
            try {
                params[i] = computeFormulaNumericParameter(arrayOfParameters[i], sheet);
            } catch (RuntimeException exception) {
                Cell errorCell = new Cell("#ERROR: " + exception.getMessage());
                errorCell.setType(Cell.Type.ERROR);
                return errorCell;
            }
        }

        BigDecimal resultOfDivision = params[0].divide(params[1], 7, RoundingMode.FLOOR);

        Cell cellResult = new Cell(resultOfDivision);
        cellResult.setType(Cell.Type.NUMERIC);
        return cellResult;
    }

    static Cell greaterThan(String[] arrayOfParameters, Sheet<Cell> sheet) {
        BigDecimal[] params = new BigDecimal[arrayOfParameters.length];
        for (int i = 0; i < arrayOfParameters.length; i++) {
            try {
                params[i] = computeFormulaNumericParameter(arrayOfParameters[i], sheet);
            } catch (RuntimeException exception) {
                Cell errorCell = new Cell("#ERROR: " + exception.getMessage());
                errorCell.setType(Cell.Type.ERROR);
                return errorCell;
            }
        }
        int resultOfCompare = params[0].compareTo(params[1]);
        boolean isGreater = resultOfCompare == 1 ? true : false;
        Cell cellResult = new Cell(isGreater);
        cellResult.setType(Cell.Type.BOOLEAN);
        return cellResult;
    }

    static Cell equal(String[] arrayOfParameters, Sheet<Cell> sheet) {
        BigDecimal[] params = new BigDecimal[arrayOfParameters.length];
        for (int i = 0; i < arrayOfParameters.length; i++) {
            try {
                params[i] = computeFormulaNumericParameter(arrayOfParameters[i], sheet);
            } catch (RuntimeException exception) {
                Cell errorCell = new Cell("#ERROR: " + exception.getMessage());
                errorCell.setType(Cell.Type.ERROR);
                return errorCell;
            }
        }
        int resultOfCompare = params[0].compareTo(params[1]);
        boolean isEqual = resultOfCompare == 0 ? true : false;
        Cell cellResult = new Cell(isEqual);
        cellResult.setType(Cell.Type.BOOLEAN);
        return cellResult;
    }

    static Cell not(String formulaValue, Sheet<Cell> sheet) {
        Cell cellResult = new Cell(!computeFormulaBooleanParameter(formulaValue, sheet));
        cellResult.setType(Cell.Type.BOOLEAN);
        return cellResult;
    }

    static Cell and(String[] arrayOfParameters, Sheet<Cell> sheet) {
        boolean[] params = new boolean[arrayOfParameters.length];
        for (int i = 0; i < arrayOfParameters.length; i++) {
            try {
                params[i] = computeFormulaBooleanParameter(arrayOfParameters[i], sheet);
            } catch (RuntimeException exception) {
                Cell errorCell = new Cell("#ERROR: " + exception.getMessage());
                errorCell.setType(Cell.Type.ERROR);
                return errorCell;
            }
        }
        boolean booleanResult = params[0];
        for (int i = 1; i < params.length; i++) {
            booleanResult = booleanResult && params[i];
        }
        Cell cellResult = new Cell(booleanResult);
        cellResult.setType(Cell.Type.BOOLEAN);
        return cellResult;
    }

    static Cell or(String[] arrayOfParameters, Sheet<Cell> sheet) {
        boolean[] params = new boolean[arrayOfParameters.length];
        for (int i = 0; i < arrayOfParameters.length; i++) {
            try {
                params[i] = computeFormulaBooleanParameter(arrayOfParameters[i], sheet);
            } catch (RuntimeException exception) {
                Cell errorCell = new Cell("#ERROR: " + exception.getMessage());
                errorCell.setType(Cell.Type.ERROR);
                return errorCell;
            }
        }
        boolean booleanResult = params[0];
        for (int i = 0; i < params.length - 1; i++) {
            booleanResult = params[i + 1] || booleanResult;
        }
        Cell cellResult = new Cell(booleanResult);
        cellResult.setType(Cell.Type.BOOLEAN);
        return cellResult;
    }

    static Cell ifFormula(String[] arrayOfParameters, Sheet<Cell> sheet) {
        boolean condition;
        try {
            condition = computeFormulaBooleanParameter(arrayOfParameters[0], sheet);
        } catch (RuntimeException exception) {
            Cell errorCell = new Cell("#ERROR: " + exception.getMessage());
            errorCell.setType(Cell.Type.ERROR);
            return errorCell;
        }
        Cell cellResult;
        if (condition) {
            cellResult = new Cell(arrayOfParameters[1]);
        } else {
            cellResult = new Cell(arrayOfParameters[2]);
        }
        if (cellResult.getValue() instanceof Number) {
            cellResult.setType(Cell.Type.NUMERIC);
        } else if (cellResult.getValue() instanceof Boolean) {
            cellResult.setType(Cell.Type.BOOLEAN);
        } else if (cellResult.getValue() instanceof String) {
            String stringObj = cellResult.getValue().toString();
            if (stringObj.startsWith("=") || stringObj.matches("[A-Z][0-9]+")) {
                cellResult.setType(Cell.Type.FORMULA);
            } else {
                cellResult.setType(Cell.Type.STRING);
            }
        }
        return checkIfCellHasFormulaAndCompute(cellResult, sheet);
    }


    static Cell concat(String formulaValue, Sheet<Cell> sheet) {
        List<String> arrayOfParameters = splitStringUsingCommaAndSpaceWhenNotSurroundedByQuotes(formulaValue);
        Cell cellResult = new Cell(arrayOfParameters.stream()
                .map(str -> computeFormulaConcatParameter(str, sheet))
                .collect(Collectors.joining("")));
        cellResult.setType(Cell.Type.STRING);
        return cellResult;
    }

    static Cell notationRerefence(String notation, Sheet<Cell> sheet) {
        Cell result = sheet.getElementByNotation(notation);
        return checkIfCellHasFormulaAndCompute(result, sheet);
    }

    static List<String> splitStringUsingCommaAndSpaceWhenNotSurroundedByQuotes(String formulaValue) {
        List<String> matchList = new ArrayList<>();
        Pattern regex = Pattern.compile("[^,\\s\"]+|\"[^\"]*\"");
        Matcher regexMatcher = regex.matcher(formulaValue);
        while (regexMatcher.find()) {
            matchList.add(regexMatcher.group());
        }
        return matchList;
    }
}
