package com.vladimirbabin.wixgrow.spreadsheet_evaluator_swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ConfigurationPropertiesScan
@OpenAPIDefinition(
		info = @Info(
				title = "Spreadsheet evaluator",
				version = "1.0.0",
				description = "This is web interface for calling the spreadsheet evaluator program",
				contact = @Contact(
						name = "Vladimir",
						email = "vladimir.sol.rojo@gmail.com"
		)
		)
)
public class SpreadsheetEvaluatorSwaggerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpreadsheetEvaluatorSwaggerApplication.class, args);
	}

}
