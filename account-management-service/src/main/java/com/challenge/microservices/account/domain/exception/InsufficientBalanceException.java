package com.challenge.microservices.account.domain.exception;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException() {
        super("Saldo no disponible");
    }
}
