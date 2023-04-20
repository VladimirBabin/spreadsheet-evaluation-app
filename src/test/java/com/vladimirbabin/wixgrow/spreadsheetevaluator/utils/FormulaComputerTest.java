package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.entity.Sheet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FormulaComputerTest {

    @Autowired
    FormulaComputer formulaComputer;


    private Sheet sheetWithThreeParametersFormula;
    private Sheet sheetWithNumberAndNotationParametersFormula;


    @Test
    void applyForEmptySheet() {
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

        Cell expected = new Cell(212234);
        expected.setType(Type.NUMERIC);

        assertEquals(expected, formulaComputer.computeFormula(formulaInfo.getCellStringOrParameterValue(),
                sheetWithTwoParametersFormula));
    }
}