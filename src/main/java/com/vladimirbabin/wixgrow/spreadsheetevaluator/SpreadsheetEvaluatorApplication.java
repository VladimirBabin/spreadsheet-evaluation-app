package com.vladimirbabin.wixgrow.spreadsheetevaluator;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.DTO.Spreadsheet;
import com.vladimirbabin.wixgrow.spreadsheetevaluator.service.SpreadsheetClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SpreadsheetEvaluatorApplication implements CommandLineRunner {

    @Autowired
    SpreadsheetClient client;

    public static void main(String[] args) {
        SpringApplication.run(SpreadsheetEvaluatorApplication.class, args);
    }

    @Override
    public void run(String[] args) {
        Spreadsheet resultSpreadsheet = client.getSpreadsheet();
        client.sendEvaluatedSpreadsheetAndLogResult(resultSpreadsheet);
    }
}
