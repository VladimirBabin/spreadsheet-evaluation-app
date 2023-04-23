package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component("DIVIDE")
public class DivideFormulaApplier implements FormulaApplier {
    @Override
    public Input apply(List<Input> resolvedParameters, Sheet<Input> sheet) {
        if (resolvedParameters.size() != 2) {
            Input errorCell = new Input("#ERROR: There has to be two parameters for DIVIDE formula");
            errorCell.setType(Type.ERROR);
            return errorCell;
        }
        for (Input parameter : resolvedParameters) {
            if (!parameter.getType().equals(Type.NUMERIC)) {
                Input errorCell = new Input("#ERROR: Invalid parameter type");
                errorCell.setType(Type.ERROR);
                return errorCell;
            }
        }
        BigDecimal numerator = new BigDecimal(resolvedParameters.get(0).getValue().toString());
        BigDecimal denominator = new BigDecimal(resolvedParameters.get(1).getValue().toString());
        BigDecimal resultOfDivision = numerator.divide(denominator, 7, RoundingMode.FLOOR);
        Input cellResult = new Input(resultOfDivision);
        return cellResult;
    }
}
