package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("NOT")
public class NotFormulaApplier implements FormulaApplier {

    @Override
    public Input apply(List<Input> resolvedParameters, Sheet<Input> sheet) {
        throw new UnsupportedOperationException("This method should be used with single Input parameter");
    }

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

    @Override
    public Input apply(String formulaContents, Sheet<Input> sheet) {
        throw new UnsupportedOperationException("This method should be used with single Input parameter");
    }
}



