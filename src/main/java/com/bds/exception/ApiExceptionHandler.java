package com.bds.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;
import java.util.Set;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = DuplicateResourceException.class)
    public ResponseEntity<?> handleException(
            DuplicateResourceException e
    ) {

        ApiException apiException = new ApiException(
                Set.of(e.getMessage()),
                HttpStatus.CONFLICT,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(
                apiException,
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = RequestValidationException.class)
    public ResponseEntity<?> handleException(
            RequestValidationException e
    ) {
        ApiException apiException = new ApiException(
                Set.of(e.getMessage()),
                HttpStatus.NOT_ACCEPTABLE,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(
                apiException,
                HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<?> handleException(
            ResourceNotFoundException e
    ) {
        ApiException apiException = new ApiException(
                Set.of(e.getMessage()),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(
                apiException,
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ObjectNotValidException.class)
    public ResponseEntity<?> handleException(
            ObjectNotValidException e
    ) {
        ApiException apiException = new ApiException(
                e.getErrorMessages(),
                HttpStatus.NOT_ACCEPTABLE,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(
                apiException,
                HttpStatus.NOT_ACCEPTABLE);
    }
}
