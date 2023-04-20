package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

public class FormulaInfo {
    String cellStringOrParameterValue;

    FormulaInfo(String cellStringOrParameterValue) {
        if (cellStringOrParameterValue.startsWith("=")) {
            this.cellStringOrParameterValue = cellStringOrParameterValue.substring(1);
        } else {
            this.cellStringOrParameterValue = cellStringOrParameterValue;
        }
    }

    String getCellStringOrParameterValue() {
        return cellStringOrParameterValue;
    }

    String getFormulaName() {
        if (isFormula(cellStringOrParameterValue)) {
            return cellStringOrParameterValue.substring(0, cellStringOrParameterValue.indexOf("("));
        } else {
            if (cellStringOrParameterValue.matches("[A-Z][0-9]+")) {
                return "NOTATION";
            }
            return cellStringOrParameterValue;
        }
    }

    String getFormulaContents() {
        if (isFormula(cellStringOrParameterValue)) {
            return cellStringOrParameterValue.substring(cellStringOrParameterValue.indexOf("(") + 1,
                    cellStringOrParameterValue.lastIndexOf(")"));
        } else {
            return cellStringOrParameterValue;
        }
    }

    String[] getArrayOfParameters() {
        if (isFormula(cellStringOrParameterValue)) {
            return getFormulaContents().split(",\s(?![^(]*?\\))");
        } else {
            String [] arrayOfOneParameter = {cellStringOrParameterValue};
            return arrayOfOneParameter;
        }
    }

    boolean isFormula(String cellOrParameterStringValue) {
        return cellOrParameterStringValue.contains("(");
    }
}