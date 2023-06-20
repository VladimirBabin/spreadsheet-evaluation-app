package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component("GT")
public class GtFormulaApplier implements FormulaApplier {
    @Override
    public Input apply(FormulaInfo formulaInfo, Sheet<Input> sheet) {
        List<Input> resolvedParameters = formulaInfo.getResolvedParameters();
        if (resolvedParameters.size() != 2) {
            return errorCell("There has to be two parameters for GT formula");
        }
        if (!validateParametersTypes(resolvedParameters, Type.NUMERIC)) {
            return errorCell("Invalid parameter type");
        }
        BigDecimal first = new BigDecimal(resolvedParameters.get(0).getValue().toString());
        BigDecimal second = new BigDecimal(resolvedParameters.get(1).getValue().toString());
        boolean isGreater = first.compareTo(second) > 0;
//        implied bug - returns a numeric value
        Input cellResult = new Input(1);
        cellResult.setType(Type.NUMERIC);
        return cellResult;
    }
}
