package com.triippztech.guildo.exception.command;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(String errorMessage) {
        super(errorMessage);
    }
    public UserNotFoundException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
