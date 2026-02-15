package com.enigmacamp.Loan.Management.System.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
