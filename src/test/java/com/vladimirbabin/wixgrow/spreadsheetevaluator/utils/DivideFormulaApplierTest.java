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
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class DivideFormulaApplierTest {
//    @Autowired
//    private DivideFormulaApplier divideFormulaApplier;
//
//    @Autowired
//    private InputTypeDeterminer inputTypeDeterminer;
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
//    public void applyWithTwoParameters() {
//        firstCell.setValue(6);
//        firstCell.setType(Type.NUMERIC);
//        secondCell.setValue(4);
//        secondCell.setType(Type.NUMERIC);
//
//        BigDecimal expectedNumber = new BigDecimal(6).divide(new BigDecimal(4), 7, RoundingMode.FLOOR);
//        expected.setValue(expectedNumber);
//        expected.setType(Type.NUMERIC);
//
//        List<Input> parameters = List.of(firstCell, secondCell);
//
//        Input result = divideFormulaApplier.apply(parameters, sheet);
//        result = inputTypeDeterminer.determineType(result);
//
//        assertEquals(expected.getType(), result.getType());
//        assertEquals(expected.getValue(), result.getValue());
//    }
//
//    @Test
//    public void applyWithThreeParameters() {
//        firstCell.setValue(20);
//        firstCell.setType(Type.NUMERIC);
//        secondCell.setValue(5);
//        secondCell.setType(Type.NUMERIC);
//        thirdCell.setValue(2);
//        thirdCell.setType(Type.NUMERIC);
//
//        List<Input> parameters = List.of(firstCell, secondCell, thirdCell);
//
//        Input result = divideFormulaApplier.apply(parameters, sheet);
//
//        assertTrue(result.getType().equals(Type.ERROR));
//        assertEquals("#ERROR: There has to be two parameters for DIVIDE formula", result.getValue());
//    }
//
//    @Test
//    public void applyWithInvalidParameter() {
//        firstCell.setValue("String");
//        firstCell.setType(Type.STRING);
//        secondCell.setValue(212212);
//        secondCell.setType(Type.NUMERIC);
//
//        List<Input> parameters = List.of(firstCell, secondCell);
//
//        Input result = divideFormulaApplier.apply(parameters, sheet);
//
//        assertTrue(result.getType().equals(Type.ERROR));
//        assertEquals("#ERROR: Invalid parameter type", result.getValue());
//    }
//}