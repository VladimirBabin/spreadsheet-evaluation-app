package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component("SUM")
public class SumFormulaApplier implements FormulaApplier {
    Logger logger = LoggerFactory.getLogger(SumFormulaApplier.class);

    @Override
    public Input apply(List<Input> resolvedParameters, Sheet<Input> sheet) {
        for (Input parameter : resolvedParameters) {
            logger.info(parameter.toString());
            if (!parameter.getType().equals(Type.NUMERIC)) {
                Input errorCell = new Input("#ERROR: Invalid parameter type");
                errorCell.setType(Type.ERROR);
                logger.error(parameter.toString() + " caused an error");
                return errorCell;
            }
        }
        BigDecimal resultOfSum = resolvedParameters.stream()
                .map(i -> i.getValue().toString())
                .map(BigDecimal::new)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Input cellResult = new Input(resultOfSum);
        return cellResult;
    }
}
