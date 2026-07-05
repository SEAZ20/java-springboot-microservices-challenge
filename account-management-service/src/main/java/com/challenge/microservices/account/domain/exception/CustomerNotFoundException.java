package com.challenge.microservices.account.domain.exception;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(Long id) {
        super("Cliente no encontrado con id: " + id);
    }
}
