package com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.service;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto.*;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.exception_handling.NoSubmittedResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ValidatorService {
    private final Logger logger = LoggerFactory.getLogger(ValidatorService.class);
    private final SheetCellsDeterminer sheetCellsDeterminer;
    private final ReportWriter reportWriter;

    public ValidatorService(ReportWriter reportWriter) {
        this.sheetCellsDeterminer = new SheetCellsDeterminer(new InputTypeDeterminer());
        this.reportWriter = reportWriter;
    }


    public ValidatorResponse verifySpreadsheet(SpreadsheetToVerify spreadsheet) {
        ValidatorResponse responseDto = new ValidatorResponse();
        List<Report> reports = new ArrayList<>();

        if (spreadsheet == null || spreadsheet.getResults() == null) {
            throw new NoSubmittedResult("No result submitted, please send the result in json format");
        }
        logger.info("Received a computed spreadsheet from " + spreadsheet.getEmail());

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
            if (sheet.getData() == null) {
                Report nullSheetReport = new Report();
                nullSheetReport.setError("invalid  result: " +
                        "the sheet can't be empty");
                reports.add(nullSheetReport);
                continue;
            }
            Sheet<Input> inputsSheet = sheetCellsDeterminer.replaceObjectsWithCells(sheet);
            Report report = reportWriter.check(inputsSheet);
            if (report.getError() != null) {
                containsError = true;
            }
            reports.add(reportWriter.check(inputsSheet));
        }
        if (containsError) {
            responseDto.setReports(reports);
            for (Report report : reports) {
                logger.error("Errors on validation:");
                logger.info(report.toString());
            }
        } else {
            responseDto.setMessage(
                            "All results are correct. " +
                            "You can clean up and submit your source code now. " +
                            "Your passcode: place-for-the-passcode"
            );
            logger.info("Validation successful:");
            logger.info(responseDto.getMessage());
        }
        return responseDto;
    }
}
