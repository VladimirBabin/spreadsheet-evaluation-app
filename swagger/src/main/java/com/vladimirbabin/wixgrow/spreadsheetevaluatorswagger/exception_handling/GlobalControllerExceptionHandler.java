package com.vladimirbabin.wixgrow.spreadsheetevaluatorswagger.exception_handling;

import com.vladimirbabin.wixgrow.spreadsheetevaluatorswagger.dto.BadRequestDto;
import com.vladimirbabin.wixgrow.spreadsheetevaluatorswagger.dto.ErrorDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ WrongResponseException.class })
    protected ResponseEntity<Object> handleWrongResponseException(WrongResponseException ex) {
        BadRequestDto badRequestDto = ex.getBadRequestDto();
        badRequestDto.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(
                badRequestDto, new HttpHeaders(), badRequestDto.getStatus());
    }
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex) {
        ErrorDto errorDto = new ErrorDto(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "error occurred");
        return new ResponseEntity<>(
                errorDto, new HttpHeaders(), errorDto.getStatus());
    }
}
