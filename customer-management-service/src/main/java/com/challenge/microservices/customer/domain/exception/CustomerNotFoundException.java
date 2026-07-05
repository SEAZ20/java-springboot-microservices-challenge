package com.challenge.microservices.customer.domain.exception;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(Long id) {
        super("Cliente no encontrado con id: " + id);
    }
}
