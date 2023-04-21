package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;


import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FormulaComputer {
    @Autowired
    private Map<String, FormulaApplier> map;

    private NotationApplier notationApplier;
    private InputTypeDeterminer inputTypeDeterminer;

    Logger logger = LoggerFactory.getLogger(FormulaComputer.class);

    public FormulaComputer(NotationApplier notationApplier,
                           InputTypeDeterminer inputTypeDeterminer) {
        this.notationApplier = notationApplier;
        this.inputTypeDeterminer = inputTypeDeterminer;
    }

    /*    This method checks if cell value contains a formula or a notation. If it does - it computes formula and returns
    an Input object, if not - it returns the input itself.
     */
    Input checkIfInputHasFormulaOrNotationAndCompute(Input input, Sheet<Input> sheet) {
        if (input == null) {
            return new Input("");
        }
        Input resultInput;
        if (input.getType().equals(Type.NOTATION) || input.getType().equals(Type.FORMULA)) {
            resultInput = computeFormula(input, sheet);
        } else {
            resultInput = input;
        }
        resultInput = inputTypeDeterminer.determineType(resultInput);
        if (resultInput.getType().equals(Type.FORMULA) || resultInput.getType().equals(Type.NOTATION)) {
            resultInput = checkIfInputHasFormulaOrNotationAndCompute(resultInput, sheet);
        }
        return resultInput;
    }

    public Input computeFormula(Input input, Sheet<Input> sheet) {
        logger.info(input.getType().toString() + " " + input.getValue());

        List<Input> resolvedParameters = new ArrayList<>();

        FormulaInfo formulaInfo = new FormulaInfo(input.getValue().toString());
        String[] rawParameters = formulaInfo.getArrayOfParameters();

        FormulaApplier formulaApplier = map.get(formulaInfo.getFormulaName());
        if (formulaApplier == null) {
            throw new UnsupportedOperationException(formulaInfo.getFormulaName() + " not supported");
        }
        for (int i = 0; i < rawParameters.length; i++) {
            Input resolvedParameter = new Input(rawParameters[i]);
            resolvedParameter = inputTypeDeterminer.determineType(resolvedParameter);
            Type resolvedParameterType = resolvedParameter.getType();
            if (resolvedParameterType.equals(Type.NOTATION)) {
                resolvedParameter = notationApplier.apply(resolvedParameter.getValue().toString(), sheet);
                resolvedParameter = inputTypeDeterminer.determineType(resolvedParameter);
                return checkIfInputHasFormulaOrNotationAndCompute(resolvedParameter, sheet);
            }
            if (resolvedParameterType.equals(Type.FORMULA) || resolvedParameterType.equals(Type.NOTATION)) {
                resolvedParameter = computeFormula(resolvedParameter, sheet);
            }
            resolvedParameters.add(resolvedParameter);
        }
        return formulaApplier.apply(resolvedParameters, sheet);
}


//    private boolean isFormula(String cellOrParameterStringValue) {
//        return cellOrParameterStringValue.contains("(");
//    }
//
//    private boolean isNotation(String parameter) {
//        return parameter.matches("[A-Z][0-9]+");
//    }
//
//    private String getFormulaName(String cellStringOrParameterValue) {
//        return cellStringOrParameterValue.substring(0, cellStringOrParameterValue.indexOf("("));
//    }

//    private String getFormulaContents(String cellStringOrParameterValue) {
//        return cellStringOrParameterValue.substring(cellStringOrParameterValue.indexOf("(") + 1,
//                cellStringOrParameterValue.lastIndexOf(")"));
//    }

//    BigDecimal computeFormulaNumericParameter(String parameter, Sheet<Cell> sheet) {
//        Input tempCell;
//        if (NumberUtils.isParsable(parameter)) {
//            return new BigDecimal(parameter);
//        } else if (isFormula(parameter)) {
//            tempCell = computeFormula(parameter, sheet);
//        } else {
//            if (parameter.startsWith("=")) {
//                parameter = parameter.substring(1);
//            }
//            try {
//                tempCell = sheet.getElementByNotation(parameter);
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new RuntimeException("Sheet doesn't contain such notation");
//            }
//            tempCell = checkIfInputHasFormulaOrNotationAndCompute(tempCell, sheet);
//        }
//        if (!tempCell.getType().equals(Type.NUMERIC)) {
//            throw new RuntimeException("Cell type in incompatible");
//        }
//        return new BigDecimal(tempCell.getValue().toString());
//    }
//
//    private boolean computeFormulaBooleanParameter(String parameter, Sheet<Cell> sheet) {
//        Input tempCell;
//        if (parameter.equalsIgnoreCase("true")) {
//            return true;
//        } else if (parameter.equalsIgnoreCase("false")) {
//            return false;
//        } else if (isFormula(parameter)) {
//            tempCell = computeFormula(parameter, sheet);
//        } else {
//            if (parameter.startsWith("=")) {
//                parameter = parameter.substring(1);
//            }
//            try {
//                tempCell = sheet.getElementByNotation(parameter);
//            } catch (ArrayIndexOutOfBoundsException e) {
//                e.printStackTrace();
//                throw new RuntimeException("Sheet doesn't contain such notation");
//            }
//            tempCell = checkIfInputHasFormulaOrNotationAndCompute(tempCell, sheet);
//        }
//        if (!tempCell.getType().equals(Type.BOOLEAN)) {
//            throw new RuntimeException("Cell type is incompatible");
//        }
//        return Boolean.parseBoolean(tempCell.getValue().toString());
//    }
//
//    private String computeFormulaConcatParameter(String parameter, Sheet<Cell> sheet) {
//        Input tempCell;
//        if (parameter.startsWith("\"")) {
//            return parameter.substring(1, parameter.length() - 1);
//        }
//        if (isFormula(parameter)) {
//            tempCell = computeFormula(parameter, sheet);
//        } else {
//            if (parameter.startsWith("=")) {
//                parameter = parameter.substring(1);
//            }
//            try {
//                tempCell = sheet.getElementByNotation(parameter);
//            } catch (ArrayIndexOutOfBoundsException e) {
//                e.printStackTrace();
//                throw new RuntimeException("Sheet doesn't contain such notation");
//            }
//            tempCell = checkIfInputHasFormulaOrNotationAndCompute(tempCell, sheet);
//        }
//        return tempCell.getValue().toString();
//    }

//    Cell sum(String[] arrayOfParameters, Sheet<Cell> sheet) {
//        BigDecimal[] params = new BigDecimal[arrayOfParameters.length];
//        for (int i = 0; i < arrayOfParameters.length; i++) {
//            try {
//                params[i] = computeFormulaNumericParameter(arrayOfParameters[i], sheet);
//            } catch (RuntimeException exception) {
//                Cell errorCell = new Cell("#ERROR: " + exception.getMessage());
//                errorCell.setType(Type.ERROR);
//                return errorCell;
//            }
//        }
//
//        BigDecimal resultOfSum = Arrays.stream(params).reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        Cell cellResult = new Cell(resultOfSum);
//        cellResult.setType(Type.NUMERIC);
//        return cellResult;
//    }
//
//    Cell multiply(String[] arrayOfParameters, Sheet<Cell> sheet) {
//        BigDecimal[] params = new BigDecimal[arrayOfParameters.length];
//
//        for (int i = 0; i < arrayOfParameters.length; i++) {
//            try {
//                params[i] = computeFormulaNumericParameter(arrayOfParameters[i], sheet);
//            } catch (RuntimeException exception) {
//                Cell errorCell = new Cell("#ERROR: " + exception.getMessage());
//                errorCell.setType(Type.ERROR);
//                return errorCell;
//            }
//        }
//
//        BigDecimal resultOfMultiplication = Arrays.stream(params).reduce(BigDecimal.ONE, BigDecimal::multiply);
//
//        Cell cellResult = new Cell(resultOfMultiplication);
//        cellResult.setType(Type.NUMERIC);
//        return cellResult;
//    }
//
//    Cell divide(String[] arrayOfParameters, Sheet<Cell> sheet) {
//        BigDecimal[] params = new BigDecimal[arrayOfParameters.length];
//
//        for (int i = 0; i < arrayOfParameters.length; i++) {
//            try {
//                params[i] = computeFormulaNumericParameter(arrayOfParameters[i], sheet);
//            } catch (RuntimeException exception) {
//                Cell errorCell = new Cell("#ERROR: " + exception.getMessage());
//                errorCell.setType(Type.ERROR);
//                return errorCell;
//            }
//        }
//
//        BigDecimal resultOfDivision = params[0].divide(params[1], 7, RoundingMode.FLOOR);
//
//        Cell cellResult = new Cell(resultOfDivision);
//        cellResult.setType(Type.NUMERIC);
//        return cellResult;
//    }
//
//    Cell greaterThan(String[] arrayOfParameters, Sheet<Cell> sheet) {
//        BigDecimal[] params = new BigDecimal[arrayOfParameters.length];
//        for (int i = 0; i < arrayOfParameters.length; i++) {
//            try {
//                params[i] = computeFormulaNumericParameter(arrayOfParameters[i], sheet);
//            } catch (RuntimeException exception) {
//                Cell errorCell = new Cell("#ERROR: " + exception.getMessage());
//                errorCell.setType(Type.ERROR);
//                return errorCell;
//            }
//        }
//        int resultOfCompare = params[0].compareTo(params[1]);
//        boolean isGreater = resultOfCompare == 1 ? true : false;
//        Cell cellResult = new Cell(isGreater);
//        cellResult.setType(Type.BOOLEAN);
//        return cellResult;
//    }
//
//    Cell equal(String[] arrayOfParameters, Sheet<Cell> sheet) {
//        BigDecimal[] params = new BigDecimal[arrayOfParameters.length];
//        for (int i = 0; i < arrayOfParameters.length; i++) {
//            try {
//                params[i] = computeFormulaNumericParameter(arrayOfParameters[i], sheet);
//            } catch (RuntimeException exception) {
//                Cell errorCell = new Cell("#ERROR: " + exception.getMessage());
//                errorCell.setType(Type.ERROR);
//                return errorCell;
//            }
//        }
//        int resultOfCompare = params[0].compareTo(params[1]);
//        boolean isEqual = resultOfCompare == 0 ? true : false;
//        Cell cellResult = new Cell(isEqual);
//        cellResult.setType(Type.BOOLEAN);
//        return cellResult;
//    }
//
//    Cell not(String formulaValue, Sheet<Cell> sheet) {
//        Cell cellResult = new Cell(!computeFormulaBooleanParameter(formulaValue, sheet));
//        cellResult.setType(Type.BOOLEAN);
//        return cellResult;
//    }
//
//    Cell and(String[] arrayOfParameters, Sheet<Cell> sheet) {
//        boolean[] params = new boolean[arrayOfParameters.length];
//        for (int i = 0; i < arrayOfParameters.length; i++) {
//            try {
//                params[i] = computeFormulaBooleanParameter(arrayOfParameters[i], sheet);
//            } catch (RuntimeException exception) {
//                Cell errorCell = new Cell("#ERROR: " + exception.getMessage());
//                errorCell.setType(Type.ERROR);
//                return errorCell;
//            }
//        }
//        boolean booleanResult = params[0];
//        for (int i = 1; i < params.length; i++) {
//            booleanResult = booleanResult && params[i];
//        }
//        Cell cellResult = new Cell(booleanResult);
//        cellResult.setType(Type.BOOLEAN);
//        return cellResult;
//    }
//
//    Cell or(String[] arrayOfParameters, Sheet<Cell> sheet) {
//        boolean[] params = new boolean[arrayOfParameters.length];
//        for (int i = 0; i < arrayOfParameters.length; i++) {
//            try {
//                params[i] = computeFormulaBooleanParameter(arrayOfParameters[i], sheet);
//            } catch (RuntimeException exception) {
//                Cell errorCell = new Cell("#ERROR: " + exception.getMessage());
//                errorCell.setType(Type.ERROR);
//                return errorCell;
//            }
//        }
//        boolean booleanResult = params[0];
//        for (int i = 0; i < params.length - 1; i++) {
//            booleanResult = params[i + 1] || booleanResult;
//        }
//        Cell cellResult = new Cell(booleanResult);
//        cellResult.setType(Type.BOOLEAN);
//        return cellResult;
//    }
//
//    Input ifFormula(String[] arrayOfParameters, Sheet<Cell> sheet) {
//        boolean condition;
//        try {
//            condition = computeFormulaBooleanParameter(arrayOfParameters[0], sheet);
//        } catch (RuntimeException exception) {
//            Cell errorCell = new Cell("#ERROR: " + exception.getMessage());
//            errorCell.setType(Type.ERROR);
//            return errorCell;
//        }
//        Cell cellResult;
//        if (condition) {
//            cellResult = new Cell(arrayOfParameters[1]);
//        } else {
//            cellResult = new Cell(arrayOfParameters[2]);
//        }
//        if (cellResult.getValue() instanceof Number) {
//            cellResult.setType(Type.NUMERIC);
//        } else if (cellResult.getValue() instanceof Boolean) {
//            cellResult.setType(Type.BOOLEAN);
//        } else if (cellResult.getValue() instanceof String) {
//            String stringObj = cellResult.getValue().toString();
//            if (stringObj.startsWith("=") || stringObj.matches("[A-Z][0-9]+")) {
//                cellResult.setType(Type.FORMULA);
//            } else {
//                cellResult.setType(Type.STRING);
//            }
//        }
//        return checkIfInputHasFormulaOrNotationAndCompute(cellResult, sheet);
//    }
//
//
//    Cell concat(String formulaValue, Sheet<Cell> sheet) {
//        List<String> arrayOfParameters = splitStringUsingCommaAndSpaceWhenNotSurroundedByQuotes(formulaValue);
//        Cell cellResult = new Cell(arrayOfParameters.stream()
//                .map(str -> computeFormulaConcatParameter(str, sheet))
//                .collect(Collectors.joining("")));
//        cellResult.setType(Type.STRING);
//        return cellResult;
//    }
//
//    List<String> splitStringUsingCommaAndSpaceWhenNotSurroundedByQuotes(String formulaValue) {
//        List<String> matchList = new ArrayList<>();
//        Pattern regex = Pattern.compile("[^,\\s\"]+|\"[^\"]*\"");
//        Matcher regexMatcher = regex.matcher(formulaValue);
//        while (regexMatcher.find()) {
//            matchList.add(regexMatcher.group());
//        }
//        return matchList;
//    }
}
