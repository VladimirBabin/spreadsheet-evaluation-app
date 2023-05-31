package com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.service;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto.*;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.exception_handling.NoSubmittedResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VerificationService {
    private final SheetCellsDeterminer sheetCellsDeterminer;
    private final ReportWriter reportWriter;

    public VerificationService(ReportWriter reportWriter) {
        this.sheetCellsDeterminer = new SheetCellsDeterminer(new InputTypeDeterminer());
        this.reportWriter = reportWriter;
    }


    public VerificationResponse verifySpreadsheet(SpreadsheetToVerify spreadsheet) {
        VerificationResponse responseDto = new VerificationResponse();
        List<Report> reports = new ArrayList<>();

        if (spreadsheet == null || spreadsheet.getResults() == null) {
            throw new NoSubmittedResult("No result submitted, please send the result in json format");
        }

        if (spreadsheet.getResults().isEmpty()) {
            Report nullSpreadsheetReport = new Report();
            nullSpreadsheetReport.setError("invalid  result: " +
                    "the spreadsheet can't be empty");
            reports.add(nullSpreadsheetReport);
            responseDto.setReports(reports);
            return responseDto;
        }

        boolean containsError = false;
        for (Sheet sheet : spreadsheet.getResults()) {
            Sheet<Input> inputsSheet = sheetCellsDeterminer.replaceObjectsWithCells(sheet);
            Report report = reportWriter.check(inputsSheet);
            if (report.getError() != null) {
                containsError = true;
            }
            reports.add(reportWriter.check(inputsSheet));
        }
        if (containsError) {
            responseDto.setReports(reports);
        } else {
            responseDto.setMessage(
                            "All results are correct. " +
                            "You can clean up and submit your source code now. " +
                            "Your passcode: place-for-the-passcode"
            );
        }
        return responseDto;
    }
}
