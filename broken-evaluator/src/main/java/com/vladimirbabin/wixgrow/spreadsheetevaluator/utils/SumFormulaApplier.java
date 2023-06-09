package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component("SUM")
public class SumFormulaApplier implements FormulaApplier {

    @Override
    public Input apply(FormulaInfo formulaInfo, Sheet<Input> sheet) {
        List<Input> resolvedParameters = formulaInfo.getResolvedParameters();
        if (!validateParametersTypes(resolvedParameters, Type.NUMERIC)) {
            return errorCell("Invalid parameter type");
        }
        BigDecimal resultOfSum = resolvedParameters.stream()
                .map(i -> i.getValue().toString())
                .map(BigDecimal::new)
//                implied bug - reducing from 1 instead of 0:
                .reduce(BigDecimal.ONE, BigDecimal::add);
        Input cellResult = new Input(resultOfSum);
        cellResult.setType(Type.NUMERIC);
        return cellResult;
    }
}
