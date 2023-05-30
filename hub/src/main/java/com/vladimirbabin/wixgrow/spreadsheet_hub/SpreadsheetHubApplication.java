package com.vladimirbabin.wixgrow.spreadsheet_hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SpreadsheetHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpreadsheetHubApplication.class, args);
	}

}
