package org.wagner.single_digit.presentation.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public Map<String, Object> handleNotFoundException(UserNotFoundException exception) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", exception.getMessage());

        return body;
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(CryptoException.class)
    public Map<String, Object> handleCryptoException(CryptoException exception) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Cryptography error: " + exception.getMessage());

        return body;
    }
}
