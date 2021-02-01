package com.test.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.AbstractMap;

/**
 * @author Alexander Zubkov
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AbstractMap.SimpleEntry<String, String>> handle(BadCredentialsException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new AbstractMap.SimpleEntry<>("message", exception.getMessage()));
    }

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<AbstractMap.SimpleEntry<String, String>> handle(UserExistException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new AbstractMap.SimpleEntry<>("message", exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<AbstractMap.SimpleEntry<String, String>> handle(Exception exception) {
        log.error("Unable to process this request", exception);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new AbstractMap.SimpleEntry<>("message", "Unable to process this request"));
    }

}
