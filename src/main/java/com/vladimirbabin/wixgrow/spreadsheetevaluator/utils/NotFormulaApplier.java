package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import org.springframework.stereotype.Component;

@Component("NOT")
public class NotFormulaApplier implements FormulaApplier {

    @Override
    public Input apply(FormulaInfo formulaInfo, Sheet<Input> sheet) {
        Input singleParameter = formulaInfo.getResolvedParameters().get(0);
        if (formulaInfo.getResolvedParameters().size() > 1) {
            Input errorCell = new Input("#ERROR: There has to be only one parameter for NOT formula");
            errorCell.setType(Type.ERROR);
            return errorCell;
        }
        if (!singleParameter.getType().equals(Type.BOOLEAN)) {
            Input errorCell = new Input("#ERROR: Invalid parameter type");
            errorCell.setType(Type.ERROR);
            return errorCell;
        }
        Boolean resultOfNotFormula = !Boolean.parseBoolean(singleParameter.getValue().toString());
        Input cellResult = new Input(resultOfNotFormula);
        return cellResult;
    }
}



