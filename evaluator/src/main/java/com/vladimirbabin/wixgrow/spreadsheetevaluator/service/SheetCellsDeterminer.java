package com.vladimirbabin.wixgrow.spreadsheetevaluator.service;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SheetCellsDeterminer {
    private final InputTypeDeterminer inputTypeDeterminer;

    public SheetCellsDeterminer(InputTypeDeterminer inputTypeDeterminer) {
        this.inputTypeDeterminer = inputTypeDeterminer;
    }

    public Sheet<Input> replaceObjectsWithCells(Sheet<Object> sheet) {
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
