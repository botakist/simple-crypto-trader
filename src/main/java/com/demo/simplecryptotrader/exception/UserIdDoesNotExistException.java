package com.demo.simplecryptotrader.exception;

public class UserIdDoesNotExistException extends RuntimeException {
    public UserIdDoesNotExistException(String message) {
        super(message);
    }
}
