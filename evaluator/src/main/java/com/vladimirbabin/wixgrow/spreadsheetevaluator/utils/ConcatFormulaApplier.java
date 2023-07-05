package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component("CONCAT")
public class ConcatFormulaApplier implements FormulaApplier {
    @Override
    public Input apply(FormulaInfo formulaInfo, Sheet<Input> sheet) {
        for (Input parameter : formulaInfo.getResolvedParameters()) {
            Type type = parameter.getType();
            if (type.equals(Type.ERROR) || type.equals(Type.FORMULA) || type.equals(Type.NOTATION)) {
                return errorCell("Invalid parameter type");
            }
        }

        String concatenationResult = formulaInfo.getResolvedParameters().stream()
                .map(Input::getValue)
                .map(Object::toString)
                .map(s -> s.replace("\"", ""))
                .collect(Collectors.joining(""));

        Input cellResult = new Input(concatenationResult);
        cellResult.setType(Type.STRING);
        return cellResult;
    }
}
