package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Type;
import org.springframework.stereotype.Component;

@Component("NOT")
public class NotFormulaApplier implements FormulaApplier {

    @Override
    public Input apply(Input singleParameter, Sheet<Input> sheet) {
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



