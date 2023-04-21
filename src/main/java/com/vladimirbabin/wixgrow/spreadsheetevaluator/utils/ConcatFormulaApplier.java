package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("CONCAT")
public class ConcatFormulaApplier implements FormulaApplier {
    @Override
    public Input apply(List<Input> resolvedParameters, Sheet<Input> sheet) {
        return null;
    }
}
