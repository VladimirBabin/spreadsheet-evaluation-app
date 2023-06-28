package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.service.InputTypeDeterminer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotationApplierTest {

    private final InputTypeDeterminer inputTypeDeterminer = new InputTypeDeterminer();
    private final NotationApplier notationApplier = new NotationApplier(inputTypeDeterminer);

    private Sheet sheet;
    private final String sheetWithTwoNotations = "{\"id\":\"sheet-2\",\"data\":[[5,\"=A1\",22,\"=C1\"]]}";
    private final String sheetWithNotationsReferencingNotations =
            "{\"id\":\"sheet-22\",\"data\":[[\"First\"],[\"=A1\"],[\"=A2\"],[\"=A3\"],[\"=A4\"],[\"=A5\"],[\"=A6\"]]}";

    @Test
    public void applyForSheetWithTwoNotations() {
        FormulaInfo formulaInfo = new FormulaInfo("=A1");

        try {
            sheet = new ObjectMapper().readValue(sheetWithTwoNotations, Sheet.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Input result = notationApplier.apply(formulaInfo, sheet);
        assertEquals(Type.NUMERIC, result.getType());
        assertEquals(5, result.getValue());
    }

    @Test
    public void applyForSheetWithNotationsReferencingNotations() {
        FormulaInfo formulaInfo = new FormulaInfo("=A6");

        try {
            sheet = new ObjectMapper().readValue(sheetWithNotationsReferencingNotations, Sheet.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Input result = notationApplier.apply(formulaInfo, sheet);
        assertEquals(Type.NOTATION, result.getType());
        assertEquals("=A5", result.getValue());
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

        assertEquals(result.getType(), Type.ERROR);
        assertEquals("#ERROR: Invalid parameter type", result.getValue());
    }
}