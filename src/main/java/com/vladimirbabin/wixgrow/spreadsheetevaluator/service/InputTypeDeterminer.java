package com.vladimirbabin.wixgrow.spreadsheetevaluator.service;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

@Service
public class InputTypeDeterminer {

    public <T extends Input> T determineType(T input) {
        Object value = input.getValue();
        Type type = Arrays.stream(Type.values())
                .filter(t -> t.deserialize(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid value"));

        input.setType(type);
        return input;
    }
}
