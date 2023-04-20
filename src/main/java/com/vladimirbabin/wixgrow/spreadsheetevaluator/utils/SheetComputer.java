package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.entity.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SheetComputer {

    @Autowired
    private FormulaComputer formulaComputer;

    public Sheet computeSheet(Sheet sheet) {
        if (sheet.getData() == null) {
            return sheet;
        }
        Sheet<Object> resultSheet = new Sheet<>();
        resultSheet.setId(sheet.getId());
        List<List<Object>> listOfResultRows = new ArrayList<>();
        Sheet<Cell> cellSheet = replaceObjectsWithCells(sheet);
        for (List<Cell> listOfCells : cellSheet.getData()) {
            List<Object> rowOfResultObjects = new ArrayList<>();
            for (Cell cell : listOfCells) {
                Cell result = formulaComputer.checkIfCellHasFormulaAndCompute(cell, cellSheet);
                rowOfResultObjects.add(result.getValue());
            }
            listOfResultRows.add(rowOfResultObjects);
        }
        resultSheet.setData(listOfResultRows);
        return resultSheet;
    }


    static private Sheet<Cell> replaceObjectsWithCells (Sheet<Object> sheet) {
        Sheet<Cell> sheetResult = new Sheet<>();
        sheetResult.setId(sheet.getId());
        List<List<Cell>> listOfRows = new ArrayList<>();
        for (List<Object> listOfObjects : sheet.getData()) {
            List<Cell> rowOfCells = new ArrayList<>();
            for (Object object : listOfObjects) {
                Cell cell = new Cell(object);
                if (object instanceof Number) {
                    cell.setType(Type.NUMERIC);
                } else if (object instanceof Boolean) {
                    cell.setType(Type.BOOLEAN);
                } else if (object instanceof String) {
                    String stringObj = object.toString();
                    if (stringObj.startsWith("=")) {
                        cell.setType(Type.FORMULA);
                    } else {
                        cell.setType(Type.STRING);
                    }
                }
                rowOfCells.add(cell);
            }
            listOfRows.add(rowOfCells);
        }
        sheetResult.setData(listOfRows);
        return sheetResult;
    }


}
