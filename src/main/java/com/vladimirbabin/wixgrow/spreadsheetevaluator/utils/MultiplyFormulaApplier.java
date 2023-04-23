package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Type;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component("MULTIPLY")
public class MultiplyFormulaApplier implements FormulaApplier {

    @Override
    public Input apply(List<Input> resolvedParameters, Sheet<Input> sheet) {

        for (Input parameter : resolvedParameters) {
            if (!parameter.getType().equals(Type.NUMERIC)) {
                Input errorCell = new Input("#ERROR: Invalid parameter type");
                errorCell.setType(Type.ERROR);
                return errorCell;
            }
        }

        BigDecimal resultOfMultiplication = resolvedParameters
                .stream()
                .map(i -> i.getValue().toString())
                .map(BigDecimal::new)
                .reduce(BigDecimal.ONE, BigDecimal::multiply);

        Input cellResult = new Input(resultOfMultiplication);
        return cellResult;
    }
}
