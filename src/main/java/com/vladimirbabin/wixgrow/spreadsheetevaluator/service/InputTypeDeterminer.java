package com.vladimirbabin.wixgrow.spreadsheetevaluator.service;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import org.springframework.stereotype.Service;

import java.util.Arrays;

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
