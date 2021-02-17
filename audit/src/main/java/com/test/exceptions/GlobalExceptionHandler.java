package com.test.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.AbstractMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author Alexander Zubkov
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map.Entry<String, String>> handle(NoSuchElementException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new AbstractMap.SimpleEntry<>("message", exception.getMessage()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map.Entry<String, String>> handle(HttpRequestMethodNotSupportedException exception) {
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new AbstractMap.SimpleEntry<>("message", exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Map.Entry<String, String>> handle(Exception exception) {
        log.error("Unable to process this request", exception);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new AbstractMap.SimpleEntry<>("message", "Unable to process this request"));
    }

}
