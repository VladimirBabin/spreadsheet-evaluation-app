package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.entity.Sheet;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SumFormulaApplierTest {

    @Test
    void applyForSheetWithTwoParameters() {
        Sheet sheetWithTwoParametersFormula = new Sheet();
        sheetWithTwoParametersFormula.setId("sheet-1");
        List<List<Cell>> data = new ArrayList<>();
        Cell firstCell = new Cell(22);
        firstCell.setType(Type.NUMERIC);
        Cell secondCell = new Cell(212212);
        firstCell.setType(Type.NUMERIC);
        Cell thirdCell = new Cell("=SUM(A1, B1)");
        firstCell.setType(Type.FORMULA);
        data.add(List.of(firstCell, secondCell, thirdCell));
        sheetWithTwoParametersFormula.setData(data);

        FormulaInfo formulaInfo = new FormulaInfo("=SUM(A1, B1)");

        Cell expected = new Cell(new BigDecimal(212234));
        expected.setType(Type.NUMERIC);

        SumFormulaApplier sumFormulaApplier = new SumFormulaApplier();
        assertEquals(expected.getType(), sumFormulaApplier.apply(formulaInfo, sheetWithTwoParametersFormula).getType());
        assertEquals(expected.getValue(), sumFormulaApplier.apply(formulaInfo, sheetWithTwoParametersFormula).getValue());
    }

    @Test
    public void applyForSheetWithThreeParametersFormula() {
        Sheet sheetWithTwoParametersFormula = new Sheet();
        sheetWithTwoParametersFormula.setId("sheet-1");
        List<List<Cell>> data = new ArrayList<>();
        Cell firstCell = new Cell(221212);
        firstCell.setType(Type.NUMERIC);
        Cell secondCell = new Cell(22);
        firstCell.setType(Type.NUMERIC);
        Cell thirdCell = new Cell("=SUM(A1, B1, D1)");
        firstCell.setType(Type.FORMULA);
        Cell forthCell = new Cell(212);
        forthCell.setType(Type.NUMERIC);
        data.add(List.of(firstCell, secondCell, thirdCell, forthCell));
        sheetWithTwoParametersFormula.setData(data);

        FormulaInfo formulaInfo = new FormulaInfo("=SUM(A1, B1, D1)");

        Cell expected = new Cell(new BigDecimal(221446));
        expected.setType(Type.NUMERIC);

        SumFormulaApplier sumFormulaApplier = new SumFormulaApplier();
        assertEquals(expected.getType(), sumFormulaApplier.apply(formulaInfo, sheetWithTwoParametersFormula).getType());
        assertEquals(expected.getValue(), sumFormulaApplier.apply(formulaInfo, sheetWithTwoParametersFormula).getValue());
    }

    @Test
    public void applyForSheetWithNumberAndNotationParametersFormula() {
        Sheet sheetWithTwoParametersFormula = new Sheet();
        sheetWithTwoParametersFormula.setId("sheet-1");
        List<List<Cell>> data = new ArrayList<>();
        Cell firstCell = new Cell(22);
        firstCell.setType(Type.NUMERIC);
        Cell secondCell = new Cell(212212);
        firstCell.setType(Type.NUMERIC);
        Cell thirdCell = new Cell("=SUM(A1, 6, B1)");
        firstCell.setType(Type.FORMULA);
        data.add(List.of(firstCell, secondCell, thirdCell));
        sheetWithTwoParametersFormula.setData(data);

        FormulaInfo formulaInfo = new FormulaInfo("=SUM(A1, 6, B1)");

        Cell expected = new Cell(new BigDecimal(212240));
        expected.setType(Type.NUMERIC);

        SumFormulaApplier sumFormulaApplier = new SumFormulaApplier();
        assertEquals(expected.getType(), sumFormulaApplier.apply(formulaInfo, sheetWithTwoParametersFormula).getType());
        assertEquals(expected.getValue(), sumFormulaApplier.apply(formulaInfo, sheetWithTwoParametersFormula).getValue());
    }
}