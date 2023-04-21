package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

@Component("MULTIPLY")
public class MultiplyFormulaApplier implements FormulaApplier {
    @Override
    public Cell apply(FormulaInfo formulaInfo, Sheet<Cell> sheet) {
        String[] arrayOfParameters = formulaInfo.getArrayOfParameters();
        BigDecimal[] params = new BigDecimal[arrayOfParameters.length];

        for (int i = 0; i < arrayOfParameters.length; i++) {
            try {
                params[i] = new BigDecimal(arrayOfParameters[i]);
            } catch (RuntimeException exception) {
                Cell errorCell = new Cell("#ERROR: " + exception.getMessage());
                errorCell.setType(Type.ERROR);
                return errorCell;
            }
        }

        BigDecimal resultOfMultiplication = Arrays.stream(params).reduce(BigDecimal.ONE, BigDecimal::multiply);

        Cell cellResult = new Cell(resultOfMultiplication);
        cellResult.setType(Type.NUMERIC);
        return cellResult;
    }
}
