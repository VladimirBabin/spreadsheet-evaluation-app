package com.vladimirbabin.wixgrow.spreadsheetevaluator.service;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.ConfigurationProperties;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Message;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.ResultSubmission;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Sheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Spreadsheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.utils.SheetComputer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpreadsheetClient {
    private final SheetComputer sheetComputer;
    private final WebClient client;
    private final ConfigurationProperties properties;
    private static Logger logger = LoggerFactory.getLogger(SpreadsheetClient.class);

    public SpreadsheetClient(SheetComputer sheetComputer, ConfigurationProperties properties) {
        this.client = WebClient.builder().build();
        this.sheetComputer = sheetComputer;
        this.properties = properties;
    }

    public Spreadsheet getSpreadsheet() {
        Spreadsheet spreadsheet = client
                .get()
                .uri(getUrl())
                .retrieve()
                .bodyToMono(Spreadsheet.class)
                .block();
        return spreadsheet;
    }

    public void sendEvaluatedSpreadsheetAndLogResult(Spreadsheet initialSpreadsheet) {
        ResultSubmission result = new ResultSubmission();
        List<Sheet> resultListOfSheets = new ArrayList<>();
        for (Sheet sheet : initialSpreadsheet.getSheets()) {
            resultListOfSheets.add(sheetComputer.computeSheet(sheet));
        }
        result.setResults(resultListOfSheets);

        Message responseWithPasscode = client
                .post()
                .uri(initialSpreadsheet.getSubmissionUrl())
                .body(Mono.just(result), ResultSubmission.class)
                .retrieve()
                .bodyToMono(Message.class)
                .block();

        logger.info(responseWithPasscode.getMessage());
    }

    private String getUrl() {return properties.urlForGettingTheTask();}
}

