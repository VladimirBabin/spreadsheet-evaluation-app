package com.vladimirbabin.wixgrow.spreadsheetevaluator.service;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.AppProperties;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.*;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.exception_handling.WrongResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * SpreadsheetClient class is responsible for the communication with the hub and getting the spreadsheet sheets.
 * After receiving them it iterates through the sheets, delegating computation of each sheet to SheetComputer
 * and sends the computation results back. After submitting the correct results it prints the response message in logger.
 */
@Service
public class SpreadsheetClient {
    private final SheetComputer sheetComputer;
    private final WebClient client;
    private final AppProperties properties;
    private static final Logger logger = LoggerFactory.getLogger(SpreadsheetClient.class);
    private String responseMessage;


    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public SpreadsheetClient(SheetComputer sheetComputer, AppProperties properties) {
        this.client = WebClient.builder().build();
        this.sheetComputer = sheetComputer;
        this.properties = properties;
    }

    public Spreadsheet getSpreadsheet() {
        return client
                .get()
                .uri(getUrl())
                .retrieve()
                .bodyToMono(Spreadsheet.class)
                .block();
    }

    public void evaluateAndSendSpreadsheet(Spreadsheet initialSpreadsheet) {
        ResultSubmission result = new ResultSubmission(properties);
        List<Sheet<Object>> resultListOfSheets = new ArrayList<>();
        for (Sheet<Object> sheet : initialSpreadsheet.getSheets()) {
            resultListOfSheets.add(sheetComputer.computeSheet(sheet));
        }
        logger.info(resultListOfSheets.toString());
        result.setResults(resultListOfSheets);

        Message responseWithPasscode = client
                .post()
                .uri(initialSpreadsheet.getSubmissionUrl())
                .body(Mono.just(result), ResultSubmission.class)
                .retrieve()
                .onStatus(
                        HttpStatus.BAD_REQUEST::equals,
                        response -> response.bodyToMono(BadRequestDto.class)
                                .flatMap(entity -> Mono.error(new WrongResponseException(entity.getMessage()))))
                .onStatus(
                        HttpStatus.BAD_GATEWAY::equals,
                        response -> response.bodyToMono(BadRequestDto.class)
                                .flatMap(entity -> Mono.error(new WrongResponseException(entity.getMessage()))))
                .bodyToMono(Message.class)
                .block();

        if (responseWithPasscode != null && responseWithPasscode.getMessage() != null) {
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
