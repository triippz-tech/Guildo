package com.triippztech.guildo.exception.command;

public class NoBanFoundExcetion extends Exception {
    public NoBanFoundExcetion(String errorMessage) {
        super(errorMessage);
    }
    public NoBanFoundExcetion(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
