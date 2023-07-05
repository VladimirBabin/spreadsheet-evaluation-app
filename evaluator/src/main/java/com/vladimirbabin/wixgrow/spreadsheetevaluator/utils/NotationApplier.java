package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import org.springframework.stereotype.Component;

@Component("NOTATION")
public class NotationApplier implements FormulaApplier {

    public Input apply(FormulaInfo formulaInfo, Sheet<Input> sheet) {
        String formulaContents = formulaInfo.getFormulaContents();
        if (!formulaContents.matches("[A-Z][1-9]+")) {
            return errorCell("Invalid parameter type");
        }

        return sheet.getElementByNotation(formulaContents);
    }
}
