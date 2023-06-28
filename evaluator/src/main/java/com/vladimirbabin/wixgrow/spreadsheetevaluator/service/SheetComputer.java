package com.vladimirbabin.wixgrow.spreadsheetevaluator.service;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.utils.FormulaComputer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * The SheetComputer class replaces all objects in the sheets with cells that has both object and type, so that
 * the type can be used for future computations. After that it iterates through the sheet with cells and if they
 * contain a formula - it delegates formula computation to FormulaComputer.
 */
@Service
public class SheetComputer {
    private final FormulaComputer formulaComputer;
    private final SheetCellsDeterminer sheetCellsDeterminer;
    private static final Logger logger = LoggerFactory.getLogger(SheetComputer.class);

    SheetComputer(FormulaComputer formulaComputer, SheetCellsDeterminer sheetCellsDeterminer) {
        this.formulaComputer = formulaComputer;
        this.sheetCellsDeterminer = sheetCellsDeterminer;
    }

    Sheet computeSheet(Sheet sheet) {
        logger.info("Before computation: " + sheet);
        if (sheet.getData() == null) {
            return sheet;
        }
        Sheet<Object> resultSheet = new Sheet<>();
        resultSheet.setId(sheet.getId());
        List<List<Object>> listOfResultRows = new ArrayList<>();
        Sheet<Input> cellSheet = sheetCellsDeterminer.replaceObjectsWithCells(sheet);
        for (List<Input> listOfCells : cellSheet.getData()) {
            List<Object> rowOfResultObjects = new ArrayList<>();
            for (Input cell : listOfCells) {
                Input result;
                if (cell.getType().equals(Type.FORMULA) || cell.getType().equals(Type.NOTATION)) {
                    result = formulaComputer.computeFormula(cell, sheet);
                } else {
                    result = cell;
                }
                rowOfResultObjects.add(result.getValue());
            }
            listOfResultRows.add(rowOfResultObjects);
        }
        resultSheet.setData(listOfResultRows);
        logger.info("After computation: " + resultSheet);
        return resultSheet;
    }

}
