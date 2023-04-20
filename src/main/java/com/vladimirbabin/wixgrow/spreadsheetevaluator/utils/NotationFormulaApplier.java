package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.entity.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("NOTATION")
public class NotationFormulaApplier implements FormulaApplier {

    @Override
    public Cell apply(FormulaInfo formulaInfo, Sheet<Cell> sheet) {
        Cell result = sheet.getElementByNotation(formulaInfo.getCellStringOrParameterValue());
        return result;
    }
}
