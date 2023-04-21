package com.vladimirbabin.wixgrow.spreadsheetevaluator.utils;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Type;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParametersResolver {
    private final InputTypeDeterminer inputTypeDeterminer;

    public ParametersResolver(InputTypeDeterminer inputTypeDeterminer) {
        this.inputTypeDeterminer = inputTypeDeterminer;
    }

    List<Input> resolveParameters(List<String> listOfParameters, Sheet sheet) {
        List<Input> resolvedList = new ArrayList<>();
        for (String string : listOfParameters) {
            Input parameter;
            try {
                parameter = resolveSingleParameter(string);
            } catch (Exception exception) {
                parameter = new Input("#ERROR: " + exception.getMessage());
                parameter.setType(Type.ERROR);
            }
            resolvedList.add(parameter);
        }
        return resolvedList;
    }

    private Input resolveSingleParameter(String parameterValue) {
        Input parameter = new Input(parameterValue);
        parameter = inputTypeDeterminer.determineType(parameter);
        return parameter;
    }

}
