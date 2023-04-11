package com.vladimir_babin.wix_grow.spreadsheet_evaluator;

import com.vladimir_babin.wix_grow.spreadsheet_evaluator.utils.SheetComputer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpreadsheetEvaluatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpreadsheetEvaluatorApplication.class, args);
        String url = "https://www.wix.com/_serverless/hiring-task-spreadsheet-evaluator/sheets";

        WebClient.Builder builder = WebClient.builder();

        Spreadsheet spreadsheet = builder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(Spreadsheet.class)
                .block();


        ResultSubmission result = new ResultSubmission();
        List<Sheet> resultListOfSheets = new ArrayList<>();
        for (Sheet sheet: spreadsheet.getSheets()) {
            resultListOfSheets.add(SheetComputer.computeSheet(sheet));
        }
        result.setResults(resultListOfSheets);

        Message responseWithPasscode = builder.build()
                .post()
                .uri(spreadsheet.getSubmissionUrl())
                .body(Mono.just(result), ResultSubmission.class)
                .retrieve()
                .bodyToMono(Message.class)
                        .block();

        System.out.println(responseWithPasscode);
    }

}
