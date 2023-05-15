package com.vladimirbabin.wixgrow.spreadsheetevaluator;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.dto.Spreadsheet;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SpreadsheetEvaluatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpreadsheetEvaluatorApplication.class, args);
    }

}