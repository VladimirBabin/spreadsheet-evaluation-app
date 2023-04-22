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

    @Override
    public Input apply(Input singleParameter, Sheet<Input> sheet) {
        throw new UnsupportedOperationException("This method should be used with multiple Input parameters");
    }

    @Override
    public Input apply(String formulaContents, Sheet<Input> sheet) {
        throw new UnsupportedOperationException("This method should be used with multiple Input parameters");
    }
}
