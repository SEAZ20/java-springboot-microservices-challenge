package com.challenge.microservices.account.domain.exception;

public class DuplicateAccountNumberException extends RuntimeException {

    public DuplicateAccountNumberException(String accountNumber) {
        super("El numero de cuenta ya existe: " + accountNumber);
    }
}
