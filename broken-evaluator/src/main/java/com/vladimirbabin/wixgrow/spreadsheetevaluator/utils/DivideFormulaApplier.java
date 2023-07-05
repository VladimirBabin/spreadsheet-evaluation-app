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
    public Input apply(FormulaInfo formulaInfo, Sheet<Input> sheet) {
        List<Input> resolvedParameters = formulaInfo.getResolvedParameters();
        if (resolvedParameters.size() != 2) {
            return errorCell("There has to be two parameters for DIVIDE formula");
        }
        if (!validateParametersTypes(resolvedParameters, Type.NUMERIC)) {
            return errorCell("Invalid parameter type");
        }
        BigDecimal numerator = new BigDecimal(resolvedParameters.get(0).getValue().toString());
        BigDecimal denominator = new BigDecimal(resolvedParameters.get(1).getValue().toString());
        BigDecimal resultOfDivision = numerator.divide(denominator, 7, RoundingMode.FLOOR);
        Input cellResult = new Input(resultOfDivision);
        cellResult.setType(Type.NUMERIC);
        return cellResult;
    }
}
