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

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EqFormulaApplierTest {
    @Autowired
    private EqFormulaApplier eqFormulaApplier;

    @Autowired
    private InputTypeDeterminer inputTypeDeterminer;

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
    public void applyWithTwoEqualParameters() {
        firstCell.setValue(new BigDecimal(10.74));
        firstCell.setType(Type.NUMERIC);
        secondCell.setValue(new BigDecimal(10.74));
        secondCell.setType(Type.NUMERIC);

        expected.setValue(true);
        expected.setType(Type.BOOLEAN);

        List<Input> parameters = List.of(firstCell, secondCell);

        Input result = eqFormulaApplier.apply(parameters, sheet);
        result = inputTypeDeterminer.determineType(result);

        assertEquals(expected.getType(), result.getType());
        assertEquals(expected.getValue(), result.getValue());
    }

    @Test
    public void applyWithTwoNonEqualParameters() {
        firstCell.setValue(new BigDecimal(10.74));
        firstCell.setType(Type.NUMERIC);
        secondCell.setValue(new BigDecimal(10.75));
        secondCell.setType(Type.NUMERIC);

        expected.setValue(false);
        expected.setType(Type.BOOLEAN);

        List<Input> parameters = List.of(firstCell, secondCell);

        Input result = eqFormulaApplier.apply(parameters, sheet);
        result = inputTypeDeterminer.determineType(result);

        assertEquals(expected.getType(), result.getType());
        assertEquals(expected.getValue(), result.getValue());
    }

    @Test
    public void applyWithThreeParameters() {
        firstCell.setValue(new BigDecimal(5));
        firstCell.setType(Type.NUMERIC);
        secondCell.setValue(new BigDecimal(4));
        secondCell.setType(Type.NUMERIC);
        thirdCell.setValue(new BigDecimal(3));
        thirdCell.setType(Type.NUMERIC);


        List<Input> parameters = List.of(firstCell, secondCell, thirdCell);

        Input result = eqFormulaApplier.apply(parameters, sheet);

        assertTrue(result.getType().equals(Type.ERROR));
        assertEquals("#ERROR: There has to be two parameters for EQ formula", result.getValue());
    }

    @Test
    public void applyWithInvalidParameter() {
        firstCell.setValue("String");
        firstCell.setType(Type.STRING);
        secondCell.setValue(new BigDecimal(22));
        secondCell.setType(Type.NUMERIC);
        List<Input> parameters = List.of(firstCell, secondCell);

        Input result = eqFormulaApplier.apply(parameters, sheet);

        assertTrue(result.getType().equals(Type.ERROR));
        assertEquals("#ERROR: Invalid parameter type", result.getValue());
    }
}