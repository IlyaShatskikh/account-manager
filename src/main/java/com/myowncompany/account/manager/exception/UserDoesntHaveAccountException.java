package com.myowncompany.account.manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="User doesn't have account")
public class UserDoesntHaveAccountException extends RuntimeException {
    public UserDoesntHaveAccountException(String message) {
        super(message);
    }
}
