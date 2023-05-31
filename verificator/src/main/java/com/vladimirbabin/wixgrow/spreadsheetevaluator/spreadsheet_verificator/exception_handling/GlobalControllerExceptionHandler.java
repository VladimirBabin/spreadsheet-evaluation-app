package com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.exception_handling;

import com.vladimirbabin.wixgrow.spreadsheetevaluator.spreadsheet_verificator.dto.ErrorDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ WrongResult.class })
    protected ResponseEntity<Object> handleWrongResultException(WrongResult ex) {

        ErrorDto errorDto = new ErrorDto(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(
                errorDto, new HttpHeaders(), errorDto.getStatus());
    }

    @ExceptionHandler({ NoSubmittedResult.class })
    protected ResponseEntity<Object> handleNoSubmittedResultException(NoSubmittedResult ex) {

        ErrorDto errorDto = new ErrorDto(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(
                errorDto, new HttpHeaders(), errorDto.getStatus());
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex) {
        ErrorDto errorDto = new ErrorDto(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "error occurred");
        return new ResponseEntity<>(
                errorDto, new HttpHeaders(), errorDto.getStatus());
    }
}
