package com.enigmacamp.Loan.Management.System.exception;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String message) {
        super(message);
    }
}
