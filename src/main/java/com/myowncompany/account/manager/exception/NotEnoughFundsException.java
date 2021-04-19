package com.myowncompany.account.manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Not enough funds")
public class NotEnoughFundsException extends RuntimeException {
    public NotEnoughFundsException() {
    }
}
