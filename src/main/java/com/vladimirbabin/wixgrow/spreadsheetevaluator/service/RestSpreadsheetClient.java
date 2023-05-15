package com.vladimirbabin.wixgrow.spreadsheetevaluator.service;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.AppProperties;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Message;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.ResultSubmission;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Spreadsheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * SpreadsheetClient class is responsible for the communication with the hub and getting the spreadsheet sheets.
 * After receiving them it iterates through the sheets, delegating computation of each sheet to SheetComputer
 * and sends the computation results back. After submitting the correct results it prints the response message in logger.
 */
@Service
public class RestSpreadsheetClient {
    private final SheetComputer sheetComputer;
    private final RestTemplate client;
    private final AppProperties properties;
    private static final Logger logger = LoggerFactory.getLogger(RestSpreadsheetClient.class);

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    private String responseMessage;

    public RestSpreadsheetClient(SheetComputer sheetComputer, AppProperties properties, RestTemplateBuilder builder) {
        this.client = builder.build();
        this.sheetComputer = sheetComputer;
        this.properties = properties;
    }

    public Spreadsheet getSpreadsheet() {
        return client.getForEntity(getUrl(), Spreadsheet.class).getBody();
    }

    public void sendEvaluatedSpreadsheetAndLogResult(Spreadsheet initialSpreadsheet) {
        ResultSubmission result = new ResultSubmission(properties);
        List<Sheet> resultListOfSheets = new ArrayList<>();
        for (Sheet sheet : initialSpreadsheet.getSheets()) {
            resultListOfSheets.add(sheetComputer.computeSheet(sheet));
        }
        result.setResults(resultListOfSheets);

        Message responseWithPasscode = client.postForEntity(initialSpreadsheet.getSubmissionUrl(),
                result, Message.class).getBody();

        if (responseWithPasscode != null) {
            logger.info(responseWithPasscode.getMessage());
            setResponseMessage(responseWithPasscode.getMessage());
        } else {
            logger.error("No response");
        }
    }

    private String getUrl() {
        return properties.getUrlForGettingTheTask();
    }
}
