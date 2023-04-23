package com.vladimirbabin.wixgrow.spreadsheetevaluator;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Spreadsheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.service.SpreadsheetClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SpreadsheetEvaluatorApplication implements CommandLineRunner {

    private final SpreadsheetClient client;

    public SpreadsheetEvaluatorApplication(SpreadsheetClient client) {
        this.client = client;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpreadsheetEvaluatorApplication.class, args);
    }

    @Override
    public void run(String[] args) {
        Spreadsheet resultSpreadsheet = client.getSpreadsheet();
        client.sendEvaluatedSpreadsheetAndLogResult(resultSpreadsheet);
    }
}
