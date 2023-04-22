package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Type;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.service.InputTypeDeterminer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GtFormulaApplierTest {
    @Autowired
    private GtFormulaApplier gtFormulaApplier;

    @Autowired
    private InputTypeDeterminer inputTypeDeterminer;

    @Test
    public void applyWithTwoParameters() {
        Sheet sheet = null;
        Input firstCell = new Input(new BigDecimal(1));
        firstCell.setType(Type.NUMERIC);
        Input secondCell = new Input(new BigDecimal(3));
        secondCell.setType(Type.NUMERIC);

        Input expected = new Input(false);
        expected.setType(Type.BOOLEAN);

        List<Input> parameters = List.of(firstCell, secondCell);

        Input result = gtFormulaApplier.apply(parameters, sheet);
        result = inputTypeDeterminer.determineType(result);

        assertEquals(expected.getType(), result.getType());
        assertEquals(expected.getValue(), result.getValue());
    }

    @Test
    public void applyWithInvalidParameter() {
        Sheet sheet = null;
        Input firstCell = new Input("String");
        firstCell.setType(Type.STRING);
        Input secondCell = new Input(22);
        secondCell.setType(Type.NUMERIC);

        List<Input> parameters = List.of(firstCell, secondCell);

        Input result = gtFormulaApplier.apply(parameters, sheet);

        assertTrue(result.getType().equals(Type.ERROR));
        assertEquals("#ERROR: Invalid parameter type", result.getValue());
    }
}