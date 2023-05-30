package com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Type;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.service.InputTypeDeterminer;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.service.SheetCellsDeterminer;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InputChecker {
//    matrix of spreadsheet data
    ObjectMapper objectMapper = new ObjectMapper();
    final Map<String, List<List<Input>>> mapOfInputs;

    public InputChecker() {
        List<Sheet<Object>> objectSheets = new ArrayList<>();
        try {
            objectSheets = objectMapper.readValue(new File(
                    "C:\\Users\\mi\\Spreadsheet_Verificator\\src\\main\\resources\\correct-sheets.json")
                    , new TypeReference<List<Sheet<Object>>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        SheetCellsDeterminer sheetCellsDeterminer = new SheetCellsDeterminer(new InputTypeDeterminer());
        List<Sheet<Input>> inputSheets = new ArrayList<>();
        for(Sheet sheet : objectSheets) {
            inputSheets.add(sheetCellsDeterminer.replaceObjectsWithCells(sheet));
        }
        mapOfInputs = inputSheets.stream()
                .collect(Collectors.toMap(Sheet::getId, Sheet::getData));
    }



    public String check(String sheetId, Input input, int i, int j) {
        List<List<Input>> data = mapOfInputs.get(sheetId);
        if (data == null) {
            return "invalid result: invalid data for sheet "
                    + sheetId
                    + ", shouldn't be null";
        }
        Input toCompare = data.get(i).get(j);
        char[] notationChars = {(char) (j + 65), (char) (i + 49)};
        String notation = String.valueOf(notationChars);
        //check type
        if (!input.getType().equals(toCompare.getType())) {
            return "invalid result: invalid type in cell "
                    + notation
                    + ", should be " + toCompare.getType();
        }

        //check value
        boolean equalValue = true;
        if (!input.getType().equals(Type.ERROR)) {
            if (!input.getValue().equals(toCompare.getValue())) {
                equalValue = false;
            }
            Object value = input.getValue();
            if (input.getType().equals(Type.NUMERIC) && !(value instanceof Number)) {
                String stringVal = (String) value;
                double inputVal = Double.parseDouble(stringVal);
                equalValue = inputVal == ((double) toCompare.getValue());
            } else if (input.getType().equals(Type.BOOLEAN) && !(value instanceof Boolean)) {
                String stringVal = (String) value;
                boolean booleanVal = stringVal.equalsIgnoreCase("true")
                        || stringVal.equalsIgnoreCase("false");
                equalValue = booleanVal == (boolean) toCompare.getValue();
            }
        }

        if (!equalValue) {
            return "invalid result: invalid value in cell " + notation
                    + ", value " + input.getValue() + " is incorrect";
        }
        return "ok";
    }

    public boolean checkIfDataForCorrectSheetIsNullOrEmpty(String sheetId) {
        List<List<Input>> data = mapOfInputs.get(sheetId);
        return data == null || data.isEmpty();
    }
}
