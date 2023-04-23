package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;

import java.util.List;

public abstract class FormulaApplier {
    Input apply(List<Input> resolvedParameters, Sheet<Input> sheet) {
        Input errorCell = new Input("#ERROR: Multiple parameters can't be applied for " + this.getClass().getName());
        errorCell.setType(Type.ERROR);
        return errorCell;
    }
    Input apply(Input singleParameter, Sheet<Input> sheet) {
        Input errorCell = new Input("#ERROR: Single Input parameter can't be applied for " + this.getClass().getName());
        errorCell.setType(Type.ERROR);
        return errorCell;
    }
    Input apply(String formulaContents, Sheet<Input> sheet) {
        Input errorCell = new Input("#ERROR: Single String parameter can't be applied for " + this.getClass().getName());
        errorCell.setType(Type.ERROR);
        return errorCell;
    }
}
