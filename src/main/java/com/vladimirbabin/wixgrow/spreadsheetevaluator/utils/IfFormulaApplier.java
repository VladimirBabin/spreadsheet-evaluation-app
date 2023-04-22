package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Type;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("IF")
public class IfFormulaApplier implements FormulaApplier {
    @Override
    public Input apply(List<Input> resolvedParameters, Sheet<Input> sheet) {
        Input condition = resolvedParameters.get(0);
        if (!condition.getType().equals(Type.BOOLEAN)) {
            Input errorCell = new Input("#ERROR: Invalid parameter type");
            errorCell.setType(Type.ERROR);
            return errorCell;
        }
        boolean resultOfIfCondition = Boolean.parseBoolean(condition.getValue().toString());
        Input cellResult;
        if (resultOfIfCondition) {
            cellResult = resolvedParameters.get(1);
        } else {
            cellResult = resolvedParameters.get(2);
        }
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
