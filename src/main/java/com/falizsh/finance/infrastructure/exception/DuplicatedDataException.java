package com.falizsh.finance.infrastructure.exception;

public class DuplicatedDataException extends RuntimeException {

    public DuplicatedDataException(String message) {
        super(message);
    }

}
