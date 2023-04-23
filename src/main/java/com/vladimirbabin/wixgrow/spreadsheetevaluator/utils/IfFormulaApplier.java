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
        if (resolvedParameters.size() != 3) {
            Input errorCell = new Input("#ERROR: There has to be three parameters for IF formula");
            errorCell.setType(Type.ERROR);
            return errorCell;
        }
        Input condition = resolvedParameters.get(0);
        if (!condition.getType().equals(Type.BOOLEAN)) {
            Input errorCell = new Input("#ERROR: Invalid parameter type: " +
                    "first argument in IF formula should be of BOOLEAN type");
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
}
