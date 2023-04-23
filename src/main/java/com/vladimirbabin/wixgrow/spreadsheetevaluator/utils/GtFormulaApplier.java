package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Type;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component("GT")
public class GtFormulaApplier implements FormulaApplier {
    @Override
    public Input apply(List<Input> resolvedParameters, Sheet<Input> sheet) {
        if (resolvedParameters.size() != 2) {
            Input errorCell = new Input("#ERROR: There has to be two parameters for GT formula");
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
        BigDecimal first = new BigDecimal(resolvedParameters.get(0).getValue().toString());
        BigDecimal second = new BigDecimal(resolvedParameters.get(1).getValue().toString());
        boolean isGreater = first.compareTo(second) > 0;
        Input cellResult = new Input(isGreater);
        cellResult.setType(Type.BOOLEAN);
        return cellResult;
    }
}