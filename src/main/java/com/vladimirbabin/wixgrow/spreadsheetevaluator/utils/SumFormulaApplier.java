package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

@Component("SUM")
public class SumFormulaApplier implements FormulaApplier {
    @Override
    public Cell apply(FormulaInfo formulaInfo, Sheet<Cell> sheet) {
        String[] arrayOfParameters = formulaInfo.getArrayOfParameters();
        BigDecimal[] params = new BigDecimal[arrayOfParameters.length];
        for (int i = 0; i < arrayOfParameters.length; i++) {
            try {
                params[i] = checkIfNotationAndCompute(arrayOfParameters[i], sheet);
            } catch (RuntimeException exception) {
                Cell errorCell = new Cell("#ERROR: " + exception.getMessage());
                errorCell.setType(Type.ERROR);
                return errorCell;
            }
        }
        BigDecimal resultOfSum = Arrays.stream(params).reduce(BigDecimal.ZERO, BigDecimal::add);
        Cell cellResult = new Cell(resultOfSum);
        cellResult.setType(Type.NUMERIC);
        return cellResult;
    }

    private BigDecimal checkIfNotationAndCompute(String parameter, Sheet<Cell> sheet) {
        Cell tempCell = null;
        FormulaInfo formulaInfo = new FormulaInfo(parameter);
        if (NumberUtils.isParsable(parameter)) {
            return new BigDecimal(parameter);
        } else {
            try {
                tempCell = sheet.getElementByNotation(parameter);
                tempCell.setType(Type.NUMERIC);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Sheet doesn't contain such notation");
            }
        }
        if (tempCell == null || !tempCell.getType().equals(Type.NUMERIC)) {
            throw new RuntimeException("Null or not compatible cell type");
        }
        return new BigDecimal(tempCell.getValue().toString());
    }
}
