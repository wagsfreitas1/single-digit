package org.wagner.single_digit.presentation.exception;

public class CryptoException extends RuntimeException {
    public CryptoException(String message, Throwable cause) {

        super(message, cause);
    }
}
