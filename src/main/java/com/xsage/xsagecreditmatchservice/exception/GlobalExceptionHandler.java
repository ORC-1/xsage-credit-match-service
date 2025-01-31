package com.xsage.xsagecreditmatchservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<APIError> methodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        log.error("validation exception : " +
                  ex.getLocalizedMessage() +
                  " for " +
                  request.getRequestURI());

        return new ResponseEntity<>(
                APIError.builder()
                        .errorMessage(errors)
                        .errorCode(HttpStatus.BAD_REQUEST.toString())
                        .request(request.getRequestURI())
                        .requestType(request.getMethod())
                        .customMessage("Request is not valid")
                        .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<APIError> validationException(
            ValidationException ex,
            HttpServletRequest request) {

        log.error("validation exception : " +
                  ex.getLocalizedMessage() +
                  " for " +
                  request.getRequestURI());
        List<String> errors = Arrays.asList(ex.getMessage());
        return new ResponseEntity<>(
                APIError.builder()
                        .errorMessage(errors)
                        .errorCode(HttpStatus.BAD_REQUEST.toString())
                        .request(request.getRequestURI())
                        .requestType(request.getMethod())
                        .customMessage("Request is not valid")
                        .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<APIError> genericException(
            Exception ex,
            HttpServletRequest request) {

        log.error("exception : " +
                  ex.getLocalizedMessage() +
                  " for " +
                  request.getRequestURI());
        List<String> errors = Arrays.asList(ex.getMessage());
        return new ResponseEntity<>(
                APIError.builder()
                        .errorMessage(errors)
                        .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .request(request.getRequestURI())
                        .requestType(request.getMethod())
                        .customMessage("Could not process request")
                        .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
