package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.service.InputTypeDeterminer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class NotationApplierTest {

    private NotationApplier notationApplier;

    private InputTypeDeterminer inputTypeDeterminer;

    private Sheet sheet;

    @Value("${spreadsheet.json.notation}")
    private String sheetWithTwoNotations;

    @Value("${spreadsheet.json.notations-referencing}")
    private String sheetWithNotationsReferencingNotations;


    @BeforeEach
    void setUp() {
        inputTypeDeterminer = new InputTypeDeterminer();
        notationApplier = new NotationApplier(inputTypeDeterminer);
    }

    @Test
    public void applyForSheetWithTwoNotations() {
        FormulaInfo formulaInfo = new FormulaInfo("=A1");

        try {
            sheet = new ObjectMapper().readValue(sheetWithTwoNotations, Sheet.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Input firstCell = notationApplier.apply(formulaInfo, sheet);
        assertEquals(Type.NUMERIC, firstCell.getType());
        assertEquals(5, firstCell.getValue());
    }

    @Test
    public void applyForSheetWithNotationsReferencingNotations() {
        FormulaInfo formulaInfo = new FormulaInfo("=A6");

        try {
            sheet = new ObjectMapper().readValue(sheetWithNotationsReferencingNotations, Sheet.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Input firstCell = notationApplier.apply(formulaInfo, sheet);
        assertEquals(Type.FORMULA, firstCell.getType());
        assertEquals("=A5", firstCell.getValue());
    }

    @Test
    public void applyWithInvalidParameter() {
        FormulaInfo formulaInfo = new FormulaInfo("9");

        try {
            sheet = new ObjectMapper().readValue(sheetWithTwoNotations, Sheet.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Input result = notationApplier.apply(formulaInfo, sheet);

        assertTrue(result.getType().equals(Type.ERROR));
        assertEquals("#ERROR: Invalid parameter type", result.getValue());
    }
}