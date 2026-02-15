package com.enigmacamp.Loan.Management.System.exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
