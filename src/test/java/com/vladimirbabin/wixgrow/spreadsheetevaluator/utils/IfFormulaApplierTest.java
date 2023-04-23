package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.service.InputTypeDeterminer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IfFormulaApplierTest {
    @Autowired
    IfFormulaApplier ifFormulaApplier;

    @Autowired
    InputTypeDeterminer inputTypeDeterminer;

    private Sheet sheet;
    private Input firstCell;
    private Input secondCell;
    private Input thirdCell;
    private Input expected;

    @BeforeEach
    void setUp() {
        sheet = Mockito.mock(Sheet.class);
        firstCell = new Input();
        secondCell = new Input();
        thirdCell = new Input();
        expected = new Input();
    }

    @Test
    void applyWithTwoParameters() {
        firstCell.setValue(22);
        firstCell.setType(Type.NUMERIC);
        secondCell.setValue(212212);
        secondCell.setType(Type.NUMERIC);

        expected.setValue(new BigDecimal(212234));
        expected.setType(Type.NUMERIC);

        List<Input> parameters = List.of(firstCell, secondCell);

        Input result = ifFormulaApplier.apply(parameters, sheet);
        result = inputTypeDeterminer.determineType(result);

        assertTrue(result.getType().equals(Type.ERROR));
        assertEquals("#ERROR: There has to be three parameters for IF formula", result.getValue());
    }

    @Test
    public void applyWithThreeValidParameters() {
        firstCell.setValue(true);
        firstCell.setType(Type.BOOLEAN);
        secondCell.setValue(22);
        secondCell.setType(Type.NUMERIC);
        thirdCell.setValue(212);
        thirdCell.setType(Type.NUMERIC);

        expected.setValue(22);
        expected.setType(Type.NUMERIC);

        List<Input> parameters = List.of(firstCell, secondCell, thirdCell);

        Input result = ifFormulaApplier.apply(parameters, sheet);
        result = inputTypeDeterminer.determineType(result);

        assertEquals(expected.getType(), result.getType());
        assertEquals(expected.getValue(), result.getValue());
    }

    @Test
    public void applyWithInvalidConditionParameter() {
        firstCell.setValue("String");
        firstCell.setType(Type.STRING);
        secondCell.setValue(212212);
        secondCell.setType(Type.NUMERIC);
        thirdCell.setValue(212);
        thirdCell.setType(Type.NUMERIC);

        List<Input> parameters = List.of(firstCell, secondCell, thirdCell);
        Input result = ifFormulaApplier.apply(parameters, sheet);

        assertTrue(result.getType().equals(Type.ERROR));
        assertEquals("#ERROR: Invalid parameter type: " +
                "first argument in IF formula should be of BOOLEAN type", result.getValue());
    }
}