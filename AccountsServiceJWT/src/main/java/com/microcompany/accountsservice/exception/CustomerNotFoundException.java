package com.microcompany.accountsservice.exception;

public class CustomerNotFoundException extends GlobalException{

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
