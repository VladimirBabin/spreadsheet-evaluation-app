package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SheetComputer {
    private FormulaComputer formulaComputer;
    private NotationApplier notationApplier;
    private Logger logger = LoggerFactory.getLogger(SheetComputer.class);

    public SheetComputer(FormulaComputer formulaComputer, NotationApplier notationApplier) {
        this.formulaComputer = formulaComputer;
        this.notationApplier = notationApplier;
    }

    public Sheet computeSheet(Sheet sheet) {
        logger.info(sheet.toString());
        if (sheet.getData() == null) {
            return sheet;
        }
        Sheet<Object> resultSheet = new Sheet<>();
        resultSheet.setId(sheet.getId());
        List<List<Object>> listOfResultRows = new ArrayList<>();
        Sheet<Input> cellSheet = replaceObjectsWithCells(sheet);
        for (List<Input> listOfCells : cellSheet.getData()) {
            List<Object> rowOfResultObjects = new ArrayList<>();
            for (Input cell : listOfCells) {
                Input result = formulaComputer.checkIfInputHasFormulaOrNotationAndCompute(cell, sheet);
                logger.info(cell.getValue().toString());
                rowOfResultObjects.add(result);
            }
            listOfResultRows.add(rowOfResultObjects);
        }
        resultSheet.setData(listOfResultRows);
        return resultSheet;
    }


    static private Sheet<Input> replaceObjectsWithCells (Sheet<Object> sheet) {
        InputTypeDeterminer inputTypeDeterminer = new InputTypeDeterminer();
        Sheet<Input> sheetResult = new Sheet<>();
        sheetResult.setId(sheet.getId());
        List<List<Input>> listOfRows = new ArrayList<>();
        for (List<Object> listOfObjects : sheet.getData()) {
            List<Input> rowOfCells = new ArrayList<>();
            for (Object object : listOfObjects) {
                Input cell = inputTypeDeterminer.determineType(new Input(object));
                rowOfCells.add(cell);
            }
            listOfRows.add(rowOfCells);
        }
        sheetResult.setData(listOfRows);
        return sheetResult;
    }


}
