package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("IF")
public class IfFormulaApplier implements FormulaApplier {
    @Override
    public Input apply(FormulaInfo formulaInfo, Sheet<Input> sheet) {
        List<Input> resolvedParameters = formulaInfo.getResolvedParameters();
        if (resolvedParameters.size() != 3) {
            return errorCell("There has to be three parameters for IF formula");
        }
        Input condition = resolvedParameters.get(0);
        if (!condition.getType().equals(Type.BOOLEAN)) {
            return errorCell("Invalid parameter type: first argument in IF formula should be of BOOLEAN type");
        }
        boolean resultOfIfCondition = Boolean.parseBoolean(condition.getValue().toString());
        Input cellResult;
        if (resultOfIfCondition) {
            cellResult = resolvedParameters.get(1);
        } else {
            cellResult = resolvedParameters.get(2);
        }
        cellResult.setType(Type.BOOLEAN);
        return cellResult;
    }
}
