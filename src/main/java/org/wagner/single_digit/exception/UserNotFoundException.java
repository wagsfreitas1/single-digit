package org.wagner.single_digit.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Integer id) {
        super("Could not find user with id: " + id);
    }
}
