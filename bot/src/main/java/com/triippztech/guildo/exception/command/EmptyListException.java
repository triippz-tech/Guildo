package com.triippztech.guildo.exception.command;

public class EmptyListException extends Exception {
    public EmptyListException(String errorMessage) {
        super(errorMessage);
    }
    public EmptyListException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
