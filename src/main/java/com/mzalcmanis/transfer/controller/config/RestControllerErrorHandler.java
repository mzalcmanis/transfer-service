package com.mzalcmanis.transfer.controller.config;

import com.mzalcmanis.transfer.service.thirdparty.ExchangeRateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//leave other exceptions to spring built-in ErrorController
@RestControllerAdvice(annotations = RestController.class)
public class RestControllerErrorHandler {

    @ExceptionHandler(ExchangeRateException.class)
    public ResponseEntity handleExchangeRateException(ExchangeRateException e) {
        return ResponseEntity.of(
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.BAD_REQUEST,
                        "Unable to fetch exchange rates, try again later"
                )
        ).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationError(MethodArgumentNotValidException exception) {
        //Take first field error for simplicity
        FieldError fieldError = exception.getBindingResult().getFieldError();
        if (fieldError == null) {
            return ResponseEntity.of(exception.getBody()).build();
        }
        return ResponseEntity.of(
                ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                        String.format("%s = %s resulted in violation: %s",
                                fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage())
                )).build();
    }
}
