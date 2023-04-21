package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Type;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class InputTypeDeterminerTest {
    InputTypeDeterminer inputTypeDeterminer = new InputTypeDeterminer();

    @ParameterizedTest
    @ValueSource(ints = {1, 0, -59, Integer.MAX_VALUE})
    public void determineNumericTypeTest(int value) {
        Input input = new Input(value);
        Input resultInput = inputTypeDeterminer.determineType(input);
        assertEquals(Type.NUMERIC, resultInput.getType());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void determineBooleanTypeTest(boolean value) {
        Input input = new Input(value);
        Input resultInput = inputTypeDeterminer.determineType(input);
        assertEquals(Type.BOOLEAN, resultInput.getType());
    }

    @ParameterizedTest
    @ValueSource(strings = {"true", "false", "FALSE"})
    public void determineStringBooleanTypeTest(String value) {
        Input input = new Input(value);
        Input resultInput = inputTypeDeterminer.determineType(input);
        assertEquals(Type.BOOLEAN, resultInput.getType());
    }

    @ParameterizedTest
    @ValueSource(strings = {"=SUM(A1, B2)", "=A1", "=true", "=DIVIDE(A1, B1)",
            "=CONCAT(\"Hello\", \", \", \"World!\")"})
    public void determineFormulaTypeTest(String value) {
        Input input = new Input(value);
        Input resultInput = inputTypeDeterminer.determineType(input);
        assertEquals(Type.FORMULA, resultInput.getType());
    }

    @ParameterizedTest
    @ValueSource(strings = {"A1", "B4", "E7"})
    public void determineNotationTypeTest(String value) {
        Input input = new Input(value);
        Input resultInput = inputTypeDeterminer.determineType(input);
        assertEquals(Type.NOTATION, resultInput.getType());
    }

    @ParameterizedTest
    @ValueSource(strings = {", ", "World!", " is "})
    public void determineStringTypeTest(String value) {
        Input input = new Input(value);
        Input resultInput = inputTypeDeterminer.determineType(input);
        assertEquals(Type.STRING, resultInput.getType());
    }

}