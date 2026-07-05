package com.challenge.microservices.account.domain.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(Long id) {
        super("Cuenta no encontrada con id: " + id);
    }

    public AccountNotFoundException(String accountNumber) {
        super("Cuenta no encontrada con numero: " + accountNumber);
    }
}
