package com.entity;

public class EIException extends RuntimeException {
    private final int code;

    public EIException(String message, int code) {
        super(message);
        this.code = code;
    }

    public EIException(String message) {
        super(message);
        this.code = 500;
    }

    public int getCode() {
        return code;
    }
}

