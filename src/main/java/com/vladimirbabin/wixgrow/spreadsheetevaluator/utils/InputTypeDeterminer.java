package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Type;
import org.springframework.stereotype.Service;

@Service
public class InputTypeDeterminer {

    /* Parameter or Cell can be of 5 types:
    Numeric
    Boolean
    String
    Notation
    (Another) formula
    * */

    public <T extends Input> T determineType(T input) {
        Object value = input.getValue();
        if (value instanceof Number) {
            input.setType(Type.NUMERIC);
        } else if (value instanceof Boolean) {
            input.setType(Type.BOOLEAN);
        } else if (value instanceof String) {
            String stringObj = value.toString();
            if (stringObj.startsWith("=")) {
                input.setType(Type.FORMULA);
            } else if (isNotation(stringObj)) {
                input.setType(Type.NOTATION);
            } else if (isBoolean(stringObj)) {
                input.setType(Type.BOOLEAN);
            } else {
                input.setType(Type.STRING);
            }
        }
        return input;
    }

    private boolean isBoolean(String stringObj) {
        if (stringObj.equalsIgnoreCase("true") || stringObj.equalsIgnoreCase("false")) {
            return true;
        }
        return false;
    }

    private boolean isNotation(String parameter) {
        return parameter.matches("[A-Z][0-9]+");
    }
}
