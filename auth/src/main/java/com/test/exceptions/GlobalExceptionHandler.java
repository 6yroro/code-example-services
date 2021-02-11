package com.test.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import javax.validation.Path.Node;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Alexander Zubkov
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final String comma = ", ";

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map.Entry<String, String>> handle(BadCredentialsException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new AbstractMap.SimpleEntry<>("message", exception.getMessage()));
    }

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<Map.Entry<String, String>> handle(UserExistException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new AbstractMap.SimpleEntry<>("message", exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getBindingResult().getAllErrors().stream()
                        .collect(Collectors.toMap(error -> ((FieldError) error).getField(),
                                DefaultMessageSourceResolvable::getDefaultMessage,
                                (a, b) -> a + comma + b)));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleValidation(ConstraintViolationException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getConstraintViolations().forEach(violation -> {
            String field = null;
            for (Node node : violation.getPropertyPath()) {
                field = node.getName();
            }
            errors.merge(field, violation.getMessage(), (a, b) -> a + comma + b);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler
    public ResponseEntity<Map.Entry<String, String>> handle(Exception exception) {
        log.error("Unable to process this request", exception);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new AbstractMap.SimpleEntry<>("message", "Unable to process this request"));
    }

}
