package com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.service;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto.Type;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * The InputTypeDeterminer class intakes a cell or parameter, applies the deserialize method from the Type class
 * to each of the Type, finds the first that matches and due to the ordering in Type enums sets the correct type
 * for the input.
 */
@Service
public class InputTypeDeterminer {

    public Input determineType(Input input) {
        Object value = input.getValue();
        Type type = Arrays.stream(Type.values())
                .filter(t -> t.deserialize(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid value"));

        input.setType(type);
        return input;
    }
}
