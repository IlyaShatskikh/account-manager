package com.myowncompany.account.manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Account doesn't exist")
public class AccountDoesntExistException extends RuntimeException {
    public AccountDoesntExistException() {
    }
    public AccountDoesntExistException(String message) {
        super(message);
    }
}
