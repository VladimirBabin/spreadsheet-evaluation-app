package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormulaInfo {
    private final String cellStringOrParameterValue;
    private final static String CONCAT = "CONCAT";

    FormulaInfo(String cellStringOrParameterValue) {
        if (cellStringOrParameterValue.startsWith("=")) {
            this.cellStringOrParameterValue = cellStringOrParameterValue.substring(1);
        } else {
            this.cellStringOrParameterValue = cellStringOrParameterValue;
        }
    }

    String getFormulaName() {
        if (hasParameters()) {
            return cellStringOrParameterValue.substring(0, cellStringOrParameterValue.indexOf("("));
        } else if (isNotation()) {
            return "NOTATION";
        }
        return cellStringOrParameterValue;

    }

    String getFormulaContents() {
        if (hasParameters()) {
            return cellStringOrParameterValue.substring(cellStringOrParameterValue.indexOf("(") + 1,
                    cellStringOrParameterValue.lastIndexOf(")"));
        } else {
            return cellStringOrParameterValue;
        }
    }

    List<String> getArrayOfParameters() {
        if (!isConcat()) {
            return List.of(getFormulaContents().split(""));
        } else {
            return splitStringUsingCommaAndSpaceWhenNotSurroundedByQuotes(getFormulaContents());
        }
    }

    List<String> splitStringUsingCommaAndSpaceWhenNotSurroundedByQuotes(String formulaValue) {
        List<String> matchList = new ArrayList<>();
        Pattern regex = Pattern.compile("[^,\\s\"]+|\"[^\"]*\"");
        Matcher regexMatcher = regex.matcher(formulaValue);
        while (regexMatcher.find()) {
            matchList.add(regexMatcher.group());
        }
        return matchList;
    }

    boolean isConcat() {
        return getFormulaName().equals(CONCAT);
    }

    boolean hasSingleParameter() {
        return getArrayOfParameters().size() == 1;
    }

    boolean isNotation() {
        return this.cellStringOrParameterValue.matches("[A-Z][1-9]+");
    }

    public boolean hasParameters() {
        return this.cellStringOrParameterValue.contains("(");
    }
}
