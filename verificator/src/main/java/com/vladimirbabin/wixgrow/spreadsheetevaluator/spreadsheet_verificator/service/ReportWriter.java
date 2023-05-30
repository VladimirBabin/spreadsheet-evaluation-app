package com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.service;


import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto.Input;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto.Report;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto.Sheet;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportWriter {
    private final InputChecker inputChecker;

    public ReportWriter(InputChecker inputChecker) {
        this.inputChecker = inputChecker;
    }

    public Report check(Sheet<Input> inputsSheet) {
        Report report = new Report();
        report.setId(inputsSheet.getId());
        List<List<Input>> data = inputsSheet.getData();

        if (inputsSheet.getData() == null || inputsSheet.getData().isEmpty()) {
            if (!inputChecker.checkIfDataForCorrectSheetIsNullOrEmpty(inputsSheet.getId())) {
                report.setError("invalid result: invalid sheet "
                        + inputsSheet.getId()
                        + ", should be not null");
                return report;
            }
            report.setOk(true);
            return report;
        }

        if (inputChecker.checkIfDataForCorrectSheetIsNullOrEmpty(inputsSheet.getId())) {
            report.setError("invalid result: invalid sheet "
                    + inputsSheet.getId()
                    + ", should not be null/empty");
            return report;
        }

        for (int i = 0; i < data.size(); i++) {
            List<Input> inputsList = data.get(i);
            for (int j = 0; j < inputsList.size(); j++) {
                Input input = inputsList.get(j);
                String checked = inputChecker.check(inputsSheet.getId(), input, i, j);
                if (!checked.equals("ok")) {
                    report.setError(checked);
                    return report;
                }
            }
        }
        report.setOk(true);
        return report;
    }
}
