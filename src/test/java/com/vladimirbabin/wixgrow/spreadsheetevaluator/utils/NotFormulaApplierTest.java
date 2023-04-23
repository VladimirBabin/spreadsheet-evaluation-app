package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Type;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.service.InputTypeDeterminer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NotFormulaApplierTest {

    @Autowired
    private NotFormulaApplier notFormulaApplier;

    @Autowired
    private InputTypeDeterminer inputTypeDeterminer;

    private Sheet sheet;
    private Input firstCell;
    private Input secondCell;
    private Input expected;

    @BeforeEach
    void setUp() {
        sheet = Mockito.mock(Sheet.class);
        firstCell = new Input();
        secondCell = new Input();
        expected = new Input();
    }

    @Test
    public void applyWithOneParameter() {
        firstCell.setValue(false);
        firstCell.setType(Type.BOOLEAN);

        expected.setValue(true);
        expected.setType(Type.BOOLEAN);

        Input result = notFormulaApplier.apply(firstCell, sheet);
        result = inputTypeDeterminer.determineType(result);

        assertEquals(expected.getType(), result.getType());
        assertEquals(expected.getValue(), result.getValue());
    }

    @Test
    public void applyWithTwoParameters() {
        firstCell.setValue(false);
        firstCell.setType(Type.BOOLEAN);
        secondCell.setValue(true);
        secondCell.setType(Type.BOOLEAN);

        List<Input> parameters = List.of(firstCell, secondCell);

        Input result = notFormulaApplier.apply(parameters, sheet);

        assertTrue(result.getType().equals(Type.ERROR));
        assertEquals("#ERROR: Multiple parameters can't be applied for " + notFormulaApplier.getClass().getName()
                , result.getValue());
    }

    @Test
    public void applyWithInvalidParameter() {
        firstCell.setValue("String");
        firstCell.setType(Type.STRING);

        Input result = notFormulaApplier.apply(firstCell, sheet);

        assertTrue(result.getType().equals(Type.ERROR));
        assertEquals("#ERROR: Invalid parameter type", result.getValue());
    }
}