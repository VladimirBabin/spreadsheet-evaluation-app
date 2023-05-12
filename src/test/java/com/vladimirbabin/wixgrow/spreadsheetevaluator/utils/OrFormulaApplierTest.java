//package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;
//
//import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
//import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
//import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
//import com.vladimirbabin.wixgrow.spreadsheetevaluator.service.InputTypeDeterminer;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class OrFormulaApplierTest {
//
//    @Autowired
//    OrFormulaApplier orFormulaApplier;
//
//    @Autowired
//    InputTypeDeterminer inputTypeDeterminer;
//
//    private Sheet sheet;
//    private Input firstCell;
//    private Input secondCell;
//    private Input thirdCell;
//    private Input expected;
//
//    @BeforeEach
//    void setUp() {
//        sheet = Mockito.mock(Sheet.class);
//        firstCell = new Input();
//        secondCell = new Input();
//        thirdCell = new Input();
//        expected = new Input();
//    }
//
//    @Test
//    void applyWithTwoParametersWhenOneIsTrue() {
//        firstCell.setValue(true);
//        firstCell.setType(Type.BOOLEAN);
//        secondCell.setValue(false);
//        secondCell.setType(Type.BOOLEAN);
//
//        expected.setValue(true);
//        expected.setType(Type.BOOLEAN);
//
//        List<Input> parameters = List.of(firstCell, secondCell);
//
//        Input result = orFormulaApplier.apply(parameters, sheet);
//        result = inputTypeDeterminer.determineType(result);
//
//        assertEquals(expected.getType(), result.getType());
//        assertEquals(expected.getValue(), result.getValue());
//    }
//
//    @Test
//    public void applyWithThreeParametersWhereOneIsTrue() {
//        firstCell.setValue(false);
//        firstCell.setType(Type.BOOLEAN);
//        secondCell.setValue(false);
//        secondCell.setType(Type.BOOLEAN);
//        thirdCell.setValue(true);
//        thirdCell.setType(Type.BOOLEAN);
//
//        expected.setValue(true);
//        expected.setType(Type.BOOLEAN);
//
//        List<Input> parameters = List.of(firstCell, secondCell, thirdCell);
//
//        Input result = orFormulaApplier.apply(parameters, sheet);
//        result = inputTypeDeterminer.determineType(result);
//
//        assertEquals(expected.getType(), result.getType());
//        assertEquals(expected.getValue(), result.getValue());
//    }
//
//    @Test
//    public void applyWithThreeParametersWhereNoneIsTrue() {
//        firstCell.setValue(false);
//        firstCell.setType(Type.BOOLEAN);
//        secondCell.setValue(false);
//        secondCell.setType(Type.BOOLEAN);
//        thirdCell.setValue(false);
//        thirdCell.setType(Type.BOOLEAN);
//
//        expected.setValue(false);
//        expected.setType(Type.BOOLEAN);
//
//        List<Input> parameters = List.of(firstCell, secondCell, thirdCell);
//
//        Input result = orFormulaApplier.apply(parameters, sheet);
//        result = inputTypeDeterminer.determineType(result);
//
//        assertEquals(expected.getType(), result.getType());
//        assertEquals(expected.getValue(), result.getValue());
//    }
//
//    @Test
//    public void applyWithInvalidParameter() {
//        firstCell.setValue(false);
//        firstCell.setType(Type.BOOLEAN);
//        secondCell.setValue(212212);
//        secondCell.setType(Type.NUMERIC);
//
//        List<Input> parameters = List.of(firstCell, secondCell);
//        Input result = orFormulaApplier.apply(parameters, sheet);
//
//        assertTrue(result.getType().equals(Type.ERROR));
//        assertEquals("#ERROR: Invalid parameter type", result.getValue());
//    }
//}